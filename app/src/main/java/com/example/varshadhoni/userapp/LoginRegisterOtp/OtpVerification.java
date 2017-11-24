package com.example.varshadhoni.userapp.LoginRegisterOtp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.varshadhoni.userapp.R;


public class OtpVerification extends android.app.Fragment implements View.OnClickListener {

    Button submit;
    ImageView editimg;
    PinView setOtp;
    TextView mob;
    String strtext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    void intilizeVariables(View view) {
        submit = (Button) view.findViewById(R.id.submit_otp);
        submit.setOnClickListener(this);
        editimg = (ImageView) view.findViewById(R.id.edit_otp_mobile_number);
        editimg.setOnClickListener(this);
        setOtp = (PinView) view.findViewById(R.id.pinview);
        mob = (TextView) view.findViewById(R.id.otp_entered_mobile_number);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        strtext = getArguments().getString("CID");
        View oview = inflater.inflate(R.layout.fragment_otp_verification, container, false);
        intilizeVariables(oview);

        mob.setText(strtext);

        return oview;
    }


    @Override
    public void onClick(View view) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch (view.getId()) {
            case R.id.submit_otp:
                Toast.makeText(getActivity(), "Compleate Verification", Toast.LENGTH_SHORT).show();
                fragmentTransaction.replace(R.id.main_fragment, new RegisterPage()).commit();
                break;

            case R.id.edit_otp_mobile_number:
                Toast.makeText(getActivity(), "Back", Toast.LENGTH_SHORT).show();
                fragmentTransaction.replace(R.id.framellayout, new OtpGenerate()).commit();
                break;

        }
    }
}
