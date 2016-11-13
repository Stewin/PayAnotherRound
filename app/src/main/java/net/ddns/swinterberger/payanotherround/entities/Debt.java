package net.ddns.swinterberger.payanotherround.entities;

import net.ddns.swinterberger.payanotherround.currency.Currency;

/**
 * Represents a Debt from a User to Another (Relationship Table).
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class Debt {

    private long creditorId;
    private long debtorId;
    private Currency amount;

    public Debt(final long creditorId, final long debtorId,
                final Currency amount) {
        this.creditorId = creditorId;
        this.debtorId = debtorId;
        this.amount = amount;
    }

    public Currency getAmount() {
        return amount;
    }

    public void setAmount(Currency amount) {
        this.amount = amount;
    }

    public final void increaseAmountIntegerPart(final int amount) {
        this.amount.addAmount(amount, 0);
    }

    public final void decreaseAmountIntegerPart(final int amount) {
        this.amount.subtractAmount(amount, 0);
    }

    public final long getCreditorId() {
        return creditorId;
    }

    public final long getDebtorId() {
        return debtorId;
    }

    public void increaseAmountDecimalPart(final int debtAmountPerDebtorDecimal) {
        amount.addAmount(0, debtAmountPerDebtorDecimal);
    }
}
