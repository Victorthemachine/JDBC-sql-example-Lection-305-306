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
import javax.persistence.TypedQuery;
import pgclassesandothersmallthings.pgClasses.exceptions.IllegalOrphanException;
import pgclassesandothersmallthings.pgClasses.exceptions.NonexistentEntityException;

/**
 *
 * @author davidmitic
 */
public class AirlineJpaController implements Serializable {

    public AirlineJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Airline airline) {
        if (airline.getRouteList() == null) {
            airline.setRouteList(new ArrayList<Route>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Route> attachedRouteList = new ArrayList<Route>();
            for (Route routeListRouteToAttach : airline.getRouteList()) {
                routeListRouteToAttach = em.getReference(routeListRouteToAttach.getClass(), routeListRouteToAttach.getId());
                attachedRouteList.add(routeListRouteToAttach);
            }
            airline.setRouteList(attachedRouteList);
            em.persist(airline);
            for (Route routeListRoute : airline.getRouteList()) {
                Airline oldAirlineIdOfRouteListRoute = routeListRoute.getAirlineId();
                routeListRoute.setAirlineId(airline);
                routeListRoute = em.merge(routeListRoute);
                if (oldAirlineIdOfRouteListRoute != null) {
                    oldAirlineIdOfRouteListRoute.getRouteList().remove(routeListRoute);
                    oldAirlineIdOfRouteListRoute = em.merge(oldAirlineIdOfRouteListRoute);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Airline airline) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Airline persistentAirline = em.find(Airline.class, airline.getId());
            List<Route> routeListOld = persistentAirline.getRouteList();
            List<Route> routeListNew = airline.getRouteList();
            List<String> illegalOrphanMessages = null;
            for (Route routeListOldRoute : routeListOld) {
                if (!routeListNew.contains(routeListOldRoute)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Route " + routeListOldRoute + " since its airlineId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Route> attachedRouteListNew = new ArrayList<Route>();
            for (Route routeListNewRouteToAttach : routeListNew) {
                routeListNewRouteToAttach = em.getReference(routeListNewRouteToAttach.getClass(), routeListNewRouteToAttach.getId());
                attachedRouteListNew.add(routeListNewRouteToAttach);
            }
            routeListNew = attachedRouteListNew;
            airline.setRouteList(routeListNew);
            airline = em.merge(airline);
            for (Route routeListNewRoute : routeListNew) {
                if (!routeListOld.contains(routeListNewRoute)) {
                    Airline oldAirlineIdOfRouteListNewRoute = routeListNewRoute.getAirlineId();
                    routeListNewRoute.setAirlineId(airline);
                    routeListNewRoute = em.merge(routeListNewRoute);
                    if (oldAirlineIdOfRouteListNewRoute != null && !oldAirlineIdOfRouteListNewRoute.equals(airline)) {
                        oldAirlineIdOfRouteListNewRoute.getRouteList().remove(routeListNewRoute);
                        oldAirlineIdOfRouteListNewRoute = em.merge(oldAirlineIdOfRouteListNewRoute);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = airline.getId();
                if (findAirline(id) == null) {
                    throw new NonexistentEntityException("The airline with id " + id + " no longer exists.");
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
            Airline airline;
            try {
                airline = em.getReference(Airline.class, id);
                airline.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The airline with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Route> routeListOrphanCheck = airline.getRouteList();
            for (Route routeListOrphanCheckRoute : routeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Airline (" + airline + ") cannot be destroyed since the Route " + routeListOrphanCheckRoute + " in its routeList field has a non-nullable airlineId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(airline);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Airline> findAirlineEntities() {
        return findAirlineEntities(true, -1, -1);
    }

    public List<Airline> findAirlineEntities(int maxResults, int firstResult) {
        return findAirlineEntities(false, maxResults, firstResult);
    }

    private List<Airline> findAirlineEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Airline.class));
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

    public Airline findAirline(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Airline.class, id);
        } finally {
            em.close();
        }
    }
    
    public Airline findAirlineByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Airline> q = em.createQuery("SELECT a FROM Airline a WHERE a.name = :name", Airline.class);
            q.setParameter("name", name);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getAirlineCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Airline> rt = cq.from(Airline.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
