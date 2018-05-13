package com.example.spqrapid.churchinventory;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.spqrapid.churchinventory.data.ChurchDbHelper;
import com.example.spqrapid.churchinventory.data.ContentProvider;

import com.example.spqrapid.churchinventory.data.ChurchContract.ChurchEntry;


/**
 * Created by SPQRapid on 12/10/2017.
 */

public class ChurchActivity extends AppCompatActivity {

    // Identifier for the Church data loader.
    private static final int CHURCH_LOADER = 1;
    // Identifier for the church image data loader.
    private static final int CHURCH_IMAGE_REQUEST = 0;
    // Identifier for the ChurchDbHelper class.
    private ChurchDbHelper mDbHelper;
    // Content URI for the existing product.
    Uri mCurrentChurchUri;
    // Boolean flag that keeps track of whether the product has been edited (true) or not (false).
    Boolean mProductHasChanged = false;
    // Assigning a global long variable to the product.
    long mCurrentItemId;

    // Global EditText variable for the churchProductName (name of the product).
    EditText mChurchProductName;
    // Global EditText variable for the churchProductPrice (price of the product).
    EditText mChurchProductPrice;
    // Global EditText variable for the churchSupplierName (name of the supplier, distributor).
    EditText mChurchSupplierName;
    // Global EditText variable for the churchSupplierEmail (email of the supplier, distributor).
    EditText mChurchSupplierEmail;
    // Global EditText variable for the churchProductQuantity.
    EditText mChurchProductQuantity;
    // Global Button variable for the churchProductDecrease.
    Button mChurchProductDecrease;
    // Global Button variable for the churchProductIncrease.
    Button mChurchProductIncrease;
    // Global Button variable for the churchInsertImage.
    Button mChurchInsertImage;
    // Global Button variable for the churchProductImage.
    ImageView mChurchProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from.
        mChurchProductName = (EditText) findViewById(R.id.product_name);
        mChurchProductPrice = (EditText) findViewById(R.id.product_price);
        mChurchSupplierName = (EditText) findViewById(R.id.product_supplier);
        mChurchSupplierEmail = (EditText) findViewById(R.id.supplier_email);
        mChurchProductQuantity = (EditText) findViewById(R.id.product_quantity);
        mChurchProductDecrease = (Button) findViewById(R.id.product_minus);
        mChurchProductIncrease = (Button) findViewById(R.id.product_plus);
        mChurchInsertImage = (Button) findViewById(R.id.insert_image);
        mChurchProductImage = (ImageView) findViewById(R.id.product_image);

        mDbHelper = new ChurchDbHelper(this);
        mCurrentItemId = getIntent().getLongExtra("itemId", 0);


        if (mCurrentItemId == 0) {
            setTitle(getString(R.string.add_new_product));
        } else {
            setTitle(getString(R.string.edit_selected_product));
            addValuesToEditItem(mCurrentItemId);
        }

        // Creating a click listener for the mChurchProductDecrease button ( minus ).
        mChurchProductDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractOneToQuantity();
                mProductHasChanged = true;
            }
        });

        // Creating a click listener for the mChurchProductIncrease button ( plus ).
        mChurchProductIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumOneToQuantity();
                mProductHasChanged = true;
            }
        });

        // Creating a click listener for the mChurchInsertImage button ( inserting image ).
        mChurchInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToOpenImageSelector();
                mProductHasChanged = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        /**
         * Create a click listener to handle the user confirming that
         * changes should be discarded.
         */
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show a dialog that notifies the user they have unsaved changes.
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void subtractOneToQuantity() {
        String previousValueString = mChurchProductQuantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            mChurchProductQuantity.setText(String.valueOf(previousValue - 1));
        }
    }

    private void sumOneToQuantity() {
        String previousValueString = mChurchProductQuantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        mChurchProductQuantity.setText(String.valueOf(previousValue + 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemId == 0) {
            MenuItem deleteOneItemMenuItem = menu.findItem(R.id.product_delete);
            MenuItem orderMenuItem = menu.findItem(R.id.product_order);
            deleteOneItemMenuItem.setVisible(false);
            orderMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_save:
                // save item in DB
                if (!addItemToDb()) {
                    // saying to onOptionsItemSelected that user clicked button
                    return true;
                }
                finish();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(ChurchActivity.this);
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            case R.id.product_order:
                // dialog with phone and email
                showOrderConfirmationDialog();
                return true;
            case R.id.product_delete:
                // delete one item
                showDeleteConfirmationDialog(mCurrentItemId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean addItemToDb() {
        boolean isAllOk = true;
        if (!checkIfValueSet(mChurchProductName, "name")) {
            isAllOk = false;
        }
        if (!checkIfValueSet(mChurchProductPrice, "price")) {
            isAllOk = false;
        }
        if (!checkIfValueSet(mChurchProductQuantity, "quantity")) {
            isAllOk = false;
        }
        if (!checkIfValueSet(mChurchSupplierName, "supplier name")) {
            isAllOk = false;
        }
        if (!checkIfValueSet(mChurchSupplierEmail, "supplier email")) {
            isAllOk = false;
        }
        if (mCurrentChurchUri == null && mCurrentItemId == 0) {
            isAllOk = false;
            mChurchInsertImage.setError("Missing image");
        }
        if (!isAllOk) {
            return false;
        }

        if (mCurrentItemId == 0) {
            ContentProvider item = new ContentProvider(
                    mChurchProductName.getText().toString().trim(),
                    mChurchProductPrice.getText().toString().trim(),
                    Integer.parseInt(mChurchProductQuantity.getText().toString().trim()),
                    mChurchSupplierName.getText().toString().trim(),
                    mChurchSupplierEmail.getText().toString().trim(),
                    mCurrentChurchUri.toString());
            mDbHelper.insertItem(item);
        } else {
            int quantity = Integer.parseInt(mChurchProductQuantity.getText().toString().trim());
            mDbHelper.updateItem(mCurrentItemId, quantity);
        }
        return true;
    }

    private boolean checkIfValueSet(EditText text, String description) {
        if (TextUtils.isEmpty(text.getText())) {
            text.setError("Missing product " + description);
            return false;
        } else {
            text.setError(null);
            return true;
        }
    }

    private void addValuesToEditItem(long itemId) {
        Cursor cursor = mDbHelper.readItem(itemId);
        cursor.moveToFirst();
        mChurchProductName.setText(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_NAME)));
        mChurchProductPrice.setText(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_PRICE)));
        mChurchProductQuantity.setText(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_QUANTITY)));
        mChurchSupplierName.setText(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_SUPPLIER_NAME)));
        mChurchSupplierEmail.setText(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_SUPPLIER_EMAIL)));
        mChurchProductImage.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ChurchEntry.COLUMN_IMAGE))));

        mChurchProductName.setEnabled(false);
        mChurchProductPrice.setEnabled(false);
        mChurchSupplierName.setEnabled(false);
        mChurchInsertImage.setEnabled(false);
    }

    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.order_message);

        builder.setNegativeButton(R.string.email, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // intent to email
                Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + mChurchSupplierEmail.getText().toString().trim()));
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Recurrent new order");
                String bodyMessage = "Please send us as soon as possible more " +
                        mChurchProductName.getText().toString().trim() +
                        "!!!";
                intent.putExtra(android.content.Intent.EXTRA_TEXT, bodyMessage);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int deleteAllRowsFromTable() {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return database.delete(ChurchEntry.TABLE_NAME, null, null);
    }

    private int deleteOneItemFromTable(long itemId) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        String selection = ChurchEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};
        int rowsDeleted = database.delete(
                ChurchEntry.TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;
    }

    private void showDeleteConfirmationDialog(final long itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_product);
        builder.setPositiveButton(R.string.delete_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (itemId == 0) {
                    deleteAllRowsFromTable();
                } else {
                    deleteOneItemFromTable(itemId);
                }
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CHURCH_LOADER);

        }
        openImageSelector();
    }

    private void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHURCH_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CHURCH_LOADER: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageSelector();
                    // permission was granted
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == CHURCH_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                mCurrentChurchUri = resultData.getData();
                mChurchProductImage.setImageURI(mCurrentChurchUri);
                mChurchProductImage.invalidate();
            }
        }
    }
}
