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

    public Currency(final String currencyAbbreviation, final float exchangeRatio) {
        this.currencyAbbreviation = currencyAbbreviation;
        this.exchangeRatio = exchangeRatio;
    }

    public final long getId() {
        return id;
    }

    public final void setId(final long id) {
        this.id = id;
    }

    public final String getCurrencyAbbreviation() {
        return currencyAbbreviation;
    }

    public final void setCurrencyAbbreviation(String currencyAbbreviation) {
        this.currencyAbbreviation = currencyAbbreviation;
    }

    public final float getExchangeRatio() {
        return exchangeRatio;
    }

    public final void setExchangeRatio(float exchangeRatio) {
        this.exchangeRatio = exchangeRatio;
    }

    public final String amountToString(final long amountInCent) {

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
