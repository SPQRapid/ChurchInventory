package com.example.spqrapid.churchinventory;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spqrapid.churchinventory.data.ChurchContract.ChurchEntry;

/**
 * Created by SPQRapid on 12/10/2017.
 */

public class ChurchCursorAdapter extends CursorAdapter {

    private final MainActivity activity;

    public ChurchCursorAdapter(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml .
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * @param view    Existing view, returned earlier by newView() method.
     * @param context app context.
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list_item layout.
        TextView nameTextView = (TextView) view.findViewById(R.id.productName);
        String name = cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_NAME));
        nameTextView.setText(name);

        // Find individual views that we want to modify in the list_item layout.
        TextView quantityTextView = (TextView) view.findViewById(R.id.productQuantity);
        final int quantity = cursor.getInt(cursor.getColumnIndex(ChurchEntry.COLUMN_QUANTITY));
        quantityTextView.setText(String.valueOf(quantity));

        // Read the pet attributes from the Cursor for the current pet.\
        String price = cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_PRICE));

        // Find individual views that we want to modify in the list_item layout.
        TextView priceTextView = (TextView) view.findViewById(R.id.productPrice);
        priceTextView.setText(price);

        // Find individual views that we want to modify in the list_item layout.
        ImageView image = (ImageView) view.findViewById(R.id.productImage);
        image.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_IMAGE))));

        final long id = cursor.getLong(cursor.getColumnIndex(ChurchEntry._ID));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnViewItem(id);
            }
        });

        ImageView sale = (ImageView) view.findViewById(R.id.productSale);

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnSale(id,
                        quantity);
            }
        });
    }

}
