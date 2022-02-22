package com.pueblo.software.base.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pueblo.software.base.common.CommonMethods;
import com.pueblo.software.base.common.CommonVariables;
import com.pueblo.software.base.dto.LoginDTO;
import com.pueblo.software.base.interfaces.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import software.pueblo.com.fucha.R;

public class LoginActivity extends AppCompatActivity {

    Button doLoginButton;
    TextView loginTV, passwordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        doLoginButton = findViewById(R.id.doLoginButtonId);
        loginTV = findViewById(R.id.loginInputId);
        passwordTV = findViewById(R.id.passwordInputId);

        doLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkFields()){
                    RetrofitInterface retrofitInterface = CommonMethods.getRetrofitInterface();
                    Call<LoginDTO> call = retrofitInterface.doLogin(loginTV.getText().toString(),passwordTV.getText().toString());
                    call.enqueue(new Callback<LoginDTO>() {
                        @Override
                        public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                            if(response.isSuccessful()){
                                LoginDTO loginDTO = response.body();
                                if(loginDTO.getOnLoginResponse().equals(CommonVariables.ON_LOGIN_OK)){
                                    CommonVariables.loginDTO = loginDTO;
                                    CommonVariables.avatar = loginDTO.getAvatar();
                                    Intent intent = new Intent(LoginActivity.this, HomeMapActivity.class);
                                    startActivity(intent);
                                }
                                if(loginDTO.getOnLoginResponse().equals(CommonVariables.ON_LOGIN_WRONG_CREDENTIALS)){
                                    Toast.makeText(LoginActivity.this,R.string.wrong_login_credentials_toast, Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginDTO> call, Throwable t) {

                            Log.e (this.getClass().getSimpleName(),t.getMessage());
                        }
                    });

                }
            }
        });
    }

    private boolean checkFields() {
        if(loginTV.getText().length()==0){
            loginTV.setError(getResources().getString(R.string.fill_login_field));
            return false;
        }
        if(passwordTV.getText().length()==0){
            passwordTV.setError(getResources().getString(R.string.fill_password_field));
            return false;
        }
        return true;
    }
}
