/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tanya
 */
public class AirlineClient {

    private final Connection connection;

    public AirlineClient(Connection connection) {
        this.connection = connection;
    }

    public long getSize() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM airline")) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    public List<String> getNames() throws SQLException {
        List<String> temp = new ArrayList();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM airline")) {
                while (rs.next()) {
                    temp.add(rs.getString("name"));
                }
                return temp;
            }
        }
    }

    public void add(String name) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            name = name.replace("'", "''");
            stmt.executeUpdate("INSERT into airline (name) VALUES ('" + name + "')");
        }
    }

    /*
    SELECT s.name AS source, d.name AS destination, geodistance(s.latitude, s.longitude, d.latitude, d.longitude) AS distance
        FROM route
        LEFT JOIN airport AS s ON s.id = source_airport_id
        LEFT JOIN airport AS d ON d.id = destination_airport_id
        WHERE s.name='Ruzyne'
        ORDER BY distance DESC
     */
    //Overview of the SQL query ^
    public void getNearestAirport(float latitude, float longitude) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT d.name AS destination, d.city AS city, d.country AS country, d.latitude AS lat, d.longitude AS lon, geo_distance('" + latitude + "', '" + longitude + "', d.latitude, d.longitude) AS distance FROM route LEFT JOIN airport AS d ON d.id = destination_airport_id ORDER BY distance ASC")) {
                while (rs.next()) {
                    System.out.println(rs.getString("destination") + "," + rs.getString("city") + "," + rs.getString("country") + " " + "[" + ((rs.getFloat("lat") < 0) ? (Math.abs(rs.getFloat("lat")) + "S") : (rs.getFloat("lat") + "N")) + "" + ";" + ((rs.getFloat("lon") < 0) ? (Math.abs(rs.getFloat("lon")) + "S") : (rs.getFloat("lon") + "N")) + "]" + "; " + (float) (Math.round(rs.getFloat("distance") * 0.0003048 * 10) * 0.1) + " km");
                    break;
                }
            }
        }
    }

    public List<String> getCountries() throws SQLException {
        Set<String> temp = new HashSet<>();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT country FROM airline")) {
                while (rs.next()) {
                    temp.add(rs.getString("country"));
                }
                return new ArrayList(temp);
            }
        }
    }

    public List<String> getAirportsByCountry(String country) throws SQLException {
        List<String> temp = new ArrayList();
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT name FROM airline WHERE country = '" + country + "'")) {
                while (rs.next()) {
                    temp.add(rs.getString("name"));
                }
                return temp;
            }
        }
    }

    //=========Multiple versions of the same method=========
    /*    public String getCountryByName(String name) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            name = name.replaceAll("'", "''");
            try (ResultSet rs = stmt.executeQuery("SELECT country FROM airport WHERE name = '" + name + "'")) {
                if (rs.next()) {
                    return rs.getString("country");
                } else {
                    return null;
                }
            }
        }
    }*/
//    public String getCountryByName(String name) throws SQLException {
//        try (PreparedStatement stmt = connection.prepareStatement(("SELECT country FROM airport WHERE name LIKE ?"))) {
//            stmt.setString(1, name);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getString("country");
//                } else {
//                    return null;
//                }
//            }
//        }
//    }
    public String getCountryByName(String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(("SELECT country FROM airport WHERE name LIKE ?"))) {
            stmt.setString(1, name);
            Random x = new Random();
            for (int i = 0; i < 3; i++) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("country");
                    } else {
                        return null;
                    }
                } catch (SQLException ex) {
                    if (ex.getErrorCode() != 100) {
                        throw ex;
                    }
                    try {
                        Thread.sleep(10 + x.nextInt(190));
                    } catch (InterruptedException ex1) {

                    }
                }
            }
            throw new SQLException("Not possible, attempted to execute 3 times");
        }
    }
    //=========Multiple versions of the same method=========

    public void addRoute(int id, int airlineId, int sourceId, int destId) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO route (id, airline, airline_id, source_airport, source_airport_id, destination_airport, destination_airport_id, codeshare, stops, equipment)"
                + "VALUES (?, '', ?,'', ?,'',?,0,0, '')")) {
            stmt.setInt(1, id);
            stmt.setInt(2, airlineId);
            stmt.setInt(3, sourceId);
            stmt.setInt(4, destId);
            stmt.execute();
            System.out.println("Added route!");
        }
    }

}
