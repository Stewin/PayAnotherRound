package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a basic, non existent Currency.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class Currency {

    protected long id;
    protected String currencyAbbreviation;
    protected float exchangeRatio;

    public Currency() {
        this.currencyAbbreviation = "DEF";
        this.exchangeRatio = 1;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getCurrencyAbbreviation() {
        return currencyAbbreviation;
    }

    public void setCurrencyAbbreviation(String currencyAbbreviation) {
        this.currencyAbbreviation = currencyAbbreviation;
    }

    public float getExchangeRatio() {
        return exchangeRatio;
    }

    public void setExchangeRatio(float exchangeRatio) {
        this.exchangeRatio = exchangeRatio;
    }

    public String amountToString(final long amountInCent) {

        //Zehner Stelle
        long seconddigit = Math.abs((amountInCent / 10) % 10);

        //Einer Stelle
        long firstDigit = Math.abs(amountInCent % 10);

        StringBuilder sb = new StringBuilder();
        sb.append(currencyAbbreviation).append(": ");
        sb.append(amountInCent / 100).append(".").append(seconddigit).append(firstDigit);
        return sb.toString();
    }
}
