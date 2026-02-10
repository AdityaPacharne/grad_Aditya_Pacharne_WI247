package helper;

import java.util.*;
import java.sql.*;

import util.DBHelper;

public class OwnerHelper {

    Connection con;
    public OwnerHelper() { con = DBHelper.getConnection(); }

    public void addOwner(String name) {
        String query =  """
                        insert into
                        owners (name)
                        values ( ? )
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}

