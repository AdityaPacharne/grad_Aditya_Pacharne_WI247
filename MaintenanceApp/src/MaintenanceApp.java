import java.io.*;
import java.sql.*;
import java.util.*;

import util.DBHelper;
import helper.SiteHelper;
import helper.OwnerHelper;
import helper.RequestHelper;

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


public class MaintenanceApp {
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
                                3. Exit
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
                                                5. View All Owners
                                                6. View All Sites
                                                7. Exit 
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
                                System.out.println("Current Owners");
                                oh.viewOwners();
                                System.out.print("Enter the owner_id to remove: ");
                                int owner_id = Integer.parseInt(br.readLine());
                                oh.deleteOwner(owner_id);
                            }
                            
                            else if(admin_choice == 5) {
                                oh.viewOwners();
                            }

                            else if(admin_choice == 6) {
                                sh.viewAllSites();
                            }

                            else {
                                break;
                            }
                        }
                    }
                    else{
                        System.out.println("Incorrect Password :(");
                        break;
                    }
                }
                else if(user_choice == 2) {
                    System.out.print("Enter Owner ID: ");
                    int owner_id = Integer.parseInt(br.readLine());

                    if(!oh.ownerExists(owner_id)) {
                        System.out.println("Owner ID not found!");
                        continue;
                    }

                    while(true) {
                        System.out.print(   """
                                            1. View My Info
                                            2. View My Sites Info
                                            3. Request Site Type Change
                                            4. Make Payment
                                            5. Exit
                                            """);

                        int owner_choice = Integer.parseInt(br.readLine());

                        if(owner_choice == 1) {
                            oh.viewOwnerInfo(owner_id);
                        }

                        else if(owner_choice == 2) {
                            sh.viewSitesByOwner(owner_id);
                        }

                        else if(owner_choice == 3) {
                            sh.viewSitesByOwner(owner_id);
                            System.out.print("Enter site_id to change: ");
                            int site_id = Integer.parseInt(br.readLine());

                            if(!sh.verifySiteOwner(site_id, owner_id)) {
                                System.out.println("You dont own this site");
                                continue;
                            }

                            System.out.println( """
                                                Available site types:
                                                1. OPEN
                                                2. VILLA
                                                3. APARTMENT
                                                4. HOUSE
                                                Enter new site type number: 
                                                """);
                            int type_choice = Integer.parseInt(br.readLine());

                            String new_type = "";
                            switch (type_choice) {
                                case 1 -> new_type = "OPEN";
                                case 2 -> new_type = "VILLA";
                                case 3 -> new_type = "APARTMENT";
                                case 4 -> new_type = "HOUSE";
                                default -> {
                                    System.out.println("Invalid choice");
                                    continue;
                                }
                            }

                            rh.createTypeChangeRequest(site_id, new_type);
                        }

                        else if(owner_choice == 4) {
                            oh.viewOwnerInfo(owner_id);
                            System.out.print("\nEnter payment amount: ");
                            int payment = Integer.parseInt(br.readLine());
                            oh.makePayment(owner_id, payment);
                        }

                        else {
                            break;
                        }
                    }
                }
                else {
                    break;
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
