/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Tanya
 */
@Entity
@Table(name = "route")
@NamedQueries({
    @NamedQuery(name = "Route.findAll", query = "SELECT r FROM Route r")})
public class Route implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "airline")
    private String airline;
    @Basic(optional = false)
    @Column(name = "source_airport")
    private String sourceAirport;
    @Basic(optional = false)
    @Column(name = "destination_airport")
    private String destinationAirport;
    @Basic(optional = false)
    @Column(name = "codeshare")
    private boolean codeshare;
    @Basic(optional = false)
    @Column(name = "stops")
    private boolean stops;
    @Basic(optional = false)
    @Column(name = "equipment")
    private String equipment;
    @JoinColumn(name = "airline_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Airline airlineId;
    @JoinColumn(name = "source_airport_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Airport sourceAirportId;
    @JoinColumn(name = "destination_airport_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Airport destinationAirportId;

    public Route() {
    }

    public Route(Integer id) {
        this.id = id;
    }

    public Route(Integer id, String airline, String sourceAirport, String destinationAirport, boolean codeshare, boolean stops, String equipment) {
        this.id = id;
        this.airline = airline;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.codeshare = codeshare;
        this.stops = stops;
        this.equipment = equipment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getSourceAirport() {
        return sourceAirport;
    }

    public void setSourceAirport(String sourceAirport) {
        this.sourceAirport = sourceAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public boolean getCodeshare() {
        return codeshare;
    }

    public void setCodeshare(boolean codeshare) {
        this.codeshare = codeshare;
    }

    public boolean getStops() {
        return stops;
    }

    public void setStops(boolean stops) {
        this.stops = stops;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Airline getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Airline airlineId) {
        this.airlineId = airlineId;
    }

    public Airport getSourceAirportId() {
        return sourceAirportId;
    }

    public void setSourceAirportId(Airport sourceAirportId) {
        this.sourceAirportId = sourceAirportId;
    }

    public Airport getDestinationAirportId() {
        return destinationAirportId;
    }

    public void setDestinationAirportId(Airport destinationAirportId) {
        this.destinationAirportId = destinationAirportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Route)) {
            return false;
        }
        Route other = (Route) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pgclassesandothersmallthings.pgClasses.Route[ id=" + id + " ]";
    }
    
}
