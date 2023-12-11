package ch.engenius.bank.service;

import ch.engenius.bank.data.Account;

import java.math.BigDecimal;

public interface AccountService {

    void withdraw(Account account, BigDecimal bigDecimal);

    void deposit(Account account, BigDecimal bigDecimal);

    void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount);
}
