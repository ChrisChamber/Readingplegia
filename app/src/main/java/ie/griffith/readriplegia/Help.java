package ie.griffith.readriplegia;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Help extends AppCompatActivity implements View.OnClickListener{
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    ListView listView;
    TextView textView;
    String[] listItem;
    BottomAppBar bar;
    ListView mList;
    FloatingActionButton speakButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_screen);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_GRANTED);

        speakButton = (FloatingActionButton) findViewById(R.id.fab);
        speakButton.setOnClickListener(this);

        listView=(ListView)findViewById(R.id.commandlist);
        textView=(TextView)findViewById(R.id.listtext);
        listItem = getResources().getStringArray(R.array.array_commands);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);

        init();
        voiceinputbuttons();
    }

    private void init(){
        this.bar = findViewById(R.id.bar);
        setSupportActionBar(bar);

    }

    public void voiceinputbuttons() {
        speakButton = (FloatingActionButton) findViewById(R.id.fab);
        mList = (ListView) findViewById(R.id.list);
        mList.setVisibility(View.GONE);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say a command");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onClick(View v){
        startVoiceRecognitionActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));

            if (matches.contains("go back") || matches.contains("back")){
                finish();
            }

            if (matches.contains("sign out")){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_bbar, menu);

        MenuItem help = menu.findItem(R.id.m_help);
        help.setEnabled(false);
        MenuItem item = menu.findItem(R.id.itemEmpty);
        item.setEnabled(false);
        MenuItem item2 = menu.findItem(R.id.itemEmpty2);
        item2.setEnabled(false);
        MenuItem item3 = menu.findItem(R.id.itemEmpty3);
        item3.setEnabled(false);
        MenuItem item4 = menu.findItem(R.id.itemEmpty4);
        item4.setEnabled(false);
        MenuItem item5 = menu.findItem(R.id.itemEmpty5);
        item5.setEnabled(false);
        MenuItem item6 = menu.findItem(R.id.itemEmpty6);
        item6.setEnabled(false);

        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_logout:  {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);

    }
}
