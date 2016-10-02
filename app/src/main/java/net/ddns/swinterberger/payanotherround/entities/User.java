package net.ddns.swinterberger.payanotherround.entities;

import android.graphics.Color;

import java.util.List;

/**
 * Represents a User.
 *
 * @author Stefan Winterberger
 * @version 1.0
 */
public final class User {

    private static int userCounter = 0;
    private long id;
    private String name;
    private Color color;
    private List<Debt> debts;
    //Image

    private boolean checkboxEnabled;

    public User() {
        this.id = userCounter;
        userCounter++;
    }

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

    public final boolean isCheckboxEnabled() {
        return checkboxEnabled;
    }

    public final void setCheckboxEnabled(final boolean checkboxEnabled) {
        this.checkboxEnabled = checkboxEnabled;
    }

    @Override
    public final boolean equals(final Object other) {

        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        if (this.id != ((User) other).getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }
}
