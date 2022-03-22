package com.small.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Formatter;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wesson
 * Create at 2021/12/16 22:32 周四
 */
@Slf4j
public class JvmInfo {

    private static final int CPUTIME = 5000;
    /**
     * 网管进程信息采集周期(注意：PERIOD_TIME 一定要大于 SLEEP_TIME )
     */
    private static final int PERIOD_TIME = 1000 * 60 * 15;
    /**
     * 此类中Thread.sleep()里的线程睡眠时间
     */
    private static final int SLEEP_TIME = 1000 * 60 * 9;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private final String isWindowsOrLinux = isWindowsOrLinux();
    private String pid = "";

    private Timer sysInfoGetTimer = new Timer("sysInfoGet");


    /**
     * 初始化bean的时候就立即获取JVM进程的PID及执行任务
     */
    @PostConstruct
    public void init() {
        if ("windows".equals(isWindowsOrLinux)) {
            getJvmPIDOnWindows();
        } else {
            getJvmPIDOnLinux();
        }
        sysInfoGetTimer.schedule(new SysInfoAcquirerTimerTask(), 10 * 1000, PERIOD_TIME);
    }

    /**
     * 判断是服务器的系统类型是Windows 还是 Linux
     */
    public String isWindowsOrLinux() {
        String osName = System.getProperty("os.name");
        String sysName = "";
        if (osName.toLowerCase().startsWith("windows")) {
            sysName = "windows";
        } else if (osName.toLowerCase().startsWith("linux")) {
            sysName = "linux";
        }
        return sysName;
    }

    /**
     * 获取JVM 的CPU占用率（%）
     */
    public String getCPURate() {
        String cpuRate = "";
        if ("windows".equals(isWindowsOrLinux)) {
            cpuRate = getCPURateForWindows();
        } else {
            cpuRate = getCPURateForLinux();
        }
        return cpuRate;
    }

    /**
     * windows环境下获取JVM的PID
     */
    public void getJvmPIDOnWindows() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        pid = runtime.getName().split("@")[0];
        log.info("PID of JVM:" + pid);
    }

    /**
     * linux环境下获取JVM的PID
     */
    public void getJvmPIDOnLinux() {
        String command = "pidof java";
        BufferedReader in = null;
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringTokenizer ts = new StringTokenizer(in.readLine());
            pid = ts.nextToken();
            log.info("PID of JVM:" + pid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取JVM的内存占用率（%）
     */
    public String getMemoryRate() {
        String memRate = "";
        if ("windows".equals(isWindowsOrLinux)) {
            memRate = getMemoryRateForWindows();
        } else {
            memRate = getMemoryRateForLinux();
        }
        return memRate;
    }

    /**
     * 获取JVM 线程数
     */
    public int getThreadCount() {
        int threadCount = 0;
        if ("windows".equals(isWindowsOrLinux)) {
            threadCount = getThreadCountForWindows();
        } else {
            threadCount = getThreadCountForLinux();
        }
        return threadCount;
    }


    /**
     * 获取网口吞吐量（MB/s）
     */
    public String getNetworkThroughput() {
        String throughput;
        if ("windows".equals(isWindowsOrLinux)) {
            throughput = getNetworkThroughputForWindows();
        } else {
            throughput = getNetworkThroughputForLinux();
        }
        return throughput;
    }

    /**
     * 获取Windows环境下网口的上下行速率
     */
    public String getNetworkThroughputForWindows() {
        Process pro1;
        Process pro2;
        Runtime r = Runtime.getRuntime();
        BufferedReader input;
        String rxPercent = "";
        String txPercent = "";
        JSONObject jsonObject = new JSONObject();
        try {
            String command = "netstat -e";
            pro1 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String[] result1 = readInLine(input, "windows");
            Thread.sleep(SLEEP_TIME);
            pro2 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            String[] result2 = readInLine(input, "windows");
            rxPercent = formatNumber((Long.parseLong(result2[0]) - Long.parseLong(result1[0]))
                    / (float) (1024 * 1024 * (SLEEP_TIME / 1000)));
            txPercent = formatNumber((Long.parseLong(result2[1]) - Long.parseLong(result1[1]))
                    / (float) (1024 * 1024 * (SLEEP_TIME / 1000)));
            input.close();
            pro1.destroy();
            pro2.destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        jsonObject.put("rxPercent", rxPercent);
        jsonObject.put("txPercent", txPercent);
        return JSON.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
    }

    /**
     * 获取Linux环境下网口的上下行速率
     */
    public String getNetworkThroughputForLinux() {
        Process pro1 = null;
        Process pro2 = null;
        Runtime r = Runtime.getRuntime();
        BufferedReader input = null;
        String rxPercent = "";
        String txPercent = "";
        JSONObject jsonObject = new JSONObject();
        try {
            String command = "watch ifconfig";
            pro1 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro1.getInputStream()));

            String[] result1 = readInLine(input, "linux");
            Thread.sleep(SLEEP_TIME);
            pro2 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            String[] result2 = readInLine(input, "linux");
            rxPercent = formatNumber((Long.parseLong(result2[0]) - Long.parseLong(result1[0]))
                    / (float) (1024 * 1024 * (SLEEP_TIME / 1000)));
            txPercent = formatNumber((Long.parseLong(result2[1]) - Long.parseLong(result1[1]))
                    / (float) (1024 * 1024 * (SLEEP_TIME / 1000)));
            input.close();
            pro1.destroy();
            pro2.destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        jsonObject.put("rxPercent", rxPercent);
        jsonObject.put("txPercent", txPercent);
        return JSON.toJSONStringWithDateFormat(jsonObject, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
    }

    /**
     * 获取windows环境下JVM的cpu占用率
     */
    public String getCPURateForWindows() {
        try {
            String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe  process "
                    + "  get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                long cpuRate = PERCENT * (busytime) / (busytime + idletime);
                if (cpuRate > 100) {
                    cpuRate = 100;
                } else if (cpuRate < 0) {
                    cpuRate = 0;
                }
                return String.valueOf(PERCENT * (busytime) / (busytime + idletime));

            } else {
                return "0.0";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0.0";
        }
    }

    /**
     * 获取linux环境下JVM的cpu占用率
     */
    public String getCPURateForLinux() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat;
        String user;
        String linuxVersion = System.getProperty("os.version");
        try {
            System.out.println("Linux版本: " + linuxVersion);

            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "top -b -p " + pid});
            try {
                // top命令默认3秒动态更新结果信息，让线程睡眠5秒以便获取最新结果
                Thread.sleep(CPUTIME);
                is = process.getInputStream();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);

            if ("2.4".equals(linuxVersion)) {
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();

                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                tokenStat.nextToken();
                user = tokenStat.nextToken();
                tokenStat.nextToken();
                String system = tokenStat.nextToken();
                tokenStat.nextToken();
                String nice = tokenStat.nextToken();

                System.out.println(user + " , " + system + " , " + nice);

                user = user.substring(0, user.indexOf("%"));
                system = system.substring(0, system.indexOf("%"));
                nice = nice.substring(0, nice.indexOf("%"));

                float userUsage = Float.parseFloat(user);
                float systemUsage = Float.parseFloat(system);
                float niceUsage = Float.parseFloat(nice);
                return String.valueOf((userUsage + systemUsage + niceUsage) / 100);
            } else {
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                brStat.readLine();
                tokenStat = new StringTokenizer(brStat.readLine());
                tokenStat.nextToken();
                String userUsage = tokenStat.nextToken();
                user = userUsage.substring(0, userUsage.indexOf("%"));
                process.destroy();
            }

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            freeResource(is, isr, brStat);
            return "100";
        } finally {
            freeResource(is, isr, brStat);
        }
        return user; // jvm cpu占用率
    }

    /**
     * 获取Linux环境下JVM的内存占用率
     */
    public String getMemoryRateForLinux() {
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        String remCount = "";
        try {
            String command = "top -b  -n 1 -H -p" + pid;
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();
            StringTokenizer ts = new StringTokenizer(in.readLine());
            int i = 1;
            while (ts.hasMoreTokens()) {
                i++;
                ts.nextToken();
                if (i == 10) {
                    remCount = ts.nextToken();
                }
            }
            in.close();
            pro.destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return remCount;
    }

    /**
     * 获取windows环境下jvm的内存占用率
     */
    public String getMemoryRateForWindows() {
        String command = "TASKLIST /NH /FO CSV /FI \"PID EQ " + pid + " \"";
        String remCount = "";
        BufferedReader in;
        String result = "";
        try {
            Process pro = Runtime.getRuntime().exec(command);
            in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringTokenizer ts = new StringTokenizer(in.readLine(), "\"");
            int i = 1;
            while (ts.hasMoreTokens()) {
                i++;
                ts.nextToken();
                if (i == 9) {
                    remCount = ts.nextToken().replace(",", "").replace("K", "").trim();
                }
            }
            long physicalJvmMem = Long.parseLong(remCount) / 1024;
            OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            long physicalTotal = osmxb.getTotalPhysicalMemorySize() / (1024 * 1024);
            result = formatNumber(physicalJvmMem / (float) physicalTotal);
            if (Float.parseFloat(result) > 1) {
                result = "1";
            } else if (Float.parseFloat(result) < 0) {
                result = "0";
            }
            in.close();
            pro.destroy();
        } catch (Exception e) {
            log.error("getThreadCountForWindows()报异常：" + e);
        }
        return String.valueOf((Float.parseFloat(result) * 100));
    }


    /**
     * 获取Linux服务器的语言环境
     */
    public String getLocale() {
        Process pro;
        Runtime r = Runtime.getRuntime();
        String command = "locale";
        BufferedReader in;
        StringTokenizer ts = null;
        try {
            pro = r.exec(command);
            in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            ts = new StringTokenizer(in.readLine());
            in.close();
            pro.destroy();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return ts.nextToken();
    }

    /**
     * 获取Linux环境下JVM的线程数
     */
    public int getThreadCountForLinux() {
        Process pro;
        Runtime r = Runtime.getRuntime();
        String command = "top -b -n 1 -H -p " + pid;
        BufferedReader in;
        int result = 0;
        try {
            pro = r.exec(new String[]{"sh", "-c", command});
            in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            in.readLine();
            StringTokenizer ts = new StringTokenizer(in.readLine());
            ts.nextToken();
            result = Integer.parseInt(ts.nextToken());
            in.close();
            pro.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 获取Windows环境下JVM的线程数
     */
    public int getThreadCountForWindows() {
        String command = "wmic process " + pid + "  list brief";
        int count = 0;
        BufferedReader in = null;
        try {
            Process pro = Runtime.getRuntime().exec(command);
            in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            in.readLine();
            in.readLine();
            StringTokenizer ts = new StringTokenizer(in.readLine());
            int i = 1;

            while (ts.hasMoreTokens()) {
                i++;
                ts.nextToken();
                if (i == 5) {
                    count = Integer.parseInt(ts.nextToken());
                }
            }
            in.close();
            pro.destroy();
        } catch (Exception e) {
            log.error("getThreadCountForWindows()报异常：" + e);
        }
        return count;
    }

    private void freeResource(InputStream is, InputStreamReader isr, BufferedReader br) {
        try {
            if (is != null) {
                is.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
    }

    /**
     * 读取CPU信息
     */
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            // Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = this.substring(line, capidx, cmdidx - 1).trim();
                String cmd = this.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.contains("javaw.exe")) {
                    continue;
                }
                // log.info("line="+line);
                if ("System Idle Process".equals(caption) || "System".equals(caption)) {
                    idletime += Long.parseLong(this.substring(line, kmtidx, rocidx - 1).trim());
                    idletime += Long.parseLong(this.substring(line, umtidx, wocidx - 1).trim());
                    continue;
                }

                kneltime += Long.parseLong(this.substring(line, kmtidx, rocidx - 1).trim());
                usertime += Long.parseLong(this.substring(line, umtidx, wocidx - 1).trim());
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取网口上下行速率
     */
    public String[] readInLine(BufferedReader input, String osType) {
        String rxResult = "";
        String txResult = "";
        StringTokenizer tokenStat = null;
        try {
            if ("linux".equals(osType)) { // 获取linux环境下的网口上下行速率
                String[] result = input.readLine().split(" ");
                int j = 0, k = 0;
                for (int i = 0; i < result.length; i++) {
                    if (result[i].contains("RX")) {
                        j++;
                        if (j == 2) {
                            rxResult = result[i + 1].split(":")[1];
                        }
                    }
                    if (result[i].contains("TX")) {
                        k++;
                        if (k == 2) {
                            txResult = result[i + 1].split(":")[1];
                            break;
                        }
                    }
                }

            } else { // 获取windows环境下的网口上下行速率
                input.readLine();
                input.readLine();
                input.readLine();
                input.readLine();
                tokenStat = new StringTokenizer(input.readLine());
                tokenStat.nextToken();
                rxResult = tokenStat.nextToken();
                txResult = tokenStat.nextToken();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new String[]{rxResult, txResult};
    }

    /**
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：
     *
     * @param src      要截取的字符串
     * @param startIdx 开始坐标（包括该坐标)
     * @param endIdx   截止坐标（包括该坐标）
     * @return String
     */
    private String substring(String src, int startIdx, int endIdx) {
        byte[] b = src.getBytes();
        StringBuilder tgt = new StringBuilder();
        for (int i = startIdx; i <= endIdx; i++) {
            tgt.append((char) b[i]);
        }
        return tgt.toString();
    }

    /**
     * 格式化浮点数(float 和 double)，保留两位小数
     */
    private String formatNumber(Object obj) {
        String result = "";
        if ("Float".equals(obj.getClass().getSimpleName())) {
            result = new Formatter().format("%.2f", (float) obj).toString();
        } else if ("Double".equals(obj.getClass().getSimpleName())) {
            result = new Formatter().format("%.2f", (double) obj).toString();
        }
        return result;
    }

    /**
     * 测试方法 ：监测java执行相关命令后是否能获取到结果集(注：此方法执行后会中断程序的执行，测试完后请注释掉)
     */
    public void testGetInput(BufferedReader in) {
        int y = 0;
        try {
            while ((y = in.read()) != -1) {
                System.out.print((char) y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class SysInfoAcquirerTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                System.out.println("任务开始：");
                long startTime = System.currentTimeMillis();
                int threadCount = getThreadCount();
                String cpuRate = getCPURate(); // CPU使用率
                String memoryRate = getMemoryRate(); // 内存占用率
                JSONObject jsonObj = JSON.parseObject(getNetworkThroughput());
                String upSpeed = jsonObj.getString("txPercent");// 上行速度
                String downSpeed = jsonObj.getString("rxPercent"); // 下行速度
                System.out.println("JVM  PID：" + pid);
                System.out.println("JVM 线程数：" + threadCount);
                System.out.println("内存占用率：" + memoryRate + "%");
                System.out.println("CPU使用率：" + cpuRate + "%");
                System.out.println("上行速度：" + upSpeed + "MB/s 下行速度：" + downSpeed + "MB/s");
                //后续操作为将采集到的数据存放到数据库，可自行设计
                long endTime = System.currentTimeMillis();
                System.out.println("任务总耗时：" + (endTime - startTime) / (1000 * 60) + "分钟");
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.toString());
            }
        }

    }

    @PreDestroy
    public void destroy() {
        log.info("do nothing in @PreDestroy method");
    }

}
