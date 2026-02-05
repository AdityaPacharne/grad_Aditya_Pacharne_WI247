import java.util.*;
import java.sql.*;

import util.DBHelper;

class OwnerHelper {

    Connection con;
    OwnerHelper() {
        con = DBHelper.getConnection();
    }

    void addOwner(String name) {
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

