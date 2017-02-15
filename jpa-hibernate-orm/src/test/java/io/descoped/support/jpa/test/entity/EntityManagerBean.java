package io.descoped.support.jpa.test.entity;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by oranheim on 11/02/2017.
 */
@RequestScoped
public class EntityManagerBean {

    @PersistenceContext(unitName = "PU1")
    private EntityManager em;

    @Inject
    private Instance<EntityManager> emProxy;

    public EntityManager em() {
        return em;
    }

    public Instance<EntityManager> emProxy() {
        return emProxy;
    }
}
