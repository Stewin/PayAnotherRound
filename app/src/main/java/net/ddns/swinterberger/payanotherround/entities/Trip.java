package net.ddns.swinterberger.payanotherround.entities;

import java.util.List;

/**
 * Created by Stefan on 18.09.2016.
 */
public class Trip {

    private long id;
    private String name;
    private List<User> users;
    private List<Bill> bills;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
}
