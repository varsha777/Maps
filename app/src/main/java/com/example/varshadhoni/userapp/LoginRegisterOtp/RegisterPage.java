package com.example.varshadhoni.userapp.LoginRegisterOtp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.varshadhoni.userapp.R;
import com.example.varshadhoni.userapp.RetrofitFiles.ApiService;
import com.example.varshadhoni.userapp.RetrofitFiles.Constants;
import com.example.varshadhoni.userapp.RetrofitFiles.ImageClass;
import com.example.varshadhoni.userapp.RetrofitFiles.ServerRequest;
import com.example.varshadhoni.userapp.RetrofitFiles.ServerResponse;
import com.example.varshadhoni.userapp.RetrofitFiles.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class RegisterPage extends android.app.Fragment implements View.OnClickListener {

    EditText userName, userPhone, userEmail, userPassword, userRePassword;
    Button submit;
    CircleImageView userProfil;
    RelativeLayout name_phone, email_password;
    int count = 0, countSub = 0;
    String picturePath;
    Account[] accounts;
    CharSequence[] options;
    String[] email_list;
    ProgressBar progress;
    KonfettiView konfettiView;
    Bitmap bitmap;


//    File file = new File(picturePath);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    void initialVariables(View view) {
        userName = (EditText) view.findViewById(R.id.user_name);
        userEmail = (EditText) view.findViewById(R.id.user_email);
        userPassword = (EditText) view.findViewById(R.id.user_password);
        userPhone = (EditText) view.findViewById(R.id.user_phone);
        userRePassword = (EditText) view.findViewById(R.id.user_rePassword);
        submit = (Button) view.findViewById(R.id.user_signup);
        name_phone = (RelativeLayout) view.findViewById(R.id.user_name_phone);
        email_password = (RelativeLayout) view.findViewById(R.id.user_email_password);
        userProfil = (CircleImageView) view.findViewById(R.id.circular_profile);
        progress = (ProgressBar) view.findViewById(R.id.progressBar);


        submit.setOnClickListener(this);
        userProfil.setOnClickListener(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rview = inflater.inflate(R.layout.fragment_register_page, container, false);
        initialVariables(rview);

        email_password.setVisibility(View.GONE);
        name_phone.setVisibility(View.VISIBLE);
        submit.setEnabled(false);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        accounts = AccountManager.get(getActivity()).getAccounts();
        options = new String[accounts.length];
        int j = 0;
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                options[j] = account.name;
                j++;
            }
        }

        email_list = new String[j + 1];
        for (int i = 0; i < j; i++) {
            email_list[i] = options[i].toString();
        }

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String str = userName.getText().toString();
                if (str.length() == 0) {
                    buttonDesableName(0, "Fill All", "Required Filed");
                } else if (str.length() < 6) {
                    buttonDesableName(0, "Fill All", "Name Length Too Low");
                } else if (str.length() >= 12) {
                    buttonDesableName(0, "Fill All", "Exceeds Name length");
                } else {
                    count = 1;
                }
                if (count == 2) {
                    submit.setText("next");
                    submit.setEnabled(true);
                }

            }
        });

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
                    buttonDesablePhone(1, "Fill All", "Not A Valid Mobile Number");
                } else if (str.length() < 10) {
                    buttonDesablePhone(1, "Fill All", "Not A Valid Mobile Number");
                } else if ((str.length() > 10)) {
                    buttonDesablePhone(1, "Fill All", "Not A Valid Mobile Number");
                } else {
                    count = count + 1;
                }

                if (count == 2) {
                    submit.setText("next");
                    submit.setEnabled(true);
                }

            }
        });

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String estr = userEmail.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(estr).matches()) {
                    buttonDesableEmail(0, "Fill All", "Not A Email");
                } else {
                    countSub = 1;
                }

                if (countSub == 3) {
                    submit.setText("submit");
                    submit.setEnabled(true);
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
                } else if (str.length() < 6) {
                    buttonDesablePassword(1, "Fill All", "Password Length Too Short");
                } else if (str.length() >= 12) {
                    buttonDesablePassword(1, "Fill All", "Exceeds Password length");
                } else {
                    countSub = 2;
                }
                if (countSub == 3) {
                    submit.setText("submit");
                    submit.setEnabled(true);
                }
            }
        });

        userRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String pass = userPassword.getText().toString();
                String repass = userRePassword.getText().toString();

                if (!pass.equals(repass)) {
                    buttonDesableRePassword(2, "Fill All", "Password Dosn't Match");
                } else {
                    countSub = 3;
                }

                if (countSub == 3) {
                    submit.setText("submit");
                    submit.setEnabled(true);
                }
            }
        });

        return rview;
    }


    private void selectEmail() {

        //final CharSequence[] options2 = {"Take Photo", "Choose from Gallery", "Cancel"};

        email_list[email_list.length - 1] = "Cancel";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Email");
        builder.setItems(email_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (email_list[item].equals("Cancel")) {
                    dialog.dismiss();
                } else {
                    userEmail.setText(options[item]);
                }
            }
        });
        builder.show();

    }


    void buttonDesableEmail(int c, String b_title, String error) {
        this.countSub = c;
        submit.setText(b_title);
        userEmail.setError(error);
        submit.setEnabled(false);
    }

    void buttonDesablePassword(int c, String b_title, String error) {
        this.countSub = c;
        submit.setText(b_title);
        userPassword.setError(error);
        submit.setEnabled(false);
    }

    void buttonDesableRePassword(int c, String b_title, String error) {
        this.countSub = c;
        submit.setText(b_title);
        userRePassword.setError(error);
        submit.setEnabled(false);
    }

    void buttonDesablePhone(int c, String b_title, String error) {
        this.count = c;
        submit.setText(b_title);
        userPhone.setError(error);
        submit.setEnabled(false);

    }

    void buttonDesableName(int c, String b_title, String error) {
        this.count = c;
        submit.setText(b_title);
        userName.setError(error);
        submit.setEnabled(false);

    }


    @Override
    public void onClick(View view) {

        String ss = submit.getText().toString();
        switch (ss) {
            case "next":
                name_phone.setVisibility(View.GONE);
                email_password.setVisibility(View.VISIBLE);
                selectEmail();
                break;
            case "submit":
                Toast.makeText(getActivity(), "All Details filled", Toast.LENGTH_SHORT).show();
                registerProcess(userName.getText().toString(), userEmail.getText().toString(), userPhone.getText().toString(), userPassword.getText().toString());
                break;
        }

        switch (view.getId()) {
            case R.id.circular_profile:
                Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
                selectImage();
                break;
        }

    }

    private void registerProcess(String name, String email, String phone, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService requestInterface = retrofit.create(ApiService.class);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                progress.setVisibility(View.VISIBLE);
                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if (resp.getResult().equals("success")) {
                    //showAnimateDiaogBox();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, "failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    void showAnimateDiaogBox() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mview = getActivity().getLayoutInflater().inflate(R.layout.animation_view, null);
        konfettiView = (KonfettiView) mview.findViewById(R.id.konfettiView);
        TextView txt = (TextView) mview.findViewById(R.id.congratstext);
        txt.setText("Congratulations \n Your Account is Created \n WELCOME");

        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(300, 5000L);

        Button click = (Button) mview.findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerSuccessReport();
            }
        });
        mBuilder.setView(mview);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void registerSuccessReport() {

        Toast.makeText(getActivity(), "Registration Compleate Login\n Now with Credientials", Toast.LENGTH_SHORT).show();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, new LoginPage()).commit();
    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);


                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    // userProfil.setImageBitmap(bitmap);

                    Glide.with(getActivity())
                            .load(bitmap)
                            .into(userProfil);


                    picturePath = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "UserApp" + File.separator + "Images";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(picturePath, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("path of image ", picturePath + "");
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                Log.e("Image Uri::", selectedImage.toString());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));
                Log.e("path of image ", picturePath + "");
                //userProfil.setImageBitmap(thumbnail);

                Glide.with(getActivity())
                        .load(bitmap)
                        .into(userProfil);
            }
        }
    }


}