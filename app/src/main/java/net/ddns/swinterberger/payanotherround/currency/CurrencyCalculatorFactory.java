package net.ddns.swinterberger.payanotherround.currency;


public final class CurrencyCalculatorFactory {

    private CurrencyCalculatorFactory() {

    }

    public static final CurrencyCalculator getCalculatorForType(final Currency currency) {

        CurrencyCalculator calculator = new CurrencyCalculator();
        calculator.setCurrency(currency);

        return calculator;

    }
}
