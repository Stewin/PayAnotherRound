package net.ddns.swinterberger.payanotherround.entities;

import net.ddns.swinterberger.payanotherround.currency.Currency;

/**
 * Represents a Debt from a User to Another (Relationship Table).
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class Debt {

    private long creditorId;
    private long debtorId;
    private Currency amount;
    private int amountIntegerPart;
    private int amountDecimalPart;

    public Debt(final long creditorId, final long debtorId,
                final int amountIntegerPart, final int amountDecimalPart) {
        this.creditorId = creditorId;
        this.debtorId = debtorId;
        this.amountIntegerPart = amountIntegerPart;
        this.amountDecimalPart = amountDecimalPart;
    }

    public final int getAmountIntegerPart() {
        return amountIntegerPart;
    }

    public final int getAmountDecimalPart() {
        return this.amountDecimalPart;
    }

    public final void increaseAmountIntegerPart(final float amount) {
        this.amountIntegerPart += amount;
    }

    public final void decreaseAmountIntegerPart(final float amount) {
        this.amountIntegerPart -= amount;
    }

    public final long getCreditorId() {
        return creditorId;
    }

    public final long getDebtorId() {
        return debtorId;
    }

    public void increaseAmountDecimalPart(final int debtAmountPerDebtorDecimal) {
        amountDecimalPart += debtAmountPerDebtorDecimal;
    }
}
