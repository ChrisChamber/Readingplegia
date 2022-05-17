package ie.griffith.readriplegia;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static java.net.Proxy.Type.HTTP;

public class BookStore extends AppCompatActivity implements View.OnClickListener{
    private Button button, button2, button3, filepicker;
    private static final int OPEN_REQUEST_CODE = 41;
    BottomAppBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_store);

        Intent intent = getIntent();
        String genre = intent.getStringExtra("GENRE");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Genre " + genre + " is available in the following book stores, please select one to be brought to the store page:");

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        filepicker = (Button) findViewById(R.id.filepicker);
        filepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openFile(v);
            }
        });

        init();

    }

    private void init(){
        this.bar = findViewById(R.id.bar);
        setSupportActionBar(bar);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_bbar, menu);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                loadGenre(1);
                break;

            case R.id.button2:
                loadGenre(2);
                break;

            case R.id.button3:
                loadGenre(3);
                break;
        }
    }

    private void loadGenre(int n){
        Intent intent = getIntent();
        String genre = intent.getStringExtra("GENRE");
        String url;
        if (n == 1){
            if (genre.equals("history")){
                url = "https://www.ebooks.com/en-ie/subjects/history/4/?SubjectId=4&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDwaa2w-OmN75qLzy5s-oXk2hA0oqEzSBErwL9o4lrUHLLHJoCp9eU90SKHf3pc3AeUIAmOxHNjN3XWZrbJVrRbTYdA2YmqsbU8cS0137s93YkMAwABejDs7fxQW7Indb1E&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("sci-fi")){
                url = "https://www.ebooks.com/en-ie/subjects/science-fiction/122/?SubjectId=122&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDx3qkDFehxpj94zqNCN5CZU4nEOREiKW-IYHAC1PPq9A4MtOMS6Jm6JGfU5q4IxD9Xao_EFuhE2b9cVA6kvUitW8zZsvwuB4A3GwuFUYf2bkewDRXiAeMPDTijQPW3kIrs&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("fantasy")){
                url = "https://www.ebooks.com/en-ie/subjects/fantasy/329/?SubjectId=329&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDwZQWAbuwqflu3TPl0x_LULtyWm8B543IkYSIKv3RqUK5Fgzc20nAOEmxYd08TbYUthro5amMzXMBfDUiByJPS3cfG1He3zTZQq4IzoaFszr7fzQQyWRVjoXXYUIBv-RO4&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("mystery")){
                url = "https://www.ebooks.com/en-ie/subjects/fiction-mystery-ebooks/14544/?SubjectId=14544&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDzKqlVdzYHfEy6d-Af4GfNzfr9IIKrwf8u9Zz_4_lVVKIi04TcyJ14JnK-a0LQiHD5qJNpkttA-8n90_COi6G4Et-l2CsfmT_fA3RRw4EjmXa3UKRRxT2h0J3e69YX3KDc&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("cooking")){
                url = "https://www.ebooks.com/en-ie/subjects/health-fitness-nutrition-ebooks/737/?SubjectId=737&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDxMb-sgBj1Mc3KPkJModnqtDuKo5hnUYDsd5sqdvsi13ecXKeIifRVpWBjmUrzOtaD8iJfgpx0Xg4eAbCLv4YLuMED1Z3OEFTtZUeuLqtWDLkKEfH42dL1RNK8-vK2RIjc&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("romance")){
                url = "https://www.ebooks.com/en-ie/subjects/romance/440/?SubjectId=440&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDxZ4Cs_qnYLp02oiXiXVF9WzwIpso-aeFMGZXrUYPwi-a3-jRW_X260RSU473MRuiF2IkoXddvZ3XdE4SgJyRIVVf35rhct_L6Ypi2Ryq8Fl3YYwD1yNkplqYfaQqssH7M&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("thriller")){
                url = "https://www.ebooks.com/en-ie/subjects/romance-suspense-mystery-thriller-ebooks/821/?SubjectId=821&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDyuWqxmXK_6tUAhaGKWYHk5M6rtlOHJISqhnrhxGtlJejp03Z5inPK3GOf9oane_Z1CiXGh_OLBixWcRYNs0SGJSkPrHFhNp9hhhcfKZr8AlscWv-BnOsXtL5tQC8cLI9U&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("finance")){
                url = "https://www.ebooks.com/en-ie/subjects/business-finance-ebooks/33/?SubjectId=33&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDxmmwxy9BJdpeXO6SEjMT0eyswCq4RvDBRmRd9Dm9iKAz80YWzXu2ehsU8h_iBNFK8ub-n3l29k4JbrLpjA2YYr3JkVYwtOCujmKTy1dRXieMjhLfoQ-Dtu00LNpEPgc0g&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("biography")){
                url = "https://www.ebooks.com/en-ie/subjects/biography-autobiography/82/?SubjectId=82&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDyUfaI3iYnctv6OO9NJVs_8r0SqbJyFKMif-Zme2CPPELoyaZPEik0u7cYSXvHySBLgjOeiagEMeo9-bQb-V62OzfDi_deD4NOeJQb2b4vzxZUVP5GhHDrI-q81FM8fmaQ&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("art")){
                url = "https://www.ebooks.com/en-ie/subjects/art/317/?SubjectId=317&Filter.Term=&Filter.DateMadeActive=All&Filter.Language=0&Filter.FormatType=Epub&Filter.DrmFree=true&__RequestVerificationToken=CfDJ8OlkAWWhCpJDrCc3t02umDwCPIh69RX6rCArD_HTdEMWZ0BiVo76LHop16Ysfj1xGmslQ7xpSfvg4bjLFl0alstDAG8Y88NPXvCKLoXoCsmBPkBJx_K_zwyls953HQygZxoi04yxDACp_-HjehzyuOE&Filter.DrmFree=false";
                openBrowser(BookStore.this, url);
            }
        }
        if (n == 2){
            if (genre.equals("history")){
                url = "https://www.smashwords.com/shelves/home/84/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("sci-fi")){
                url = "https://www.smashwords.com/shelves/home/1213/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("fantasy")){
                url = "https://www.smashwords.com/shelves/home/1206/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("mystery")){
                url = "https://www.smashwords.com/shelves/home/879/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("cooking")){
                url = "https://www.smashwords.com/shelves/home/89/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("romance")){
                url = "https://www.smashwords.com/shelves/home/1235/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("thriller")){
                url = "https://www.smashwords.com/shelves/home/874/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("finance")){
                url = "https://www.smashwords.com/shelves/home/93/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("biography")){
                url = "https://www.smashwords.com/shelves/home/94/any/any";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("art")){
                url = "https://www.smashwords.com/shelves/home/95/any/any";
                openBrowser(BookStore.this, url);
            }
        }
        if(n == 3){
            if (genre.equals("history")){
                url = "https://www.gutenberg.org/ebooks/subject/2091";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("sci-fi")){
                url = "https://www.gutenberg.org/ebooks/subject/105";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("fantasy")){
                url = "https://www.gutenberg.org/ebooks/subject/295";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("mystery")){
                url = "https://www.gutenberg.org/ebooks/subject/7508";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("cooking")){
                url = "https://www.gutenberg.org/ebooks/subject/14827";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("romance")){
                url = "https://www.gutenberg.org/ebooks/subject/18234";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("thriller")){
                url = "https://www.gutenberg.org/ebooks/subject/1065";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("finance")){
                url = "https://www.gutenberg.org/ebooks/search/?query=finance&submit_search=Go%21&sort_order=downloads";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("biography")){
                url = "https://www.gutenberg.org/ebooks/search/?query=biography&submit_search=Go%21";
                openBrowser(BookStore.this, url);
            }
            if (genre.equals("art")){
                url = "https://www.gutenberg.org/ebooks/subject/848";
                openBrowser(BookStore.this, url);
            }
        }
    }

    public static void openBrowser(final Context context, String url) {

        Uri webpage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        context.startActivity(webIntent);
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/epub+zip");
        startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = new Intent(this, OpenDownloadedBook.class);

        Uri currentUri = null;

        if (resultCode == RESULT_OK){
            if (requestCode == OPEN_REQUEST_CODE){
                if(SDK_INT >= Build.VERSION_CODES.R){
                        if (data != null){
                            currentUri = data.getData();
                            //displayToast(s);
                            intent.putExtra("SelectedBook", currentUri.toString());
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(BookStore.this, "Access to storage has been denied.", Toast.LENGTH_LONG).show();
                        }
                }
            }

        }
    }

    private void displayToast(String s) {
        //show Toast with file path
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }
}
