package io.descoped.support.jpa.test.entity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by oranheim on 11/02/2017.
 */
@RequestScoped
public class EmBean {

    @PersistenceContext(unitName = "PU1")
    private EntityManager em;

    public EntityManager em() {
        return em;
    }
}
