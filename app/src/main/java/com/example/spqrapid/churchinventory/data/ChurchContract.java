package com.example.spqrapid.churchinventory.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SPQRapid on 12/10/2017.
 */

public class ChurchContract {

    /**
     * o prevent someone from accidentally instantiating the contract class,
     * give it an empty constructor.
     */
    public ChurchContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.example.spqrapid.churchinventory";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PETS = "pets";

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class ChurchEntry implements BaseColumns {


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);


        // Name of database table for church
        public static final String TABLE_NAME = "church_stock";

        /**
         * Unique ID number for the church table.
         * <p>
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the column.
         * <p>
         * Type: TEXT.
         */
        public static final String COLUMN_NAME = "name";

        /**
         * Price of the product.
         * <p>
         * Type: TEXT.
         */
        public static final String COLUMN_PRICE = "price";

        /**
         * Quantity of the product.
         * <p>
         * Type: TEXT.
         */
        public static final String COLUMN_QUANTITY = "quantity";

        /**
         * Name of the supplier ( the one that delivers the products).
         * <p>
         * Type: TEXT.
         */
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Email of the supplier ( the one that delivers the products).
         * <p>
         * Type: TEXT.
         */
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";

        /**
         * Image of the product.
         * <p>
         * Type: INTEGER.
         */
        public static final String COLUMN_IMAGE = "image";

        /**
         * Creating the Table.
         */
        public static final String SQL_CREATE_CHURCH_TABLE = "CREATE TABLE " +
                ChurchContract.ChurchEntry.TABLE_NAME + "(" +
                ChurchContract.ChurchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChurchContract.ChurchEntry.COLUMN_NAME + " TEXT NOT NULL," +
                ChurchContract.ChurchEntry.COLUMN_PRICE + " TEXT NOT NULL," +
                ChurchContract.ChurchEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
                ChurchContract.ChurchEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
                ChurchContract.ChurchEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL," +
                ChurchEntry.COLUMN_IMAGE + " TEXT NOT NULL" + ");";
    }
}
