package com.example.stealth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    EditText edtEmail;
    Button btnSubmit;
    ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtEmail = findViewById(R.id.edtEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                if(!validateCredentials(email))
                {
                    progressBar.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    LayoutInflater inflater = LayoutInflater.from(ForgotPassword.this);
                                    View customView = inflater.inflate(R.layout.custom_alert_dialog, null);
                                    TextView titleTextView = customView.findViewById(R.id.dialog_title);
                                    TextView messageTextView = customView.findViewById(R.id.dialog_message);
                                    Button button = customView.findViewById(R.id.dialog_button);
                                    titleTextView.setText("Success");
                                    messageTextView.setText("An email with instructions to reset your password has been sent to " + email + ".");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                                    builder.setView(customView);
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.setCanceledOnTouchOutside(false);

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent var = new Intent(ForgotPassword.this, SignIn.class);
                                            startActivity(var);
                                            finish();
                                        }
                                    });
                                    alertDialog.show();
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
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
    private boolean validateCredentials(String email) {
        boolean chk = true;
        if (!isValidEmail(email)) {
            edtEmail.setError("Email is Invalid.");
            chk = false;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is Required.");
            chk = false;
        }
        return chk;
    }
}