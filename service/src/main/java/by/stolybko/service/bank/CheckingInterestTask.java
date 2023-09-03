package by.stolybko.service.bank;

import by.stolybko.service.service.AccountService;
import by.stolybko.service.util.PropertiesManager;

import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * класс для выполнения задачи начисления процентов на баланс счёта в конце месяца
 */
public class CheckingInterestTask implements Runnable {
    private final AccountService accountService = AccountService.getInstance();

    private boolean interestAccrued = false;
    @Override
    public void run() {
        LocalDate currentDay = LocalDate.now();
        LocalDate lastDay = currentDay.with(lastDayOfMonth());
        System.out.println("\n============service message============");
        System.out.println("checking interest accrual");
        if (currentDay == lastDay) {
            if (!interestAccrued) {
                accountService.accrueInterest(Double.valueOf(PropertiesManager.get("percent")));
                System.out.println("interest accrued");
                interestAccrued = true;
            } else {
                System.out.println("interest has already been accrued today");
            }

        } else {
            System.out.println("interest accrual is not required");
            interestAccrued = false;
        }
        System.out.println("============service message============\n");
    }
}
