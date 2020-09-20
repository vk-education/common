package com.example.first.Account;

public class AccountCash {
    private static AccountCash accountCash;

    private AccountRepo accountRepo;

    public AccountRepo getAccountRepo() {
        return accountRepo;
    }

    public static AccountCash getInstance() {
        if (accountCash == null) {
            accountCash = new AccountCash();
        }

        return accountCash;
    }

    private AccountCash() {
        accountRepo = new AccountRepo();
    }
}
