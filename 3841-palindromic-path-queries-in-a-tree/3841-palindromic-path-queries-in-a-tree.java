import java.util.*;

class Solution {
    // Adjacency list for the tree
    private List<Integer>[] adj;
    private int[] parent, depth, heavy, head, pos;
    private char[] charArray;
    private int curPos;
    private SegmentTree segTree;

    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }

        charArray = s.toCharArray();
        parent = new int[n];
        depth = new int[n];
        heavy = new int[n];
        head = new int[n];
        pos = new int[n];
        Arrays.fill(heavy, -1);

        dfs1(0, -1, 0);
        curPos = 0;
        dfs2(0, 0);

        // Segment tree stores 26-bit XOR masks for the flattened tree nodes
        int[] initialValues = new int[n];
        for (int i = 0; i < n; i++) {
            initialValues[pos[i]] = (1 << (charArray[i] - 'a'));
        }
        segTree = new SegmentTree(initialValues);

        List<Boolean> result = new ArrayList<>();

        for (String q : queries) {
            String[] parts = q.split(" ");
            if (parts[0].equals("update")) {
                int u = Integer.parseInt(parts[1]);
                char newChar = parts[2].charAt(0);
                
                int oldMask = (1 << (charArray[u] - 'a'));
                int newMask = (1 << (newChar - 'a'));
                charArray[u] = newChar;
                
                segTree.update(1, 0, n - 1, pos[u], newMask);
            } else {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                
                int pathMask = queryPath(u, v);
                // Check if at most one bit is set
                boolean isPalindrome = (pathMask & (pathMask - 1)) == 0;
                result.add(isPalindrome);
            }
        }

        return result;
    }

    private void dfs1(int u, int p, int d) {
        parent[u] = p;
        depth[u] = d;
        int maxSubtree = 0;
        for (int v : adj[u]) {
            if (v != p) {
                dfs1(v, u, d + 1);
                int subSize = 1; // placeholder for subtree size calculation if needed
                if (heavy[u] == -1 || subSize > maxSubtree) {
                    maxSubtree = subSize;
                    heavy[u] = v;
                }
            }
        }
    }

    private void dfs2(int u, int h) {
        head[u] = h;
        pos[u] = curPos++;
        if (heavy[u] != -1) {
            dfs2(heavy[u], h);
        }
        for (int v : adj[u]) {
            if (v != parent[u] && v != heavy[u]) {
                dfs2(v, v);
            }
        }
    }

    private int queryPath(int u, int v) {
        int resMask = 0;
        while (head[u] != head[v]) {
            if (depth[head[u]] > depth[head[v]]) {
                int temp = u; u = v; v = temp; // swap
            }
            resMask ^= segTree.query(1, 0, charArray.length - 1, pos[head[v]], pos[v]);
            v = parent[head[v]];
        }
        if (depth[u] > depth[v]) {
            int temp = u; u = v; v = temp; // swap
        }
        resMask ^= segTree.query(1, 0, charArray.length - 1, pos[u], pos[v]);
        return resMask;
    }

    // Standard Segment Tree supporting Range XOR queries and Point updates
    static class SegmentTree {
        private int[] tree;

        public SegmentTree(int[] arr) {
            int n = arr.length;
            tree = new int[4 * n];
            build(1, 0, n - 1, arr);
        }

        private void build(int node, int start, int end, int[] arr) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(2 * node, start, mid, arr);
            build(2 * node + 1, mid + 1, end, arr);
            tree[node] = tree[2 * node] ^ tree[2 * node + 1];
        }

        public void update(int node, int start, int end, int idx, int val) {
            if (start == end) {
                tree[node] = val;
                return;
            }
            int mid = (start + end) / 2;
            if (idx >= start && idx <= mid) {
                update(2 * node, start, mid, idx, val);
            } else {
                update(2 * node + 1, mid + 1, end, idx, val);
            }
            tree[node] = tree[2 * node] ^ tree[2 * node + 1];
        }

        public int query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return query(2 * node, start, mid, l, r) ^ query(2 * node + 1, mid + 1, end, l, r);
        }
    }
}