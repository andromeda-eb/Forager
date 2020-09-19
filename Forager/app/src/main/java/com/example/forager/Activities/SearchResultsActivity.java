package com.example.forager.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forager.Misc.BooksAdapter;
import com.example.forager.Database.Book;
import com.example.forager.Misc.BookLoader;
import com.example.forager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    public static final String REPLY_KEY = "BOOK_RESULTS_REPLY";
    public static final int SEARCH_RESULT_REQUEST = 4;
    private  List<Book> bookList;
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();

        String queryString = intent.getStringExtra(MainActivity.SEARCH_KEY);

        Toast.makeText(getApplicationContext(), intent.getStringExtra(MainActivity.GREETING), Toast.LENGTH_SHORT).show();

        bookList = new ArrayList<>();

        progress = findViewById(R.id.searchResultsProgressBar);

        searchBooks(queryString);

        initRecyclerView();

        Intent reply_intent = new Intent();

        reply_intent.putExtra(REPLY_KEY, getString(R.string.book_results_reply));

        setResult(RESULT_OK, reply_intent);

    }

    public void searchBooks(String queryString){
        Bundle queryBundle = new Bundle();

        queryBundle.putString("query_string", queryString);

        getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    // Called when instantiating the loader
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null)
            queryString = args.getString("query_string");

        return new BookLoader(this, queryString);
    }

    // Called when loader's task finishes. Add code to update UI with results here
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        progress.setVisibility(View.INVISIBLE);

        try{
            // Convert response string to JSON object
            JSONObject results = new JSONObject(data);
            // Get the JSONArray of book items
            JSONArray itemsArray = results.getJSONArray("items");

            // Initialize iterator and results fields
            int i = 0;

            /*
                Stores title, authors, description, thumbnail and price,
                currency code, and buyLink for each book
            */
            String [] currentBook = new String[7];

            JSONObject book;

            JSONObject volumeInfo;

            JSONObject saleInfo;

            // Get the properties of the book and store in bookInfo arr
            while(i < itemsArray.length()){
                // Empty the book info arr for current book
                // bookInfo = emptyBookInfoArr(bookInfo);

                book = itemsArray.getJSONObject(i);
                // Get volumeInfo object
                volumeInfo = book.getJSONObject( "volumeInfo" );

                // Get the saleInfo object
                saleInfo = book.getJSONObject("saleInfo");

                currentBook = getBookFromJSON(currentBook, volumeInfo, saleInfo);

                bookList.add( new Book(currentBook) );

                // Move on to next book
                ++i;
            }

            adapter.notifyDataSetChanged();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Cleans up any remaining resources
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public String[] getBookFromJSON(String [] currentBook, JSONObject volumeInfo, JSONObject saleInfo) throws JSONException {
        currentBook [0] = volumeInfo.getString("title");

        if( volumeInfo.has("authors") ) {
            currentBook [1] = volumeInfo.getString("authors");
            // remove brackets and quotation
            currentBook [1] = currentBook [1].substring(2, currentBook [1].length()-2);

            currentBook [1] = currentBook [1].replace("\"", "");
        }
        else
            currentBook [1] = "No authors available";

        if( volumeInfo.has("description") )
            currentBook [2] = volumeInfo.getString("description");
        else
            currentBook [2] = "No description available";

        if( volumeInfo.has("imageLinks")) {
            currentBook[3] = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
            // replace with https protocol to load into imageView
            currentBook[3] = currentBook[3].replace("http", "https");

            currentBook[3] = currentBook[3].replace("zoom=1","zoom=0");
        }
        else
            currentBook [3] = "";

        // If the book can be bought get the data
        if(saleInfo.has("retailPrice")) {
            currentBook [4] = saleInfo.getJSONObject("retailPrice").getString("amount");

            currentBook [5] = saleInfo.getJSONObject("retailPrice").getString("currencyCode");

            currentBook [6] = saleInfo.getString("buyLink");
        }else{
            currentBook [4] = "No price available";

            currentBook [5] = "";

            currentBook [6] = getString(R.string.buyLinkStatusError);
        }

        return currentBook ;
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BooksAdapter(this, bookList);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        boolean check_result_code = resultCode == Activity.RESULT_OK;

        if(MainActivity.SELECTED_BOOK_REQUEST == requestCode && check_result_code){

            String reply = data.getStringExtra(  SelectedBookActivity.REPLY_KEY );

            Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
        }

    }
}
