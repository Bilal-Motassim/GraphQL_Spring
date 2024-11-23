package ma.projet.graph.controllers;

import lombok.AllArgsConstructor;
import ma.projet.graph.entities.Compte;
import ma.projet.graph.entities.Transaction;
import ma.projet.graph.entities.TransactionInput;
import ma.projet.graph.repositories.CompteRepository;
import ma.projet.graph.repositories.TransactionRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
public class TransactionControllerGraphQL {

    private TransactionRepository transactionRepository;

    private CompteRepository compteRepository;

    @QueryMapping
    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

    @QueryMapping
    public List<Transaction> getTransactionsByCompte(@Argument Long id){
        return transactionRepository.findByCompte_Id(id);
    }

    @QueryMapping
    public Transaction getTransactionById(@Argument Long id){
        return transactionRepository.findById(id).orElse(null);
    }


    @MutationMapping
    public Transaction createTransaction(
            @Argument TransactionInput transactionInput,
            @Argument Long compteId) {

        // Fetch the associated Compte
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new IllegalArgumentException("Compte not found with ID: " + compteId));

        // Parse the transactionDate from String to Date
        Date transactionDate;
        try {
            transactionDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(transactionInput.getTransactionDate());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use ISO-8601, e.g., 2024-01-01T12:00:00", e);
        }

        // Create a new Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionInput.getAmount());
        transaction.setType(transactionInput.getType());
        transaction.setTransactionDate(transactionDate);
        transaction.setCompte(compte);

        // Save and return the Transaction
        return transactionRepository.save(transaction);
    }

    @MutationMapping
    public boolean deleteTransaction(@Argument Long id){
        transactionRepository.deleteById(id);
        return true;
    }

}
