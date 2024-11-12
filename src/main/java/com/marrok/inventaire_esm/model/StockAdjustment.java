package com.marrok.inventaire_esm.model;

import java.util.Date;

public class StockAdjustment {
    private int id;
    private int articleId;
    private int userId;
    private Date adjustmentDate;
    private int quantity;
    private String adjustmentType;  // 'increase' or 'decrease'
    private String remarks;

    // Constructor
    public StockAdjustment(int id, int articleId, int userId, Date adjustmentDate, int quantity, String adjustmentType, String remarks) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.adjustmentDate = adjustmentDate;
        this.quantity = quantity;
        this.adjustmentType = adjustmentType;
        this.remarks = remarks;
    }

    // Default constructor
    public StockAdjustment() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(Date adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "StockAdjustment{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", userId=" + userId +
                ", adjustmentDate=" + adjustmentDate +
                ", quantity=" + quantity +
                ", adjustmentType='" + adjustmentType + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
