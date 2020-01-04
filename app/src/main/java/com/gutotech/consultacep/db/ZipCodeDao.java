package com.gutotech.consultacep.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ZipCodeDao {
    @Insert
    long insert(ZipCodeEntity zipCodeEntity);

    @Delete
    void delete(ZipCodeEntity zipCodeEntity);

    @Query("SELECT * FROM zipcodes ORDER BY dateSearched DESC")
    List<ZipCodeEntity> getAll();

    @Update
    void updateDateSearched(ZipCodeEntity zipCodeEntity);
}
