/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import pgclassesandothersmallthings.pgClasses.exceptions.IllegalOrphanException;
import pgclassesandothersmallthings.pgClasses.exceptions.NonexistentEntityException;

/**
 *
 * @author davidmitic
 */
public class AirportJpaController implements Serializable {

    public AirportJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Airport airport) {
        if (airport.getSourceList() == null) {
            airport.setSourceList(new ArrayList<Route>());
        }
        if (airport.getDestinationList() == null) {
            airport.setDestinationList(new ArrayList<Route>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Route> attachedSourceList = new ArrayList<Route>();
            for (Route sourceListRouteToAttach : airport.getSourceList()) {
                sourceListRouteToAttach = em.getReference(sourceListRouteToAttach.getClass(), sourceListRouteToAttach.getId());
                attachedSourceList.add(sourceListRouteToAttach);
            }
            airport.setSourceList(attachedSourceList);
            List<Route> attachedDestinationList = new ArrayList<Route>();
            for (Route destinationListRouteToAttach : airport.getDestinationList()) {
                destinationListRouteToAttach = em.getReference(destinationListRouteToAttach.getClass(), destinationListRouteToAttach.getId());
                attachedDestinationList.add(destinationListRouteToAttach);
            }
            airport.setDestinationList(attachedDestinationList);
            em.persist(airport);
            for (Route sourceListRoute : airport.getSourceList()) {
                Airport oldSourceAirportIdOfSourceListRoute = sourceListRoute.getSourceAirportId();
                sourceListRoute.setSourceAirportId(airport);
                sourceListRoute = em.merge(sourceListRoute);
                if (oldSourceAirportIdOfSourceListRoute != null) {
                    oldSourceAirportIdOfSourceListRoute.getSourceList().remove(sourceListRoute);
                    oldSourceAirportIdOfSourceListRoute = em.merge(oldSourceAirportIdOfSourceListRoute);
                }
            }
            for (Route destinationListRoute : airport.getDestinationList()) {
                Airport oldDestinationAirportIdOfDestinationListRoute = destinationListRoute.getDestinationAirportId();
                destinationListRoute.setDestinationAirportId(airport);
                destinationListRoute = em.merge(destinationListRoute);
                if (oldDestinationAirportIdOfDestinationListRoute != null) {
                    oldDestinationAirportIdOfDestinationListRoute.getDestinationList().remove(destinationListRoute);
                    oldDestinationAirportIdOfDestinationListRoute = em.merge(oldDestinationAirportIdOfDestinationListRoute);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Airport airport) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Airport persistentAirport = em.find(Airport.class, airport.getId());
            List<Route> sourceListOld = persistentAirport.getSourceList();
            List<Route> sourceListNew = airport.getSourceList();
            List<Route> destinationListOld = persistentAirport.getDestinationList();
            List<Route> destinationListNew = airport.getDestinationList();
            List<String> illegalOrphanMessages = null;
            for (Route sourceListOldRoute : sourceListOld) {
                if (!sourceListNew.contains(sourceListOldRoute)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Route " + sourceListOldRoute + " since its sourceAirportId field is not nullable.");
                }
            }
            for (Route destinationListOldRoute : destinationListOld) {
                if (!destinationListNew.contains(destinationListOldRoute)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Route " + destinationListOldRoute + " since its destinationAirportId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Route> attachedSourceListNew = new ArrayList<Route>();
            for (Route sourceListNewRouteToAttach : sourceListNew) {
                sourceListNewRouteToAttach = em.getReference(sourceListNewRouteToAttach.getClass(), sourceListNewRouteToAttach.getId());
                attachedSourceListNew.add(sourceListNewRouteToAttach);
            }
            sourceListNew = attachedSourceListNew;
            airport.setSourceList(sourceListNew);
            List<Route> attachedDestinationListNew = new ArrayList<Route>();
            for (Route destinationListNewRouteToAttach : destinationListNew) {
                destinationListNewRouteToAttach = em.getReference(destinationListNewRouteToAttach.getClass(), destinationListNewRouteToAttach.getId());
                attachedDestinationListNew.add(destinationListNewRouteToAttach);
            }
            destinationListNew = attachedDestinationListNew;
            airport.setDestinationList(destinationListNew);
            airport = em.merge(airport);
            for (Route sourceListNewRoute : sourceListNew) {
                if (!sourceListOld.contains(sourceListNewRoute)) {
                    Airport oldSourceAirportIdOfSourceListNewRoute = sourceListNewRoute.getSourceAirportId();
                    sourceListNewRoute.setSourceAirportId(airport);
                    sourceListNewRoute = em.merge(sourceListNewRoute);
                    if (oldSourceAirportIdOfSourceListNewRoute != null && !oldSourceAirportIdOfSourceListNewRoute.equals(airport)) {
                        oldSourceAirportIdOfSourceListNewRoute.getSourceList().remove(sourceListNewRoute);
                        oldSourceAirportIdOfSourceListNewRoute = em.merge(oldSourceAirportIdOfSourceListNewRoute);
                    }
                }
            }
            for (Route destinationListNewRoute : destinationListNew) {
                if (!destinationListOld.contains(destinationListNewRoute)) {
                    Airport oldDestinationAirportIdOfDestinationListNewRoute = destinationListNewRoute.getDestinationAirportId();
                    destinationListNewRoute.setDestinationAirportId(airport);
                    destinationListNewRoute = em.merge(destinationListNewRoute);
                    if (oldDestinationAirportIdOfDestinationListNewRoute != null && !oldDestinationAirportIdOfDestinationListNewRoute.equals(airport)) {
                        oldDestinationAirportIdOfDestinationListNewRoute.getDestinationList().remove(destinationListNewRoute);
                        oldDestinationAirportIdOfDestinationListNewRoute = em.merge(oldDestinationAirportIdOfDestinationListNewRoute);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = airport.getId();
                if (findAirport(id) == null) {
                    throw new NonexistentEntityException("The airport with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Airport airport;
            try {
                airport = em.getReference(Airport.class, id);
                airport.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The airport with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Route> sourceListOrphanCheck = airport.getSourceList();
            for (Route sourceListOrphanCheckRoute : sourceListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Airport (" + airport + ") cannot be destroyed since the Route " + sourceListOrphanCheckRoute + " in its sourceList field has a non-nullable sourceAirportId field.");
            }
            List<Route> destinationListOrphanCheck = airport.getDestinationList();
            for (Route destinationListOrphanCheckRoute : destinationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Airport (" + airport + ") cannot be destroyed since the Route " + destinationListOrphanCheckRoute + " in its destinationList field has a non-nullable destinationAirportId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(airport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Airport> findAirportEntities() {
        return findAirportEntities(true, -1, -1);
    }

    public List<Airport> findAirportEntities(int maxResults, int firstResult) {
        return findAirportEntities(false, maxResults, firstResult);
    }

    private List<Airport> findAirportEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Airport.class));
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

    public Airport findAirport(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Airport.class, id);
        } finally {
            em.close();
        }
    }

    public int getAirportCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Airport> rt = cq.from(Airport.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
