package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id ;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDate date;
    private Double curretBalance;

    /*relacion de muchos a una cuenta */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="account_id")
    private Account account;//account de una cuenta

    public Transaction() {}

    public Transaction(TransactionType type, Double amount, String description, LocalDate date, Double curretBalance, Account account) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.curretBalance = curretBalance;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    @JsonIgnore
    public Account getTransfer() {
        return account;
    }

    public void setTransfer(Account account) {
        this.account = account;
    }

    public Double getCurretBalance() {
        return curretBalance;
    }

    public void setCurretBalance(Double curretBalance) {
        this.curretBalance = curretBalance;
    }
}
