package ma.projet.graph.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInput {
    private double amount;
    private String transactionDate;
    private TransactionType type;
}
