import java.sql.*;
import java.util.Vector;
import org.apache.xmlrpc.webserver.WebServer;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.XmlRpcException;

public class TinyBookServer {
   private static Connection c;

   public static Object[] search(String topic){
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
         System.exit(0);
      }

      return return_value.toArray();
   }

   public String lookup(String item_number){
      return "";
   }

   public String buy(String item_number){
      return "";
   }


   public static void main( String args[] ) {
      c = null;
      Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
         System.out.println("Opened database successfully");

         System.out.println(search("College Life"));

         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }

   }
}