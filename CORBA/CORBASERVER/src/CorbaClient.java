import Restaurant.*; 
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

import java.io.IOException;
import java.io.*;
import org.omg.CORBA.*;

public class CorbaClient
{
  static RestC RestImpl;
  public static int NumberInput() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();
		int choice = -1;
		try {
			choice = Integer.valueOf(s);
		} catch (NumberFormatException e) {
			System.out.println("****Input is not a number. Try again.****");
			return choice;
		}
		if (choice < 0) {
			System.out.println("****input is Negative. Try again.****");
			choice = -1;
		}
		return choice;
	}

	public static String StringInput() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();
		return s;
	}

  public static void main(String args[])
    {
      try{
        // create and initialize the ORB
        ORB orb = ORB.init(args, null);

        // get the root naming context
        org.omg.CORBA.Object objRef = 
            orb.resolve_initial_references("NameService");
        // Use NamingContextExt instead of NamingContext. This is 
        // part of the Interoperable naming Service.  
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
        // resolve the Object Reference in Naming
        String name = "Hello";
        RestImpl = RestCHelper.narrow(ncRef.resolve_str(name));

        System.out.println("Obtained a handle on server object: " + RestImpl);

        System.out.println(RestImpl.sayHello());
        //final String MANAGER_PASSWORD = "password";

        int[] userinput = {0};
        String userType = "C";
        while(true) {
          
    	    System.out.println("Are you a Manager or a Customer? (M/C)");
    	    userType = StringInput().toUpperCase();
    	   do {
               if (userType.equals("C")) {
                   System.out.println("\nCustomer Options:");
                   System.out.println("1.menu");
                   System.out.println("2.Place new order");
                   System.out.println("3.View order status");
                   System.out.println("4.Exit to MainMenu");
                   userinput[0] = NumberInput();
                   customeroptions(userinput);
                   
               } else if (userType.equals("M")) {
                   
            	   
            	   System.out.println("\nHello There ordered items are!!!!!");
            	   System.out.println("---------------------------\n");
                   System.out.println(RestImpl.viewcurrentorders());
                   break;
               } else {
                   System.out.println("Invalid user type. Try again...");
                   break;
               }
           } while (userinput[0] != 4);
       }  
               
      
    }
         catch (Exception e) {
          System.out.println("ERROR : " + e) ;
          e.printStackTrace(System.out);
       
         }
  
    }
   public static void customeroptions(int[] userinput) throws Exception {
	   switch (userinput[0]) {
	   case 1: {
			 String menu = RestImpl.Menu();
			 System.out.println(menu);
			  
			break;
		}
	   case 2: {
		System.out.println("\n*************************");
		System.out.println("\nEnter Your Name for Order");
		String user=StringInput();
		System.out.println("\n--------------------------");
		System.out.println("\nEnter Quantity of fried Chicken");
		int friedchicken= NumberInput();
		System.out.println("\n--------------------------");
		System.out.println("\nEnter Quantity of Cola");
		int cola = NumberInput();
		System.out.println("\n--------------------------");
		System.out.println("\nEnter Money");
		float money = NumberInput();
		System.out.println("\n--------------------------");
		String result= RestImpl.OrderFood(user, friedchicken, cola, money);
		System.out.println(result);
		System.out.println("\n*************************");
			  
			
			break;
		}
	   case 3: {
		   System.out.println("\n*************************");
		   System.out.println("\nEnter *ORDERID* - user for Order");
		   System.out.println("\n*************************");
		   String user = StringInput();
		   String result = RestImpl.customerOrder(user);
		   if (result == null || result.isEmpty()) {
			   System.out.println("\n*************************");
               System.out.println("\nNo order found for user: " + user);
               System.out.println("\n*************************");
           } else {
        	   System.out.println("\n*************************");
               System.out.println("\nOrder Status: On Progress" +  user);
               System.out.println(result);
               System.out.println("\n*************************");
           }
		   break;
	   }
	   case 4:{
		    
		    return;
		   
		   //break;
	   }
	default:
		System.out.println("\n*************************");
		System.out.println("Wrong Input!! PLease Try again.. ");
		System.out.println("\n*************************");
		return;
	}
   }
}