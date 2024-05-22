package user;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import learning.Subject;
import payment.Card;

import utilityAndServices.ApplicationSite;
import utilityAndServices.JdbcSettings;
import utilityAndServices.UtilityClass;
import learning.Course;
import learning.Quiz;
import utilityAndServices.crudInterface;

public class User extends crudInterface<User>  { // a user can be both student and teacher
    private Map<Course, Map<Quiz, String>> courseProgress; // the courses where the user is enrolled and points obtained for every quiz
    private String userName; // user name has to be unique
    private String registrationDate;
    private boolean administrator;
    private String lastName;
    private String firstName;
    private String password;
    protected List <Card> cardList;
    private static int userNo;

    private final UUID id;

    public User (UUID id, String lastName, boolean administrator, String firstName, String password, String userName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.administrator = administrator;
        this.cardList = new ArrayList<>(); // at first there are no cards
        this.password = password;
        this.userName = userName;
        this.courseProgress = new HashMap<>();
    }

    {
        registrationDate = UtilityClass.getCurrentDate();
        administrator = false;
    }
    public User (String lastName, boolean administrator, String firstName, String password, String userName) {
        this(UUID.randomUUID(), lastName, administrator, firstName, password, userName);
    }

    public User (){ id = UUID.randomUUID();};
    @Override
    public String getObjectType() {
        return "User";
    }

    @Override
    public List<Object> getParametersValues() { // se respecta ordinea atributelor ordinea aributelor
        List<Object> lista = new ArrayList<>(List.of(id.toString(), userName, registrationDate, administrator, lastName, firstName, password));
        return lista;
    }

    @Override
    public User read() {
        ApplicationSite S = ApplicationSite.getApplicationSite();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        this.userName = scanner.nextLine();
        for (User user : S.getUserList()) { // we ensure there isn't already a user with the same user name
            if (user.getUserName().equals(userName)) {
                System.out.println("Username must be unique");
                return this;
            }
        }

        System.out.println("Enter password:");
        this.password = scanner.nextLine();
        System.out.println("Enter last name:");
        this.lastName = scanner.nextLine();
        System.out.println("Enter first name:");
        this.firstName = scanner.nextLine();
        return this;
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
    public void addCard() throws SQLException { // method for an user to add a card which can be used to pay for courses
        Card newCard = new Card();
        newCard.read();
        cardList.add(newCard); // this is composition, a card exists only for its user
        System.out.println("Card has been added succsefully.\n");

        //Add it in jdbc
        JdbcSettings J = JdbcSettings.getJdbcSettings();
        newCard.createJDBC(J.getConnection());
        //now we have to add in the UserHasCard table
        String queryText = "insert into UserHasCard values(?,?)";

        try(PreparedStatement pstmt = (J.getConnection()).prepareStatement(queryText)) {
            pstmt.setString(1, getIdUser().toString());
            pstmt.setString(2, (newCard.getIdCard()).toString());
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("Card creation failed" + se.toString());
        }
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

    public boolean getAdministrator() {
        return administrator;
    }
    public Map<Course, Map<Quiz, String>> getCourseProgress() {return this.courseProgress;}
    public UUID getIdUser() {
        return this.id;
    }
    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
    public void setPassword(String password){
        this.password = password;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, userName);
    }
    @Override
    public String toString() {
        return "user " + userName + " registered on " + registrationDate + (administrator ? " ADMINISTRATOR" : "");
    }
}
