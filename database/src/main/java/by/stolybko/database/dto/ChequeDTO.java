package by.stolybko.database.dto;

import by.stolybko.database.entity.Transaction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * класс представляющий банковский чек, содержащий информацию о банковской транзакции.
 */
@Data
@Builder
public class ChequeDTO {
    private Transaction transaction;

    /**
     * метод формирует строковую версию банковского чека, содержащего информацию о банковской транзакции.
     */
    @Override
    public String toString() {
        String firstEndLine = "+--------------------------------------+\n";
        String patternInfoTwoColumn = "| %-20s%16s |\n";
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(firstEndLine);
        stringBuilder.append("|              Bank cheque             |\n");
        stringBuilder.append( String.format(patternInfoTwoColumn, "Cheque:", getNumber()));
        stringBuilder.append( String.format(patternInfoTwoColumn, date.format(transaction.getTimestamp()), time.format(transaction.getTimestamp())));
        stringBuilder.append( String.format(patternInfoTwoColumn, "Transaction type:", transaction.getTransactionType().getName()));
        stringBuilder.append( String.format(patternInfoTwoColumn, "Sender's bank:", transaction.getFromAccount().getBank().getName()));
        stringBuilder.append( String.format(patternInfoTwoColumn, "Recipient's bank:", transaction.getToAccount().getBank().getName()));
        stringBuilder.append( String.format(patternInfoTwoColumn, "Sender's account:", transaction.getFromAccount().getAccountNumber()));
        stringBuilder.append( String.format(patternInfoTwoColumn, "Recipient's account:", transaction.getToAccount().getAccountNumber()));
        stringBuilder.append( String.format(patternInfoTwoColumn, "Amount:", String.format(Locale.US ,"%.2f", (transaction.getAmount())) + " BYN"));

        stringBuilder.append(firstEndLine);
        return stringBuilder.toString();
    }

    /**
     * метод формирует уникальный номер чека на основе текущей даты и типа транзакции.
     */
    public String getNumber() {
        LocalDateTime localDateTime = transaction.getTimestamp();
        DateTimeFormatter uniqId = DateTimeFormatter.ofPattern("yyDDDA");
        return localDateTime.format(uniqId) + transaction.getTransactionType().getName().substring(0,2);
    }
}

