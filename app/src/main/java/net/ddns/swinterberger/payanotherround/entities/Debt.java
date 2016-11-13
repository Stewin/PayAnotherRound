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

    public final void increaseAmountInCent(final int amount) {
        this.amount.addAmount(amount);
    }

    public final void decreaseAmountInCent(final int amount) {
        this.amount.subtractAmount(amount);
    }

    public final long getCreditorId() {
        return creditorId;
    }

    public final long getDebtorId() {
        return debtorId;
    }
}
