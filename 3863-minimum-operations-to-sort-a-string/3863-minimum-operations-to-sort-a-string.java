class Solution {
    public int minOperations(String s) {
        int n = s.length();
        char[] arr = s.toCharArray();
        
        // 1. Check if already sorted
        boolean sorted = true;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                sorted = false;
                break;
            }
        }
        if (sorted) return 0;
        
        // 2. If length is 2 and not sorted, it's impossible
        if (n == 2) return -1;
        
        char minChar = arr[0], maxChar = arr[0];
        for (char c : arr) {
            if (c < minChar) minChar = c;
            if (c > maxChar) maxChar = c;
        }
        
        // 3. Check if 1 operation is enough
        // Min char at the beginning (excluding full string) or max char at the end
        boolean minAtStart = false;
        boolean maxAtEnd = false;
        
        for (int i = 0; i < n; i++) {
            if (arr[i] == minChar) {
                if (i < n - 1) minAtStart = true;
                break;
            } else {
                break; // first occurrence
            }
        }
        
        for (int i = n - 1; i >= 0; i--) {
            if (arr[i] == maxChar) {
                if (i > 0) maxAtEnd = true;
                break;
            } else {
                break; // last occurrence
            }
        }
        
        if (arr[0] == minChar && minAtStart) return 1;
        if (arr[n - 1] == maxChar && maxAtEnd) return 1;
        
        // 4. Check for 3 operations case
        boolean hasMinInProperPrefix = false;
        boolean hasMaxInProperSuffix = false;
        
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] == minChar) {
                hasMinInProperPrefix = true;
                break;
            }
        }
        
        for (int i = 1; i < n; i++) {
            if (arr[i] == maxChar) {
                hasMaxInProperSuffix = true;
                break;
            }
        }
        
        if (!hasMinInProperPrefix && !hasMaxInProperSuffix) {
            return 3;
        }
        
        // 5. Fallback to 2 operations
        return 2;
    }
}