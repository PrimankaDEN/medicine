package com.primankaden.medicine;

import com.primankaden.medicine.model.Item;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

public interface Api {
    @GET("master/data.json")
    Observable<List<Item>> getData();
}
