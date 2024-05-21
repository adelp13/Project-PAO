package payment;

public final class Card { //immutable class
    private final String cardNo;
    private final String nameOnCard;
    private final String expirationDate;
    private final int CVV;

    public Card (String cardNo, String nameOnCard, String expirationDate, int CVV) {
        this.cardNo = cardNo;
        this.nameOnCard = nameOnCard;
        this.expirationDate = expirationDate;
        this.CVV = CVV;
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

    public int getCVV() {
        return CVV;
    }
    // we don't need setters, members are final
    @Override
    public String toString() {
        return "name on card: " + nameOnCard + " card number: " + cardNo;
    }
}
