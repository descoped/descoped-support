# Descoped Java EE Support modules

## Intro

2017-02-09: Check out: org.jboss.weld.bootstrap.api.Service (instead of custom cdictrl)

Important notice:

* Make sure DeltaSpike Test Control is not on classpath
* Make sure Mock TestUtils in apache-deltaspike.properties is not configured, because that will lead to a RequestScoped context not active issue !!
