package com.example.first.mainScreen.database.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CredentialDao {

    @Query("SELECT * FROM credential WHERE id = :id")
    Credential getById(String id);

    @Query("SELECT * FROM credential")
    List<Credential> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void change(Credential credential);

    @Delete
    void delete(Credential credential);
}
