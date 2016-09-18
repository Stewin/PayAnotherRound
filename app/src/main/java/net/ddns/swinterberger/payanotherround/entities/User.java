package net.ddns.swinterberger.payanotherround.entities;

import android.graphics.Color;

import net.ddns.swinterberger.payanotherround.UserObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Stefan on 31.08.2016.
 */
public class User {

    private static int userCounter = 0;
    private int id;
    private String name;
    private Color color;
    private List<Debt> debts;
    //Image
    private List<UserObserver> userObserverList;

    public User() {
        this.userObserverList = new LinkedList<>();
        this.id = userCounter;
        userCounter++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        userChanged();
    }

    private void userChanged() {
        for (UserObserver userObserver : userObserverList) {
            userObserver.userChanged();
        }
    }

    public void addUserObserver(final UserObserver userObserver) {
        this.userObserverList.add(userObserver);
    }
}
