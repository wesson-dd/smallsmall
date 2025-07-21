package com.small.pojo;

import java.util.*;
import java.util.stream.Collectors;

public class OptimizedRouteGrouper {

    public static class Route {
        private String id;
        private String deptCode;
        private String workday;

        public Route(String id, String deptCode, String workday) {
            this.id = id;
            this.deptCode = deptCode;
            this.workday = workday;
        }
        public String getDeptCode() { return deptCode; }
        public String getWorkday() { return workday; }
        public String getId() { return id; }

        // 将workday转换为位掩码（'1'->bit0, '7'->bit6）
        public int toBitMask() {
            int mask = 0;
            for (char c : workday.toCharArray()) {
                int bitPosition = c - '1';  // '1'->0, '2'->1, ..., '7'->6
                if (bitPosition >= 0 && bitPosition < 7) {
                    mask |= (1 << bitPosition);
                }
            }
            return mask;
        }

        @Override
        public String toString() {
            return id + "(" + workday + ")";
        }
    }

    public static Map<String, List<List<Route>>> groupRoutes(List<Route> routes) {
        Map<String, List<Route>> deptMap = routes.stream()
                .collect(Collectors.groupingBy(Route::getDeptCode));

        Map<String, List<List<Route>>> result = new HashMap<>();

        deptMap.forEach((dept, deptRoutes) -> {
            result.put(dept, optimizeGrouping(deptRoutes));
        });
        return result;
    }

    // 使用图着色算法优化分组
    private static List<List<Route>> optimizeGrouping(List<Route> routes) {
        // 分离全字符路线(1234567)
        List<Route> fullCoverageRoutes = routes.stream()
                .filter(route -> route.toBitMask() == 0x7F)
                .collect(Collectors.toList());

        List<Route> otherRoutes = routes.stream()
                .filter(route -> route.toBitMask() != 0x7F)
                .collect(Collectors.toList());

        // 全字符路线各自独立成组
        List<List<Route>> groups = fullCoverageRoutes.stream()
                .map(Collections::singletonList)
                .collect(Collectors.toList());

        if (otherRoutes.isEmpty()) {
            return groups;
        }

        // 为其他路线构建冲突图
        int n = otherRoutes.size();
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        // 计算位掩码数组
        int[] masks = new int[n];
        for (int i = 0; i < n; i++) {
            masks[i] = otherRoutes.get(i).toBitMask();
        }

        // 构建冲突图：如果路线i和j有重叠字符，则添加边
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((masks[i] & masks[j]) != 0) {
                    graph[i].add(j);
                    graph[j].add(i);
                }
            }
        }

        // 计算顶点度数并排序（度数高的先着色）
        int[] degrees = new int[n];
        for (int i = 0; i < n; i++) {
            degrees[i] = graph[i].size();
        }

        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (a, b) -> degrees[b] - degrees[a]);

        // 执行贪心着色算法
        int[] colors = new int[n]; // 0表示未着色
        int maxColor = 0;
        boolean[] availableColors;

        for (int idx : indices) {
            // 查找所有邻居已使用的颜色
            Set<Integer> usedColors = new HashSet<>();
            for (int neighbor : graph[idx]) {
                if (colors[neighbor] != 0) {
                    usedColors.add(colors[neighbor]);
                }
            }

            // 分配最小可用颜色
            int color = 1;
            while (usedColors.contains(color)) {
                color++;
            }

            colors[idx] = color;
            if (color > maxColor) {
                maxColor = color;
            }
        }

        // 根据颜色创建分组
        for (int color = 1; color <= maxColor; color++) {
            List<Route> group = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (colors[i] == color) {
                    group.add(otherRoutes.get(i));
                }
            }
            groups.add(group);
        }

        return groups;
    }

    // 打印分组结果
    private static void printGroupedRoutes(Map<String, List<List<Route>>> grouped) {
        grouped.forEach((dept, groups) -> {
            System.out.println("部门: " + dept);
            for (int i = 0; i < groups.size(); i++) {
                System.out.println("  分组 " + (i+1) + ": " +
                        groups.get(i).stream()
                                .map(Route::toString)
                                .collect(Collectors.joining(", ")));
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("========== 修正用例1: 销售部门优化合并 ==========");
        testSalesCase();

        System.out.println("\n========== 修正用例2: 行政部门完全合并 ==========");
        testAdminCase();

        System.out.println("\n========== 测试用例3: 全字符路线处理 ==========");
        testFullCoverageCase();
    }

    private static void testSalesCase() {
        List<Route> routes = Arrays.asList(
                // 销售部门（应分为2组）
                new Route("A1", "销售", "135"),  // 组1
                new Route("A2", "销售", "246"),  // 组1
                new Route("A3", "销售", "7"),    // 组1
                new Route("A4", "销售", "123"),  // 组2
                new Route("A5", "销售", "456"),  // 组2
                new Route("A6", "销售", "7")     // 组2
        );

        Map<String, List<List<Route>>> grouped = groupRoutes(routes);
        printGroupedRoutes(grouped);
    }

    private static void testAdminCase() {
        List<Route> routes = Arrays.asList(
                // 行政部门（应合并为1组）
                new Route("C1", "行政", "1"),
                new Route("C2", "行政", "2"),
                new Route("C3", "行政", "3"),
                new Route("C4", "行政", "4"),
                new Route("C5", "行政", "5"),
                new Route("C6", "行政", "6"),
                new Route("C7", "行政", "7")
        );

        Map<String, List<List<Route>>> grouped = groupRoutes(routes);
        printGroupedRoutes(grouped);
    }

    private static void testFullCoverageCase() {
        List<Route> routes = Arrays.asList(
                // 测试部门（混合全字符路线）
                new Route("F1", "测试", "1234567"), // 独立组
                new Route("P1", "测试", "1"),       // 可合并组
                new Route("P2", "测试", "2"),       // 可合并组
                new Route("P3", "测试", "3"),       // 可合并组
                new Route("F2", "测试", "1234567")  // 独立组
        );

        Map<String, List<List<Route>>> grouped = groupRoutes(routes);
        printGroupedRoutes(grouped);
    }
}
