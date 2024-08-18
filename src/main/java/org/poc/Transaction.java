package org.poc;

public  class Transaction {

    private final String id;
    private final String accountId;
    private final double amount;
    private final String customerName;

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

    @Override
    public String toString() {
        return "Transaction{id='" + id + "', accountId='" + accountId + "', amount=" + amount + " EUR, customerName='" + customerName + "'}";
    }
}
