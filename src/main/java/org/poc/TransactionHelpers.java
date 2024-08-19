package org.poc;

import java.time.Duration;
import java.util.Map;

import reactor.core.publisher.Mono;

public class TransactionHelpers {
    private static final double EXCHANGE_RATE = 0.90; //exchange rate from USD to EUR

    // Convert amount to EUR
    public static Transaction convertToEuros(Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("The transaction's amount cannot be negative.");
        }
        double amountInEuros = transaction.getAmount() * EXCHANGE_RATE;
        transaction.setAmount(amountInEuros);
        return transaction;
    }

    //Storing the transaction simulating a connection to a DB, 
    public static Mono<Void> storeTransaction(Transaction transaction) {
        int delay = NumbersHelpers.getRandomNumber(100, 2000); // delay in milliseconds
    
        return Mono.delay(Duration.ofMillis(delay))
                   .then(Mono.fromRunnable(() -> 
                       System.out.println("Storing transaction: " + transaction)
                   ));
    }

    //Adding the customer name
    public static Mono<Transaction> enrichTransaction(Transaction transaction, Map<String, String> customerData) {
        String customerName = customerData.getOrDefault(transaction.getAccountId(), "Unknown Customer");
        transaction.setCustomerName(customerName);
        return Mono.just(transaction);
    }

    //Validating the transaction
    public static boolean   isValidTransaction(Transaction transaction) {
        if (transaction.getAmount() > 0){
            return true;
        }
        throw new IllegalArgumentException("El valor de la transacción no puede ser negativo.");
    }
}
