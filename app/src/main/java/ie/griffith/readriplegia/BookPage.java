package ie.griffith.readriplegia;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class BookPage extends AppCompatActivity implements View.OnClickListener{
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    BottomAppBar bar;
    DisplayMetrics displayMetrics;
    WebView webView;
    TextView textView;
    ListView mList;
    FloatingActionButton speakButton;
    int page = 0;
    int pagination = 1;
    int bookmark = 0;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookpage);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_GRANTED);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        speakButton = (FloatingActionButton) findViewById(R.id.fab);
        speakButton.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.pagenumber);
        textView.setText("page " + pagination);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultFontSize(20);

        try {
            int temppage = preferences.getInt(getEpubTitle(), MODE_PRIVATE);
            bookmark = (temppage / 1926) + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bookmark != 0 && bookmark != 1){
            Toast.makeText(BookPage.this, "Bookmark detected on page " + bookmark, Toast.LENGTH_LONG).show();
        }

        Intent i = getIntent();
        String uriString = i.getStringExtra("OpenBook");
        Uri openBook = Uri.parse(uriString);

        // disable scroll on touch
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        try {
            openEpub(openBook);
        } catch (IOException e) {
            e.printStackTrace();
        }


        init();

        final Intent intent = getIntent();
        final String action = intent.getAction();

        voiceinputbuttons();

    }

    private void init(){
        this.bar = findViewById(R.id.bar);
        setSupportActionBar(bar);

    }

    private void createBookmark() throws IOException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.contains(getEpubTitle())) {
            editor.remove(getEpubTitle());
            editor.apply();
        }
        int temppagination = (page / 1926) + 1;
        bookmark = temppagination;
        editor.putInt(getEpubTitle(), page);
        editor.apply();
    }

    private void goToBookmark() throws IOException{
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        page = preferences.getInt(getEpubTitle(), MODE_PRIVATE);
        webView.scrollTo(0, page);
        pagination = bookmark;
        textView.setText("page " + pagination);
    }

    private void removeBookmark() throws IOException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        page = preferences.getInt(getEpubTitle(), MODE_PRIVATE);
        int temppagination = (page / 1926) + 1;
        bookmark = temppagination;
        editor.remove(getEpubTitle());
        editor.apply();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));

            if (matches.contains("help")) {
                startActivity(new Intent(this, Help.class));
            }

            if (matches.contains("turn page") || matches.contains("turn the page") || matches.contains("next page") || matches.contains("turn") || matches.contains("next") || matches.contains("go to next page")) {
                webView.scrollTo(0, scrollForward());
                textView.setText("page " + pagination);
                invalidateOptionsMenu();
            }

            if (matches.contains("go back") || matches.contains("previous page") || matches.contains("back") || matches.contains("turn back")) {
                if (page == 0){

                }
                else{
                    webView.scrollTo(0, scrollBack());
                    textView.setText("page " + pagination);
                    invalidateOptionsMenu();
                }
            }

            if (matches.contains("create bookmark") || matches.contains("add a bookmark") || matches.contains("add bookmark") || matches.contains("bookmark") || matches.contains("bookmark page") || matches.contains("mark page")){
                try {
                    createBookmark();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(BookPage.this, "Bookmarked Page " + pagination, Toast.LENGTH_LONG).show();
            }

            if (matches.contains("go to bookmark") || matches.contains("go to bookmarked page")) {
                if (bookmark == 0 || bookmark == 1) {
                    Toast.makeText(BookPage.this, "There is no bookmark", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        goToBookmark();
                        textView.setText("page " + pagination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (matches.contains("remove bookmark") || matches.contains("delete bookmark")){
                if (bookmark == 0){
                    Toast.makeText(BookPage.this, "There is no bookmark", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        removeBookmark();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(BookPage.this, "Removed bookmark on page " + bookmark, Toast.LENGTH_LONG).show();
                    bookmark = 0;
                }
            }

            if (matches.contains("sign out") || matches.contains("log out")){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.page_bbar, menu);

        MenuItem item = menu.findItem(R.id.itemEmpty);
        item.setEnabled(false);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (webView.getScrollY() == 0) {
            menu.findItem(R.id.m_back).setEnabled(false);
        }
        else{
            menu.findItem(R.id.m_back).setEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        webView = (WebView) findViewById(R.id.webview);
        switch (item.getItemId()) {
            case R.id.m_back:  {
                webView.scrollTo(0, scrollBack());
                textView.setText("page " + pagination);
                invalidateOptionsMenu();
                return true;
            }
            case R.id.m_forward: {
                webView.scrollTo(0, scrollForward());
                textView.setText("page " + pagination);
                invalidateOptionsMenu();
                return true;
            }
            case R.id.bookmark: {
                try {
                    createBookmark();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(BookPage.this, "Bookmarked Page " + pagination, Toast.LENGTH_LONG).show();
                invalidateOptionsMenu();
                return true;
            }
            case R.id.remove_bookmark: {
                if (bookmark == 1 || bookmark == 0){
                    Toast.makeText(BookPage.this, "There is no bookmark", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        removeBookmark();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(BookPage.this, "Removed bookmark on page " + bookmark, Toast.LENGTH_LONG).show();
                    bookmark = 0;
                }
                invalidateOptionsMenu();
                return true;
            }
            case R.id.gotobookmark: {
                if (bookmark == 1 || bookmark == 0) {
                    Toast.makeText(BookPage.this, "There is no bookmark", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        goToBookmark();
                        textView.setText("page " + pagination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                invalidateOptionsMenu();
                return true;
            }
            case R.id.m_logout:  {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            case R.id.m_help:  {
                startActivity(new Intent(this, Help.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);

    }

    private int scrollForward(){
        page = page + 1926;
        pagination = pagination + 1;
        return page;
    }

    private int scrollBack(){
        page = page - 1926;
        pagination = pagination - 1;
        return page;
    }

    private void openEpub(Uri uri) throws IOException{

        webView = (WebView) findViewById(R.id.webview);

        Book book=null;

        // find InputStream for book
        InputStream epubInputStream = getContentResolver()
                .openInputStream(uri);
        try {
            book = (new EpubReader()).readEpub(epubInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String baseUrl = Environment.getExternalStorageDirectory().getPath() + "/";
        String data=null;
        try {
            data = new String(book.getContents().get(1).getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        webView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null);

    }
    private String getEpubTitle() throws IOException{

        Intent i = getIntent();
        String uriString = i.getStringExtra("OpenBook");
        Uri openBook = Uri.parse(uriString);

        // find InputStream for book
        InputStream epubInputStream = getContentResolver()
                .openInputStream(openBook);

        // Load Book from inputStream
        Book book = (new EpubReader()).readEpub(epubInputStream);

        // Display book title
        return book.getTitle();
    }

    @Override
    public void onClick(View v) {
        startVoiceRecognitionActivity();

    }
}
