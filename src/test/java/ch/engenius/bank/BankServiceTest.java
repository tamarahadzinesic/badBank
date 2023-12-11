package ch.engenius.bank;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.data.Bank;
import ch.engenius.bank.service.BankServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankServiceTest {

    Bank bank;
    BankServiceImpl bankService;
    static int ACCOUNT_NUMBER = 123;
    static BigDecimal ACCOUNT_AMOUNT = BigDecimal.valueOf(100000.00);
    static BigDecimal NEGATIVE_ACCOUNT_AMOUNT = BigDecimal.valueOf(-100000.00);
    static int ACCOUNT_NUMBER_NOT_REGISTERED = 456;
    static String ERROR_MESSAGE_NEGATIVE_INITIAL_AMOUNT = "Initial account amount must be positive!";

    @BeforeEach
    void setUp() {
        bank = new Bank();
        bankService = new BankServiceImpl();
    }

    @Test
    void testRegisterAccount() {
        Account account = bankService.registerAccount(bank, ACCOUNT_NUMBER, ACCOUNT_AMOUNT);

        assertAll("register account",
                () -> assertEquals(account, bankService.getAccountByAccountNumber(bank, ACCOUNT_NUMBER)),
                () -> assertEquals(ACCOUNT_AMOUNT, bankService.getAccountByAccountNumber(bank, ACCOUNT_NUMBER).getMoney())
        );
    }

    @Test
    void testRegisterAccountWithNegativeInitialAmount() {
        Throwable exception = assertThrows(IllegalStateException.class, () ->
                bankService.registerAccount(bank, ACCOUNT_NUMBER, NEGATIVE_ACCOUNT_AMOUNT));
        assertEquals(ERROR_MESSAGE_NEGATIVE_INITIAL_AMOUNT, exception.getMessage());
    }

    @Test
    void testRegisterAccountAlreadyRegistered() {
        bankService.registerAccount(bank, ACCOUNT_NUMBER, ACCOUNT_AMOUNT);

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                bankService.registerAccount(bank, ACCOUNT_NUMBER, ACCOUNT_AMOUNT));
        assertEquals(getAccountAlreadyRegisteredErrorMessage(ACCOUNT_NUMBER), exception.getMessage());
    }

    @Test
    void testGetAccountWithAccountNumber() {
        Account registeredAccount = bankService.registerAccount(bank, ACCOUNT_NUMBER, ACCOUNT_AMOUNT);
        Account fetchedAccount = bankService.getAccountByAccountNumber(bank, ACCOUNT_NUMBER);

        assertEquals(registeredAccount, fetchedAccount);
    }

    @Test
    void testGetAccountThatIsNotRegistered() {
        bankService.registerAccount(bank, ACCOUNT_NUMBER, ACCOUNT_AMOUNT);

        Throwable exception = assertThrows(IllegalStateException.class, () ->
                bankService.getAccountByAccountNumber(bank, ACCOUNT_NUMBER_NOT_REGISTERED));
        assertEquals(
                getAccountThatIsNotRegisteredErrorMessage(ACCOUNT_NUMBER_NOT_REGISTERED),
                exception.getMessage()
        );
    }

    private String getAccountAlreadyRegisteredErrorMessage(int accountNumber) {
        return String.format("Account with number %s is already registered!", accountNumber);
    }

    private String getAccountThatIsNotRegisteredErrorMessage(int accountNumber) {
        return String.format("Account with number %s does not exist!", accountNumber);
    }
}
