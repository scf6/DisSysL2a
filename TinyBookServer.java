import java.sql.*;
import java.util.*;
import org.apache.xmlrpc.webserver.WebServer;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.XmlRpcException;
import java.io.InputStream;
import java.time.*;

public class TinyBookServer {
   //Database connection shared by all instances
   private static Connection c;

   //Search method exposed to clients
   public Object[] search(String topic){
      Statement stmt = null;
      Vector<String> return_value = new Vector<String>();

      try{
         //Get all books that are of that topic
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS where TOPIC='" + topic + "' ;"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");
            
            //Add title and id number to list to return
            return_value.add("TITLE=" + title + ",ID=" + id);
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }

      //Return object array which is a supported type of XMLRPC
      return return_value.toArray();
   }

   //Public lookup method for getting all info related to a particular book
   public String lookup(String item_number){
	   Statement stmt = null;
      String result = "";

      try{
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS where ID='" + item_number + "' ;"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");

            result = "ID=" + id + ",TITLE=" + title + ",TOPIC=" + returned_topic + ",STOCK=" + stock  + ",PRICE=" + price;
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }

      return result;	

   }

   //Public method which decrements stock by one and logs purchase as long as book has positive stock
   public String buy(String item_number){
	   Statement stmt = null;
      String result = "";

      try{
         //Get book of that ID number
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS where ID='" + item_number + "' ;"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");
            
            //Verify that this book is actually in stock
            if(stock > 0){
               //Decrement stock number
               Statement update = c.createStatement();
               String query = "UPDATE BOOKS set STOCK = " + (stock-1) + " where ID=" + id + ";";
               update.executeUpdate(query);
               update.close();

               //Add row to log denoting this purchase
               Statement log_update = c.createStatement();
               sql = "INSERT INTO LOG (TIME, PURCHASE) " +
                        "VALUES ('" + java.time.LocalDateTime.now() + "', '" + title + "') ;"; 
               log_update.executeUpdate(sql);
               log_update.close();
            }else{
					stmt.close();
					return "Failure: There were no books in stock";
				}
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         return "Failure";
      }

      return "Success";	
   }

   //Method for sysadmin that prints log of purchases
   private static void log(){
      Statement stmt = null;

      try{
         //Get all rows of LOG and print them
         stmt = c.createStatement();
         String sql = "SELECT * FROM LOG;"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            String time = results.getString("time");
            String purchase = results.getString("purchase");

            System.out.println(time + "  " + purchase); 
         }
      }catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }           
   }

   //Method for sysadmin to see all rows of BOOKs table
   private static void all(){
      Statement stmt = null;

      try{
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS;"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");

            System.out.println(id + " " + title+ " " + returned_topic+ " " + stock+ " " + price);
         }
      }catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }           
   }

   //Method for sysadmin to add some number of books to some book
   private static void restock(String item_number, String num_books){
      Statement stmt = null;

      try{
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS where ID=" + item_number + ";"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");
            
            Statement update = c.createStatement();
            int new_stock = stock+Integer.parseInt(num_books);
            String query = "UPDATE BOOKS set STOCK = " + new_stock + " where ID=" + id + ";";
            
            update.executeUpdate(query);
            update.close();
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }

   }

   //Method for sysadmin to update the price of a book
   private static void update(String item_number, String new_price){
      Statement stmt = null;
      String result = "";

      try{
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS where ID=" + item_number + ";"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");
            
            Statement update = c.createStatement();
            String query = "UPDATE BOOKS set PRICE = " + new_price + " where ID=" + id + ";";
            
            update.executeUpdate(query);
            update.close();
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }
   }

   public static void main( String args[] ) {
      c = null;
      Statement stmt = null;
      
      try {
	      PropertyHandlerMapping phm = new PropertyHandlerMapping();
	      XmlRpcServer xmlRpcServer;
   
         //Initialize web server
	      WebServer server = new WebServer(8888);
	      xmlRpcServer = server.getXmlRpcServer();

         //Connect Property Handler Mapping
	      phm.addHandler("service", TinyBookServer.class);
	      xmlRpcServer.setHandlerMapping(phm);
	      server.start();
	      System.out.println("Started successfully.");
	      System.out.println("Accepting requests. (Halt program to stop.)");
      } catch (Exception exception) {
         System.err.println("JavaServer: " + exception);
	   }

      try {
         //Connect to database
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
         System.out.println("Connected to stock database successfully");
      
         //Clear the log
         stmt = c.createStatement();
         String query = "DELETE FROM LOG;";
         stmt.executeUpdate(query);
         stmt.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      

      //Loop for client input of sysadmin
      InputStream source = System.in;
      Scanner input = new Scanner(source);

      while(true){
         try{
            System.out.println("Usage: log | restock item_id num_books_added | update item_id new_price | all ");
            System.out.print(">");
            String user_input = input.nextLine();

            if(user_input.equals("log")){
               log();
            }else if(user_input.startsWith("restock")){
               StringTokenizer splitter = new StringTokenizer(user_input, " ");
               splitter.nextToken();
               restock(splitter.nextToken(), splitter.nextToken());
            }else if(user_input.startsWith("update")){
               StringTokenizer splitter = new StringTokenizer(user_input, " ");
               splitter.nextToken();
               update(splitter.nextToken(), splitter.nextToken());
            }else if(user_input.equals("all")){
               all();
            }
         }catch( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
         }
      }

   }
}
