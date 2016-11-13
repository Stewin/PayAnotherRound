package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a basic, non existent Currency.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class Currency {

    protected String currencyAbbreviation;
    protected float exchangeRatio;

    private int amountInCent;

    public Currency() {
        this.currencyAbbreviation = "DEF";
        this.exchangeRatio = 1;
    }

    public int getAmountInCent() {
        return this.amountInCent;
    }

    public final void addAmount(final int amount) {
        this.amountInCent += amount;
    }

    public final void subtractAmount(final int amount) {
        this.amountInCent -= amount;
    }

    public String getCurrencyAbbreviation() {
        return currencyAbbreviation;
    }

    public float getExchangeRatio() {
        return exchangeRatio;
    }

    public final void setAmount(final int amount) {
        this.amountInCent = amount;
    }

    public void divideByUsers(int numberUsers) {
        this.amountInCent = Math.round(this.amountInCent / numberUsers);
    }

    public void exchangeAmount() {
        this.amountInCent = (int) (this.amountInCent * this.exchangeRatio);
    }

    @Override
    public String toString() {

        //Zehner Stelle
        int seconddigit = Math.abs((this.amountInCent / 10) % 10);

        //Einer Stelle
        int firstDigit = Math.abs(this.amountInCent % 10);

        StringBuilder sb = new StringBuilder();
        sb.append(currencyAbbreviation).append(": ");
        sb.append(amountInCent / 100).append(".").append(seconddigit).append(firstDigit);
        return sb.toString();
    }
}
