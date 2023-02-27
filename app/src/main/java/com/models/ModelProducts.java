package com.models;

public class ModelProducts {

    String  Category , ProductImage , ProductLink, ProductName,ProductPrice , pId;


    public ModelProducts() {
    }

    public ModelProducts(String category, String productImage, String productLink, String productName, String productPrice, String pId) {
        Category = category;
        ProductImage = productImage;
        ProductLink = productLink;
        ProductName = productName;
        ProductPrice = productPrice;
        this.pId = pId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductLink() {
        return ProductLink;
    }

    public void setProductLink(String productLink) {
        ProductLink = productLink;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}


