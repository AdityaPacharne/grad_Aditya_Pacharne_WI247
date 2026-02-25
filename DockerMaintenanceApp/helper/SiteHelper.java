package helper;

import java.util.*;
import java.sql.*;

import util.DBHelper;

public class SiteHelper {

    int costOpen    = 6;
    int costNonOpen = 9;

    Connection con;
    public SiteHelper() { con = DBHelper.getConnection(); }

    public void viewAllSites() {
        String query = """
                        select 
                            s.site_id,
                            s.length,
                            s.breadth,
                            s.persqft,
                            s.site_type,
                            s.owner_id,
                            o.name as owner_name
                        from sites s
                        left join owners o on s.owner_id = o.owner_id
                        order by s.site_id
                        """;
        
        System.out.printf("%-8s %-8s %-8s %-8s %-12s %-10s %-15s%n", 
                         "Site ID", "Length", "Breadth", "Rs/SqFt", "Type", "Owner ID", "Owner Name");
        System.out.println("-------------------------------------------------------------------------------");
        
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()) {
                System.out.printf(
                    "%-8d %-8d %-8d %-8d %-12s %-10s %-15s%n",
                    rs.getInt("site_id"),
                    rs.getInt("length"),
                    rs.getInt("breadth"),
                    rs.getInt("persqft"),
                    rs.getString("site_type"),
                    (rs.getObject("owner_id") != null ? rs.getInt("owner_id") : "N/A"),
                    (rs.getString("owner_name") != null ? rs.getString("owner_name") : "Unassigned")
                );
            }
            System.out.println("-------------------------------------------------------------------------------");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void viewSitesByOwner(int owner_id) {
        String query =  """
                        select
                            s.site_id,
                            s.length,
                            s.breadth,
                            s.persqft,
                            s.site_type,
                            (s.length * s.breadth) as area,
                            (s.length * s.breadth * s.persqft) as monthly_charge
                        from sites s
                        where s.owner_id = ?
                        order by s.site_id
                        """;
        
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, owner_id);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.printf("%-8s %-8s %-8s %-10s %-12s %-12s %-15s%n", 
                             "Site ID", "Length", "Breadth", "Area(sqft)", "Rs/SqFt", "Type", "Monthly Charge");
            System.out.println("-----------------------------------------------------------------------------------");
            
            boolean hasData = false;
            int totalCharge = 0;
            
            while (rs.next()) {
                hasData = true;
                int monthlyCharge = rs.getInt("monthly_charge");
                totalCharge += monthlyCharge;
                
                System.out.printf(
                    "%-8d %-8d %-8d %-10d %-12d %-12s Rs. %-12d%n",
                    rs.getInt("site_id"),
                    rs.getInt("length"),
                    rs.getInt("breadth"),
                    rs.getInt("area"),
                    rs.getInt("persqft"),
                    rs.getString("site_type"),
                    monthlyCharge
                );
            }
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.printf("Total Monthly Charge: Rs. %d%n", totalCharge);
            System.out.println("-----------------------------------------------------------------------------------");

        }
        catch(Exception e) { System.out.println(e); }
    }

    public boolean verifySiteOwner(int site_id, int owner_id) {
        String query =  """
                        select count(*) 
                        from sites 
                        where site_id = ? and owner_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, site_id);
            pstmt.setInt(2, owner_id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) return rs.getInt(1) > 0;
        }
        catch(Exception e) { System.out.println(e); }
        return false;
    }

    String fetchSiteType(int site_id) {
        String query =  """
                        select site_type
                        from sites
                        where site_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, site_id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) return rs.getString(1);
        }
        catch(Exception e) { System.out.println(e); }
        return "";
    }

    void ownerUpdate(int site_id, int new_owner_id) {
        String query =  """
                        update sites
                        set owner_id = ?
                        where site_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, new_owner_id);
            pstmt.setInt(2, site_id);
            pstmt.executeUpdate();
            System.out.println("Site Owner Updated");
        }
        catch(Exception e) { System.out.println(e); }
    }

    void typeUpdate(int site_id, String new_site_type) {
        String query1 = """
                        update sites
                        set site_type = ?,
                        persqft = ?
                        where site_id = ?
                        """;

        String query2 = """
                        update sites
                        set site_type = ?
                        where site_id = ?
                        """;
        try {
            String currentType = fetchSiteType(site_id);
            
            if(currentType.equals("OPEN") || new_site_type.equals("OPEN")) {
                PreparedStatement pstmt = con.prepareStatement(query1);
                pstmt.setString(1, new_site_type);
                pstmt.setInt(3, site_id);
                
                if(currentType.equals("OPEN"))  pstmt.setInt(2, costNonOpen);
                else                            pstmt.setInt(2, costOpen);
                pstmt.executeUpdate();
            }
            else {
                PreparedStatement pstmt = con.prepareStatement(query2);
                pstmt.setString(1, new_site_type);
                pstmt.setInt(2, site_id);
                pstmt.executeUpdate();
            }
            System.out.println("Site Type Updated");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void chargeMT() {
        String query =  """
                        update owners o
                        set maintenance = coalesce(o.maintenance, 0) + (
                            select coalesce(sum(s.length * s.breadth * s.persqft), 0)
                        from sites s
                        where s.owner_id = o.owner_id)
                        """;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            System.out.println("Maintenace charged to all owners");
        }
        catch(Exception e) { System.out.println(e); }
    }
}

