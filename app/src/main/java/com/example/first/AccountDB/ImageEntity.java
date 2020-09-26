package com.example.first.AccountDB;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "myImage")
public class ImageEntity {
    public static final String DEFAULT_NUMBER = "def";

    @PrimaryKey
    @NonNull
    public String id = DEFAULT_NUMBER;

    @TypeConverters({ConvertImage.class})
    public Bitmap image;

    public ImageEntity() {
    }

    public ImageEntity(@NotNull String id, Bitmap image) {
        this.id = id; // пока фотка одна - испульзую дефолтный id
        this.image = image;
    }
}
