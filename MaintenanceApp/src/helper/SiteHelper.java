import java.util.*;
import java.sql.*;

import util.DBHelper;

class SiteHelper {

    int costOpen    = 6;
    int costNonOpen = 9;

    Connection con;
    public SiteHelper() { con = DBHelper.getConnection(); }

    int fetchMT(int site_id) {
        String query =  """
                        select maintenance
                        from sites
                        where site_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, site_id);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return -1;
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
            return rs.getString(1);
        }
        catch(Exception e) {
            System.out.println(e);
        }
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
        }
        catch(Exception e) {
            System.out.println(e);
        }
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
            PreparedStatement pstmt1 = con.prepareStatement(query1);
            PreparedStatement pstmt2 = con.prepareStatement(query2);
            PreparedStatement pstmt;

            String currentType = fetchSiteType(site_id);

            if(currentType.equals("OPEN") || new_site_type.equals("OPEN")) {
                pstmt1.setString(1, new_site_type);
                pstmt1.setInt(3, site_id);
                if      (currentType.equals("OPEN"))    pstmt1.setInt(2, costNonOpen);
                else if (new_site_type.equals("OPEN"))  pstmt1.setInt(2, costOpen);
                pstmt = pstmt1;
            }
            else {
                pstmt2.setString(1, new_site_type);
                pstmt2.setInt(2, site_id);
                pstmt = pstmt2;
            }
            pstmt.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}

