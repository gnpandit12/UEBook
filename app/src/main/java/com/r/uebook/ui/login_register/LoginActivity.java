package com.r.uebook.ui.login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.r.uebook.R;
import com.r.uebook.data.remote.client.RetrofitClient;
import com.r.uebook.data.remote.model.LoginResponse;
import com.r.uebook.data.remote.service.ApiService;
import com.r.uebook.ui.activity.MainActivity;
import com.r.uebook.utils.AppUtils;
import com.r.uebook.utils.Prefs;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.r.uebook.utils.ApplicationConstants.PASSWORD;
import static com.r.uebook.utils.ApplicationConstants.USERNAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email_et;
    EditText password_et;
    Button login;
    TextView forget_pass_tc;
    TextView tv_register;
    ImageView img_show_pswd;
    private ApiService apiService;

    private boolean showPassword = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        initView();

        login.setOnClickListener(this);

        forget_pass_tc.setOnClickListener(view -> {
            //Do something for Forget Password Scenario
            Timber.d("forget password works here !!!");
//            Toast.makeText(getApplicationContext(), "Forget Password", Toast.LENGTH_SHORT).show();

        });

        img_show_pswd.setOnClickListener(v -> {

            if (showPassword) {
                //Show Password:
                showPassword = false;
                password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            } else {
                //hide
                showPassword = true;
                password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }


        });


    }

    private void initView() {
        email_et = findViewById(R.id.email);
        password_et = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        forget_pass_tc = findViewById(R.id.forgot_pass);
        tv_register = findViewById(R.id.new_user_tv);
        img_show_pswd = findViewById(R.id.shpassword);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public void open_register_activity(View v) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);

    }

    private void userSignIn(String email, String password) {

        apiService.isValidUser(email, password)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse result = response.body();
                            if (result != null && result.getResp().equals("true")) {
                                Timber.d(result.toString());
                                //print result.getResp()
                                Timber.d(result.getResp());
                                Timber.d(String.valueOf(result.getResp().contains("rue")));
                                Timber.d(String.valueOf(result.getCount()));
                                Timber.d(result.getFriends().toString());
                                AppUtils.setLogin(result.getUserProfile().get(0));
                                Prefs.setPreferences(USERNAME, result.getUserProfile().get(0).getUserMail());
                                Prefs.setPreferences(PASSWORD, password);
                                if (result.getResp().contains("rue")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("loginResponse", result);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(getApplication(), "Check your username and password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                        Timber.d("Error occurred in login api");
                        Toast.makeText(getApplication(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == login) {
            if (checkDataEntered()) {
                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();
                userSignIn(email, password);
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
            hideKeyboard();
        }
    }

    Boolean checkDataEntered() {
        if (!isEmail(email_et)) {
            email_et.setError("Enter valid email");
            return false;
        }
        return true;

    }

}

