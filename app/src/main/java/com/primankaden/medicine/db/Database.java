package com.primankaden.medicine.db;

import android.arch.persistence.room.RoomDatabase;

import com.primankaden.medicine.model.Item;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

@android.arch.persistence.room.Database(entities = {Item.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract ItemDao itemDao();
}
