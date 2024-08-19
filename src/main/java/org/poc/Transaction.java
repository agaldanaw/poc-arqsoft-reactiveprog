package org.poc;

public  class Transaction {

    private final String id;
    private final String accountId;
    private double amount; //IN USD
    private String customerName;

    public Transaction(String id, String accountId, double amount) {
        this(id, accountId, amount, "Unknown");
    }

    public Transaction(String id, String accountId, double amount, String customerName) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.customerName = customerName;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Transaction {id='" + id + "', accountId='" + accountId + "', amount=" + amount + " EUR, customerName='" + customerName + "'}";
    }
}
