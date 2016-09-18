package net.ddns.swinterberger.payanotherround.entities;

import java.util.List;

/**
 * Created by Stefan on 18.09.2016.
 */
public class Trip {

    private int id;
    private String name;
    private List<User> users;
    private List<Bill> bills;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
