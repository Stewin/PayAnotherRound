package net.ddns.swinterberger.payanotherround.currency;

/**
 * Represents a Amount of SwissFrancs.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class SwissFranc extends Currency {

    public SwissFranc() {
        this.currencyAbbreviation = "CHF";
        this.exchangeRatio = 1;
    }
}
