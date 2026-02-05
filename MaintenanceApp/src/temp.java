import java.io.*;
import java.sql.*;
import java.util.*;

import util.DBHelper;

/*
0 - Open site
1 - Villa
2 - Apartment
3 - Independent House
*/

// create table sites 
// site_id serial primary_key,
// length int,
// breadth int,
// persqft int,
// site_type varchar(20), (OPEN, VILLA, APARTMENT, HOUSE)
// maintenance int,
// owner_id references owner(owner_id);
//
// create table owners
// owner_id serial primary_key,
// name varchar(20);
//
// create table requests
// request_id serial primary_key,
// type varchar(20), // (OWNER_CHANGE, MAINTENANCE, TYPE_CHANGE)
// owner_change int,
// mt_change int,
// type_change varchar(20),
// site_id references site(site_id);
//

class SiteHelper {

    int costOpen = 6;
    int costNonOpen = 9;

    Connection con = dh.getConnection();
    Statement stmt = con.createStatement();

    int fetchMT(int site_id) {
        ResultSet rs = stmt.executeQuery("select maintenance from sites where site_id=" + site_id);
        return rs.getInt(1);
    }

    String fetchSiteType(int site_id) {
        ResultSet rs = stmt.executeQuery("select site_type from sites where site_id=" + site_id);
        return rs.getString(1);
    }

    // void reduceMT(int site_id, int amount) {
    //     int current_mt = fetchMT(site_id);
    //     stmt.executeUpdate("update sites set maintenance=" + current_mt - amount + " where site_id=" + site_id);
    // }

    void ownerUpdate(int site_id, int new_owner_id) {
        stmt.executeUpdate("update sites set owner_id=" + new_owner_id + " where site_id=" + site_id);
    }

    void typeUpdate(int site_id, String new_site_type) {
        String current_type = fetchSiteType(site_id);
        if(current_type == "OPEN")          stmt.executeUpdate("update sites set site_type=" + new_site_type + ", persqft=" + costNonOpen + " where site_id=" + site_id);
        else if(new_site_type == "OPEN")    stmt.executeUpdate("update sites set site_type=" + new_site_type + ", persqft=" + costOpen + " where site_id=" + site_id);
        else                                stmt.executeUpdate("update sites set site_type=" + new_site_type + " where site_id=" + site_id);
    }
}

class OwnerHelper {
    Connection con = DBHelper.getConnection();

    Statement stmt = con.createStatement();

    void addOwner(String name) {
        stmt.executeUpdate("insert into owners (name) values (" + name + ")");
    }
}

class RequestHelper {

    SiteHelper sh = new SiteHelper();
    OwnerHelper oh = new OwnerHelper();

    Connection con = DBHelper.getConnection();
    Statement stmt = con.createStatement();

    void displayAllRequests() {
        System.out.println("Here are the pending requests");
        ResultSet rs = stmt.executeQuery("select * from requests");

        while(rs.next()){
            System.out.println("Request ID: " + rs.getInt(1));
            System.out.println("Request Type: " + rs.getString(2));
        }
    }

    void displayOneRequest(int request_id) {
        ResultSet request = stmt.executeQuery("select * from requests where request_id=" + request_id);
        int site_id;
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

    void fetchSiteAndOwner(int site_id) {
        String query = "select s.site_id, s.length, s.breadth, s.persqft, s.sitetype, s.maintenance, o.owner_id, o.owner_name from sites s join owners o on s.owner_id = o.owner_id ";
                    
        ResultSet rs = stmt.executeQuery(query);

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

    void acceptRequest(int request_id) {
        ResultSet request = stmt.executeQuery("select * from requests where request_id=" + request_id);
        if(request.next()) {

            String requestType      = request.getString(2);
            int new_owner_id        = request.getInt(3);
            int mt_amount           = request.getInt(4);
            String new_site_type    = request.getString(5);
            int site_id             = request.getInt(6);

            // if(requestType == "MAINTENANCE")        sh.reduceMT(site_id, amount);
            if(requestType == "OWNER_UPDATE")       sh.ownerUpdate(site_id, new_owner_id);
            else if(requestType == "TYPE_UPDATE")   sh.typeUpdate(site_id, new_site_type);
        }

        System.out.println("Request Accpeted :) ");
        deleteRequest(request_id);
    }

    void rejectRequest(int request_id) {
        System.out.println("Request Rejected :( ");
        deleteRequest(request_id);
    }

    void deleteRequest(int request_id) {
        stmt.executeUpdate("delete from requests where request_id=" + request_id);
    }
}

public class temp {
    public static void main (String[] args) {

        SiteHelper sh = new SiteHelper();
        OwnerHelper oh = new OwnerHelper();
        RequestHelper rh = new RequestHelper();

        Connection con = DBHelper.getConnection();
        Statement stmt = con.createStatement();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true) {

            System.out.print("Choose type of user \n1. Admin \2.Site Owner");
            int user_choice = Integer.parseInt(br.readLine());

            if(user_choice == 1) {
                System.out.print("Enter Password: ");
                String password = br.readLine();

                if(password == "1234568") {
                    System.out.println("Correct Password :)");

                    while(true) {

                        System.out.println("\n1. View Request \n2. Charge Maintenance \n3. Add Owner \n4. Edit Site Details \n5. Remove Details \n6. Exit"); 

                        int admin_choice = Integer.parseInt(br.readLine());

                        if(admin_choice == 1) {

                            rh.displayAllRequests();

                            System.out.print("Enter the request_id: ");
                            int request_id = Integer.parseInt(br.readLine());

                            rh.displayOneRequest(request_id);

                            System.out.print("Enter 1-Accept 2-Reject: ");
                            int accept_reject = Integer.parseInt(br.readLine());

                            if(accept_reject == 1)      rh.acceptRequest(request_id);
                            else                        rh.rejectRequest(request_id);
                        }
                        
                        else if(admin_choice == 2) {
                            stmt.executeUpdate("update sites set maintenance = maintenance - (length * breadth * cost)");
                        }

                        else if(admin_choice == 3) {
                            System.out.print("Enter the Owner Name to be added: ");
                            String name = br.readLine();
                            oh.addOwner(name);
                        }

                        else if(admin_choice == 4) {
                            System.out.print("Enter the site_id to make changes to: ");
                            int site_id = Integer.parseInt(br.readLine());
                        }
                    }
                }
                else{
                    System.out.println("Incorrect Password :(");
                    break;
                }
            }
            else{
                System.out.print("Enter Owner ID: ");
                int owner_id = Integer.parseInt(br.readLine());

                while(true) {
                    System.out.print("Operation available: \n1. View Site Details \n2. Request for Owner Update \n3. Request for Site Type Update \n4. Exit");
                    int owner_choice = Integer.parseInt(br.readLine());

                    if(owner_choice == 1) {

                    }
                }
            }
        }
    }
}


/*
Layout Maintenance Application
---------------------------------------
Types -  Villa, Apartment, Independent House, Open Site
Total Sites - 35 Sites
First 10 sites are of 40x60 ft size
Next 10 sites are of 30x50 ft size
Last 15 sites are of 30x40 ft size
Open sites are charged 6Rs/sqft
Occupied sites are charged 9Rs./sqft
Admin	- 
	Can add/edit/remove the owner details and site details
	Can collect the maintenance and update
	Can see the pending details of all sites or the specific site
	Can approve/reject the site owners update about their own sites
Site Owner -
	Can only see/update the details of his/her own site (but should be approved by Admin)
*/

/*
Repo Name:  grad_<yourName>_<WT00ID>
Directory:  MaintenanceApp
                src -> (.java files)
                db  -> (.sql files)
*/
