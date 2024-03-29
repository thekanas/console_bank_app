package by.stolybko.database.entity.enam;

/**
 * Enum представляющий тип транзакции.
 */
public enum TransactionType {
    INSERT("Insert"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer");

    private String name;
    TransactionType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
