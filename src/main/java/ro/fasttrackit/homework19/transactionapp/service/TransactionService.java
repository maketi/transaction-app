package ro.fasttrackit.homework19.transactionapp.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.homework19.transactionapp.model.Transaction;
import ro.fasttrackit.homework19.transactionapp.model.TransactionType;
import ro.fasttrackit.homework19.transactionapp.model.TransactionReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionService(TransactionReader transactionReader) throws Exception {
        this.transactions.addAll(transactionReader.readTransactionsFromFile());
    }

    public List<Transaction> getAllTransactionsFilterable(String productName, TransactionType productType, Double minAmount, Double maxAmount) {
        return transactions.stream()
                .filter(transaction -> productName == null || transaction.product().equalsIgnoreCase(productName))
                .filter(transaction -> productType == null || transaction.type() == productType)
                .filter(transaction -> minAmount == null || transaction.amount() >= minAmount)
                .filter(transaction -> maxAmount == null || transaction.amount() <= maxAmount)
                .collect(toList());
    }

    public Optional<Transaction> getById(int productId) {
        return transactions.stream()
                .filter(transaction -> transaction.id() == productId)
                .findFirst();
    }

   /* public Transaction addTransaction(Transaction transaction) {
        return addTransaction(maxId() + 1, transaction);
    }*/

    public Transaction addTransaction(Transaction transaction) {
        Transaction newTransaction = new Transaction(
                maxId() + 1,
                transaction.product(),
                transaction.type(),
                transaction.amount()
        );
        this.transactions.add(newTransaction);
        return newTransaction;
    }

    private int maxId() {
        return this.transactions.stream()
                .mapToInt(Transaction::id)
                .max()
                .orElse(1);
    }

    public Optional<Transaction> deleteTransaction(int transactionId) {
        Optional<Transaction> transactionOptional = getById(transactionId);
        transactionOptional.
                ifPresent(transactions::remove);
        return transactionOptional;
    }
}