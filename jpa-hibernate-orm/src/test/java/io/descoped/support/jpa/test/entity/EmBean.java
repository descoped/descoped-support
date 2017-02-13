package io.descoped.support.jpa.test.entity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;

/**
 * Created by oranheim on 11/02/2017.
 */
@ApplicationScoped
public class EmBean {

//    @PersistenceContext
    private EntityManager em;

}
