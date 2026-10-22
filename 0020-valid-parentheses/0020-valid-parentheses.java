import java.util.Stack;

class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            // Push the corresponding closing bracket for every opening bracket
            if (c == '(') {
                stack.push(')');
            } else if (c == '{') {
                stack.push('}');
            } else if (c == '[') {
                stack.push(']');
            } 
            // If it's a closing bracket, check if stack is empty or top doesn't match
            else if (stack.isEmpty() || stack.pop() != c) {
                return false;
            }
        }
        
        // If stack is empty, all opening brackets had a valid match
        return stack.isEmpty();
    }
}