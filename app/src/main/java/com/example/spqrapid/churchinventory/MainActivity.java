package com.example.spqrapid.churchinventory;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.spqrapid.churchinventory.data.ChurchDbHelper;
import com.example.spqrapid.churchinventory.data.ContentProvider;

public class MainActivity extends AppCompatActivity {

    ChurchDbHelper dbHelper;
    ChurchCursorAdapter mAdapter;
    int lastVisibleItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new ChurchDbHelper(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChurchActivity.class);
                startActivity(intent);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.relative_main);
        listView.setEmptyView(emptyView);

        Cursor cursor = dbHelper.readStock();

        mAdapter = new ChurchCursorAdapter(this, cursor);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) return;
                final int currentFirstVisibleItem = view.getFirstVisiblePosition();
                if (currentFirstVisibleItem > lastVisibleItem) {
                    fab.show();
                } else if (currentFirstVisibleItem < lastVisibleItem) {
                    fab.hide();
                }
                lastVisibleItem = currentFirstVisibleItem;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapCursor(dbHelper.readStock());
    }

    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, ChurchActivity.class);
        intent.putExtra("itemId", id);
        startActivity(intent);
    }

    public void clickOnSale(long id, int quantity) {
        dbHelper.sellOneItem(id, quantity);
        mAdapter.swapCursor(dbHelper.readStock());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_default:
                // add dummy data for testing
                addDummyData();
                mAdapter.swapCursor(dbHelper.readStock());
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDummyData() {
        //  Creating dummy data, for bible.
        ContentProvider bible = new ContentProvider(
                "Holy Bible",
                "29",
                17,
                "Bible network",
                "bibleonline@yahoo.com",
                "android.resource://com.example.spqrapid.churchinventory/drawable/holy_bible");
        dbHelper.insertItem(bible);

        //  Creating dummy data, for churchWallpaper.
        ContentProvider churchWallpaper = new ContentProvider(
                "Church",
                "36",
                3,
                "Bible network",
                "bibleonline@yahoo.com",
                "android.resource://com.example.spqrapid.churchinventory/drawable/notre_dames");
        dbHelper.insertItem(churchWallpaper);

        //  Creating dummy data, for crossWallpaper.
        ContentProvider crossWallpaper = new ContentProvider(
                "Cross",
                "10",
                3,
                "Bible network",
                "bibleonline@yahoo.com",
                "android.resource://com.example.spqrapid.churchinventory/drawable/cross_desktop");
        dbHelper.insertItem(crossWallpaper);

        //  Creating dummy data, for oldBible.
        ContentProvider oldBible = new ContentProvider(
                "Old Bible",
                "10",
                10,
                "Bible network",
                "bibleonline@yahoo.com",
                "android.resource://com.example.spqrapid.churchinventory/drawable/cross_bible_background");
        dbHelper.insertItem(oldBible);
    }
}

