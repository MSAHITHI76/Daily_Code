
import java.util.*;

class Solution {
    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;

        long[] cost = new long[n - x + 1];

        // Coordinate compression
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int m = 0;
        for (int v : sorted) {
            if (m == 0 || sorted[m - 1] != v) {
                sorted[m++] = v;
            }
        }

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Arrays.binarySearch(sorted, 0, m, nums[i]) + 1;
        }

        // Fenwick trees for count and sum
        long[] bitCount = new long[m + 1];
        long[] bitSum = new long[m + 1];

        long windowSum = 0;

        for (int i = 0; i < x; i++) {
            update(bitCount, a[i], 1);
            update(bitSum, a[i], nums[i]);
            windowSum += nums[i];
        }

        for (int i = 0; i <= n - x; i++) {
            int medianRank = (x + 1) / 2;
            int medIndex = findKth(bitCount, medianRank);
            long median = sorted[medIndex - 1];

            long leftCount = query(bitCount, medIndex);
            long leftSum = query(bitSum, medIndex);

            long rightCount = x - leftCount;
            long rightSum = windowSum - leftSum;

            cost[i] = median * leftCount - leftSum
                    + rightSum - median * rightCount;

            if (i < n - x) {
                update(bitCount, a[i], -1);
                update(bitSum, a[i], -nums[i]);

                update(bitCount, a[i + x], 1);
                update(bitSum, a[i + x], nums[i + x]);

                windowSum += (long) nums[i + x] - nums[i];
            }
        }

        // DP: minimum cost to select j non-overlapping windows
        long INF = Long.MAX_VALUE / 4;

        long[] prev = new long[n + 1];
        long[] curr = new long[n + 1];

        Arrays.fill(prev, 0);

        for (int j = 1; j <= k; j++) {
            Arrays.fill(curr, INF);

            for (int i = 1; i <= n; i++) {
                // Skip current element
                curr[i] = curr[i - 1];

                // Select subarray ending at i-1
                if (i >= x && prev[i - x] != INF) {
                    curr[i] = Math.min(
                        curr[i],
                        prev[i - x] + cost[i - x]
                    );
                }
            }

            long[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }

    private void update(long[] bit, int index, long value) {
        while (index < bit.length) {
            bit[index] += value;
            index += index & -index;
        }
    }

    private long query(long[] bit, int index) {
        long sum = 0;
        while (index > 0) {
            sum += bit[index];
            index -= index & -index;
        }
        return sum;
    }

    private int findKth(long[] bit, int k) {
        int index = 0;
        int step = Integer.highestOneBit(bit.length - 1);

        while (step > 0) {
            int next = index + step;

            if (next < bit.length && bit[next] < k) {
                index = next;
                k -= (int) bit[next];
            }

            step >>= 1;
        }

        return index + 1;
    }
}
