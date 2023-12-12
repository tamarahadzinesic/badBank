package ch.engenius.bank;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    Account account;
    AccountServiceImpl accountService;
    static BigDecimal INITIAL_ACCOUNT_AMOUNT = BigDecimal.valueOf(100000.00);
    static BigDecimal DEPOSIT_AMOUNT = BigDecimal.valueOf(5000.00);
    static BigDecimal NEGATIVE_DEPOSIT_AMOUNT = BigDecimal.valueOf(-5000.00);
    static BigDecimal WITHDRAW_AMOUNT = BigDecimal.valueOf(5000.00);
    static BigDecimal UNAVAILABLE_WITHDRAW_AMOUNT = BigDecimal.valueOf(200000.00);
    static BigDecimal TRANSFER_AMOUNT = BigDecimal.valueOf(10000.00);
    static BigDecimal NEGATIVE_TRANSFER_AMOUNT = BigDecimal.valueOf(-10000.00);
    static String ERROR_MESSAGE_UNAVAILABLE_WITHDRAW_AMOUNT = "Not enough credits on the account!";
    static String ERROR_MESSAGE_NEGATIVE_AMOUNT = "Amount cannot be negative!";

    @BeforeEach
    public void setUp() {
        account = new Account(INITIAL_ACCOUNT_AMOUNT);
        accountService = new AccountServiceImpl();
    }

    @Test
    void testGetAmount() {
        assertEquals(INITIAL_ACCOUNT_AMOUNT, account.getMoney());
    }

    @Test
    void testDepositAmount() {
        BigDecimal expectedAmount = INITIAL_ACCOUNT_AMOUNT.add(DEPOSIT_AMOUNT);
        accountService.deposit(account, DEPOSIT_AMOUNT);

        assertEquals(expectedAmount, account.getMoney());
    }

    @Test
    void testNegativeDepositAmount() {
        Throwable exception = assertThrows(IllegalStateException.class, () ->
                accountService.deposit(account, NEGATIVE_DEPOSIT_AMOUNT));
        assertEquals(ERROR_MESSAGE_NEGATIVE_AMOUNT, exception.getMessage());
    }

    @Test
    void testWithdrawAmount() {
        BigDecimal expectedAmount = INITIAL_ACCOUNT_AMOUNT.subtract(WITHDRAW_AMOUNT);
        accountService.withdraw(account, WITHDRAW_AMOUNT);

        assertEquals(expectedAmount, account.getMoney());
    }

    @Test
    void testWithdrawUnavailableAmount() {
        Throwable exception = assertThrows(IllegalStateException.class, () ->
                accountService.withdraw(account, UNAVAILABLE_WITHDRAW_AMOUNT));
        assertEquals(ERROR_MESSAGE_UNAVAILABLE_WITHDRAW_AMOUNT, exception.getMessage());
    }

    @Test
    void testTransferAmount() {
        Account sourceAccount = new Account(INITIAL_ACCOUNT_AMOUNT);
        Account targetAccount = new Account(INITIAL_ACCOUNT_AMOUNT);
        BigDecimal expectedAmountSourceAccount = INITIAL_ACCOUNT_AMOUNT.subtract(TRANSFER_AMOUNT);
        BigDecimal expectedAmountTargetAccount = INITIAL_ACCOUNT_AMOUNT.add(TRANSFER_AMOUNT);
        accountService.transfer(sourceAccount, targetAccount, TRANSFER_AMOUNT);

        assertEquals(expectedAmountSourceAccount, sourceAccount.getMoney());
        assertEquals(expectedAmountTargetAccount, targetAccount.getMoney());
    }

    @Test
    void testTransferNegativeAmount() {
        Account sourceAccount = new Account(INITIAL_ACCOUNT_AMOUNT);
        Account targetAccount = new Account(INITIAL_ACCOUNT_AMOUNT);

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                accountService.transfer(sourceAccount, targetAccount, NEGATIVE_TRANSFER_AMOUNT));
        assertEquals(ERROR_MESSAGE_NEGATIVE_AMOUNT, exception.getMessage());
    }
}
