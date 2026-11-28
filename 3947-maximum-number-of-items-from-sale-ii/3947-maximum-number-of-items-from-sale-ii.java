import java.util.Arrays;

class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;
        int[] freq = new int[n + 1];
        long minPrice = Long.MAX_VALUE;

        for (int[] item : items) {
            freq[item[0]]++;
            minPrice = Math.min(minPrice, item[1]);
        }

        // multiples[f] = total count of items whose factor is divisible by f
        int[] multiples = new int[n + 1];
        for (int f = 1; f <= n; f++) {
            if (freq[f] == 0) continue;
            for (int m = f; m <= n; m += f) {
                multiples[f] += freq[m];
            }
        }

        // Sort items by price ascending
        Arrays.sort(items, (a, b) -> Integer.compare(a[1], b[1]));

        long totalItems = 0;
        long remBudget = budget;

        for (int[] item : items) {
            int factor = item[0];
            long price = item[1];

            // If buying 1 copy + 1 free copy costs more than buying 2 copies of the cheapest item,
            // it's better to spend the rest of the budget on the cheapest item.
            if (price > 2 * minPrice) {
                break;
            }

            // Number of distinct other items this item can grant for free
            long gain = multiples[factor] - 1;
            if (gain <= 0) continue;

            long canBuy = Math.min(gain, remBudget / price);
            totalItems += canBuy * 2;
            remBudget -= canBuy * price;

            if (remBudget < price) {
                break;
            }
        }

        // Spend any remaining budget on the cheapest available item
        totalItems += remBudget / minPrice;

        return (int) totalItems;
    }
}