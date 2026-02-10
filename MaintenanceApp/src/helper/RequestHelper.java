package helper;

import java.util.*;
import java.sql.*;

import util.DBHelper;

public class RequestHelper {

    Connection con;
    public RequestHelper() { con = DBHelper.getConnection(); }

    SiteHelper  sh = new SiteHelper();
    OwnerHelper oh = new OwnerHelper();

    public void displayAllRequests() {
        String query =  """
                        select
                            r.request_id,
                            r.type,
                            r.site_id,
                            s.site_type as current_type,
                            o.name as owner_name
                        from requests r
                        join sites s on r.site_id = s.site_id
                        join owners o on s.owner_id = o.owner_id
                        order by r.request_id
                        """;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.printf("%-12s %-15s %-10s %-15s %-15s%n", "Request ID", "Type", "Site ID", "Current Type", "Owner");
            System.out.println("-----------------------------------------------------------------------");
            
            while(rs.next()) {
                System.out.printf(
                    "%-12d %-15s %-10d %-15s %-15s%n",
                    rs.getInt("request_id"),
                    rs.getString("type"),
                    rs.getInt("site_id"),
                    rs.getString("current_type"),
                    rs.getString("owner_name")
                );
            }
            System.out.println("-----------------------------------------------------------------------");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void displayOneRequest(int request_id) {
        String query =  """
                        select
                            r.request_id,
                            r.type,
                            r.type_change,
                            r.site_id,
                            s.site_id,
                            s.length,
                            s.breadth,
                            s.persqft,
                            s.site_type,
                            o.owner_id,
                            o.name,
                            o.maintenance
                        from requests r
                        join sites s on r.site_id = s.site_id
                        join owners o on s.owner_id = o.owner_id
                        where r.request_id = ?
                        """;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, request_id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                System.out.println("\n--- REQUEST INFORMATION ---");
                System.out.println("Request ID:                 " + rs.getInt("request_id"));
                System.out.println("Request Type:               " + rs.getString("type"));
                System.out.println("Requested New Site Type:    " + rs.getString("type_change"));
                
                System.out.println("\n--- SITE INFORMATION ---");
                System.out.println("Site ID:                    " + rs.getInt("site_id"));
                System.out.println("Site Length:                " + rs.getInt("length") + " ft");
                System.out.println("Site Breadth:               " + rs.getInt("breadth") + " ft");
                System.out.println("Site Area:                  " + (rs.getInt("length") * rs.getInt("breadth")) + " sqft");
                System.out.println("Current Rate Per SqFt:      Rs. " + rs.getInt("persqft"));
                System.out.println("Current Site Type:          " + rs.getString("site_type"));
                
                System.out.println("\n--- OWNER INFORMATION ---");
                System.out.println("Owner ID:                   " + rs.getInt("owner_id"));
                System.out.println("Owner Name:                 " + rs.getString("name"));
                System.out.println("Current Maintenance Due:    Rs. " + rs.getInt("maintenance"));
            }
            else System.out.println("Request not found");
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void acceptRequest(int request_id) {
        String query =  """
                        select *
                        from requests
                        where request_id = ?
                        """;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, request_id);
            ResultSet request = pstmt.executeQuery();

            if(request.next()) {

                String requestType      = request.getString(2);
                int new_owner_id        = request.getInt(3);
                int mt_amount           = request.getInt(4);
                String new_site_type    = request.getString(5);
                int site_id             = request.getInt(6);

                if(requestType == "OWNER_UPDATE")       sh.ownerUpdate(site_id, new_owner_id);
                else if(requestType == "TYPE_UPDATE")   sh.typeUpdate(site_id, new_site_type);
            }
            System.out.println("Request Accpeted :) ");
            deleteRequest(request_id);
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void rejectRequest(int request_id) {
        System.out.println("Request Rejected :( ");
        deleteRequest(request_id);
    }

    public void deleteRequest(int request_id) {
        String query =  """
                        delete from requests
                        where request_id = ?
                        """;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, request_id);
            pstmt.executeUpdate();
        }
        catch(Exception e) { System.out.println(e); }
    }

    public void createTypeChangeRequest(int site_id, String new_type) {
        String check =  """
                        select count(*)
                        from requests
                        where site_id = ?
                        """;

        try {
            PreparedStatement checkStmt = con.prepareStatement(check);
            checkStmt.setInt(1, site_id);
            ResultSet rs = checkStmt.executeQuery();
            
            if(rs.next() && rs.getInt(1) > 0) {
                System.out.println("A request for this site is already pending!");
                return;
            }
            
            String insert = """
                            insert into requests (type, type_change, site_id)
                            values ('TYPE_CHANGE', ?, ?)
                            """;
            
            PreparedStatement pstmt = con.prepareStatement(insert);
            pstmt.setString(1, new_type);
            pstmt.setInt(2, site_id);
            pstmt.executeUpdate();

            System.out.println("Type change request submitted successfully");
            System.out.println("Your request will be reviewed by the admin");
        }
        catch(Exception e) { System.out.println(e); }
    }
}

