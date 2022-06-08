import service.UserService;

public class Main {
    public static void main(String args[]) {
        UserService userService = new UserService();
        int[] result = userService.getUserTransactions("4", "debit", "02-2019");
        System.out.println("Output: ");
        for(int i=0; i<result.length; i++)
            System.out.print("   " + result[i]);
    }
}
