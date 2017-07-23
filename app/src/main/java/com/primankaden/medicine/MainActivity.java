package com.primankaden.medicine;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.primankaden.medicine.db.Repository;
import com.primankaden.medicine.model.Item;
import com.primankaden.medicine.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner, Observer<List<Item>> {
    public static final java.lang.String SEARCH_TERM = "searchTerm";
    private static final int SEARCH_LOADER_ID = 1022;
    protected EditText searchView;
    @BindView(R.id.list)
    protected RecyclerView listView;
    @BindView(R.id.empty)
    protected View emptyView;
    private ListAdapter adapter;
    private boolean isSearchState = false;
    private ItemViewModel viewModel;
    @Nullable
    private List<SearchResult> searchResultList;
    @Nullable
    private List<Item> itemList;


    /**
     * @see android.arch.lifecycle.LifecycleFragment
     */
    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    /**
     * @see android.arch.lifecycle.LifecycleFragment
     */
    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        adapter = new ListAdapter();
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
        refresh();
        viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        viewModel.init(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (EditText) searchItem.getActionView().findViewById(R.id.search_view);
        View clearBtn = searchItem.getActionView().findViewById(R.id.clear_button);
        MenuItemCompat.setOnActionExpandListener(searchItem, searchExpandListener);
        searchView.addTextChangedListener(searchTextWatcher);
        searchView.setHint(R.string.search_hint);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private MenuItemCompat.OnActionExpandListener searchExpandListener = new MenuItemCompat.OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            searchView.requestFocus();
            Utils.displayKeyboard(searchView);
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            isSearchState = false;
            refresh();
            Utils.hideKeyboard(searchView);
            return true;
        }
    };

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() < 2) {
                getLoaderManager().destroyLoader(SEARCH_LOADER_ID);
                isSearchState = false;
                refresh();
            } else {
                Bundle b = new Bundle();
                b.putString(SEARCH_TERM, s.toString());
                getSupportLoaderManager().restartLoader(SEARCH_LOADER_ID, b, searchLoaderCallback).forceLoad();
                isSearchState = true;
                refresh();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void onClick(Item item) {

    }

    @Override
    public void onChanged(@Nullable List<Item> items) {
        itemList = items;
        refresh();
    }

    public static class ItemViewModel extends ViewModel {
        public void init(LifecycleOwner owner, Observer<List<Item>> observer) {
            LiveData<List<Item>> data = Repository.getAll();
            data.observe(owner, observer);
        }
    }

    public void refresh() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private final LoaderManager.LoaderCallbacks<List<SearchResult>> searchLoaderCallback = new LoaderManager.LoaderCallbacks<List<SearchResult>>() {
        private String searchTerm;

        @Override
        public Loader<List<SearchResult>> onCreateLoader(int id, Bundle args) {
            searchTerm = args.getString(SEARCH_TERM);
            return new AsyncTaskLoader<List<SearchResult>>(MainActivity.this) {
                @Override
                public List<SearchResult> loadInBackground() {
                    List<SearchResult> searchResultList = new ArrayList<>();
                    List<Item> items = App.getDB().itemDao().search(String.format("%%%s%%", searchTerm).toLowerCase());
                    if (items == null) {
                        return new ArrayList<>();
                    } else {
                        for (Item q : items) {
                            searchResultList.add(new SearchResult(q, searchTerm));
                        }
                        return searchResultList;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<SearchResult>> loader, List<SearchResult> data) {
            searchResultList = data;
            refresh();
        }

        @Override
        public void onLoaderReset(Loader<List<SearchResult>> loader) {
            searchResultList = null;
            refresh();
        }
    };

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int CATEGORY_TYPE = 1, SEARCH_TYPE = 2;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == SEARCH_TYPE) {
                return new SimpleViewHolder(MainActivity.this, parent);
            } else {
                return new SearchViewHolder(MainActivity.this, parent);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == SEARCH_TYPE) {
                ((SearchViewHolder) holder).fill(searchResultList.get(position));
            } else {
                ((SimpleViewHolder) holder).fill(itemList.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return isSearchState ? SEARCH_TYPE : CATEGORY_TYPE;
        }

        @Override
        public int getItemCount() {
            return isSearchState ?
                    searchResultList == null ? 0 : searchResultList.size() :
                    itemList == null ? 0 : itemList.size();
        }
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        protected TextView title;
        private Item item;

        public SimpleViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_simple_result, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void fill(Item item) {
            title.setText(item.getTitle());
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            MainActivity.this.onClick(item);
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        protected TextView title;
        @BindView(R.id.description)
        protected TextView description;
        private Item item;

        public SearchViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.view_search_result, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void fill(SearchResult item) {
            title.setText(item.getSpannedTitle());
            description.setText(item.getSpannedDescription());
            this.item = item.getQuestion();
        }

        @Override
        public void onClick(View v) {
            MainActivity.this.onClick(item);
        }
    }
}
