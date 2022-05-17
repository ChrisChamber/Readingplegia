package ie.griffith.readriplegia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GenreBookDownload extends AppCompatActivity implements View.OnClickListener{

    private TextView a,b,c,d,e,f,g,h,i,j;
    private String genre;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre_book_download);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        a = (TextView) findViewById(R.id.a);
        a.setOnClickListener(this);
        b = (TextView) findViewById(R.id.b);
        b.setOnClickListener(this);
        c = (TextView) findViewById(R.id.c);
        c.setOnClickListener(this);
        d = (TextView) findViewById(R.id.d);
        d.setOnClickListener(this);
        e = (TextView) findViewById(R.id.e);
        e.setOnClickListener(this);
        f = (TextView) findViewById(R.id.f);
        f.setOnClickListener(this);
        g = (TextView) findViewById(R.id.g);
        g.setOnClickListener(this);
        h = (TextView) findViewById(R.id.h);
        h.setOnClickListener(this);
        i = (TextView) findViewById(R.id.i);
        i.setOnClickListener(this);
        j = (TextView) findViewById(R.id.j);
        j.setOnClickListener(this);

        a.setVisibility(View.GONE);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        d.setVisibility(View.GONE);
        e.setVisibility(View.GONE);
        f.setVisibility(View.GONE);
        g.setVisibility(View.GONE);
        h.setVisibility(View.GONE);
        i.setVisibility(View.GONE);
        j.setVisibility(View.GONE);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){

                }
                else{
                    Toast.makeText(GenreBookDownload.this, "User is signed out", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GenreBookDownload.this, MainActivity.class));
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showGenres(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, BookStore.class);
        switch (v.getId()){
            case R.id.a:
                genre = "history";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.b:
                genre = "sci-fi";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.c:
                genre = "fantasy";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.d:
                genre = "mystery";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.e:
                genre = "cooking";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.f:
                genre = "romance";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.g:
                genre = "thriller";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.h:
                genre = "finance";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.i:
                genre = "biography";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;

            case R.id.j:
                genre = "art";
                intent.putExtra("GENRE", genre);
                startActivity(intent);
                break;
        }
    }

    private void showGenres(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            uInfo.setA(ds.child(userID).getValue(UserInformation.class).isA());
            uInfo.setB(ds.child(userID).getValue(UserInformation.class).isB());
            uInfo.setC(ds.child(userID).getValue(UserInformation.class).isC());
            uInfo.setD(ds.child(userID).getValue(UserInformation.class).isD());
            uInfo.setE(ds.child(userID).getValue(UserInformation.class).isE());
            uInfo.setF(ds.child(userID).getValue(UserInformation.class).isF());
            uInfo.setG(ds.child(userID).getValue(UserInformation.class).isG());
            uInfo.setH(ds.child(userID).getValue(UserInformation.class).isH());
            uInfo.setI(ds.child(userID).getValue(UserInformation.class).isI());
            uInfo.setJ(ds.child(userID).getValue(UserInformation.class).isJ());

           if (uInfo.isA() == true){
               a.setVisibility(View.VISIBLE);
           }
           else{
               a.setVisibility(View.GONE);
           }

            if (uInfo.isB() == true){
                b.setVisibility(View.VISIBLE);
            }
            else{
                b.setVisibility(View.GONE);
            }

            if (uInfo.isC() == true){
                c.setVisibility(View.VISIBLE);
            }
            else{
                c.setVisibility(View.GONE);
            }

            if (uInfo.isD() == true){
                d.setVisibility(View.VISIBLE);
            }
            else{
                d.setVisibility(View.GONE);
            }

            if (uInfo.isE() == true){
                e.setVisibility(View.VISIBLE);
            }
            else{
                e.setVisibility(View.GONE);
            }

            if (uInfo.isF() == true){
                f.setVisibility(View.VISIBLE);
            }
            else{
                f.setVisibility(View.GONE);
            }

            if (uInfo.isG() == true){
                g.setVisibility(View.VISIBLE);
            }
            else{
                g.setVisibility(View.GONE);
            }

            if (uInfo.isH() == true){
                h.setVisibility(View.VISIBLE);
            }
            else{
                h.setVisibility(View.GONE);
            }

            if (uInfo.isI() == true){
                i.setVisibility(View.VISIBLE);
            }
            else{
                i.setVisibility(View.GONE);
            }

            if (uInfo.isJ() == true){
                j.setVisibility(View.VISIBLE);
            }
            else{
                j.setVisibility(View.GONE);
            }
        }
    }
}
