package net.ddns.swinterberger.payanotherround.entities;

import java.util.List;

/**
 * Created by Stefan on 18.09.2016.
 */
public class Bill {

    private int id;
    private String name;
    private int ammount;
    private String currency; //Währung
    private User payer;
    private List<User> debtor; //Schuldner

}
