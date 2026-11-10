class Solution {
    public int climbStairs(int n, int[] costs) {
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;

            for (int j = 1; j <= 3; j++) {
                int prev = i - j;

                if (prev >= 0) {
                    int cost = dp[prev] + costs[i - 1] + j * j;
                    dp[i] = Math.min(dp[i], cost);
                }
            }
        }

        return dp[n];
    }
}