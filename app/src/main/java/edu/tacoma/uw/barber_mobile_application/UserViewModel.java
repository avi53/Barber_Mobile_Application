package edu.tacoma.uw.barber_mobile_application;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<Account> mCurrentAccount;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mCurrentAccount = new MutableLiveData<>();
    }

    // Expose a method to observe changes in the account details
    public void observeAccountDetails(@NonNull LifecycleOwner owner, @NonNull Observer<? super Account> observer) {
        mCurrentAccount.observe(owner, observer);
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        if (error != null && error.networkResponse == null) {
            try {
                JSONObject errorObject = new JSONObject();
                errorObject.put("error", error.getMessage());
                mResponse.setValue(errorObject);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void addClient(Account theAccount) {
        String url = "https://students.washington.edu/hfarhat/register_client.php";
        JSONObject body = new JSONObject();
        try {
            body.put("Email", theAccount.getEmail());
            body.put("FirstName", theAccount.getFirstName());
            body.put("LastName", theAccount.getLastName());
            body.put("Password", theAccount.getPassword());
            body.put("PhoneNumber", theAccount.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //no body for this get request
                mResponse::setValue,
                this::handleError);

        Log.i("edu.tacoma.uw.barber_mobile_application.UserViewModel", request.getUrl().toString());
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    public void authenticateClient(Account theAccount) {
        String url = "https://students.washington.edu/hfarhat/login_clients.php";
        JSONObject body = new JSONObject();

        try {
            body.put("Email", theAccount.getEmail());
            body.put("Password", theAccount.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //no body for this get request
                mResponse::setValue,
                this::handleError);

        Log.i("UserViewModel", request.getUrl().toString());
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    private void handleAccountResult(final JSONObject result) {
        try {
            String firstName = result.getString(Account.FIRST_NAME);
            String lastName = result.getString(Account.LAST_NAME);
            String email = result.getString(Account.EMAIL);
            int phoneNumber = result.getInt(Account.PHONE_NUMBER);

            Account account = new Account(firstName, lastName, email, phoneNumber);

            mCurrentAccount.setValue(account);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
        }
    }

    public void getAccountByEmail(String theEmail) {
        String url = "https://students.washington.edu/hfarhat/get_client_by_email.php?email=" + theEmail;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleAccountResult,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }


}
