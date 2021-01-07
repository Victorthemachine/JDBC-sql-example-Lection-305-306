/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pgclassesandothersmallthings.pgClasses.exceptions.NonexistentEntityException;

/**
 *
 * @author davidmitic
 */
public class RouteJpaController implements Serializable {

    public RouteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Route route) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Airline airlineId = route.getAirlineId();
            if (airlineId != null) {
                airlineId = em.getReference(airlineId.getClass(), airlineId.getId());
                route.setAirlineId(airlineId);
            }
            Airport sourceAirportId = route.getSourceAirportId();
            if (sourceAirportId != null) {
                sourceAirportId = em.getReference(sourceAirportId.getClass(), sourceAirportId.getId());
                route.setSourceAirportId(sourceAirportId);
            }
            Airport destinationAirportId = route.getDestinationAirportId();
            if (destinationAirportId != null) {
                destinationAirportId = em.getReference(destinationAirportId.getClass(), destinationAirportId.getId());
                route.setDestinationAirportId(destinationAirportId);
            }
            em.persist(route);
            if (airlineId != null) {
                airlineId.getRouteList().add(route);
                airlineId = em.merge(airlineId);
            }
            if (sourceAirportId != null) {
                sourceAirportId.getSourceList().add(route);
                sourceAirportId = em.merge(sourceAirportId);
            }
            if (destinationAirportId != null) {
                destinationAirportId.getSourceList().add(route);
                destinationAirportId = em.merge(destinationAirportId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Route route) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Route persistentRoute = em.find(Route.class, route.getId());
            Airline airlineIdOld = persistentRoute.getAirlineId();
            Airline airlineIdNew = route.getAirlineId();
            Airport sourceAirportIdOld = persistentRoute.getSourceAirportId();
            Airport sourceAirportIdNew = route.getSourceAirportId();
            Airport destinationAirportIdOld = persistentRoute.getDestinationAirportId();
            Airport destinationAirportIdNew = route.getDestinationAirportId();
            if (airlineIdNew != null) {
                airlineIdNew = em.getReference(airlineIdNew.getClass(), airlineIdNew.getId());
                route.setAirlineId(airlineIdNew);
            }
            if (sourceAirportIdNew != null) {
                sourceAirportIdNew = em.getReference(sourceAirportIdNew.getClass(), sourceAirportIdNew.getId());
                route.setSourceAirportId(sourceAirportIdNew);
            }
            if (destinationAirportIdNew != null) {
                destinationAirportIdNew = em.getReference(destinationAirportIdNew.getClass(), destinationAirportIdNew.getId());
                route.setDestinationAirportId(destinationAirportIdNew);
            }
            route = em.merge(route);
            if (airlineIdOld != null && !airlineIdOld.equals(airlineIdNew)) {
                airlineIdOld.getRouteList().remove(route);
                airlineIdOld = em.merge(airlineIdOld);
            }
            if (airlineIdNew != null && !airlineIdNew.equals(airlineIdOld)) {
                airlineIdNew.getRouteList().add(route);
                airlineIdNew = em.merge(airlineIdNew);
            }
            if (sourceAirportIdOld != null && !sourceAirportIdOld.equals(sourceAirportIdNew)) {
                sourceAirportIdOld.getSourceList().remove(route);
                sourceAirportIdOld = em.merge(sourceAirportIdOld);
            }
            if (sourceAirportIdNew != null && !sourceAirportIdNew.equals(sourceAirportIdOld)) {
                sourceAirportIdNew.getSourceList().add(route);
                sourceAirportIdNew = em.merge(sourceAirportIdNew);
            }
            if (destinationAirportIdOld != null && !destinationAirportIdOld.equals(destinationAirportIdNew)) {
                destinationAirportIdOld.getSourceList().remove(route);
                destinationAirportIdOld = em.merge(destinationAirportIdOld);
            }
            if (destinationAirportIdNew != null && !destinationAirportIdNew.equals(destinationAirportIdOld)) {
                destinationAirportIdNew.getSourceList().add(route);
                destinationAirportIdNew = em.merge(destinationAirportIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = route.getId();
                if (findRoute(id) == null) {
                    throw new NonexistentEntityException("The route with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Route route;
            try {
                route = em.getReference(Route.class, id);
                route.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The route with id " + id + " no longer exists.", enfe);
            }
            Airline airlineId = route.getAirlineId();
            if (airlineId != null) {
                airlineId.getRouteList().remove(route);
                airlineId = em.merge(airlineId);
            }
            Airport sourceAirportId = route.getSourceAirportId();
            if (sourceAirportId != null) {
                sourceAirportId.getSourceList().remove(route);
                sourceAirportId = em.merge(sourceAirportId);
            }
            Airport destinationAirportId = route.getDestinationAirportId();
            if (destinationAirportId != null) {
                destinationAirportId.getSourceList().remove(route);
                destinationAirportId = em.merge(destinationAirportId);
            }
            em.remove(route);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Route> findRouteEntities() {
        return findRouteEntities(true, -1, -1);
    }

    public List<Route> findRouteEntities(int maxResults, int firstResult) {
        return findRouteEntities(false, maxResults, firstResult);
    }

    private List<Route> findRouteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Route.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Route findRoute(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Route.class, id);
        } finally {
            em.close();
        }
    }

    public int getRouteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Route> rt = cq.from(Route.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
