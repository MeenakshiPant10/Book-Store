package com.learn.service;

import java.util.List;

import com.learn.model.book;
import com.learn.model.store_exception;

public interface book_service {

    public book getBookById(String bookId) throws store_exception;

    public List<book> getAllBooks() throws store_exception;

    public List<book> getBooksByCommaSeperatedBookIds(String commaSeperatedBookIds) throws store_exception;

    public String deleteBookById(String bookId) throws store_exception;

    public String addBook(book book) throws store_exception;

    public String updateBookQtyById(String bookId, int quantity) throws store_exception;
    
    public String updateBook(book book) throws store_exception;

}

