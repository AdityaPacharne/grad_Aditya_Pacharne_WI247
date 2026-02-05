package util;

import java.sql.*;

class DBHelper {
    private static Connection con;

    static Connection getConnection() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/test",
                    "postgres",
                    "root");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }
}

