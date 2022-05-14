package com.small.util;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    private static final int FAULT_LENGTH = 10;
    public static final String WINDOWS = "windows";
    public static final String LINUX = "linux";
    private final String isWindowsOrLinux = isWindowsOrLinux();
    private String pid = "";

    private final Timer sysInfoGetTimer = new Timer("sysInfoGet");


    /**
     * 初始化bean的时候就立即获取JVM进程的PID及执行任务
     */
    @PostConstruct
    public void init() {
        if (WINDOWS.equals(isWindowsOrLinux)) {
            getJvmPIDOnWindows();
        } else {
            getJvmPIDOnLinux();
        }
        sysInfoGetTimer.schedule(new SysInfoAcquirerTimerTask(), 10 * 1000L, PERIOD_TIME);
    }

    /**
     * 判断是服务器的系统类型是Windows 还是 Linux
     */
    public String isWindowsOrLinux() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith(WINDOWS)) {
            return WINDOWS;
        }
        return LINUX;
    }

    /**
     * 获取JVM 的CPU占用率（%）
     */
    public String getCPURate() {
        if (WINDOWS.equals(isWindowsOrLinux)) {
            return getCPURateForWindows();
        }
        return getCPURateForLinux();

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
        BufferedReader in;
        Process pro;
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
        if (WINDOWS.equals(isWindowsOrLinux)) {
            return getMemoryRateForWindows();
        }
        return getMemoryRateForLinux();

    }

    /**
     * 获取JVM 线程数
     */
    public int getThreadCount() {
        if (WINDOWS.equals(isWindowsOrLinux)) {
            return getThreadCountForWindows();
        }
        return getThreadCountForLinux();

    }


    /**
     * 获取网口吞吐量（MB/s）
     */
    public String getNetworkThroughput() {
        if (WINDOWS.equals(isWindowsOrLinux)) {
            return getNetworkThroughputForWindows();
        }
        return getNetworkThroughputForLinux();

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
        try {
            String command = "netstat -e";
            pro1 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String[] result1 = readInLine(input, WINDOWS);
            Thread.sleep(SLEEP_TIME);
            pro2 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            String[] result2 = readInLine(input, WINDOWS);
            rxPercent = formatNumber(String.valueOf(Long.parseLong(result2[0]) - Long.parseLong(result1[0])), String.valueOf(1024 * 1024 * (SLEEP_TIME / 1000)));
            txPercent = formatNumber(String.valueOf(Long.parseLong(result2[1]) - Long.parseLong(result1[1])), String.valueOf(1024 * 1024 * (SLEEP_TIME / 1000)));
            input.close();
            pro1.destroy();
            pro2.destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        final JSONObject object = JSONObject.of("rxPercent", rxPercent, "txPercent", txPercent);
        return JSON.toJSONString(object, "yyyy-MM-dd HH:mm:ss", JSONWriter.Feature.WriteNulls);
    }

    /**
     * 获取Linux环境下网口的上下行速率
     */
    public String getNetworkThroughputForLinux() {
        Process pro1;
        Process pro2;
        Runtime r = Runtime.getRuntime();
        BufferedReader input;
        String rxPercent = "";
        String txPercent = "";
        try {
            String command = "watch ifconfig";
            pro1 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String[] result1 = readInLine(input, LINUX);
            Thread.sleep(SLEEP_TIME);
            pro2 = r.exec(command);
            input = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            String[] result2 = readInLine(input, LINUX);
            txPercent = formatNumber(String.valueOf(Long.parseLong(result2[1]) - Long.parseLong(result1[1])), String.valueOf(1024 * 1024 * (SLEEP_TIME / 1000)));
            rxPercent = formatNumber(String.valueOf(Long.parseLong(result2[0]) - Long.parseLong(result1[0])), String.valueOf(1024 * 1024 * (SLEEP_TIME / 1000)));
            input.close();
            pro1.destroy();
            pro2.destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        final JSONObject object = JSONObject.of("rxPercent", rxPercent, "txPercent", txPercent);
        return JSON.toJSONString(object, "yyyy-MM-dd HH:mm:ss", JSONWriter.Feature.NullAsDefaultValue);
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
                long idleTime = c1[0] - c0[0];
                long busyTime = c1[1] - c0[1];

                return String.valueOf(PERCENT * (busyTime) / (busyTime + idleTime));
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
            log.info("Linux版本: " + linuxVersion);
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "top -b -p " + pid});

            // top命令默认3秒动态更新结果信息，让线程睡眠5秒以便获取最新结果
            ThreadUtil.sleep(CPUTIME);
            is = process.getInputStream();

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

                log.info(user + " , " + system + " , " + nice);

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
            log.info(ioe.getMessage());
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
        Process pro;
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
            result = formatNumber(String.valueOf(physicalJvmMem), String.valueOf(physicalTotal));
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
        assert ts != null;
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
        BufferedReader in;
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
            if (line == null || line.length() < FAULT_LENGTH) {
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
        StringTokenizer tokenStat;
        try {
            if (LINUX.equals(osType)) {
                // 获取linux环境下的网口上下行速率
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

            } else {
                // 获取windows环境下的网口上下行速率
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
     *
     * @param arg1 被除数
     * @param arg2 除数
     */
    private String formatNumber(String arg1, String arg2) {
        return new BigDecimal(arg1).divide(new BigDecimal(arg2), 2, RoundingMode.DOWN).toString();
    }


    class SysInfoAcquirerTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                int threadCount = getThreadCount();
                String cpuRate = getCPURate();
                // CPU使用率
                String memoryRate = getMemoryRate();
                // 内存占用率
                JSONObject jsonObj = JSON.parseObject(getNetworkThroughput());
                String upSpeed = jsonObj.getString("txPercent");
                // 上行速度
                String downSpeed = jsonObj.getString("rxPercent");
                // 下行速度
                log.info("JVM  PID：" + pid);
                log.info("JVM 线程数：" + threadCount);
                log.info("内存占用率：" + memoryRate + "%");
                log.info("CPU使用率：" + cpuRate + "%");
                log.info("上行速度：" + upSpeed + "MB/s 下行速度：" + downSpeed + "MB/s");
                //后续操作为将采集到的数据存放到数据库，可自行设计
                long endTime = System.currentTimeMillis();
                log.info("任务总耗时：" + (endTime - startTime) / (1000 * 60) + "分钟");
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
