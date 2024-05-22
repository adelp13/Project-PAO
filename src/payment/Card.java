package payment;

import user.User;
import utilityAndServices.ApplicationSite;
import utilityAndServices.crudInterface;

import java.util.*;

public final class Card extends crudInterface<Card> { //immutable class
    private String cardNo;
    private String nameOnCard;
    private String expirationDate;
    private int CVV;
    private final UUID id;

    public Card (String cardNo, String nameOnCard, String expirationDate, int CVV) {
        this(UUID.randomUUID(), cardNo, nameOnCard, expirationDate, CVV);
    }
    public Card (UUID id, String cardNo, String nameOnCard, String expirationDate, int CVV) {
        this.id = id;
        this.cardNo = cardNo;
        this.nameOnCard = nameOnCard;
        this.expirationDate = expirationDate;
        this.CVV = CVV;
    }
    public Card() {
        id = UUID.randomUUID();
    }
    public String getCardNo() {
        return cardNo;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public UUID getIdCard() {
        return id;
    }
    public int getCVV() {
        return CVV;
    }
    // we don't need setters, members are final

    @Override
    public String getObjectType() {
        return "Card";
    }

    @Override
    public List<Object> getParametersValues() { // se respecta ordinea atributelor ordinea aributelor
        List<Object> lista = new ArrayList<>(List.of(id.toString(), cardNo, nameOnCard, expirationDate, CVV));
        return lista;
    }

    @Override
    public Card read() {
        ApplicationSite S = ApplicationSite.getApplicationSite();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter card number: ");
        this.cardNo = scanner.nextLine();
        System.out.println("Enter name on card: ");
        this.nameOnCard = scanner.nextLine();
        System.out.println("Enter expiration date: ");
        this.expirationDate = scanner.nextLine();
        System.out.println("Enter CVV: ");
        this.CVV = scanner.nextInt();
        return this;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, cardNo);
    }
    @Override
    public String toString() {
        return "name on card: " + nameOnCard + " card number: " + cardNo;
    }
}
