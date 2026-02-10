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
        System.out.println("Here are the pending requests");
        String query =  """
                        select *
                        from requests
                        """;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                System.out.println("Request ID: " + rs.getInt(1));
                System.out.println("Request Type: " + rs.getString(2));
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void displayOneRequest(int request_id) {
        String query =  """
                        select *
                        from requests
                        where request_id = ?
                        """;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, request_id);
            ResultSet request = pstmt.executeQuery();

            int site_id = -1;
            if(request.next()) {
                System.out.println("Request ID:                 " + request.getInt(1));
                System.out.println("Request Type:               " + request.getString(2));
                System.out.println("Request New Owner:          " + request.getInt(3));
                System.out.println("Request MT Reduction:       " + request.getInt(4));
                System.out.println("Request Site Type Change:   " + request.getString(5));
                site_id = request.getInt(1);
            }
            fetchSiteAndOwner(site_id);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void fetchSiteAndOwner(int site_id) {
        String query =  """
                        select
                            s.site_id,
                            s.length,
                            s.breadth,
                            s.persqft,
                            s.site_type,
                            s.maintenance,
                            o.owner_id,
                            o.name
                        from sites s
                        join owners o
                        on s.owner_id = o.owner_id
                        where site_id = ?
                        """;

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, site_id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                System.out.println("Site ID:            " + rs.getInt(1));
                System.out.println("Site Length:        " + rs.getInt(2));
                System.out.println("Site Breadth:       " + rs.getInt(3));
                System.out.println("Site PerSqft:       " + rs.getInt(4));
                System.out.println("Site Type:          " + rs.getString(5));
                System.out.println("Site Maintenance:   " + rs.getInt(6));
                System.out.println("Site Owner ID:      " + rs.getInt(7));
                System.out.println("Site Owner Name:    " + rs.getString(8));
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
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
        catch(Exception e) {
            System.out.println(e);
        }
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
        catch(Exception e) {
            System.out.println(e);
        }
    }
}

