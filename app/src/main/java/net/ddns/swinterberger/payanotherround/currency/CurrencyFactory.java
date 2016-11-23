package net.ddns.swinterberger.payanotherround.currency;


/**
 * Produces a Currency Type depending on the Abreviation.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class CurrencyFactory {


    public static Currency getCurrencyOfType(final int currencyId) {
        Currency currency = null;


        switch (currencyId) {
            case 1:
                currency = new SwissFranc();
                break;
            case 2:

                break;
            case 3:
                currency = new UsDollar();
                break;
            default:

                break;
        }
        return currency;
    }
}
