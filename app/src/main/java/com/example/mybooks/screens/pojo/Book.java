package com.example.mybooks.screens.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mybooks.screens.converters.ConverterBookVolumeInfoString;
import com.google.gson.annotations.*;
import com.squareup.picasso.Target;

import java.io.File;

@Entity(tableName = "book")
@TypeConverters(value = ConverterBookVolumeInfoString.class)

public class Book {

    public Book(String kind, String id, String etag, String selfLink, BookVolumeInfo volumeInfo) {
        this.kind = kind;
        this.id = id;
        this.etag = etag;
        this.selfLink = selfLink;
        this.volumeInfo = volumeInfo;
    }


    @PrimaryKey(autoGenerate = true)
    private int localId;
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("selfLink")
    @Expose
    private String selfLink;
    @SerializedName("volumeInfo")
    @Expose
    private BookVolumeInfo volumeInfo;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private File file;

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    private String imageFilePath;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public BookVolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(BookVolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }
}
