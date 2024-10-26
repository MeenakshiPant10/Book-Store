package com.learn.service_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.learn.constant.response_code;
import com.learn.constant.db.book_db;
import com.learn.model.book;
import com.learn.model.store_exception;
import com.learn.service.book_service;
import com.learn.util.db_util;

public class book_service_impl implements book_service {

    private static final String getAllBooksQuery = "SELECT * FROM " + book_db.TABLE_BOOK;
    private static final String getBookByIdQuery = "SELECT * FROM " + book_db.TABLE_BOOK
            + " WHERE " + book_db.COLUMN_BARCODE + " = ?";

    private static final String deleteBookByIdQuery = "DELETE FROM " + book_db.TABLE_BOOK + "  WHERE "
            + book_db.COLUMN_BARCODE + "=?";

    private static final String addBookQuery = "INSERT INTO " + book_db.TABLE_BOOK + "  VALUES(?,?,?,?,?)";

    private static final String updateBookQtyByIdQuery = "UPDATE " + book_db.TABLE_BOOK + " SET "
            + book_db.COLUMN_QUANTITY + "=? WHERE " + book_db.COLUMN_BARCODE
            + "=?";

    private static final String updateBookByIdQuery = "UPDATE " + book_db.TABLE_BOOK + " SET "
            + book_db.COLUMN_NAME + "=? , "
            + book_db.COLUMN_AUTHOR + "=?, "
            + book_db.COLUMN_PRICE + "=?, "
            + book_db.COLUMN_QUANTITY + "=? "
            + "  WHERE " + book_db.COLUMN_BARCODE
            + "=?";

    @Override
    public book getBookById(String bookId) throws store_exception {
        book book = null;
        Connection con = db_util.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(getBookByIdQuery);
            ps.setString(1, bookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                book = new book(bCode, bName, bAuthor, bPrice, bQty);
            }
        } catch (SQLException e) {

        }
        return book;
    }

    @Override
    public List<book> getAllBooks() throws store_exception{
        List<book> books = new ArrayList<book>();
        Connection con = db_util.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement(getAllBooksQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                book book = new book(bCode, bName, bAuthor, bPrice, bQty);
                books.add(book);
            }
        } catch (SQLException e) {

        }
        return books;
    }

    @Override
    public String deleteBookById(String bookId) throws store_exception {
        String response = response_code.FAILURE.name();
        Connection con = db_util.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(deleteBookByIdQuery);
            ps.setString(1, bookId);
            int k = ps.executeUpdate();
            if (k == 1) {
                response = response_code.SUCCESS.name();
            }
        } catch (Exception e) {
            response += " : " + e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public String addBook(book book) throws store_exception{
        String responseCode = response_code.FAILURE.name();
        Connection con = db_util.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(addBookQuery);
            ps.setString(1, book.getBarcode());
            ps.setString(2, book.getName());
            ps.setString(3, book.getAuthor());
            ps.setDouble(4, book.getPrice());
            ps.setInt(5, book.getQuantity());
            int k = ps.executeUpdate();
            if (k == 1) {
                responseCode = response_code.SUCCESS.name();
            }
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public String updateBookQtyById(String bookId, int quantity) throws store_exception {
        String responseCode = response_code.FAILURE.name();
        Connection con = db_util.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(updateBookQtyByIdQuery);
            ps.setInt(1, quantity);
            ps.setString(2, bookId);
            ps.executeUpdate();
            responseCode = response_code.SUCCESS.name();
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

    @Override
    public List<book> getBooksByCommaSeperatedBookIds(String commaSeperatedBookIds) throws store_exception {
        List<book> books = new ArrayList<book>();
        Connection con = db_util.getConnection();
        try {
            String getBooksByCommaSeperatedBookIdsQuery = "SELECT * FROM " + book_db.TABLE_BOOK
                    + " WHERE " +
                    book_db.COLUMN_BARCODE + " IN ( " + commaSeperatedBookIds + " )";
            PreparedStatement ps = con.prepareStatement(getBooksByCommaSeperatedBookIdsQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);

                book book = new book(bCode, bName, bAuthor, bPrice, bQty);
                books.add(book);
            }
        } catch (SQLException e) {

        }
        return books;
    }

    @Override
    public String updateBook(book book) throws store_exception {
        String responseCode = response_code.FAILURE.name();
        Connection con = db_util.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(updateBookByIdQuery);
            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.setDouble(3, book.getPrice());
            ps.setInt(4, book.getQuantity());
            ps.setString(5, book.getBarcode());
            ps.executeUpdate();
            responseCode = response_code.SUCCESS.name();
        } catch (Exception e) {
            responseCode += " : " + e.getMessage();
            e.printStackTrace();
        }
        return responseCode;
    }

}

