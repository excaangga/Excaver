package com.excaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ActivitySignUp extends AppCompatActivity {

    private EditText mNameField, mEmailField, mPasswordField;
    private Button mRegisterBtn;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserAccount;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "RegisterActivity";

    private Uri mResultUri= null;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });
    }

    private void startRegister(){

        final String name = mNameField.getText().toString().trim();
        final String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password)){
            mProgress.setMessage("Mendaftarkan");
            mProgress.setCancelable(false);
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( ActivitySignUp.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        uid = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mUserAccount.child(uid);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("id").setValue(uid);
                        current_user_db.child("newuser").setValue("true");
                        current_user_db.child("image").setValue("default").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgress.dismiss();
                                sendVerificationEmail();
                            }
                        });
                    }

                    else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        mProgress.dismiss();
                        mEmailField.requestFocus();
                        mEmailField.setError("Alamat Email tidak valid!");

                    }

                    else if (password.length() < 8) {
                        mProgress.dismiss();
                        mPasswordField.requestFocus();
                        mPasswordField.setError("Password minimal 8 karakter!");
                    }

                    else{
                        mProgress.dismiss();
                        Toast.makeText(ActivitySignUp.this, "Akun dengan Email tersebut telah terdaftar!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else if (TextUtils.isEmpty(name)){
            Toast.makeText(ActivitySignUp.this, "Masukkan nama terlebih dahulu", Toast.LENGTH_SHORT).show();
            mNameField.requestFocus();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(ActivitySignUp.this, "Masukkan Email terlebih dahulu", Toast.LENGTH_SHORT).show();
            mEmailField.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(ActivitySignUp.this, "Masukkan Pasword terlebih dahulu", Toast.LENGTH_SHORT).show();
            mPasswordField.requestFocus();
        }
    }

    private void sendVerificationEmail(){
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            showDialog();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(ActivitySignUp.this, "Please Try Again", Toast.LENGTH_LONG).show();
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });*/
    }

    private void showDialog(){
        /*final String email = mEmailField.getText().toString().trim();

        if(!TextUtils.isEmpty(email)){

            final Dialog dialog = new Dialog(ActivitySignUp.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.passwordreset_dialog);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);
            dialog.setCancelable(false);

            TextView mdialogtxt = (TextView) dialog.findViewById(R.id.dialogtxt);
            ImageView mfblogo = (ImageView) dialog.findViewById(R.id.fblogo);
            mfblogo.setVisibility(GONE);
            ImageView memaillogo = (ImageView) dialog.findViewById(R.id.emaillogo);
            memaillogo.setVisibility(VISIBLE);
            ImageView mgooglelogo = (ImageView) dialog.findViewById(R.id.googlelogo);
            mgooglelogo.setVisibility(GONE);
            Button okbtn = (Button) dialog.findViewById(R.id.hireBtn);
            Button cancelbtn = (Button) dialog.findViewById(R.id.cancelBtn);
            cancelbtn.setVisibility(GONE);

            okbtn.setText("OK");
            okbtn.setTextColor(Color.parseColor("#0e52a5"));
            mdialogtxt.setText("An email has been sent to your registered email address to verify your account ");

            dialog.show();

            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();

                    onBackPressed();
                }
            });
        }*/
    }

    private void initView() {
        mRegisterBtn = findViewById(R.id.signup_go);
        mNameField = (EditText)findViewById(R.id.signup_name);
        mEmailField = (EditText)findViewById(R.id.signup_email);
        mPasswordField = (EditText)findViewById(R.id.signup_password);
    }
}