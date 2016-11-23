package net.ddns.swinterberger.payanotherround.entities;

/**
 * Represents a Debt from a User to Another (Relationship Table).
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class Debt {

    private long creditorId;
    private long debtorId;
    private long billId;
    private int amountInCent;

    public Debt(final long creditorId, final long debtorId, final long billId,
                final int amountInCent) {
        this.creditorId = creditorId;
        this.debtorId = debtorId;
        this.billId = billId;
        this.amountInCent = amountInCent;
    }

    public long getBillId() {
        return billId;
    }

    public final void increaseAmountInCent(final int amount) {
        this.billId += amount;
    }

    public final void decreaseAmountInCent(final int amount) {
        this.billId -= amount;
    }

    public final long getCreditorId() {
        return creditorId;
    }

    public final long getDebtorId() {
        return debtorId;
    }

    public int getAmountInCent() {
        return amountInCent;
    }
}
