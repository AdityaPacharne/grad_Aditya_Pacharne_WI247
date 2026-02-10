package helper;

import java.util.*;
import java.sql.*;

import util.DBHelper;

public class OwnerHelper {

    Connection con;
    public OwnerHelper() { con = DBHelper.getConnection(); }

    public void viewOwners() {
        String query =  """
                        select
                            owner_id,
                            name,
                            maintenance
                        from owners
                        order by owner_id
                        """;
        System.out.printf("%-10s %-10s %-12s%n", "owner_id", "name", "maintenance");
        System.out.println("-------------------------------------");

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.printf(
                        "%-10d %-10s %-12d%n",
                        rs.getInt("owner_id"),
                        rs.getString("name"),
                        rs.getInt("maintenance")
                        );
            }
            System.out.println("-------------------------------------");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void addOwner(String name) {
        String query =  """
                        insert into
                        owners (name, maintenance)
                        values ( ?, 0)
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Owner Added");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void deleteOwner(int owner_id) {
        String query =  """
                        delete from owners
                        where owner_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, owner_id);
            pstmt.executeUpdate();
            System.out.println("Owner Deleted");
        }
        catch(Exception e) { System.out.println(e); }
    }
}

