package com.example.spqrapid.churchinventory.data;

/**
 * Created by SPQRapid on 11/10/2017.
 */

public class ContentProvider {

    // String for the product name.
    private final String mProductName;

    // String for the price of the product.
    private final String mPrice;

    // INTEGER for the quantity of the product.
    private final int mQuantity;

    // String for the supplier ( the one that delivers the products).
    private final String mSupplierName;

    // String for the supplier email ( the one that delivers the products).
    private final String mSupplierEmail;

    // String for the image of the product.
    private final String mImage;

    /**
     * @param productName
     * @param price
     * @param quantity
     * @param supplierName
     * @param supplierEmail
     * @param image
     */
    public ContentProvider(String productName, String price, int quantity, String supplierName, String supplierEmail, String image) {
        this.mProductName = productName;
        this.mPrice = price;
        this.mQuantity = quantity;
        this.mSupplierName = supplierName;
        this.mSupplierEmail = supplierEmail;
        this.mImage = image;
    }

    // Returns the name of the product.
    public String getProductName() {
        return mProductName;
    }

    // Returns the price of the product.
    public String getPrice() {
        return mPrice;
    }

    // Returns the quantity of the product.
    public int getQuantity() {
        return mQuantity;
    }

    // Returns the supplier name of the product.
    public String getSupplierName() {
        return mSupplierName;
    }

    // Returns the supplier name of the product.
    public String getSupplierEmail() {
        return mSupplierEmail;
    }

    // Returns the image of the product.
    public String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "StockItem{" +
                "productName='" + mProductName + '\'' +
                ", price='" + mPrice + '\'' +
                ", quantity=" + mQuantity +
                ", supplierName='" + mSupplierName + '\'' +
                ", supplierEmail='" + mSupplierEmail + '\'' +
                '}';
    }
}
