
class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        // Build adjacency list
        List<Integer>[] children = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            children[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            children[parent[i]].add(i);
        }

        int[] newParent = parent.clone();

        // Stores the nearest ancestor for each character
        int[] last = new int[26];
        Arrays.fill(last, -1);

        // Find new parents using the original tree
        dfs(0, children, last, newParent, s);

        // Build the modified tree
        List<Integer>[] newChildren = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            newChildren[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            newChildren[newParent[i]].add(i);
        }

        // Calculate subtree sizes
        int[] answer = new int[n];
        calculateSize(0, newChildren, answer);

        return answer;
    }

    private void dfs(int node, List<Integer>[] children,
                     int[] last, int[] newParent, String s) {

        int c = s.charAt(node) - 'a';

        int previous = last[c];

        if (node != 0 && previous != -1) {
            newParent[node] = previous;
        }

        last[c] = node;

        for (int child : children[node]) {
            dfs(child, children, last, newParent, s);
        }

        // Restore ancestor state while backtracking
        last[c] = previous;
    }

    private int calculateSize(int node,
                              List<Integer>[] children,
                              int[] answer) {
        int size = 1;

        for (int child : children[node]) {
            size += calculateSize(child, children, answer);
        }

        answer[node] = size;
        return size;
    }
}
