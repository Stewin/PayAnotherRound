package net.ddns.swinterberger.payanotherround.entities;

import java.util.List;

/**
 * Represents a Trip.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class Trip {

    private long id;
    private String name;
    private List<User> users;
    private List<Bill> bills;

    public final long getId() {
        return id;
    }

    public final void setId(final long id) {
        this.id = id;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }
}
