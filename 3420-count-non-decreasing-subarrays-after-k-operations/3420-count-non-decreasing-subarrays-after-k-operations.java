
class Solution {

    class SegmentTree {
        long[] sum, max, lazy;
        boolean[] pending;
        int n;

        SegmentTree(int n) {
            this.n = n;
            sum = new long[4 * n];
            max = new long[4 * n];
            lazy = new long[4 * n];
            pending = new boolean[4 * n];
        }

        void apply(int node, int l, int r, long val) {
            sum[node] = val * (r - l + 1);
            max[node] = val;
            lazy[node] = val;
            pending[node] = true;
        }

        void push(int node, int l, int r) {
            if (!pending[node] || l == r) return;

            int mid = (l + r) / 2;

            apply(node * 2, l, mid, lazy[node]);
            apply(node * 2 + 1, mid + 1, r, lazy[node]);

            pending[node] = false;
        }

        void update(int node, int l, int r,
                    int ql, int qr, long val) {

            if (ql > r || qr < l || ql > qr) return;

            if (ql <= l && r <= qr) {
                apply(node, l, r, val);
                return;
            }

            push(node, l, r);

            int mid = (l + r) / 2;

            update(node * 2, l, mid, ql, qr, val);
            update(node * 2 + 1, mid + 1, r, ql, qr, val);

            sum[node] = sum[node * 2] + sum[node * 2 + 1];
            max[node] = Math.max(max[node * 2], max[node * 2 + 1]);
        }

        long querySum(int node, int l, int r,
                      int ql, int qr) {

            if (ql > r || qr < l) return 0;

            if (ql <= l && r <= qr) {
                return sum[node];
            }

            push(node, l, r);

            int mid = (l + r) / 2;

            return querySum(node * 2, l, mid, ql, qr)
                 + querySum(node * 2 + 1, mid + 1, r, ql, qr);
        }

        int findFirst(int node, int l, int r,
                      int ql, int qr, long val) {

            if (r < ql || l > qr || max[node] < val) {
                return -1;
            }

            if (l == r) return l;

            push(node, l, r);

            int mid = (l + r) / 2;

            int res = findFirst(
                node * 2, l, mid, ql, qr, val
            );

            if (res != -1) return res;

            return findFirst(
                node * 2 + 1, mid + 1, r, ql, qr, val
            );
        }

        void update(int l, int r, long val) {
            if (l <= r) {
                update(1, 0, n - 1, l, r, val);
            }
        }

        long querySum(int l, int r) {
            if (l > r) return 0;
            return querySum(1, 0, n - 1, l, r);
        }

        int findFirst(int l, int r, long val) {
            if (l > r) return -1;
            return findFirst(1, 0, n - 1, l, r, val);
        }
    }

    public long countNonDecreasingSubarrays(int[] nums, int k) {

        int n = nums.length;

        long[] prefix = new long[n + 1];

        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        SegmentTree seg = new SegmentTree(n);

        long answer = 0;
        int right = n - 1;
        long limit = k;

        for (int left = n - 1; left >= 0; left--) {

            long x = nums[left];

            // Find the first existing prefix maximum >= x.
            int pos = seg.findFirst(left + 1, right, x);

            // Raise all smaller prefix maxima to x.
            int end = (pos == -1) ? right : pos - 1;

            seg.update(left + 1, end, x);

            // Set the prefix maximum at the new left position.
            seg.update(left, left, x);

            // Shrink the window until its cost is within k.
            while (right > left) {

                long currentSum = seg.querySum(left, right);

                long originalSum =
                    prefix[right + 1] - prefix[left];

                long operations = currentSum - originalSum;

                if (operations <= limit) {
                    break;
                }

                right--;
            }

            answer += right - left + 1L;
        }

        return answer;
    }
}
