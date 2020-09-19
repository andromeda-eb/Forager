package com.example.forager.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "book_table")
public class Book implements Serializable{
    @PrimaryKey
    @NonNull
    //@ColumnInfo(name = "title")
    private String title;

    private String authors;

    private String description;

    private String thumbnail;

    private String price;

    private String currencyCode;

    private String buyLink;

    public Book(){}

    public Book( @NonNull String [] bookInfo ){
        this.title = bookInfo[0];

        this.authors = bookInfo[1];

        this.description = bookInfo[2];

        this.thumbnail = bookInfo[3];

        this.price = bookInfo[4];

        this.currencyCode = bookInfo[5];

        this.buyLink = bookInfo[6];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }
}
