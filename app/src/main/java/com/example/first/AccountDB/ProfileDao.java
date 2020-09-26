package com.example.first.AccountDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM myProfile WHERE id = :id")
    ProfileEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setById(ProfileEntity profileEntity);

    @Query("DELETE from myProfile WHERE id IN (:idList)")
    void deleteByIdList(List<String> idList);

    @Query("SELECT * FROM myImage WHERE id = :id")
    ImageEntity getImageById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setImageById(ImageEntity imageEntity);

    @Query("DELETE from myImage WHERE id IN (:idList)")
    void deleteImageByIdList(List<String> idList);
}
