package com.peter.springboot.store.entity;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    //    @ManyToOne(fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
//    @JoinColumn(name = "category_id")
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity_in_stock")
    private int quantityInStock;

    @Column(name = "import_price")
    private double importPrice;

    @Column(name = "export_price")
    private double exportPrice;

    @Column(name = "status")
    private boolean status;

    public Product() {
    }

    public Product(int id, int categoryId, String productName, int quantityInStock, double importPrice, double exportPrice, boolean status) {
        this.id = id;
        this.categoryId = categoryId;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.status = status;
    }

    public Product(int id, int categoryId, String productName, int quantityInStock, double importPrice, double exportPrice) {
        this.id = id;
        this.categoryId = categoryId;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
    }

    public Product(int categoryId, String productName, int quantityInStock, double importPrice, double exportPrice) {
        this.categoryId = categoryId;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
    }

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public double getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(double exportPrice) {
        this.exportPrice = exportPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", productName='" + productName + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", importPrice=" + importPrice +
                ", exportPrice=" + exportPrice +
                ", status=" + status +
                '}';
    }
}
