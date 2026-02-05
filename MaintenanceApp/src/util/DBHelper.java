package util;

import java.sql.*;

public class DBHelper {
    private static final String URL     = "jdbc:postgresql://localhost:5432/test";
    private static final String USER    = "postgres";
    private static final String PASS    = "root";
    private static Connection con;

    public static Connection getConnection() {
        try { con = DriverManager.getConnection(URL, USER, PASS); }
        catch(Exception e) { System.out.println(e); }
        return con;
    }

    public static void closeConnection() {
        try { con.close(); }
        catch(Exception e) { System.out.println(e); }
    }
}

