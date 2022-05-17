package ie.griffith.readriplegia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgottenPassword;
    private Button login;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgottenPassword = (TextView) findViewById(R.id.forgottenpassword);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.editTextTextPassword);

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // When focus is lost check that the text field has valid values.
                if (!hasFocus) {
                    String email = editTextEmail.getText().toString().trim();
                    if(email.isEmpty()){
                        login.setEnabled(false);
                        editTextEmail.setError("An email is needed to login.");
                    }

                    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        login.setEnabled(false);
                        editTextEmail.setError("Please enter a valid email address.");
                    }
                    else{
                        login.setEnabled(true);
                    }

                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // When focus is lost check that the text field has valid values.
                if (!hasFocus) {
                    String password = editTextPassword.getText().toString().trim();
                    if(password.isEmpty()){
                        login.setEnabled(false);
                        editTextPassword.setError("A password is needed to login.");
                    }

                    else{
                        login.setEnabled(true);
                    }

                }
            }
        });

        forgottenPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password:");
                passwordResetDialog.setMessage("Enter your Email to receive a reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess (Void aVoid){
                            Toast.makeText(MainActivity.this, "Reset Link Sent to your Email.", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener(){
                            @Override
                            public void onFailure(@NonNull Exception e){
                                Toast.makeText(MainActivity.this, "Error, the reset link cannot be sent" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                    }
                });
                AlertDialog alert = passwordResetDialog.create();
                alert.setOnShowListener(arg0 ->{
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.button_color));
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.button_color));
                });
                alert.show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, Registration.class));
                break;

            case R.id.login:
                login();
                break;
        }
    }

    private void login(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            login.setEnabled(false);
            editTextEmail.setError("An email is needed to login.");
            editTextEmail.requestFocus();
        }

        if(password.isEmpty()){
            login.setEnabled(false);
            editTextPassword.setError("A password is needed to login.");
            editTextPassword.requestFocus();
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> Task){
                if (Task.isSuccessful()) {
                    //go to book genre screen
                    Toast.makeText(MainActivity.this, "User has been successfully logged in", Toast.LENGTH_LONG).show();
                    SystemClock.sleep(2000);
                    startActivity(new Intent(MainActivity.this, GenreBookDownload.class));
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login user, please re-check your credentials", Toast.LENGTH_LONG).show();
                    SystemClock.sleep(2000);

                }
            }
        });
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
