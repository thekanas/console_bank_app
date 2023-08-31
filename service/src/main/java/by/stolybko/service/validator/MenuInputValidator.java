package by.stolybko.service.validator;

import java.util.Scanner;

public class MenuInputValidator {

    public static int validationMenuInput() {
        Scanner scanner = new Scanner(System.in);
        int number;
        do {
            System.out.print("\n    Enter Input:");
            while (!scanner.hasNextInt()){
                System.out.println("That not a number");
                scanner.next();
            }
            number = scanner.nextInt();
        } while (number <= 0);

        return number;
    }
}
