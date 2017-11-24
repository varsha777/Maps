package com.example.varshadhoni.userapp.LoginRegisterOtp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varshadhoni.userapp.R;


public class OtpGenerate extends android.app.Fragment implements View.OnClickListener {

    Button submit;
    EditText getOtpMobile;
    String number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void intilizeVariables(View view) {
        submit = (Button) view.findViewById(R.id.get_otp_button);
        getOtpMobile = (EditText) view.findViewById(R.id.get_mobile_number);
        submit.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_otp_generate, container, false);
        intilizeVariables(mview);

        submit.setEnabled(false);
        getOtpMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String str = getOtpMobile.getText().toString();
                if (str.length() == 0) {
                    buttonDesablePhone(1, "Fill All", "Not A Valid Mobile Number");
                } else if (str.length() < 10) {
                    buttonDesablePhone(1, "Fill All", "Not A Valid Mobile Number");
                } else if ((str.length() > 10)) {
                    buttonDesablePhone(1, "Fill All", "Not A Valid Mobile Number");
                } else {
                    submit.setText("submit");
                    submit.setEnabled(true);
                }

            }
        });

        return mview;
    }


    void buttonDesablePhone(int c, String b_title, String error) {
        submit.setText(b_title);
        getOtpMobile.setError(error);
        submit.setEnabled(false);

    }


    @Override
    public void onClick(View view) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (view.getId()) {
            case R.id.get_otp_button:
                Toast.makeText(getActivity(), "welcome  to verification", Toast.LENGTH_SHORT).show();
                number = getOtpMobile.getText().toString();
                Toast.makeText(getActivity(), number, Toast.LENGTH_SHORT).show();

                OtpVerification fr = new OtpVerification();
                Bundle args = new Bundle();
                args.putString("CID", number);
                fr.setArguments(args);

                fragmentTransaction.replace(R.id.main_fragment, fr).commit();
                break;
        }
    }

}
