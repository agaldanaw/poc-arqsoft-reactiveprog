package org.poc;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static final Integer CLIENTS_AMOUNT = 1000;
    private static final Integer TRANSACTIONS_AMOUNT = 100;

    public static void main(String[] args) {

        DataGenerator dataGenerator = new DataGenerator();

        // Generate random data for customers and transactions
        Map<String, String> customerData = dataGenerator.generateCustomerData(CLIENTS_AMOUNT);
        List<String> keys = new ArrayList<>(customerData.keySet());

        Flux<Transaction> transactionFlux = Flux.fromIterable(dataGenerator.generateTransactions(keys, TRANSACTIONS_AMOUNT));

        // Initialize the CountDownLatch; we need to block the main thread until completion to ensure the stream has time to finish
        CountDownLatch latch = new CountDownLatch(1);  

        transactionFlux.flatMap(transaction -> TransactionHelpers.enrichTransaction(transaction, customerData))
            .filter(TransactionHelpers::isValidTransaction)
            .map(TransactionHelpers::convertToEuros)
            .onErrorContinue((throwable, obj) -> 
                System.out.println("Error processing " + obj + ": " + throwable.getMessage())
            )
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap(TransactionHelpers::storeTransaction)
            .sequential()
            .doOnComplete(() -> {
                System.out.println("Stream is about to complete");
                latch.countDown();
            })
            .subscribe(
                null,
                error -> {
                    System.err.println("Flow encountered an error: " + error.getMessage());
                    latch.countDown();
                },
                () -> System.out.println("Flow completed successfully!")
            );

        try
        {
            latch.await();  // Wait until the latch is decremented
        }
        catch(InterruptedException ex)
        {
            System.out.println("Interrupted Exception Ocurred!");
        }
        catch(Exception ex)
        {
            System.out.println("Exception handled: " + ex.getMessage());
        }
    }
}