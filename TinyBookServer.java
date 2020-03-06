import java.sql.*;
import java.util.*;
import org.apache.xmlrpc.webserver.WebServer;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.XmlRpcException;
import java.io.InputStream;

public class TinyBookServer {
   private static Connection c;

   public Object[] search(String topic){
      Statement stmt = null;
      Vector<String> return_value = new Vector<String>();

      try{
         stmt = c.createStatement();
         String sql = "SELECT * FROM BOOKS where TOPIC='" + topic + "' ;"; 

         ResultSet results = stmt.executeQuery(sql);

         while (results.next() ){
            int id = results.getInt("id");
            String title = results.getString("title");
            String returned_topic = results.getString("topic");
            int stock = results.getInt("stock");
            float price = results.getFloat("price");

            return_value.add("TITLE=" + title + ",ID=" + id);
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }

      return return_value.toArray();
   }

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

            result = "ID=" + id + ",TITLE=" + title + ",TOPIC=" + returned_topic + ",STOCK=" + stock + "PRICE=" + price;
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      }

      return result;	

   }

   public String buy(String item_number){
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
            
            if(stock > 0){
               Statement update = c.createStatement();
               String query = "UPDATE BOOKS set STOCK = " + (stock-1) + "where ID=" + id + ";";
            
               update.executeUpdate(query);
            }
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         return "Failure";
      }

      return "Success";	
   }

   private static void log(){
      //TO DO
   }

   private static void restock(){
      //TO DO
   }

   private static void update(String item_number, String new_price){
      //TO DO
   }

   public static void main( String args[] ) {
      c = null;
      Statement stmt = null;
      
      try {
	      PropertyHandlerMapping phm = new PropertyHandlerMapping();
	      XmlRpcServer xmlRpcServer;

	      // set up the webserver
	      WebServer server = new WebServer(8888);
	      xmlRpcServer = server.getXmlRpcServer();
	      phm.addHandler("service", TinyBookServer.class);
	      xmlRpcServer.setHandlerMapping(phm);
	      server.start();
	      System.out.println("Started successfully.");
	      System.out.println("Accepting requests. (Halt program to stop.)");
      } catch (Exception exception) {
         System.err.println("JavaServer: " + exception);
	   }

      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
         System.out.println("Connected to database successfully");
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      

      //Loop for client input
      InputStream source = System.in;
      Scanner input = new Scanner(source);

      while(true){
         System.out.println("Usage: log | restock | update item_id new_price ");
         System.out.print(">");
         String user_input = input.nextLine();

         if(user_input.equals("log")){
            log();
         }else if(user_input.equals("restock")){
            restock();
         }else if(user_input.startsWith("update")){
            StringTokenizer splitter = new StringTokenizer(user_input, " ");
            splitter.nextToken();
            update(splitter.nextToken(), splitter.nextToken());
         }     
      }

   }
}
