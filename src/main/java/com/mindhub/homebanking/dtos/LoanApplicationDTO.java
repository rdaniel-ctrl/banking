package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private String name;
    private int amount ;
    private int payment;
    private String account;
    private Double percentage;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(String name, int amount, int payment, String account, Double percentage) {
        this.name = name;
        this.amount = amount;
        this.payment = payment;
        this.account = account;
        this.percentage = percentage;
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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
