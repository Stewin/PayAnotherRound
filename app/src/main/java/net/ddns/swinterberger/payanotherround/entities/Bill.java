package net.ddns.swinterberger.payanotherround.entities;

import java.util.Collections;
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
    private long currencyId;
    private int amountInCent;
    private long tripId;
    private long payerId;
    private List<Long> debtorIds;
    private String timestamp;

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

    public final int getAmountInCent() {
        return amountInCent;
    }

    public final void setAmountInCent(int ammountInCent) {
        this.amountInCent = ammountInCent;
    }

    public final long getCurrencyId() {
        return currencyId;
    }

    public final void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public final long getTripId() {
        return tripId;
    }

    public final void setTripId(final long tripId) {
        this.tripId = tripId;
    }

    public final long getPayerId() {
        return payerId;
    }

    public final void setPayerId(long payerId) {
        this.payerId = payerId;
    }

    public final List<Long> getDebtorIds() {
        return Collections.unmodifiableList(debtorIds);
    }

    public final void setDebtorIds(List<Long> debtorIds) {
        this.debtorIds = debtorIds;
    }

    public final String getDate() {
        return timestamp;
    }

    public final void setDate(final String date) {
        this.timestamp = date;
    }
}
