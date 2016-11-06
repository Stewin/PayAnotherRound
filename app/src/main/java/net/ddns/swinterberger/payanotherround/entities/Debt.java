package net.ddns.swinterberger.payanotherround.entities;

/**
 * Represents a Debt from a User to Another (Relationship Table).
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class Debt {

    private long creditorId;
    private long debtorId;
    private int amount;

    public Debt(long creditorId, long debtorId, int amount) {
        this.creditorId = creditorId;
        this.debtorId = debtorId;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public final void increaseAmount(final float amount) {
        this.amount += amount;
    }

    public final void decreaseAmount(final float amount) {
        this.amount -= amount;
    }

    public long getCreditorId() {
        return creditorId;
    }

    public long getDebtorId() {
        return debtorId;
    }
}
