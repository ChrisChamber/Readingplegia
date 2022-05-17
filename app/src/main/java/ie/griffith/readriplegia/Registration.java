package ie.griffith.readriplegia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextTextEmailAddress, editTextTextPassword, editTextTextPassword2;
    private Button register;
    private ScrollView scrollView;
    private CheckBox checkBoxA,checkBoxB,checkBoxC,checkBoxD,checkBoxE,checkBoxF,checkBoxG,checkBoxH
            ,checkBoxI,checkBoxJ;

    private FirebaseAuth mAuth;

    public Registration() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.register);
                register.setOnClickListener(this);
        register.setEnabled(false);

        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        editTextTextPassword2 = (EditText) findViewById(R.id.editTextTextPassword2);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        checkBoxA = (CheckBox) findViewById(R.id.a);
        checkBoxB = (CheckBox) findViewById(R.id.b);
        checkBoxC = (CheckBox) findViewById(R.id.c);
        checkBoxD = (CheckBox) findViewById(R.id.d);
        checkBoxE = (CheckBox) findViewById(R.id.e);
        checkBoxF = (CheckBox) findViewById(R.id.f);
        checkBoxG = (CheckBox) findViewById(R.id.g);
        checkBoxH = (CheckBox) findViewById(R.id.h);
        checkBoxI = (CheckBox) findViewById(R.id.i);
        checkBoxJ = (CheckBox) findViewById(R.id.j);

        editTextTextEmailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // When focus is lost check that the text field has valid values.
                if (!hasFocus) {
                            String email = editTextTextEmailAddress.getText().toString().trim();
                            if(email.isEmpty()){
                                register.setEnabled(false);
                            }

                            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                register.setEnabled(false);
                                editTextTextEmailAddress.setError("Please provide a valid email address");
                                //editTextTextEmailAddress.requestFocus();
                            }

                            else{
                                register.setEnabled(true);
                            }

                }
            }
        });

        editTextTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // When focus is lost check that the text field has valid values.
                if (!hasFocus) {
                    String password = editTextTextPassword.getText().toString().trim();
                    if(password.isEmpty()){
                        register.setEnabled(false);
                    }

                    else if(!isValidPassword(editTextTextPassword.getText().toString().trim())){
                        register.setEnabled(false);
                        editTextTextPassword.setError("Password needs at least one upper case and lowercase letter and contain digits: 0-9 and special characters: @#$%^&+=");
                    }

                    else if (password.length() < 8){
                        register.setEnabled(false);
                        editTextTextPassword.setError("Please enter a password length of more than 8 characters");
                    }

                    else{
                        register.setEnabled(true);
                    }
                }
            }
        });

        editTextTextPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // When focus is lost check that the text field has valid values.
                if (!hasFocus) {
                    String password2 = editTextTextPassword2.getText().toString().trim();
                    String password = editTextTextPassword.getText().toString().trim();

                    if(password2.isEmpty()){
                        register.setEnabled(false);
                    }

                    else if(!password2.equals(password)){
                        register.setEnabled(false);
                        editTextTextPassword2.setError("This password does not match the password above");
                    }

                    else{
                        register.setEnabled(true);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Registration();
                break;
        }
    }

    private void Registration(){
        String email = editTextTextEmailAddress.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();
        String password2 = editTextTextPassword2.getText().toString().trim();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        boolean a,b,c,d,e,f,g,h,i,j;

        if(checkBoxA.isChecked()){
            a = true;
        }
        else{
            a = false;
        }

        if(checkBoxB.isChecked()){
            b = true;
        }
        else{
            b = false;
        }

        if(checkBoxC.isChecked()){
            c = true;
        }
        else{
            c = false;
        }

        if(checkBoxD.isChecked()){
            d = true;
        }
        else{
            d = false;
        }

        if(checkBoxE.isChecked()){
            e = true;
        }
        else{
            e = false;
        }

        if(checkBoxF.isChecked()){
            f = true;
        }
        else{
            f = false;
        }

        if(checkBoxG.isChecked()){
            g = true;
        }
        else{
            g = false;
        }

        if(checkBoxH.isChecked()){
            h = true;
        }
        else{
            h = false;
        }

        if(checkBoxI.isChecked()){
            i = true;
        }
        else{
            i = false;
        }

        if(checkBoxJ.isChecked()){
            j = true;
        }
        else{
            j = false;
        }

        if(email.isEmpty()){
            editTextTextEmailAddress.setError("An email is needed to login.");
            editTextTextEmailAddress.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextTextEmailAddress.setError("Please provide a valid email address");
            editTextTextEmailAddress.requestFocus();
            return;
        }

        else if (checkBoxA.isChecked() == false && checkBoxB.isChecked() == false && checkBoxC.isChecked() == false &&
                checkBoxD.isChecked() == false && checkBoxE.isChecked() == false && checkBoxF.isChecked() == false &&
                checkBoxG.isChecked() == false && checkBoxH.isChecked() == false && checkBoxI.isChecked() == false && checkBoxJ.isChecked() == false){
            Toast.makeText(Registration.this, "Please select one or more genres from the list", Toast.LENGTH_LONG).show();
            scrollView.requestFocus();
            return;
        }

        else if(password.isEmpty()){
            editTextTextPassword.setError("A password is needed to login.");
            editTextTextPassword.requestFocus();
            return;
        }

        else if(password2.isEmpty()){
            editTextTextPassword2.setError("A password is needed to login.");
            editTextTextPassword2.requestFocus();
            return;
        }

        else if(!password2.equals(password)){
            editTextTextPassword2.setError("This password does not match the password above");
            editTextTextPassword2.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> Task){
                if (Task.isSuccessful()) {
                    User user = new User(email,a,b,c,d,e,f,g,h,i,j);

                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (Task.isSuccessful()) {
                                Toast.makeText(Registration.this, "User has been registered successfully.", Toast.LENGTH_LONG).show();
                                SystemClock.sleep(3000);
                                startActivity(new Intent(Registration.this, MainActivity.class));
                            } else {
                                Toast.makeText(Registration.this, "Failed to register user, please try again.", Toast.LENGTH_LONG).show();
                                SystemClock.sleep(3000);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Registration.this, "Failed to register user.", Toast.LENGTH_LONG).show();
                    SystemClock.sleep(3000);
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
