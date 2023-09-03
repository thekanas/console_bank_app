package by.stolybko.service.validator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;

/**
 * класс для валидации пользовательского ввода в консольном меню
 */
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

    public static BigDecimal validationMoneyInput() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        char sep=symbols.getDecimalSeparator();

        Scanner scanner = new Scanner(System.in);
        BigDecimal money;
        do {
            System.out.print("\n    Enter Input(separator:'" + sep + "'):");
            while (!scanner.hasNextBigDecimal()){
                System.out.println("That not a number");
                scanner.next();
            }
            money = scanner.nextBigDecimal();
        } while (money.compareTo(BigDecimal.ZERO) <= 0);

        return money;
    }
}
