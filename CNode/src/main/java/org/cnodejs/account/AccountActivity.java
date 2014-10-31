package org.cnodejs.account;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.cnodejs.R;
import org.cnodejs.api.GsonRequest;
import org.cnodejs.api.model.Validation;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends ActionBarActivity {

    private static final String TAG = "AccountActivity";

    private AccountManager accountManager;
    private AccountAuthenticatorResponse authResponse;
    private Bundle authResult;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accountManager = AccountManager.get(this);
        authResponse = getIntent()
                .getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (authResponse != null) {
            authResponse.onRequestContinued();
        }

        queue = Volley.newRequestQueue(this);

        Button signinButton = (Button) findViewById(R.id.signin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanning();
            }
        });

        Button signoutButton = (Button) findViewById(R.id.signout);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountUtils.cleanExistedAccounts(accountManager);
                finish();
            }
        });

        Account account = AccountUtils.getAccount(accountManager);
        if (account != null) {
            signinButton.setVisibility(View.GONE);
            signoutButton.setText("注销 " + account.name);
            signoutButton.setVisibility(View.VISIBLE);
        } else {
            signoutButton.setVisibility(View.GONE);
            signinButton.setVisibility(View.VISIBLE);
        }
    }

    private void startScanning() {
        new IntentIntegrator(this)
                .setResultDisplayDuration(0)
                .initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String token = result.getContents();
            Log.d(TAG, "token: " + token);
            validate(token);
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void validate(final String token) {
        new GsonRequest<Validation>(Request.Method.POST, Validation.class, "/accesstoken") {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected void deliverResponse(Validation response) {
                if (response.success) {
                    AccountUtils.cleanExistedAccounts(accountManager);

                    Account account = new Account(
                            response.loginname,
                            AccountAuthenticator.ACCOUNT_TYPE);

                    accountManager.addAccountExplicitly(account, token, null);

                    authResult = new Bundle();
                    authResult.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    authResult.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);

                    finish();
                } else {
                    // TODO
                }
            }

            @Override
            public void deliverError(VolleyError error) {
                Log.e(TAG, "sign in failed", error);
                if (AccountUtils.authFailed(error)) {
                    // TODO
                }
            }
        }.enqueue(queue);
    }

    @Override
    public void finish() {
        if (authResponse != null) {
            if (authResult != null) {
                authResponse.onResult(authResult);
                setResult(RESULT_OK, new Intent().putExtras(authResult));
            } else {
                authResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
                setResult(RESULT_CANCELED);
            }

            authResponse = null;
        }

        super.finish();
    }

}
