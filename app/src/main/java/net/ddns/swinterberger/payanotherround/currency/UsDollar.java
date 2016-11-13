package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a Amount of US Dollar.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class UsDollar extends Currency {

    public UsDollar() {
        this.currencyAbbreviation = "USD";
        this.exchangeRatio = 1.01f;
    }
}
