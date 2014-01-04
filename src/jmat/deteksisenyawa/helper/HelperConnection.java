/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.helper;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import jmat.deteksisenyawa.dao.DaoCiri;
import jmat.deteksisenyawa.dao.DaoRule;
import jmat.deteksisenyawa.dao.DaoSenyawa;
import jmat.deteksisenyawa.dao.impl.DaoImplCiri;
import jmat.deteksisenyawa.dao.impl.DaoImplRule;
import jmat.deteksisenyawa.dao.impl.DaoImplSenyawa;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public final class HelperConnection {

    private static Connection connection;
    private static final Properties pro = new Properties();
    private static final Logger LOGGER = Logger.getLogger(HelperConnection.class);
    private static DaoSenyawa daoSenyawa;
    private static DaoCiri daoCiri;
    private static DaoRule daoRule;

    public static Connection getConnection() {
        if (connection == null) {
            HelperConnection hel = new HelperConnection();
            try {
                pro.load(hel.load());
                String user = pro.getProperty("user");
                String password = pro.getProperty("password");
                String url = pro.getProperty("url");
                MysqlDataSource mysqlDataSource = new MysqlDataSource();
                mysqlDataSource.setURL(url);
                mysqlDataSource.setUser(user);
                mysqlDataSource.setPassword(password);
                connection = mysqlDataSource.getConnection();

            } catch (IOException | SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            } finally {
                if (hel.load() instanceof InputStream) {
                    try {
                        hel.load().close();
                    } catch (IOException ex) {
                        LOGGER.error(ex.getMessage(), ex);
                    }
                }
            }
        }
        return connection;
    }

    public static DaoCiri getDaoCiri() {
        if (daoCiri == null) {
            daoCiri = new DaoImplCiri(getConnection());
        }
        return daoCiri;
    }

    public static DaoSenyawa getDaoSenyawa() {
        if (daoSenyawa == null) {
            daoSenyawa = new DaoImplSenyawa(getConnection());
        }
        return daoSenyawa;
    }

    public static DaoRule getDaoRule() {
        if (daoRule == null) {
            daoRule = new DaoImplRule(getConnection());
        }
        return daoRule;
    }

    public InputStream load() {
        InputStream io = this.getClass().getResourceAsStream("/jmat/deteksisenyawa/helper/MysqlHelper.properties");
        return io;
    }
}
