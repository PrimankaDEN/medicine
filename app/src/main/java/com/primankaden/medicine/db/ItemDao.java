package com.primankaden.medicine.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.primankaden.medicine.model.Item;

import java.util.List;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

@Dao
public interface ItemDao {
    @Insert
    void insert(List<Item> item);

    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAll();

    @Query("SELECT * FROM item WHERE id = :id")
    LiveData<Item> get(int id);

    @Query("SELECT * FROM item WHERE titleLowReg LIKE :snippet OR descriptionLowReg LIKE :snippet")
    List<Item> search(String snippet);
}
