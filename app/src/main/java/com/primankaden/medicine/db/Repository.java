package com.primankaden.medicine.db;

import android.arch.lifecycle.LiveData;

import com.primankaden.medicine.App;
import com.primankaden.medicine.model.Item;

import java.util.List;
import java.util.Random;

import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

public class Repository {
    public static LiveData<List<Item>> getAll() {
        App.getApi().getData().subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(handler);
        return App.getDB().itemDao().getAll();
    }

    private final static Observer<List<Item>> handler = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Item> items) {
            Random r = new Random();
            for (Item item : items) {
                item.setId(r.nextInt());
                item.setTitleLowReg(item.getTitle().toLowerCase());
                item.setDescriptionLowReg(item.getDescriptionLowReg().toLowerCase());
            }
            App.getDB().itemDao().insert(items);
        }
    };
}
