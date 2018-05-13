package com.example.spqrapid.churchinventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.spqrapid.churchinventory.data.ChurchContract.ChurchEntry;

/**
 * Created by SPQRapid on 12/10/2017.
 */

public class ChurchDbHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "inventory.db";
    public final static int DB_VERSION = 1;

    public ChurchDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ChurchEntry.SQL_CREATE_CHURCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertItem(ContentProvider item) {
        SQLiteDatabase db = this.getWritableDatabase();
        /**
         * Creating a ContentValues object where column names are the keys,
         * and Church attributes from the editor are the values.
         */
        ContentValues values = new ContentValues();
        values.put(ChurchEntry.COLUMN_NAME, item.getProductName());
        values.put(ChurchEntry.COLUMN_PRICE, item.getPrice());
        values.put(ChurchEntry.COLUMN_QUANTITY, item.getQuantity());
        values.put(ChurchEntry.COLUMN_SUPPLIER_NAME, item.getSupplierName());
        values.put(ChurchEntry.COLUMN_IMAGE, item.getImage());
        values.put(ChurchEntry.COLUMN_SUPPLIER_EMAIL, item.getSupplierEmail());

        // Insert the new Church with the given values
        long id = db.insert(ChurchEntry.TABLE_NAME, null, values);
    }

    public Cursor readStock() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ChurchEntry._ID,
                ChurchEntry.COLUMN_NAME,
                ChurchEntry.COLUMN_PRICE,
                ChurchEntry.COLUMN_QUANTITY,
                ChurchEntry.COLUMN_SUPPLIER_NAME,
                ChurchEntry.COLUMN_SUPPLIER_EMAIL,
                ChurchEntry.COLUMN_IMAGE,
        };
        Cursor cursor = db.query(
                ChurchEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor readItem(long itemId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ChurchEntry._ID,
                ChurchEntry.COLUMN_NAME,
                ChurchEntry.COLUMN_PRICE,
                ChurchEntry.COLUMN_QUANTITY,
                ChurchEntry.COLUMN_SUPPLIER_NAME,
                ChurchEntry.COLUMN_SUPPLIER_EMAIL,
                ChurchEntry.COLUMN_IMAGE
        };
        String selection = ChurchEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};

        Cursor cursor = db.query(
                ChurchEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    public void updateItem(long currentItemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChurchEntry.COLUMN_QUANTITY, quantity);
        String selection = ChurchEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(currentItemId)};
        db.update(ChurchEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

    public void sellOneItem(long itemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity - 1;
        }
        ContentValues values = new ContentValues();
        values.put(ChurchEntry.COLUMN_QUANTITY, newQuantity);
        String selection = ChurchEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};
        db.update(ChurchEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }
}
