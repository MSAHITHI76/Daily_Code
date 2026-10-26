class Solution {
    public int countIslands(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int validIslandsCount = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    // Calculate the total value of the current island using DFS
                    long islandTotal = dfs(grid, i, j);
                    
                    // Check if the total value is divisible by k
                    if (islandTotal % k == 0) {
                        validIslandsCount++;
                    }
                }
            }
        }
        
        return validIslandsCount;
    }
    
    private long dfs(int[][] grid, int r, int c) {
        int m = grid.length;
        int n = grid[0].length;
        
        // Out of bounds or water cell
        if (r < 0 || c < 0 || r >= m || c >= n || grid[r][c] == 0) {
            return 0;
        }
        
        long sum = grid[r][c];
        
        // Mark the current cell as visited by setting it to 0
        grid[r][c] = 0;
        
        // Recursively visit all 4-directionally connected land cells
        sum += dfs(grid, r + 1, c);
        sum += dfs(grid, r - 1, c);
        sum += dfs(grid, r, c + 1);
        sum += dfs(grid, r, c - 1);
        
        return sum;
    }
}