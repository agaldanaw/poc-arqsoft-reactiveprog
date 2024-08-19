package org.poc;

import java.time.Duration;
import java.util.Map;

import org.LogHelper;

import reactor.core.publisher.Mono;

public class TransactionHelpers {
    private static final double EXCHANGE_RATE = 0.90; //exchange rate from USD to EUR

    // Convert amount to EUR
    public static Transaction ConvertToEuros(Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("The transaction's amount cannot be negative.");
        }
        double amountInEuros = transaction.getAmount() * EXCHANGE_RATE;
        transaction.setAmount(amountInEuros);
        return transaction;
    }

    //Storing the transaction simulating a connection to a DB, 
    public static Mono<Void> StoreTransaction(Transaction transaction) {
        int delay = NumbersHelpers.getRandomNumber(0, 2000); // delay in milliseconds
    
        return Mono.delay(Duration.ofMillis(delay))
                   .then(Mono.fromRunnable(() -> 
                        LogHelper.Log("Storing transaction: " + transaction)
                   ));
    }

    //Adding the customer name
    public static Mono<Transaction> EnrichTransaction(Transaction transaction, Map<String, String> customerData) {
        String customerName = customerData.getOrDefault(transaction.getAccountId(), "Unknown Customer");
        transaction.setCustomerName(customerName);
        return Mono.just(transaction);
    }

    //Validating the transaction
    public static boolean IsValidTransaction(Transaction transaction) {
        if (transaction.getAmount() > 0){
            return true;
        }
        throw new IllegalArgumentException("The transaction's amount cannot be negative");
    }
}
