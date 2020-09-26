package com.example.first.Account;

import android.content.Context;

import com.example.first.Account.Repositories.CompositeRepo;
import com.example.first.Account.Repositories.RepoDB;

public class AccountCache {
    private static AccountCache accountCache;

    private CompositeRepo compositeRepo;

    public RepoDB getRepo() {
        return compositeRepo;
    }

    public static AccountCache getInstance(Context context) {
        if (accountCache == null) {
            accountCache = new AccountCache(context);
        }

        return accountCache;
    }

    private AccountCache(Context context) {
        compositeRepo = new CompositeRepo(context);
    }
}
