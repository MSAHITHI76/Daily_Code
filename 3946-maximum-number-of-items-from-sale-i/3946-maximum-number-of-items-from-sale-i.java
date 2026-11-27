class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;
        int[] freeCount = new int[n];

        // Count how many other items j have factor_j divisible by factor_i
        for (int i = 0; i < n; i++) {
            int fI = items[i][0];
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && items[j][0] % fI == 0) {
                    cnt++;
                }
            }
            freeCount[i] = cnt;
        }

        // dp[b] = best total copies using budget b
        // We track both:
        // - noFreebie[b]: without triggering a new item's freebies in this step
        // - withFreebie[b]: including triggering an item's freebies
        int NEG_INF = -1_000_000_000;
        int[] base = new int[budget + 1];
        java.util.Arrays.fill(base, NEG_INF);
        base[0] = 0;

        for (int i = 0; i < n; i++) {
            int price = items[i][1];
            int bonus = freeCount[i];

            int[] next = base.clone();

            // 1. Trigger item i for the first time (1 bought copy + bonus free copies)
            if (price <= budget) {
                for (int b = budget; b >= price; b--) {
                    if (base[b - price] != NEG_INF) {
                        next[b] = Math.max(next[b], base[b - price] + 1 + bonus);
                    }
                }
            }

            // 2. Buy additional copies of item i (1 copy per purchase, no extra bonus)
            if (price <= budget) {
                for (int b = price; b <= budget; b++) {
                    if (next[b - price] != NEG_INF) {
                        next[b] = Math.max(next[b], next[b - price] + 1);
                    }
                }
            }

            base = next;
        }

        int ans = 0;
        for (int copies : base) {
            if (copies > ans) {
                ans = copies;
            }
        }
        return ans;
    }
}