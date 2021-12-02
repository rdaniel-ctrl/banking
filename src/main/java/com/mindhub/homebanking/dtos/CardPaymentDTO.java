package com.mindhub.homebanking.dtos;

public class CardPaymentDTO {
    private String number;
    private int securitycode;
    private String account;
    private int amount ;
    private String description;

    public CardPaymentDTO(String number, int securitycode, String account, int amount, String description) {
        this.number = number;
        this.securitycode = securitycode;
        this.account = account;
        this.amount = amount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getSecuritycode() {
        return securitycode;
    }

    public void setSecuritycode(int securitycode) {
        this.securitycode = securitycode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
