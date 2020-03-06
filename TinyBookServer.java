import java.sql.*;

public class TinyBookServer {
   private static Connection c;

   public static void insert_row(Connection c, String args){
      Statement stmt = null;

      try{
         stmt = c.createStatement();
         String sql = "INSERT INTO BOOKS (ID,TITLE,TOPIC,STOCK,PRICE) " +
                  "VALUES" + args + " ;"; 

         stmt.executeUpdate(sql);
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Row created successfully");
   }

   public static String search(String topic){
      Statement stmt = null;

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

            System.out.println(title);
         }
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Search for " + topic + " successful");
      return "";
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

         search("College Life");

         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }

   }
}