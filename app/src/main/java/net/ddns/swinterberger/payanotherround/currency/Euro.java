package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a Amount of Euros.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class Euro extends Currency {


    public Euro() {
        this.currencyAbbreviation = "EUR";
        this.exchangeRatio = 1.2f;
    }
}
