package io.descoped.support.jta.jdbc;

import com.arjuna.ats.jdbc.TransactionalDriver;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by oranheim on 13/02/2017.
 */
public class TransactionalDataSource implements DataSource, Referenceable {

    private final TransactionalDriver arjunaJDBC2Driver = new TransactionalDriver();
    private int loginTimeout = 0;
    private String jndiName;
    private String user;
    private String password;


    public TransactionalDataSource() {
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private boolean isEmtpy(String value) {
        return value == null || "".equals(value);
    }

    private String formatDbUrl() {
        StringBuffer url = new StringBuffer();
        url.append(TransactionalDriver.arjunaDriver);
        if (isEmtpy(jndiName)) throw new RuntimeException("A JNDI Name MUST be provided for: " + getClass());
        url.append(jndiName);
        return url.toString();
    }

    @Override
    public Reference getReference() throws NamingException {
        Reference ref = new Reference(getClass().getName());
        ref.add(new StringRefAddr("url", formatDbUrl()));
        if (!isEmtpy(user)) ref.add(new StringRefAddr(TransactionalDriver.userName, user));
        ref.add(new StringRefAddr("loginTimeout", String.valueOf(loginTimeout)));
        return ref;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties dbProps = new Properties();
        if (!isEmtpy(user)) dbProps.setProperty(TransactionalDriver.userName, user);
        if (!isEmtpy(password)) dbProps.setProperty(TransactionalDriver.password, password);
        Connection connection = arjunaJDBC2Driver.connect(formatDbUrl(), dbProps);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        this.user = username;
        this.password = password;
        return getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = seconds;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null; // not supported
    }

}
