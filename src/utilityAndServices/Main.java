package utilityAndServices;
import java.util.Scanner;
import utilityAndServices.ApplicationSite;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ApplicationSite S = ApplicationSite.getApplicationSite();
        while (true) {
            if (S.connectedUser == null) {
                System.out.println("1.Login\n2.Sign up");
                Scanner scanner = new Scanner(System.in);
                int command = scanner.nextInt();
                if (command == 1) {
                    S.connectUser();
                } else S.signupUser();
            }
        }
    }
}
