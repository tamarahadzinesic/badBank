package ch.engenius.bank.data;

import java.util.HashMap;

public class Bank {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public Account getByNumber(Integer accountNumber) {
        return accounts.get(accountNumber);
    }

    public void addAccount(Account account, Integer accountNumber) {
        accounts.put(accountNumber, account);
    }
}
