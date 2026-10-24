class Solution {
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        int aliceArrival = calculateDayOfYear(arriveAlice);
        int aliceDeparture = calculateDayOfYear(leaveAlice);
        int bobArrival = calculateDayOfYear(arriveBob);
        int bobDeparture = calculateDayOfYear(leaveBob);
        
        // The overlap interval starts at the later arrival and ends at the earlier departure
        int startOverlap = Math.max(aliceArrival, bobArrival);
        int endOverlap = Math.min(aliceDeparture, bobDeparture);
        
        // If startOverlap <= endOverlap, they overlap; otherwise 0 days
        return Math.max(0, endOverlap - startOverlap + 1);
    }
    
    private int calculateDayOfYear(String date) {
        int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int month = Integer.parseInt(date.substring(0, 2));
        int day = Integer.parseInt(date.substring(3, 5));
        
        int dayOfYear = day;
        for (int i = 0; i < month - 1; i++) {
            dayOfYear += daysInMonths[i];
        }
        return dayOfYear;
    }
}