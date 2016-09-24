package net.ddns.swinterberger.payanotherround.entities;

import java.util.List;

/**
 * Created by Stefan on 18.09.2016.
 */
public class Bill {

    private long id;
    private String description;
    private int ammount;
    private String currency; //Währung
    private User payer;
    private List<User> debtor; //Schuldner

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
