package ch.engenius.bank.service;

import ch.engenius.bank.data.Account;
import ch.engenius.bank.data.Bank;

import java.math.BigDecimal;

public interface BankService {

    Account registerAccount(Bank bank, int accountNumber, BigDecimal amount);

    Account getAccountByAccountNumber(Bank bank, int number);
}
