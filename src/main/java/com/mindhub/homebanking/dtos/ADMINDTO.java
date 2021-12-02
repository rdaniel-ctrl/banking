package com.mindhub.homebanking.dtos;

import java.util.ArrayList;
import java.util.List;

public class ADMINDTO {
    private String name;
    private int amount;
    private List<Integer> payments = new ArrayList<>();


    public ADMINDTO(String name, int amount, List<Integer> payments) {
        this.name = name;
        this.amount = amount;
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}