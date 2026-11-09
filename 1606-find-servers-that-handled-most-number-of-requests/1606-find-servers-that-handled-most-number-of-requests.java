import java.util.*;

class Solution {
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        TreeSet<Integer> available = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            available.add(i);
        }
        
        // Priority queue stores {end_time, server_id}
        PriorityQueue<int[]> busy = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        int[] requestCount = new int[k];
        
        for (int i = 0; i < arrival.length; i++) {
            int currTime = arrival[i];
            int duration = load[i];
            
            // Free up servers that have finished their tasks by current time
            while (!busy.isEmpty() && busy.peek()[0] <= currTime) {
                available.add(busy.poll()[1]);
            }
            
            // If all servers are busy, drop the request
            if (available.isEmpty()) {
                continue;
            }
            
            // Find the next available server >= (i % k)
            Integer serverId = available.ceiling(i % k);
            if (serverId == null) {
                serverId = available.first(); // Wrap around to 0
            }
            
            // Assign request to the server
            available.remove(serverId);
            busy.offer(new int[] { currTime + duration, serverId });
            requestCount[serverId]++;
        }
        
        // Find the maximum number of requests handled
        int maxRequests = 0;
        for (int count : requestCount) {
            maxRequests = Math.max(maxRequests, count);
        }
        
        // Collect all busiest servers
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (requestCount[i] == maxRequests) {
                result.add(i);
            }
        }
        
        return result;
    }
}