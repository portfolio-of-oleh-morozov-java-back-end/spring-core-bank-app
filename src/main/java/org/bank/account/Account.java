package org.bank.account;

public class Account {
    private final int id;
    private final int userId;
    private int balance;

    public Account(int id, int userId, int balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
