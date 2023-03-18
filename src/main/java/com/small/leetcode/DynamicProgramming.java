package com.small.leetcode;

import org.springframework.util.StopWatch;

/**
 * 动态规划
 *
 * @author wesson
 * Create at 2023/3/18 20:11 周六
 */
public class DynamicProgramming {
    public static void main(String[] args) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start("1");
        System.out.println(climbStairs(30));
        stopWatch.stop();
        stopWatch.start("2");
        System.out.println(climbStairs2(30));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    /*有 N 阶楼梯，每次可以上一阶或者两阶，求有多少种上楼梯的方法。*/
    public static int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        // 前一步
        int step1 = 1;
        // 前两步
        int step2 = 2;

        for (int i = 2; i < n; i++) {
            int current = step1 + step2;
            step1 = step2;
            step2 = current;
        }
        return step2;
    }

    /**
     * 递归效率不高
     * ---------------------------------------------
     * ns         %     Task name
     * ---------------------------------------------
     * 000440403  010%  1
     * 004065382  090%  2
     */
    public static int climbStairs2(int n) {
        if (n <= 2) {
            return n;
        }
        return climbStairs2(n - 1) + climbStairs2(n - 2);
    }

    /**
     * 抢劫一排住户，但是不能抢邻近的住户，求最大抢劫量。
     *
     * @param nums
     * @return
     */
    public int rob(int[] nums) {
        // 前两个的最大抢劫量
        int pre2 = 0;
        // 前一个的最大抢劫量
        int pre1 = 0;
        for (int num : nums) {
            int cur = Math.max(pre2 + num, pre1);
            pre2 = pre1;
            pre1 = cur;
        }
        return pre1;
    }

    /**
     * 环形住户
     * 抢劫一排住户，但是不能抢邻近的住户，求最大抢劫量。
     *
     * @param nums
     * @return
     */
    public int rob2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        if (n == 1) {
            return nums[0];
        }
        return Math.max(rob(nums, 0, n - 2), rob(nums, 1, n - 1));
    }

    private int rob(int[] nums, int first, int last) {
        int pre2 = 0, pre1 = 0;
        for (int i = first; i <= last; i++) {
            int cur = Math.max(pre1, pre2 + nums[i]);
            pre2 = pre1;
            pre1 = cur;
        }
        return pre1;
    }

    /**
     * 题目描述：有 N 个 信 和 信封，它们被打乱，求错误装信方式的数量。
     * <p>
     * 定义一个数组 dp 存储错误方式数量，dp[i] 表示前 i 个信和信封的错误方式数量。假设第 i 个信装到第 j 个信封里面，而第 j 个信装到第 k 个信封里面。根据 i 和 k 是否相等，有两种情况：
     * <p>
     * ● i==k，交换 i 和 j 的信后，它们的信和信封在正确的位置，但是其余 i-2 封信有 dp[i-2] 种错误装信的方式。由于 j 有 i-1 种取值，因此共有 (i-1)*dp[i-2] 种错误装信方式。
     * ● i != k，交换 i 和 j 的信后，第 i 个信和信封在正确的位置，其余 i-1 封信有 dp[i-1] 种错误装信方式。由于 j 有 i-1 种取值，因此共有 (i-1)*dp[i-1] 种错误装信方式。
     */
    public int errorNum(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }

        return (n - 1) * errorNum(n - 2) + (n - 1) * errorNum(n - 1);
    }


}
