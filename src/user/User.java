package user;
import payment.Card;
import java.util.ArrayList;
import utilityAndServices.UtilityClass;
import java.util.Scanner;
public class User {
    private final int idUser;
    private final String userName; // user name has to be unique
    private final String registrationDate;
    private String lastName;
    private String firstName;
    private String password;
    protected ArrayList <Card> cardList = new ArrayList<>();
    private static int userNo;
    static {
        userNo = 0;
    }

    {
        this.idUser = userNo++;
        this.registrationDate = UtilityClass.getCurrentDate();
    }
    public User (String lastName, String firstName, String password, String userName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.cardList = new ArrayList<>();
        this.password = password;
        this.userName = userName;
    }

    public void addCard() { // method for an user to add a card which can be used to pay for courses
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter card number: ");
        String cardNo = scanner.nextLine();

        // we have to check if the card is already in the list
        for (Card card : cardList) {
            if (card.getCardNo().equals(cardNo)){
                System.out.println("Card already registered!");
                return;
            }
        }
        System.out.println("Enter name on card: ");
        String nameOnCard = scanner.nextLine();

        System.out.println("Enter expiration date: ");
        String expirationDate = scanner.nextLine();

        System.out.println("Enter CVV: ");
        int CVV = scanner.nextInt();

        cardList.add(new Card(cardNo, nameOnCard, expirationDate, CVV));
        System.out.println("Card have been added succsefully.\n");
    }
     public String getUserName() {
        return this.userName;
     }
     public String getPassword() {
        return this.password;
    }
}
