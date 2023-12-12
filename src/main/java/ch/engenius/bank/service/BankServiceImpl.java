package ch.engenius.bank.service;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.data.Bank;

import java.math.BigDecimal;

public class BankServiceImpl implements BankService {

    /**
     * Register new account.
     *
     * @param bank          a bank within which an account is registered
     * @param accountNumber an account number to be assigned to the account
     * @param amount        an initial account money amount
     * @return registered account
     * @throws IllegalStateException if initial account money amount is negative
     * @throws IllegalStateException if there's already an account registered with the specific account number
     */
    @Override
    public Account registerAccount(Bank bank, int accountNumber, BigDecimal amount) {
        if (isNegativeAmount(amount)) {
            throw new IllegalStateException("Initial account amount must be positive!");
        }
        if (isAccountRegistered(bank, accountNumber)) {
            throw new IllegalStateException("Account with number " + accountNumber + " is already registered!");
        }

        Account account = new Account(amount);
        bank.addAccount(account, accountNumber);

        return account;
    }

    /**
     * Get the account by account number.
     *
     * @param bank          a bank within which an account is retrieved
     * @param accountNumber an account number by which an account is retrieved
     * @return account with specific account number
     * @throws IllegalStateException if there's no account with specific account number
     */
    @Override
    public synchronized Account getAccountByAccountNumber(Bank bank, int accountNumber) {
        if (!isAccountRegistered(bank, accountNumber)) {
            throw new IllegalStateException(String.format("Account with number %s does not exist!", accountNumber));
        }
        return bank.getByNumber(accountNumber);
    }

    private boolean isAccountRegistered(Bank bank, int accountNumber) {
        return (bank.getByNumber(accountNumber) != null);
    }

    private boolean isNegativeAmount(BigDecimal amount) {
        return (amount.compareTo(BigDecimal.ZERO) < 0);
    }
}
