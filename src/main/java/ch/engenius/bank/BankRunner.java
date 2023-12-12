package ch.engenius.bank;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.data.Bank;
import ch.engenius.bank.service.AccountServiceImpl;
import ch.engenius.bank.service.BankServiceImpl;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class BankRunner {

    private static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private final Random random = new Random(43);
    private final Bank bank = new Bank();

    private final AccountServiceImpl accountService = new AccountServiceImpl();
    private final BankServiceImpl bankService = new BankServiceImpl();

    private static final Logger LOGGER = Logger.getLogger(BankRunner.class.getName());

    public static void main(String[] args) {
        BankRunner runner = new BankRunner();
        int accounts = 100;
        int defaultDeposit = 1000;
        int iterations = 10000;
        runner.registerAccounts(accounts, BigDecimal.valueOf(defaultDeposit));
        runner.sanityCheck(accounts, accounts * defaultDeposit);
        runner.runBank(iterations, accounts);
        runner.sanityCheck(accounts, accounts * defaultDeposit);

    }

    private void runBank(int iterations, int maxAccount) {
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> runRandomOperation(maxAccount));
        }
        try {
            executor.shutdown();
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "Interrupted execution", e);
        }
    }

    private void runRandomOperation(int maxAccount) {
        BigDecimal transfer = BigDecimal.valueOf(random.nextDouble() * 100.0);
        int accountInNumber = random.nextInt(maxAccount);
        int accountOutNumber = random.nextInt(maxAccount);
        Account accIn = bankService.getAccountByAccountNumber(bank, accountInNumber);
        Account accOut = bankService.getAccountByAccountNumber(bank, accountOutNumber);
        accountService.transfer(accOut, accIn, transfer);
    }

    private void registerAccounts(int number, BigDecimal defaultMoney) {
        for (int i = 0; i < number; i++) {
            bankService.registerAccount(bank, i, defaultMoney);
        }
    }

    private void sanityCheck(int accountMaxNumber, int totalExpectedMoney) {
        BigDecimal sum = IntStream.range(0, accountMaxNumber)
                .mapToObj(num -> bankService.getAccountByAccountNumber(bank, num))
                .map(Account::getMoney)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.intValue() != totalExpectedMoney) {
            throw new IllegalStateException("we got " + sum + " != " + totalExpectedMoney + " (expected)");
        }
        LOGGER.log(Level.INFO, "sanity check OK");
    }


}
