package org.poc;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.LogHelper;

public class Main {

    private static final Integer CLIENTS_AMOUNT = 50;
    private static final Integer TRANSACTIONS_AMOUNT = 100;

    public static void main(String[] args) {

        DataGenerator dataGenerator = new DataGenerator();

        // Generate random data for customers and transactions
        Map<String, String> customerData = dataGenerator.generateCustomerData(CLIENTS_AMOUNT);
        List<String> keys = new ArrayList<>(customerData.keySet());
        var transactions = dataGenerator.generateTransactions(keys, TRANSACTIONS_AMOUNT);
        
        Flux<Transaction> transactionFlux = Flux.fromIterable(transactions);

        // Initialize the CountDownLatch; we need to block the main thread until completion to ensure the stream has time to finish
        CountDownLatch latch = new CountDownLatch(1);  

        transactionFlux
            .flatMap(transaction -> TransactionHelpers.EnrichTransaction(transaction, customerData))
            .filter(TransactionHelpers::IsValidTransaction)
            .map(TransactionHelpers::ConvertToEuros)
            .onErrorContinue((throwable, transaction) -> 
                LogHelper.Log("Error processing " + transaction + ": " + throwable.getMessage())
            )
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap(TransactionHelpers::StoreTransaction)
            .sequential()
            .doOnComplete(() -> {
                LogHelper.Log("Stream is about to complete");
                latch.countDown();
            })
            .subscribe(
                null,
                error -> {
                    LogHelper.Log("Flow encountered an error: " + error.getMessage());
                    System.err.println();
                    latch.countDown();
                },
                () -> LogHelper.Log("Flow completed successfully!")
            );

        try
        {
            latch.await();  // Wait until the latch is decremented
        }
        catch(InterruptedException ex)
        {
            LogHelper.Log("Interrupted Exception Ocurred!");
        }
        catch(Exception ex)
        {
            LogHelper.Log("Exception handled: " + ex.getMessage());
        }
    }
}