package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a basic, non existent Currency.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class Currency {

    protected String currencyAbreviation;
    protected float exchangeRatio;

    private int amountInCent;

    public Currency() {
        this.currencyAbreviation = "DEF";
        this.exchangeRatio = 1;
    }

    public int getAmountInCent() {
        return this.amountInCent;
    }

    public void setAmountInCent(final int amountInCent) {
        this.amountInCent = amountInCent;
    }

    public final void addAmount(final int amount) {
        this.amountInCent += amount;
    }

    public final void subtractAmount(final int amount) {
        this.amountInCent -= amount;
    }

    public String getCurrencyAbbreviation() {
        return currencyAbreviation;
    }

    public final void setAmount(final int amount) {
        this.amountInCent = amount;
    }

    public void divideByUsers(int numberUsers) {
        this.amountInCent = Math.round(this.amountInCent / numberUsers);
    }

    @Override
    public String toString() {

        //Zehner Stelle
        int seconddigit = Math.abs((this.amountInCent / 10) % 10);

        //Einer Stelle
        int firstDigit = Math.abs(this.amountInCent % 10);

        StringBuilder sb = new StringBuilder();
        sb.append(currencyAbreviation).append(": ");
        sb.append(amountInCent / 100).append(".").append(seconddigit).append(firstDigit);
        return sb.toString();
    }
}
