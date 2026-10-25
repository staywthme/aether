/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteJDBCLoader;
import org.sqlite.jdbc4.JDBC4Connection;

public class JDBC
implements Driver {
    private static final Logger logger = LoggerFactory.getLogger(JDBC.class);
    public static final String PREFIX = "jdbc:sqlite:";

    @Override
    public int getMajorVersion() {
        return SQLiteJDBCLoader.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return SQLiteJDBCLoader.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public boolean acceptsURL(String string) {
        return JDBC.isValidURL(string);
    }

    public static boolean isValidURL(String string) {
        return string != null && string.toLowerCase().startsWith(PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String string, Properties properties) throws SQLException {
        return SQLiteConfig.getDriverPropertyInfo();
    }

    @Override
    public Connection connect(String string, Properties properties) throws SQLException {
        return JDBC.createConnection(string, properties);
    }

    static String extractAddress(String string) {
        return string.substring(12);
    }

    public static SQLiteConnection createConnection(String string, Properties properties) throws SQLException {
        if (!JDBC.isValidURL(string)) {
            return null;
        }
        String string2 = string = string.trim();
        return new JDBC4Connection(string2, JDBC.extractAddress(string2), properties);
    }

    static {
        try {
            DriverManager.registerDriver(new JDBC());
        }
        catch (SQLException sQLException) {
            logger.error("Could not register driver", sQLException);
        }
    }
}

