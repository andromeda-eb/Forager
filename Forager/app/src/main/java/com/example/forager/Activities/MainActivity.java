package com.example.forager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forager.Misc.CustomReceiver;
import com.example.forager.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private EditText userInput;
    public static final String SEARCH_KEY = "SEARCH_GOOGLE_API";
    private final int SEARCH_RESULTS_REQUEST = 1;
    private final int BOOK_LIST_REQUEST = 2;
    private final int SETTINGS_REQUEST = 3;
    public static final int SELECTED_BOOK_REQUEST= 4;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nav;
    public static CustomReceiver receiver = new CustomReceiver();
    private IntentFilter filter;
    private String intentGreeting = "You came from the Main Activity";
    public static String GREETING = "greeting";

    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.input_editText);

        dl  = findViewById(R.id.drawer_layout);

        t = new ActionBarDrawerToggle(this,dl,R.string.open,R.string.close);

        dl.addDrawerListener(t);

        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav = findViewById(R.id.nav);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            // make sure to put navigationView in layout as last element or won't work
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if( id == R.id.home )
                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                else if( id == R.id.saved_books )
                    launchSavedBooksPage();
                else if ( id == R.id.settings)
                    launchSettingsPage();

                return true;
            }
        });

        filter = new IntentFilter();

        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        // Main is parent activity to all so all activities will have access
        this.registerReceiver(receiver, filter);

        int nightMode = AppCompatDelegate.getDefaultNightMode();

        searchButton = findViewById(R.id.search_button);

        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES)
            searchButton.setBackgroundResource(R.drawable.ic_search_white);
        else
            searchButton.setBackgroundResource(R.drawable.ic_search_black);
    }

    @Override
    public void onDestroy(){
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      return t.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void launchSavedBooksPage(){
        Intent intent = new Intent(this,BookListActivity.class);

        intent.putExtra(GREETING, intentGreeting);

        startActivityForResult(intent, BOOK_LIST_REQUEST);
    }

    public void launchSettingsPage(){
        Intent intent = new Intent(this, SettingsActivity.class);

        intent.putExtra(GREETING, intentGreeting);

        startActivityForResult(intent, SETTINGS_REQUEST);
    }


    public void launchBookResultsActivity(View view) {

        Intent intent = new Intent(this, SearchResultsActivity.class);

        intent.putExtra(GREETING, intentGreeting);

        String queryString = userInput.getText().toString();
        // If input isn't empty
        if(!queryString.isEmpty()){
            intent.putExtra(SEARCH_KEY, queryString);

            startActivityForResult(intent, SEARCH_RESULTS_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        int [] requestCodesArr = {SEARCH_RESULTS_REQUEST, BOOK_LIST_REQUEST, SETTINGS_REQUEST};

        String [] replyKeys = {SearchResultsActivity.REPLY_KEY, BookListActivity.REPLY_KEY, SettingsActivity.SETTINGS_KEY};

        boolean check_result_code = resultCode == Activity.RESULT_OK;

        for(int i = 0; i < requestCodesArr.length; ++i){
            if(requestCodesArr[i] == requestCode && check_result_code){

                String reply = data.getStringExtra( replyKeys[i] );

                Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
