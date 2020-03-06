//Program for creating table and placing proper rows within it
import java.sql.*;

public class TinyBookServer {

   public static void create_table(Connection c){
      Statement stmt = null;

      try{
         stmt = c.createStatement();
         String sql = "CREATE TABLE BOOKS " +
                        "(ID INT PRIMARY KEY     NOT NULL," +
                        " TITLE          CHAR(100)    NOT NULL, " + 
                        " TOPIC          CHAR(50)     NOT NULL, " + 
                        " STOCK          INT  NOT NULL, " + 
                        " PRICE          FLOAT NOT NULL)"; 
         stmt.executeUpdate(sql);
         stmt.close();
      } catch (Exception e ){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Table created successfully");
   }

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

   public static void populate_table(Connection c){
      Statement stmt = null;

      insert_row(c, "(53477, 'Achieve Less Bugs and More Hugs in CSCI 339', 'Distributed Systems', 0, 69.0 )");
      insert_row(c, "(53573, 'Distributed Systems for Dummies', 'Distributed Systems', 0, 70.0 )");
      insert_row(c, "(12365, 'Surviving College', 'College Life', 0, 71.0 )");
      insert_row(c, "(12498, 'Cooking for the Impatient Undergraduate', 'College Life', 0, 72.0 )");

      System.out.println("Rows created successfully");
   }

   


   public static void main( String args[] ) {
      Connection c = null;
      Statement stmt = null;
      
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:test.db");
         System.out.println("Opened database successfully");

         populate_table(c);

         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }

   }
}
