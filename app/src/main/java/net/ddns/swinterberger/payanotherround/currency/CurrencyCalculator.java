package net.ddns.swinterberger.payanotherround.currency;


public class CurrencyCalculator {

    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public int exchangeAmount(final int amountInCent) {
        int result = (int) (amountInCent * currency.getExchangeRatio());
        return result;
    }

    public int divideByUsers(final int amountInCent, final int numberUsers) {
        int result = amountInCent;
        if (numberUsers != 0) {
            result = Math.round(amountInCent / numberUsers);
        }
        return result;
    }
}
