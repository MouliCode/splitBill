package com.lee.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "[user]")
public class User {

    @Id
    @Column(name ="phone_no")
    private String phoneNo;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private String balance;

    @Column(name = "to_give")
    private String toGive;

    @Column(name = "to_get")
    private int toGet;


    public String getToGive() {
        return toGive;
    }

    public void setToGive(String toGive) {
        this.toGive = toGive;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getToGet() {
        return toGet;
    }

    public void setToGet(int toGet) {
        this.toGet = toGet;
    }




}
