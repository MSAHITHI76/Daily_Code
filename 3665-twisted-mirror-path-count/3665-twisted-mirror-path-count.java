class Solution {
    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        final int MOD = 1_000_000_007;

        int[][] right = new int[m][n];
        int[][] down = new int[m][n];

        // Precompute final destinations.
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {

                if (grid[i][j] == 0) {
                    right[i][j] = i * n + j;
                    down[i][j] = i * n + j;
                } else {
                    // Entering this mirror while moving right:
                    // Move below it, then continue moving down.
                    right[i][j] = (i + 1 < m)
                            ? down[i + 1][j] : -1;

                    // Entering this mirror while moving down:
                    // Move right of it, then continue moving right.
                    down[i][j] = (j + 1 < n)
                            ? right[i][j + 1] : -1;
                }
            }
        }

        long[][] dp = new long[m][n];
        dp[0][0] = 1;

        // Count paths using row-major traversal.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (dp[i][j] == 0) continue;

                long ways = dp[i][j];

                // Attempt to move right.
                if (j + 1 < n) {
                    int dest = right[i][j + 1];

                    if (dest != -1) {
                        int x = dest / n;
                        int y = dest % n;

                        dp[x][y] = (dp[x][y] + ways) % MOD;
                    }
                }

                // Attempt to move down.
                if (i + 1 < m) {
                    int dest = down[i + 1][j];

                    if (dest != -1) {
                        int x = dest / n;
                        int y = dest % n;

                        dp[x][y] = (dp[x][y] + ways) % MOD;
                    }
                }
            }
        }

        return (int) dp[m - 1][n - 1];
    }
}