package com.primankaden.medicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.primankaden.medicine.model.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PrimankaDEN on 23.07.2017.
 */

public class ItemActivity extends AppCompatActivity {
    private static final String ITEM_EXTRA = "itemExtra";
    @BindView(R.id.webView)
    protected WebView webView;

    public static void startMe(Context from, Item item) {
        Intent i = new Intent(from, ItemActivity.class);
        i.putExtra(ITEM_EXTRA, item);
        from.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
    }
}
