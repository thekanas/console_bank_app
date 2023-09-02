package by.stolybko.service.service;

import by.stolybko.database.dto.ChequeDTO;
import by.stolybko.database.entity.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChequeService {

    public static void chequeSave(Transaction transaction) {

        ChequeDTO cheque = ChequeDTO.builder()
                .transaction(transaction)
                .build();

        String dirPath = "check";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath + "/" + cheque.getNumber() + ".txt"))) {
            writer.write(cheque.toString());

            System.out.println("\n============service message=============");
            System.out.println("transaction receipt has been generated");
            System.out.println(cheque);
            System.out.println("============service message=============\n");

        } catch (IOException e) {
            System.out.println("\n============service message=============");
            System.out.println("receipt generation error");
            System.out.println("\n============service message=============");
            System.out.println(e.getMessage());
        }
    }



}
