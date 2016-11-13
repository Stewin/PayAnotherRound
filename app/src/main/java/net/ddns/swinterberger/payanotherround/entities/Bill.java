package net.ddns.swinterberger.payanotherround.entities;

import net.ddns.swinterberger.payanotherround.currency.Currency;

import java.util.List;

/**
 * Represents a Bill.
 *
 * @author Stefan Winterberger
 * @version 1.0.0
 */
public final class Bill {

    private long id;
    private String description;
    private Currency amount;
    private Trip trip;
    private long tripId;
    private User payer;
    private long payerId;
    private List<User> debtors; //Schuldner
    private List<Long> debtorIds;

    public final long getId() {
        return id;
    }

    public final void setId(final long id) {
        this.id = id;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final Currency getAmount() {
        return amount;
    }

    public final void setAmount(final Currency amount) {
        this.amount = amount;
    }

    public final Trip getTrip() {
        return trip;
    }

    public final void setTrip(final Trip trip) {
        this.trip = trip;
    }

    public final long getTripId() {
        return tripId;
    }

    public final void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public long getPayerId() {
        return payerId;
    }

    public void setPayerId(long payerId) {
        this.payerId = payerId;
    }

    public List<Long> getDebtorIds() {
        return debtorIds;
    }

    public void setDebtorIds(List<Long> debtorIds) {
        this.debtorIds = debtorIds;
    }
}
