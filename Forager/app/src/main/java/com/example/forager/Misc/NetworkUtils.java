package com.example.forager.Misc;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/*
*  Opens an internet connection to query Google Books API
*  Returns JSON string
* */

public class NetworkUtils {
    // Base URL for Books API.
    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results.
    private static final String MAX_RESULTS = "maxResults";
    // Parameter to filter by print type.
    private static final String PRINT_TYPE = "printType";

    static String getBookInfo(String queryString) {
        HttpsURLConnection urlConnection= null;
        BufferedReader reader = null;
        String resultJSONString = null;

        try{
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            // Converting URI to URL
            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpsURLConnection) requestURL.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            // Get input stream
            InputStream inputStream = urlConnection.getInputStream();

            // Create authorsTitleView buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use StringBuilder to hold the incoming response
            StringBuilder builder = new StringBuilder();

            String line;

            while((line = reader.readLine()) != null)
                builder.append(line + "\n");

            if(builder.length() == 0)
                // Stream was empty so no point parsing
                return null;

            resultJSONString = builder.toString();

            Log.d("Activity", resultJSONString);

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if (urlConnection != null)
                urlConnection.disconnect();

            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        return resultJSONString;
    }
}
