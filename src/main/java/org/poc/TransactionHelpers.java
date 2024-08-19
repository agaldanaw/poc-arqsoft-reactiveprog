package org.poc;

import java.time.Duration;
import java.util.Map;

import reactor.core.publisher.Mono;

public class TransactionHelpers {
    private static final double EXCHANGE_RATE = 0.90;
    public static Transaction convertToEuros(Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("El valor de la transacción no puede ser negativo.");
        }
        double amountInEuros = transaction.getAmount() * EXCHANGE_RATE;
        return new Transaction(transaction.getId(), transaction.getAccountId(), amountInEuros, transaction.getCustomerName());
    }

    //Simulating a connection to a DB, sotring the transaction.
    public static Mono<Void> storeTransaction(Transaction transaction) {
        int delay = NumbersHelpers.getRandomNumber(100, 2000); // delay in milliseconds
    
        return Mono.delay(Duration.ofMillis(delay))
                   .then(Mono.fromRunnable(() -> 
                       System.out.println("Almacenando transacción: " + transaction)
                   ));
    }

    public static Mono<Transaction> enrichTransaction(Transaction transaction, Map<String, String> customerData) {
        String customerName = customerData.getOrDefault(transaction.getAccountId(), "Unknown Customer");
        return Mono.just(new Transaction(transaction.getId(), transaction.getAccountId(), transaction.getAmount(), customerName));
    }

    public static boolean   isValidTransaction(Transaction transaction) {
        if (transaction.getAmount() > 0){
            return true;
        }
        throw new IllegalArgumentException("El valor de la transacción no puede ser negativo.");
    }
}
