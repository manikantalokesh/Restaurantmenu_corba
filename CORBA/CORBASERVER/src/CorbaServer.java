import Restaurant.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.util.Map.Entry;
import java.util.Properties;


class RestImpl extends RestCPOA {
  private ORB orb;

  public void setORB(ORB orb_val) {
    orb = orb_val; 
  }

  private String menu = "Menu:\n1.Fried Chicken - $15.0\n2.Cola - $5\n";

  

  public String Menu() {
    return menu;
  }

  // implement sayHello() method
  public String sayHello() {
    return "\nWelcome To Restaurant !!\n";
  }
  
  
  public String OrderFood(String username, int friedchicken, int cola, float money) {
    if (friedchicken < 0 || cola < 0 ) {
      return "Enter Quantity more than 0";
    }
    if(Globalhashmap.Orders.containsKey(username)) {
    	return "Order already present";
    	
    }
    
    OrderDetails new_order=new OrderDetails();
    
    new_order.setCount(friedchicken, cola,money);
    Globalhashmap.Orders.put(username, new_order);
    
    String OrderRecieved = "\nOrder received for user " + username + "\n";
    OrderRecieved += "Fried quantity: " + new_order.FriedCCount() + "\n";
    OrderRecieved += "Cola quantity: " + new_order.ColaCount() + "\n";
    OrderRecieved += "Your Total will be: " + new_order.Total() + "\n";
    OrderRecieved += "money given: " + new_order.money() + "\n";
    OrderRecieved += "Thank you for your order!";

    
    return OrderRecieved;
  }

public  String customerOrder(String username) {
	
      if (!Globalhashmap.Orders.containsKey(username)) {
	        return "Order not found";
	    }
	  OrderDetails orderDetails = Globalhashmap.Orders.get(username);
	  String OrderStatus = "Order details for user " + username + "\n";
	  OrderStatus += "Your order for Fried quantity.........: " + orderDetails.FriedCCount() + "\n";
	  OrderStatus += "Your order for Cola quantity..........: " + orderDetails.ColaCount() + "\n";
	  OrderStatus += "Total: " + orderDetails.Total() + "\n";
	  OrderStatus += "money given: " + orderDetails.money() + "\n";
	  OrderStatus += "Remaining: " + orderDetails.RemainingBalance() + "\n";
	
	  return OrderStatus;
	
}

public String viewcurrentorders() {
	String CurrentOrders = "";
    if (Globalhashmap.Orders.isEmpty()) {
        CurrentOrders = "No current orders\n";
    } else {
        for (Entry<String, OrderDetails> entry : Globalhashmap.Orders.entrySet()) {
            String username = entry.getKey();
            OrderDetails orderDetails = entry.getValue();
            CurrentOrders += "Order details for user " + username + "\n";
            CurrentOrders += "Fried quantity: " + orderDetails.FriedCCount() + "\n";
            CurrentOrders += "Cola quantity: " + orderDetails.ColaCount() + "\n";
            CurrentOrders += "Total: " + orderDetails.Total() + "$\n";
            CurrentOrders += "-----------------------------\n";
        }
    }
 
    return CurrentOrders;
}

 
  // implement shutdown() method
  // public void shutdown() {
  //   orb.shutdown(false);
  // }
}


public class CorbaServer {

  public static void main(String args[]) {
    try{
      // create and initialize the ORB
      ORB orb = ORB.init(args, null);

      // get reference to rootpoa & activate the POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // create servant and register it with the ORB
      RestImpl restimpl = new RestImpl();
      restimpl.setORB(orb); 

      // get object reference from the servant
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(restimpl);
      RestC href = RestCHelper.narrow(ref);
          
      // get the root naming context
      // NameService invokes the name service
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");
      // Use NamingContextExt which is part of the Interoperable
      // Naming Service (INS) specification.
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // bind the Object Reference in Naming
      String name = "Hello";
      NameComponent path[] = ncRef.to_name( name );
      ncRef.rebind(path, href);

      System.out.println("RestaurantServer ready and waiting ...\n");

      // wait for invocations from clients
      orb.run();
    } 
        
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
          
      System.out.println("HelloServer Exiting ...");
        
  }
}