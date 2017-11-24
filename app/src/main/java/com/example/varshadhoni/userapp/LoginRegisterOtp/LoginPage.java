package com.example.varshadhoni.userapp.LoginRegisterOtp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varshadhoni.userapp.MainActivity;
import com.example.varshadhoni.userapp.MainShowCase.MainShowCase;
import com.example.varshadhoni.userapp.R;
import com.example.varshadhoni.userapp.RetrofitFiles.ApiService;
import com.example.varshadhoni.userapp.RetrofitFiles.Constants;
import com.example.varshadhoni.userapp.RetrofitFiles.ServerRequest;
import com.example.varshadhoni.userapp.RetrofitFiles.ServerResponse;
import com.example.varshadhoni.userapp.RetrofitFiles.User;
import com.tapadoo.alerter.Alerter;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginPage extends android.app.Fragment implements View.OnClickListener {

    TextView newRegisteration;
    EditText userPhone, userPassword;
    Button userLoginBtn;
    int count = 0;
    KonfettiView konfettiView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void intilizeVariables(View view) {
        newRegisteration = (TextView) view.findViewById(R.id.new_user_register);
        userPassword = (EditText) view.findViewById(R.id.user_password_login);
        userPhone = (EditText) view.findViewById(R.id.user_phone_login);
        userLoginBtn = (Button) view.findViewById(R.id.user_login_btn);
        newRegisteration.setOnClickListener(this);
        userLoginBtn.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mview = inflater.inflate(R.layout.fragment_login_page, container, false);
        intilizeVariables(mview);
        userLoginBtn.setEnabled(false);


        userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String str = userPhone.getText().toString();
                if (str.length() == 0) {
                    buttonDesablePhone(0, "Fill All", "Not A Valid Mobile Number");
                } else if (str.length() < 10) {
                    buttonDesablePhone(0, "Fill All", "Not A Valid Mobile Number");
                } else if ((str.length() > 10)) {
                    buttonDesablePhone(0, "Fill All", "Not A Valid Mobile Number");
                } else {
                    count = count + 1;
                }

                if (count == 2) {
                    userLoginBtn.setText("sign in");
                    userLoginBtn.setEnabled(true);
                }

            }
        });

        userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                String str = userPassword.getText().toString();
                if (str.length() == 0) {
                    buttonDesablePassword(1, "Fill All", "Required Filed");
                } else {
                    count = 2;
                }
                if (count == 2) {
                    userLoginBtn.setText("sign in");
                    userLoginBtn.setEnabled(true);
                }
            }
        });


        return mview;
    }


    void buttonDesablePassword(int c, String b_title, String error) {
        this.count = c;
        userLoginBtn.setText(b_title);
        userPassword.setError(error);
        userLoginBtn.setEnabled(false);
    }

    void buttonDesablePhone(int c, String b_title, String error) {
        this.count = c;
        userLoginBtn.setText(b_title);
        userPhone.setError(error);
        userLoginBtn.setEnabled(false);

    }


    @Override
    public void onClick(View view) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (view.getId()) {
            case R.id.new_user_register:

                Toast.makeText(getActivity(), "welcome", Toast.LENGTH_SHORT).show();
                fragmentTransaction.replace(R.id.main_fragment, new OtpGenerate()).commit();
                break;
            case R.id.user_login_btn:

                loginProcess(userPhone.getText().toString(), userPassword.getText().toString());
                break;
        }
    }


    private void loginProcess(String phone, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService requestInterface = retrofit.create(ApiService.class);

        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                Alerter.create(getActivity())
                        .setTitle("Processing....")
                        .enableProgress(true)
                        .setProgressColorRes(R.color.white)
                        .show();

                /*if(resp.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.putString(Constants.EMAIL,resp.getUser().getEmail());
                    editor.putString(Constants.NAME,resp.getUser().getName());
                    editor.putString(Constants.UNIQUE_ID,resp.getUser().getUnique_id());
                    editor.apply();
                    goToProfile();

                }*/
                // progress.setVisibility(View.INVISIBLE);

                if (resp.getResult().equals("success")) {
                    // Alerter.hide();

                    Snackbar.make(getView(), resp.getResult() + resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    getActivity().startActivity(new Intent(getActivity(), MainShowCase.class));
                } else {
                    Alerter.hide();
                    Alerter.create(getActivity())
                            .setTitle(resp.getResult())
                            .setText(resp.getMessage())
                            .enableSwipeToDismiss()
                            .setBackgroundColorInt(getResources().getColor(R.color.alert))
                            .show();

                    Snackbar.make(getView(), "Fail to Login" + resp.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                //progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }


}
