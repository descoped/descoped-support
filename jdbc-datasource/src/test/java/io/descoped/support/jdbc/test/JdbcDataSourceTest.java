package io.descoped.support.jdbc.test;

import io.descoped.container.exception.DescopedServerException;
import io.descoped.testutils.junit4.runner.DescopedTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertNotNull;

/**
 * Created by oranheim on 09/02/2017.
 */
@RunWith(DescopedTestRunner.class)
public class JdbcDataSourceTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcDataSourceTest.class);

    @Inject
    UserTransaction utx;

    @Resource(mappedName = "em1")
    DataSource testDataSource;

    @Resource(mappedName = "em3")
    DataSource txDataSource;

    @Test
    public void testDataSource() throws Exception {
        assertNotNull(testDataSource);
        assertNotNull(txDataSource);

        Connection conn = txDataSource.getConnection();
//        Connection conn = testDataSource.getConnection();
        conn.setAutoCommit(false);


//        utx.begin();
        try {
            PreparedStatement stmt = conn.prepareStatement("CREATE TABLE test_table (a INTEGER,b INTEGER)");
            stmt.execute();

            conn.createStatement().executeUpdate("INSERT INTO test_table (a, b) VALUES (1,2)");
            conn.createStatement().executeUpdate("INSERT INTO test_table (a, b) VALUES (3,4)");
            for(int n = 5; n < 500; n++) {
                int m = n + 1;
                conn.createStatement().executeUpdate(String.format("INSERT INTO test_table (a, b) VALUES (%s,%s)", n, m));
                n++;
            }

            if (true) throw new DescopedServerException("Something went wrong");
            conn.commit();
//            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            utx.rollback();
            conn.rollback();
        }

        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM test_table");

        log.trace("{}\t{}", rs.getMetaData().getColumnLabel(1), rs.getMetaData().getColumnLabel(2));
        log.trace("--\t--");
        while (rs.next()) {
            log.trace("{}\t{}", rs.getString(1), rs.getString(2));
        }

    }

}
