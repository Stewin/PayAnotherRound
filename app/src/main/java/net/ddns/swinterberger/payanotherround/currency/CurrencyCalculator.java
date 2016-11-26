package net.ddns.swinterberger.payanotherround.currency;


/**
 * Calcualtor for different Currencies.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public class CurrencyCalculator {

    private Currency currency;

    public final Currency getCurrency() {
        return currency;
    }

    public final void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public final int exchangeAmount(final int amountInCent) {
        return (int) (amountInCent * currency.getExchangeRatio());
    }

    public final int divideByUsers(final int amountInCent, final int numberUsers) {
        int result = amountInCent;
        if (numberUsers != 0) {
            result = Math.round((float) amountInCent / numberUsers);
        }
        return result;
    }
}
