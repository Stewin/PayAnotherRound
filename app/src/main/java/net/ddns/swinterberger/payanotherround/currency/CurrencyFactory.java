package net.ddns.swinterberger.payanotherround.currency;


/**
 * Produces a Currency Type depending on the Abreviation.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class CurrencyFactory {
    public static Currency getCurrencyOfType(final String currencyAbbreviation) {
        Currency currency = null;
        switch (currencyAbbreviation) {
            case "CHF":
                currency = new SwissFranc();
                break;
            case "EUR":

                break;
            case "USD":
                currency = new UsDollar();
                break;
            default:

                break;
        }
        return currency;
    }
}
