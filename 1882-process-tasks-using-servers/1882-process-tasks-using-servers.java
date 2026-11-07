import java.util.PriorityQueue;

class Solution {
    public int[] assignTasks(int[] servers, int[] tasks) {
        int n = servers.length;
        int m = tasks.length;
        int[] ans = new int[m];

        // Priority queue for available servers: ordered by weight, then by index
        // Stores int[] {weight, index}
        PriorityQueue<int[]> available = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });

        // Priority queue for unavailable servers: ordered by free time, then weight, then index
        // Stores long[] {freeTime, weight, index}
        PriorityQueue<long[]> unavailable = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Long.compare(a[0], b[0]);
            }
            if (a[1] != b[1]) {
                return Long.compare(a[1], b[1]);
            }
            return Long.compare(a[2], b[2]);
        });

        // Initially all servers are available at time 0
        for (int i = 0; i < n; i++) {
            available.offer(new int[] {servers[i], i});
        }

        long currentTime = 0;

        for (int j = 0; j < m; j++) {
            currentTime = Math.max(currentTime, j);

            // If no servers are available, jump time to when the next server frees up
            if (available.isEmpty() && !unavailable.isEmpty()) {
                currentTime = Math.max(currentTime, unavailable.peek()[0]);
            }

            // Move all servers that have finished their tasks back to the available pool
            while (!unavailable.isEmpty() && unavailable.peek()[0] <= currentTime) {
                long[] server = unavailable.poll();
                available.offer(new int[] {(int) server[1], (int) server[2]}); // {weight, index}
            }

            // Assign the current task to the top available server
            int[] bestServer = available.poll();
            int serverWeight = bestServer[0];
            int serverIndex = bestServer[1];

            ans[j] = serverIndex;

            // Mark this server as busy until currentTime + tasks[j]
            long freeTime = currentTime + tasks[j];
            unavailable.offer(new long[] {freeTime, serverWeight, serverIndex});
        }

        return ans;
    }
}