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
