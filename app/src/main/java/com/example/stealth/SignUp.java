package com.example.stealth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;

import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    TextView signin, termsCondition;
    CheckBox termsCheckbox;
    EditText edtEmail, edtPass;
    Button btnSignUp;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        signin = findViewById(R.id.txtSignIn);
        termsCondition = findViewById(R.id.txtTermsCondition);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent var = new Intent(SignUp.this, SignIn.class);
                startActivity(var);
                finish();
            }
        });
        signin.setPaintFlags(signin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        termsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent var = new Intent(SignUp.this, TermsCondition.class);
                startActivity(var);
            }
        });
        termsCheckbox = findViewById(R.id.terms_checkbox);
        btnSignUp = findViewById(R.id.btnSignUp);
        termsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnSignUp.setEnabled(isChecked);
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, confirmPass;
                email = edtEmail.getText().toString();
                password = edtPass.getText().toString();
                btnSignUp.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if(!validateCredentials(email, password)) {
                    btnSignUp.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "User registration successful!", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(SignUp.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(SignUp.this, "Email not sent", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    }

                                    btnSignUp.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                    LayoutInflater inflater = LayoutInflater.from(SignUp.this);
                                    View customView = inflater.inflate(R.layout.custom_alert_dialog, null);
                                    TextView titleTextView = customView.findViewById(R.id.dialog_title);
                                    TextView messageTextView = customView.findViewById(R.id.dialog_message);
                                    Button button = customView.findViewById(R.id.dialog_button);
                                    titleTextView.setText("Success");
                                    messageTextView.setText("Verification email has been sent. Please check your inbox.");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    builder.setView(customView);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.show();
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent var = new Intent(SignUp.this, SignIn.class);
                                            startActivity(var);
                                            finish();
                                        }
                                    });

                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        edtEmail.setError("This email address is already in use. ");
                                        btnSignUp.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });
    }
    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // Function to validate email and password
    private boolean validateCredentials(String email, String password) {
        boolean chk = true;
        if (!isValidEmail(email)) {
            edtEmail.setError("Email is Invalid.");
            chk = false;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is Required.");
            chk = false;
        }
        if (TextUtils.isEmpty(password)) {
            edtPass.setError("Password is Required.");
            chk = false;
        }
        if (password.length() < 6) {
            edtPass.setError("Password must have at least 6 characters.");
            chk = false;
        }
        return chk;
    }

}

