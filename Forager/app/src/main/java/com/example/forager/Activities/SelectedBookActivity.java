package com.example.forager.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.forager.Database.Book;
import com.example.forager.Database.BookViewModel;
import com.example.forager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedBookActivity extends AppCompatActivity {
    public static final String REPLY_KEY = "BOOK_RESULTS_REPLY";
    private ImageView bookImage;
    private TextView titleTextView;
    private TextView authorsTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private Button buyLinkTextButton;
    private Book selectedBook;
    private BookViewModel bookViewModel;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager notifyMangager;
    private int nightMode;

    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_book);

        Toast.makeText(getApplicationContext(), getIntent().getStringExtra(MainActivity.GREETING), Toast.LENGTH_SHORT).show();

        bookImage = findViewById(R.id.bookImageDetail);

        titleTextView = findViewById(R.id.titleDetail);

        authorsTextView = findViewById(R.id.authorsDetail);

        priceTextView = findViewById(R.id.priceDetail);

        descriptionTextView = findViewById(R.id.descriptionDetail);

        buyLinkTextButton = findViewById(R.id.buyLinkDetail);

        bookViewModel =  ViewModelProviders.of(this).get(BookViewModel.class);

        selectedBook = (Book) getIntent().getSerializableExtra("selectedBook");

        Glide.with(SelectedBookActivity.this).load(selectedBook.getThumbnail()).into(bookImage);

        titleTextView.setText(selectedBook.getTitle());

        authorsTextView.setText(selectedBook.getAuthors());

        priceTextView.setText(selectedBook.getPrice() + " " +  selectedBook.getCurrencyCode());

        descriptionTextView.setText(selectedBook.getDescription());

        if(getString(R.string.buyLinkStatusError).equals(selectedBook.getBuyLink()))
            buyLinkTextButton.setVisibility(View.INVISIBLE);

        //buyLinkTextView.setText(selectedBook.getBuyLink());

        createNotificationChannel();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adding " + titleTextView.getText() + " to database", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                bookViewModel.insert(selectedBook);
                sendNotification();
            }
        });

        nightMode = AppCompatDelegate.getDefaultNightMode();

        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            titleTextView.setTextColor(Color.WHITE);
            authorsTextView.setTextColor(Color.WHITE);
            priceTextView.setTextColor(Color.WHITE);
            descriptionTextView.setTextColor(Color.WHITE);
            buyLinkTextButton.setTextColor(Color.WHITE);
        }

        Intent reply_intent = new Intent();

        reply_intent.putExtra(REPLY_KEY, getString(R.string.book_result_reply));

        setResult(RESULT_OK, reply_intent);
    }

    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        notifyMangager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    public void createNotificationChannel(){
        notifyMangager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // check to see if API version is >= 26
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID,
                    "Forager Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.enableLights(true);

            notificationChannel.setLightColor(Color.RED);

            notificationChannel.enableVibration(true);

            notificationChannel.setDescription("Notification from Forager");

            notifyMangager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, BookListActivity.class);
        //execute this at some point in the future
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this,
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Notification from Forager")
                .setContentText(titleTextView.getText() + " has been added to the database")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);

        return notifyBuilder;
    }

    public void viewOnPlayStore(View view) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);

        playStoreIntent.setData(Uri.parse(selectedBook.getBuyLink()));

        startActivity(playStoreIntent);
    }
}
