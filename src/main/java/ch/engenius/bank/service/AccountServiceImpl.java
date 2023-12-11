package ch.engenius.bank.service;

import ch.engenius.bank.data.Account;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {
    /**
     * Withdraw money from an account.
     *
     * @param account an account from which money will be withdrawn
     * @param amount  an amount to withdraw
     * @throws IllegalStateException if there's not enough money on the account to be withdrawn
     */
    @Override
    synchronized public void withdraw(Account account, BigDecimal amount) {
        if (!isEnoughMoneyOnAccountForWithdraw(account, amount)) {
            throw new IllegalStateException("Not enough credits on the account!");
        }
        account.setMoney(account.getMoney().subtract(amount));
    }

    /**
     * Deposit money to an account.
     *
     * @param account an account to which money will be deposited
     * @param amount  an amount to deposit
     * @throws IllegalStateException if the amount to be deposited is negative
     */
    @Override
    public void deposit(Account account, BigDecimal amount) {
        validateAmount(amount);
        synchronized (this) {
            account.setMoney(account.getMoney().add(amount));
        }
    }

    /**
     * Transfer money from one account to another.
     *
     * @param sourceAccount an account from which transfer amount will be withdrawn
     * @param targetAccount an account to which transfer amount will be deposited
     * @param amount        an amount to transfer
     * @throws IllegalStateException if the amount to be transferred is negative
     */
    @Override
    public synchronized void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        validateAmount(amount);
        synchronized (this) {
            withdraw(sourceAccount, amount);
            deposit(targetAccount, amount);
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (isNegativeAmount(amount)) {
            throw new IllegalStateException("Amount cannot be negative!");
        }
    }

    private boolean isEnoughMoneyOnAccountForWithdraw(Account account, BigDecimal amount) {
        return (account.getMoney().subtract(amount)).compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isNegativeAmount(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
}
