package com.learn.model;

import java.io.Serializable;

public class cart implements Serializable {

    private book book;
    private int quantity;

    public cart(book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public book getBook() {
        return book;
    }

    public void setBook(book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

