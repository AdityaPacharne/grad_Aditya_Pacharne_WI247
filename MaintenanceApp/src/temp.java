import java.io.*;
import java.sql.*;
import java.util.*;

import util.DBHelper;
import helper.SiteHelper;
import helper.OwnerHelper;
import helper.RequestHelper;

/*
   0 - Open site
   1 - Villa
   2 - Apartment
   3 - Independent House
   */

// create table sites 
// (site_id serial primary key,
// length int,
// breadth int,
// persqft int,
// site_type varchar(20)
// check (site_type in ('OPEN', 'VILLA', 'APARTMENT', 'HOUSE')),
// owner_id int references owners(owner_id));
//
// create table owners
// (owner_id serial primary key,
// name varchar(20),
// maintenance int);
//
// create table requests
// (request_id serial primary key,
// type varchar(20)
// check (type in ('OWNER_CHANGE', 'MAINTENANCE', 'TYPE_CHANGE')),
// owner_change int,
// mt_change int,
// type_change varchar(20),
// site_id int references sites(site_id));


public class temp {
    public static void main (String[] args) {

        SiteHelper sh = new SiteHelper();
        OwnerHelper oh = new OwnerHelper();
        RequestHelper rh = new RequestHelper();

        Connection con = DBHelper.getConnection();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while(true) {

                System.out.print("""
                                Choose type of user
                                1. Admin 
                                2. Site Owner
                                """);
                int user_choice = Integer.parseInt(br.readLine());

                if(user_choice == 1) {
                    System.out.print("Enter Password: ");
                    String password = br.readLine();

                    if(password.equals("admin")) {
                        System.out.println("Correct Password :)");

                        while(true) {

                            System.out.println( """
                                                1. View Request
                                                2. Charge Maintenance 
                                                3. Add Owner 
                                                4. Remove Owner 
                                                5. View Owners
                                                6. Exit 
                                                """);
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
                                sh.chargeMT();
                            }

                            else if(admin_choice == 3) {
                                System.out.print("Enter the Owner Name to be added: ");
                                String name = br.readLine();
                                oh.addOwner(name);
                            }

                            else if(admin_choice == 4) {
                                System.out.print("Enter the owner_id to remove: ");
                                int owner_id = Integer.parseInt(br.readLine());
                                oh.deleteOwner(owner_id);
                            }
                            
                            else if(admin_choice == 5) {
                                oh.viewOwners();
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
        catch(Exception e) {
            System.out.println(e);
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
