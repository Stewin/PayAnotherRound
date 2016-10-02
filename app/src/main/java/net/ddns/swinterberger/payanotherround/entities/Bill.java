package net.ddns.swinterberger.payanotherround.entities;

import java.util.List;

/**
 * Represents a Bill.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class Bill {

    private long id;
    private String description;
    private int ammount;
    private String currency; //Währung
    private Trip trip;
    private User payer;
    private List<User> debtor; //Schuldner

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

    public final int getAmmount() {
        return ammount;
    }

    public final void setAmmount(final int ammount) {
        this.ammount = ammount;
    }

    public final String getCurrency() {
        return currency;
    }

    public final void setCurrency(final String currency) {
        this.currency = currency;
    }

    public final Trip getTrip() {
        return trip;
    }

    public final void setTrip(final Trip trip) {
        this.trip = trip;
    }
}
