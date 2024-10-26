
package com.learn.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.learn.constant.response_code;
import com.learn.model.store_exception;

public class db_util {

    private static Connection connection;

    static {

        try {

            Class.forName(db_config.DRIVER_NAME);
            
            connection = DriverManager.getConnection(db_config.CONNECTION_STRING, db_config.DB_USER_NAME,
            		db_config.DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    }// End of static block

    public static Connection getConnection() throws store_exception {

        if (connection == null) {
            throw new store_exception(response_code.DATABASE_CONNECTION_FAILURE);
        }

        return connection;
    }

}
