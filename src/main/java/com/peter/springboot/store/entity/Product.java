package com.peter.springboot.store.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product",
            cascade = CascadeType.REFRESH)
    List<OrderDetail> orderDetails;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
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
        status = true;
    }

    public Product(int id, Category c, String productName, int quantityInStock, double importPrice, double exportPrice, boolean status) {
        this.id = id;
        this.category = c;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
        this.status = status;
    }

    public Product(int id, Category c, String productName, int quantityInStock, double importPrice, double exportPrice) {
        this.id = id;
        this.category = c;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
    }

    public Product(Category c, String productName, int quantityInStock, double importPrice, double exportPrice) {
        this.category = c;
        this.productName = productName;
        this.quantityInStock = quantityInStock;
        this.importPrice = importPrice;
        this.exportPrice = exportPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", importPrice=" + importPrice +
                ", exportPrice=" + exportPrice +
                ", status=" + status +
                '}';
    }
}
