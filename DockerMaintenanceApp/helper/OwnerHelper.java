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
        System.out.printf("%-12s %-20s %-15s%n", "Owner ID", "Name", "Maintenance Due");
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

    public void viewOwnerInfo(int owner_id) {
        String query =  """
                        select
                            owner_id,
                            name,
                            maintenance
                        from owners
                        where owner_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, owner_id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                System.out.println("Owner ID:           " + rs.getInt("owner_id"));
                System.out.println("Owner Name:         " + rs.getString("name"));
                System.out.println("Maintenance Due:    Rs. " + rs.getInt("maintenance"));
            }
            else System.out.println("Owner not found.");
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
        String check =  """
                        select count(*)
                        from sites
                        where owner_id = ?
                        """;

        String query =  """
                        delete from owners
                        where owner_id = ?
                        """;
        try {
            PreparedStatement checkStmt = con.prepareStatement(check);
            checkStmt.setInt(1, owner_id);
            ResultSet rs = checkStmt.executeQuery();

            if(rs.next() && rs.getInt(1) > 0) {
                System.out.println("Cannot delete owner because they have sites assigned to them, \nPlease reassign their sites first.");
            }

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, owner_id);
            pstmt.executeUpdate();
            System.out.println("Owner Deleted");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public boolean ownerExists(int owner_id) {
        String query =  """
                        select count(*)
                        from owners
                        where owner_id = ?
                        """;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, owner_id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) return rs.getInt(1) > 0;
        }
        catch(Exception e) { System.out.println(e); }
        return false;
    }

    public void makePayment(int owner_id, int amount) {
        if(amount <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        String query =  """
                        update owners
                        set maintenance = maintenance - ?
                        where owner_id = ?
                        """;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, owner_id);
            pstmt.executeUpdate();
            System.out.println("Payment Done");
            viewOwnerInfo(owner_id);
        }
        catch(Exception e) { System.out.println(e); }
    }
}

