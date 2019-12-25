package com.example.iictbeta2.AccActivity;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.iictbeta2.R;

public class ForgotPassActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout getemail;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        toolbar = findViewById(R.id.forgottoolbar);
        getemail = findViewById(R.id.editTextEmail);
        button = findViewById(R.id.go);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getemail.getEditText().getText().toString().trim();


            }
        });
    }
}
