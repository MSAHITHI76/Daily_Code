import java.util.*;

class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int ans = 0;

        for (int k = 1; k <= n; k++) {
            if (n % k != 0) {
                continue;
            }

            boolean valid = true;
            int prevMax = Integer.MIN_VALUE;

            for (int start = 0; start < n && valid; start += k) {
                int end = start + k;
                int descents = 0;
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;

                for (int i = start; i < end; i++) {
                    min = Math.min(min, nums[i]);
                    max = Math.max(max, nums[i]);

                    if (i > start && nums[i - 1] > nums[i]) {
                        descents++;
                    }
                }

                if (descents > 1) {
                    valid = false;
                } else if (descents == 1 &&
                           nums[end - 1] > nums[start]) {
                    valid = false;
                } else if (prevMax > min) {
                    valid = false;
                }

                prevMax = max;
            }

            if (valid) {
                ans += k;
            }
        }

        return ans;
    }
}