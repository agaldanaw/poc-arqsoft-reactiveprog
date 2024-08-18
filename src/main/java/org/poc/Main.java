package org.poc;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static final Integer CLIENTS_AMOUNT = 100;
    private static final Integer TRANSACTIONS_AMOUNT = 1000;

    public static void main(String[] args) {

        DataGenerator dataGenerator = new DataGenerator();

        // Generando datos aleatorios de clientes y transacciones
        Map<String, String> customerData = dataGenerator.generateCustomerData(CLIENTS_AMOUNT);
        List<String> keys = new ArrayList<>(customerData.keySet());

        Flux<Transaction> transactionFlux = Flux.fromIterable(dataGenerator.generateTransactions(keys, TRANSACTIONS_AMOUNT));

        transactionFlux
                .flatMap(transaction -> {
                    System.out.println("Enriqueciendo transacción: " + transaction);
                    return enrichTransaction(transaction, customerData);
                })
                .filter(transaction -> {
                    boolean isValid = isValidTransaction(transaction);
                    System.out.println("Validación de transacción: " + transaction + " es válida: " + isValid);
                    return isValid;
                })
                .map(transaction -> {
                    System.out.println("Convirtiendo transacción a euros: " + transaction);
                    return convertToEuros(transaction);
                })
                .onErrorContinue((error, transaction) ->
                        System.out.println("Error procesando " + transaction + ": " + error.getMessage())
                )
                .subscribe(transaction -> storeTransaction(transaction));
    }

    // Método para enriquecer la transacción con datos del cliente
    private static Mono<Transaction> enrichTransaction(Transaction transaction, Map<String, String> customerData) {
        String customerName = customerData.getOrDefault(transaction.getAccountId(), "Unknown Customer");
        return Mono.just(new Transaction(transaction.getId(), transaction.getAccountId(), transaction.getAmount(), customerName));
    }

    // Método de validación de transacciones
    private static boolean isValidTransaction(Transaction transaction) {
        if (transaction.getAmount() > 0){
            return true;
        }
        throw new IllegalArgumentException("El valor de la transacción no puede ser negativo.");
        //return transaction.getAmount() > 0; && !"Invalid Account".equals(transaction.getAccountId());
    }

    // Método para convertir el valor de una transacción a euros
    private static Transaction convertToEuros(Transaction transaction) {
        double exchangeRate = 0.85;  // Supongamos un tipo de cambio fijo de 1 USD = 0.85 EUR
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("El valor de la transacción no puede ser negativo.");
        }
        double amountInEuros = transaction.getAmount() * exchangeRate;
        return new Transaction(transaction.getId(), transaction.getAccountId(), amountInEuros, transaction.getCustomerName());
    }

    // Simulación de almacenamiento de transacción en la base de datos
    private static void storeTransaction(Transaction transaction) {
        //Timestamp igual o muy cercano (teoria)
        System.out.println("Almacenando transacción: " + transaction);
        // Aquí iría la lógica para almacenar en la base de datos
    }
}