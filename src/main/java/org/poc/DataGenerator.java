package org.poc;

import java.util.*;

public class DataGenerator {

    public final ArrayList<String> NAMES = new ArrayList<>(Arrays.asList(
            "Alejandro", "Beatriz", "Carlos", "Diana", "Eduardo", "Fernanda",
            "Gabriel", "Héctor", "Isabel", "Javier", "Karla", "Luis",
            "María", "Nicolás", "Olga", "Pablo", "Quetzal", "Raúl",
            "Sofía", "Tomás", "Úrsula", "Victoria", "Walter", "Ximena",
            "Yolanda", "Zacarías", "Alonso", "Bárbara", "Cristina",
            "Daniel", "Elena", "Francisco", "Gonzalo", "Helena",
            "Ignacio", "José", "Katherine", "Lorena", "Marta",
            "Natalia", "Oscar", "Patricia", "Ramiro", "Susana",
            "Tania", "Ulises", "Verónica", "William", "Xavier",
            "Yamil", "Zulema"
    ));

    public final ArrayList<String> LAST_NAMES = new ArrayList<>(Arrays.asList(
            "García", "Martínez", "López", "Hernández", "González",
            "Pérez", "Rodríguez", "Sánchez", "Ramírez", "Torres",
            "Flores", "Rivera", "Gómez", "Díaz", "Cruz",
            "Ortiz", "Morales", "Vázquez", "Jiménez", "Reyes",
            "Ruiz", "Castillo", "Ramos", "Mendoza", "Silva",
            "Vargas", "Romero", "Fernández", "Alvarez", "Chávez",
            "Ramón", "Ortíz", "Guerra", "Maldonado", "Castañeda",
            "Delgado", "Acosta", "Salazar", "Herrera", "Espinoza",
            "Zamora", "Ibarra", "Salinas", "Juárez", "Luna",
            "Ávila", "Carrillo", "Mejía", "Palacios"
    ));

    //Generates multiples customers
    public Map<String, String> generateCustomerData(Integer amount) {
        Map<String, String> customerData = new HashMap<>();
        for (int i = 0; i < amount; i++) {
            String id = UUID.randomUUID().toString();
            String name = NAMES.get(NumbersHelpers.getRandomNumber(0, NAMES.size() - 1)) + " " 
                + LAST_NAMES.get(NumbersHelpers.getRandomNumber(0, LAST_NAMES.size() - 1));
            customerData.put(id, name);
        }
        return customerData;
    }

    //Generates multiples transactions
    public List<Transaction> generateTransactions(List<String> customerAccountIds, Integer amount) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            String accountId = customerAccountIds.get(NumbersHelpers.getRandomNumber(0, customerAccountIds.size() - 1));
            transactions.add(generateTransaction(accountId));
        }
        return transactions;
    }

    //Generates a transaction with customer accountId
    private Transaction generateTransaction(String accountId) {
        String id = UUID.randomUUID().toString();
        double amount = NumbersHelpers.getRandomNumber(100, 10000); //In USD
        double convertedAmount = Math.random() < 0.2 ? -amount : amount; // with 20% probability of be an invalid transaction
        return new Transaction(id, accountId, convertedAmount);
    }
}
