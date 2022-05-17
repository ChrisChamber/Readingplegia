package ie.griffith.readriplegia;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class OpenDownloadedBook extends AppCompatActivity implements View.OnClickListener{
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 100;

    BottomAppBar bar;
    TextView booktitle;
    ImageView bookcover;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_book);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_GRANTED);

        Intent i = getIntent();
        String uriString = i.getStringExtra("SelectedBook");
        Uri selectedBook = Uri.parse(uriString);

        booktitle = (TextView) findViewById(R.id.booktitle);
        bookcover = (ImageView) findViewById(R.id.bookcover);
        bookcover.setOnClickListener(this);

        init();

        final Intent intent = getIntent();
        final String action = intent.getAction();

        try{
            readEpubContent(selectedBook);
        } catch (IOException e){

        }

    }

    private void init(){
        this.bar = findViewById(R.id.bar);
        setSupportActionBar(bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_bbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, BookPage.class);
        switch (v.getId()) {
            case R.id.bookcover:
                intent.putExtra("OpenBook", getUri().toString());
                startActivity(intent);
                break;
        }
    }

    private Uri getUri(){
        Intent i = getIntent();
        String uriString = i.getStringExtra("SelectedBook");
        Uri selectedBook = Uri.parse(uriString);
        return selectedBook;
    }

    private void readEpubContent (Uri uri) throws IOException{

        booktitle = (TextView) findViewById(R.id.booktitle);
        bookcover = (ImageView) findViewById(R.id.bookcover);

        // find InputStream for book
        InputStream epubInputStream = getContentResolver()
                .openInputStream(uri);

        // Load Book from inputStream
        Book book = (new EpubReader()).readEpub(epubInputStream);

        // Log the book's authors
        Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());

        // Display book title
        booktitle.setText(book.getTitle());

        // Log the book's title
        Log.i("epublib", "title: " + book.getTitle());

        // Display Book's cover image
        bookcover.setImageBitmap(BitmapFactory.decodeStream(book.getCoverImage()
                .getInputStream()));
    }
}
