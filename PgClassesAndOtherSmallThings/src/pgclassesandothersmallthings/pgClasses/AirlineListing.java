/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pgclassesandothersmallthings.pgClasses.exceptions.NonexistentEntityException;

/**
 *
 * @author davidmitic
 */
public class AirlineListing {
    public static void main(String[] args) throws NonexistentEntityException, Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PgClassesAndOtherSmallThingsPU");
        AirlineJpaController airlines = new AirlineJpaController(emf);
        List<Airline> airlineList = airlines.findAirlineEntities();
        System.out.println(airlineList.toString());
        System.out.println(airlineList.get(airlineList.size() - 1));
        Airline airline = airlines.findAirline(333);
        System.out.println(airline);
        airline.setName("Big PP airlines");
        //Air Baltic => Big PP airlines
        airlines.edit(airline);
        System.out.println(airline);
        //airlines.destroy(17577);
        airline = new Airline();
        airline.setAlias("Kyu");
        airline.setCallsign("Hewwo there");
        airline.setCountry("Land of the free keyboard");
        airline.setIata("HP");
        airline.setIcao("HuniePop");
        airline.setName("HuniePop airlines united");
        airlines.create(airline);
        System.out.println(airlineList.get(airlineList.size() - 1));
        airline = airlines.findAirlineByName("Big PP airlines");
        System.out.println(airline);
    }
}
