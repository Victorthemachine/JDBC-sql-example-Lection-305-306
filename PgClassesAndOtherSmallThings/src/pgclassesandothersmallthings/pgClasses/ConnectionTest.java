/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

import java.sql.*;

/**
 *
 * @author Tanya
 */
public class ConnectionTest {

    public static void main(String[] args) throws SQLException {
        Connection con = DriverManager.getConnection(Config.URL, Config.USER, Config.PASSWORD);
        AirlineClient air = new AirlineClient(con);
        con.setAutoCommit(false);
        try {
            /*        System.out.println(air.getCountries());
        System.out.println(air.getAirportsByCountry("Papua New Guinea"));
        air.getNearestAirport(50000000f, 10000000f);
        System.out.println(air.getCountryByName("Ruzyne"));*/
            
            //REMEMBER TO DELETE THE ROWS AFTER TRYING THESE!!!
            
            //=======Error Showcase=======
            air.addRoute(59037, 4934, 548, 1667);
            /* //( ͡° ͜ʖ ͡°)
            if (true) {
                throw new RuntimeException("troll");
            }*/
            air.addRoute(59037, 4934, 548, 1667);
            //=======Error Showcase=======        

            //=======Adding two rows to route Showcase=======        
            //air.addRoute(59037, 4934, 548, 1667);
            //air.addRoute(59038, 4934, 548, 1667);
            //=======Adding two rows to route Showcase=======        
            con.commit();
        } catch (Exception ex) {
            System.out.println("An error occured while attempting to use Airline methods\nvvv Stack trace vvv");
            ex.printStackTrace(System.err);
            con.rollback();
        }
        
        //=========OLD (split into AirlineClient class for clarity)=========
        /*        try {
            System.out.println("Connected");
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM airline")) {
                while (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM airline")) {
                rs.next();
                System.out.println(rs.getLong(1));
            }
            
        } finally {
            con.close();
            System.out.println("Closed connection");
        }*/
        //=========OLD (split into AirlineClient class for clarity)=========
        
    }
}
