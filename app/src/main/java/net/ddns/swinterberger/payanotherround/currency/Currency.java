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

    private int integerPart;
    private int decimalPart;

    public Currency() {
        this.currencyAbreviation = "DEF";
        this.exchangeRatio = 1;
    }

    public int getIntPart() {
        return this.integerPart;
    }

    public int getDecimalPart() {
        return this.decimalPart;
    }

    public void setDecimalPart(int decimalPart) {
        this.decimalPart = decimalPart;
    }

    public void setIntegerPart(int integerPart) {
        this.integerPart = integerPart;
    }

    public final void addAmount(final int franc, final int centime) {
        addIntegerPart(franc);
        addDecimalPart(centime);
    }

    public void divideByUsers(int numberOsers) {

    }

    public final void setAmount(final int franc, final int centime) {
        setIntegerPart(franc);
        setDecimalPart(centime);
    }

    public final void subtractAmount(final int intPart, final int decimalPart) {
        subtractIntegerPart(intPart);
        subtractDecimalPart(decimalPart);
    }

    public String getCurrencyAbbreviation() {
        return currencyAbreviation;
    }

    private void addIntegerPart(final int franc) {
        this.integerPart += franc;
    }

    private void subtractIntegerPart(final int franc) {
        this.integerPart -= franc;
    }

    private void addDecimalPart(final int centime) {
        this.decimalPart += centime;
        normalizeDecimalPart();
    }

    private void subtractDecimalPart(final int centime) {
        this.decimalPart -= centime;
        normalizeDecimalPart();
    }

    private void normalizeDecimalPart() {

        //Zehner Stelle
        int seconddigit = (this.decimalPart / 10) % 10;

        //Einer Stelle
        int firstDigit = this.decimalPart % 10;

        if (this.decimalPart >= 100) {
            this.integerPart += this.decimalPart / 100;
            this.decimalPart = seconddigit * 10 + firstDigit;
        } else if (this.decimalPart < 0) {
            int i = (this.decimalPart / 100) % 10;
            this.integerPart += i - 1;
            this.decimalPart = 100 + (seconddigit * 10 + firstDigit);
        }
    }

    private void roundDecimalPart() {
        //TODO: Implement Round Centime.
    }

    @Override
    public String toString() {
        return currencyAbreviation + ": " + integerPart + "." + decimalPart;
    }
}
