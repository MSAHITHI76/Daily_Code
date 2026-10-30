import java.util.*;

class Solution {
    public int shortestPath(int n, int[][] edges, String labels, int k) {
        // Build adjacency list: graph.get(u) = list of int[] {v, weight}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
        }
        
        // Priority queue stores states: {weight, current_node, last_char_code, consecutive_count}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        
        int startChar = labels.charAt(0) - 'a';
        pq.offer(new int[]{0, 0, startChar, 1});
        
        // Use a HashMap to store the minimum weight to reach each state (node, last_char, count)
        // State packing key: node * 1326 + lastChar * 51 + count 
        // (Since max n = 50000, 50000 * 26 * 51 fits safely, or we can use a custom Map or string encoding)
        Map<Long, Integer> dist = new HashMap<>();
        
        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int w = curr[0];
            int u = curr[1];
            int lastChar = curr[2];
            int count = curr[3];
            
            // If we reached the target node
            if (u == n - 1) {
                return w;
            }
            
            long stateKey = ((long) u * 1326) + (lastChar * 51) + count;
            
            if (dist.getOrDefault(stateKey, Integer.MAX_VALUE) < w) {
                continue;
            }
            
            for (int[] neighbor : graph.get(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                int nextChar = labels.charAt(v) - 'a';
                
                int newCount = (nextChar == lastChar) ? count + 1 : 1;
                
                if (newCount <= k) {
                    int newW = w + weight;
                    long nextStateKey = ((long) v * 1326) + (nextChar * 51) + newCount;
                    
                    if (newW < dist.getOrDefault(nextStateKey, Integer.MAX_VALUE)) {
                        dist.put(nextStateKey, newW);
                        pq.offer(new int[]{newW, v, nextChar, newCount});
                    }
                }
            }
        }
        
        return -1;
    }
}