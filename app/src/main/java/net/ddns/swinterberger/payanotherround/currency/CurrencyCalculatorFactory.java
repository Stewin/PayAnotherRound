package net.ddns.swinterberger.payanotherround.currency;

/**
 * Created by Stefan on 14.11.2016.
 */

public class CurrencyCalculatorFactory {

    public static final CurrencyCalculator getCalculatorForType(final Currency currency) {

        CurrencyCalculator calculator = new CurrencyCalculator();
        calculator.setCurrency(currency);

        return calculator;

    }
}
