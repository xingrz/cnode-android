package org.cnodejs.account;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.android.volley.AuthFailureError;

public class AccountUtils {

    public static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            return accounts[0];
        } else {
            return null;
        }
    }

    public static String getAccessToken(AccountManager accountManager) {
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        }

        return getAccessToken(accountManager, account);
    }

    public static String getAccessToken(AccountManager accountManager, Account account) {
        if (account == null) {
            return null;
        }

        return accountManager.getPassword(account);
    }

    public static void cleanExistedAccounts(AccountManager accountManager) {
        for (Account account : accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE)) {
            accountManager.removeAccount(account, null, null);
        }
    }

    public static boolean authFailed(Throwable e) {
        return e instanceof AuthFailureError;
    }

}
