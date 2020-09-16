package com.r.uebook.ui.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.r.uebook.R;
import com.r.uebook.data.remote.client.RetrofitClient;
import com.r.uebook.data.remote.model.LoginResponse;
import com.r.uebook.data.remote.model.Result;
import com.r.uebook.data.remote.model.UserProfile;
import com.r.uebook.data.remote.model.ValidationResponse;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText email;
    private EditText passWord;
    private EditText confirmPassword;
    private ImageView showPassword1;
    private ImageView showPassword2;
    private LoginResponse result;
    private Button create_acc_bt;
    private ImageView back_bt;
    private ApiService apiService;

    private boolean show1 = false;
    private boolean show2 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        initViews();

        back_bt.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });
        create_acc_bt.setOnClickListener(view -> {
            if (!checkDataEntered()) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            } else {
                //API Calls
                userSignUp();

            }


        });


        showPassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show2) {
                    //Show Password:
                    show2 = false;
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    //hide
                    show2 = true;
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        showPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show1) {
                    //Show Password:
                    show1 = false;
                    passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    //hide
                    show1 = true;
                    passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = s.toString();
                if (mobileNumber.length() > 6) {
                    if (!TextUtils.isEmpty(mobileNumber) && Patterns.PHONE.matcher(mobileNumber).matches()) {
                        checkIfMobileNumberExists(mobileNumber);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                if ((!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                    checkIfEmailExists(email);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void initViews() {
        firstName = findViewById(R.id.first_name_et);
        lastName = findViewById(R.id.last_name_et);
        phoneNumber = findViewById(R.id.phone_number_et);
        email = findViewById(R.id.email_et);
        passWord = findViewById(R.id.password_et);
        confirmPassword = findViewById(R.id.confirm_pass_et);
        create_acc_bt = findViewById(R.id.create_acc_button);
        back_bt = findViewById(R.id.back_bt);
        showPassword1 = findViewById(R.id.sh_password1);
        showPassword2 = findViewById(R.id.sh_password2);

    }

    //region validations

    private void checkIfEmailExists(String email_id) {
        apiService.checkEmail(email_id).enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(@NotNull Call<ValidationResponse> call, @NotNull Response<ValidationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ValidationResponse validationResponse = response.body();
                    if (validationResponse.getResp() != null) {
                        if (validationResponse.getResp().equals("False")) {
                            email.setError("Email already exists ");
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ValidationResponse> call, @NotNull Throwable t) {
                Timber.d("network error");
            }
        });
    }

    private void checkIfMobileNumberExists(String mobileNumber) {
        apiService.checkMobileNumber(mobileNumber).enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(@NotNull Call<ValidationResponse> call, @NotNull Response<ValidationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ValidationResponse validationResponse = response.body();
                    if (validationResponse.getResp() != null) {
                        if (validationResponse.getResp().equals("False")) {
                            phoneNumber.setError("Mobile Number already exists!");
                            Toast.makeText(getApplicationContext(), "Mobile Number already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ValidationResponse> call, @NotNull Throwable t) {
                Timber.d("network error");
            }
        });
    }


    boolean isFirstName(EditText text) {
        CharSequence firstName = text.getText().toString();
        return (!TextUtils.isEmpty(firstName));
    }

    boolean isLastName(EditText text) {
        CharSequence lastName = text.getText().toString();
        return (!TextUtils.isEmpty(lastName));
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isPhone(EditText text) {
        CharSequence phone = text.getText().toString();
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }

    boolean isPassword(EditText text) {
        CharSequence password = text.getText().toString();
        return (!TextUtils.isEmpty(password));
    }

    boolean confirmPasswordCheck(EditText text1, EditText text2) {
        CharSequence cPassword = text2.getText().toString();
        String string1 = text1.getText().toString();
        String string2 = text2.getText().toString();
        return string1.equals(string2) && !TextUtils.isEmpty(cPassword);
    }

    Boolean checkDataEntered() {

        if (!isFirstName(firstName)) {
            firstName.setError("Enter valid First Name!");
            return false;
        }

        if (!isLastName(lastName)) {
            lastName.setError("Enter valid Last Name!");
            return false;
        }

        if (!isEmail(email)) {
            email.setError("Enter valid email!");
            return false;
        }

        if (!isPhone(phoneNumber)) {
            phoneNumber.setError("Enter valid Phone Number!");
            return false;
        }

        if (!isPassword(passWord)) {
            passWord.setError("Password cannot be Empty");
            return false;
        }

        if (!confirmPasswordCheck(passWord, confirmPassword)) {
            confirmPassword.setError("Password mismatch!");
            Toast.makeText(getApplicationContext(), "This password does not match the password entered above", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }


    //endregion

    private void userSignUp() {
        String first_name = firstName.getText().toString();
        String last_name = lastName.getText().toString();
        String email_s = email.getText().toString();
        String phone_s = phoneNumber.getText().toString();
        String password_s = passWord.getText().toString();
        String confirm_password_s = confirmPassword.getText().toString();
        //Defining the user object as we need to pass it with the call
        UserProfile user = new UserProfile(first_name, last_name, phone_s, email_s);
        apiService.createUser(
                user.getUserMail(),
                password_s,
                user.getUserContactNo(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName()
        ).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result = response.body();
                    Timber.d(result.toString());
                    if (result != null) {
                        Timber.d(result.toString());
                        //print result.getResp()
                        Timber.d(result.getResp());
                        Timber.d(String.valueOf(result.getResp().contains("true")));
                        if (result.getResp().equals("true")) {
                            AppUtils.setLogin(result.getUserProfile().get(0));
                            Prefs.setPreferences(USERNAME, result.getUserProfile().get(0).getUserMail());
                            Prefs.setPreferences(PASSWORD, password_s);
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else if (result.getResp().equals("false")) {
                            Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_LONG).show();
                        } else if (result.getResp().contains("error")) {
                            Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_LONG).show();
                        }

                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_LONG).show();
                Timber.d("Network error ");
            }
        });
    }


}
