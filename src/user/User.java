package user;
import payment.Card;
import java.util.ArrayList;
import java.util.List;
import utilityAndServices.UtilityClass;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import learning.Course;
import learning.Quiz;
public class User { // a user can be both student and teacher
    private final int idUser;
    private Map<Course, Map<Quiz, Integer>> courseProgress; // the courses where the user is enrolled and points obtained for every quiz
    private final String userName; // user name has to be unique
    private final String registrationDate;
    private final String lastName;
    private String firstName;
    private String password;
    protected List <Card> cardList;
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
        this.cardList = new ArrayList<>(); // at first there are no cards
        this.password = password;
        this.userName = userName;
        this.courseProgress = new HashMap<>();
    }

    public void enrollToCourse(Course course) {
        courseProgress.put(course, new HashMap<>());
        System.out.println("User " + userName + " enrolled to course " + course.getName());
    }
    public boolean payWithCard(Card card) { // we verify the CVV
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter CVV: ");
        int CVV = scanner.nextInt();
        scanner.nextLine();
        if (CVV == card.getCVV()) {
            return true; // payment have been made succsefully
        }
        return false;
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

        cardList.add(new Card(cardNo, nameOnCard, expirationDate, CVV)); // this is composition, a card exists only for its user
        System.out.println("Card have been added succsefully.\n");
    }
     public String getUserName() {
        return this.userName;
     }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getPassword() {
        return this.password;
    }
    public List <Card> getCardList() { return this.cardList;}

    public String getRegistrationDate() {
        return this.registrationDate;
    }

    public Map<Course, Map<Quiz, Integer>> getCourseProgress() {return this.courseProgress;}
    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}
