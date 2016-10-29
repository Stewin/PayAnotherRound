package net.ddns.swinterberger.payanotherround.entities;

/**
 * Created by Stefan on 18.09.2016.
 */
public final class Debt {

    private long creditorId;
    private long debitorId;
    private long ammount;

    public Debt(long creditorId, long debitorId, long ammount) {
        this.creditorId = creditorId;
        this.debitorId = debitorId;
        this.ammount = ammount;
    }

    public long getAmmount() {
        return ammount;
    }

    public final void addAmount(final long amount) {
        this.ammount += amount;
    }

    public long getCreditorId() {
        return creditorId;
    }

    public long getDebitorId() {
        return debitorId;
    }
}
