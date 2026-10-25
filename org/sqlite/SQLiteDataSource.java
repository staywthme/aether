/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.sqlite.JDBC;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConnection;

public class SQLiteDataSource
implements DataSource {
    private SQLiteConfig config;
    private transient PrintWriter logger;
    private int loginTimeout = 1;
    private String url = "jdbc:sqlite:";
    private String databaseName = "";

    public SQLiteDataSource() {
        this.config = new SQLiteConfig();
    }

    public SQLiteDataSource(SQLiteConfig sQLiteConfig) {
        this.config = sQLiteConfig;
    }

    public void setConfig(SQLiteConfig sQLiteConfig) {
        this.config = sQLiteConfig;
    }

    public SQLiteConfig getConfig() {
        return this.config;
    }

    public void setUrl(String string) {
        this.url = string;
    }

    public String getUrl() {
        return this.url;
    }

    public void setDatabaseName(String string) {
        this.databaseName = string;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setSharedCache(boolean bl) {
        this.config.setSharedCache(bl);
    }

    public void setLoadExtension(boolean bl) {
        this.config.enableLoadExtension(bl);
    }

    public void setReadOnly(boolean bl) {
        this.config.setReadOnly(bl);
    }

    public void setBusyTimeout(int n2) {
        this.config.setBusyTimeout(n2);
    }

    public void setCacheSize(int n2) {
        this.config.setCacheSize(n2);
    }

    public void setCaseSensitiveLike(boolean bl) {
        this.config.enableCaseSensitiveLike(bl);
    }

    public void setCountChanges(boolean bl) {
        this.config.enableCountChanges(bl);
    }

    public void setDefaultCacheSize(int n2) {
        this.config.setDefaultCacheSize(n2);
    }

    public void setEncoding(String string) {
        this.config.setEncoding(SQLiteConfig.Encoding.getEncoding(string));
    }

    public void setEnforceForeignKeys(boolean bl) {
        this.config.enforceForeignKeys(bl);
    }

    public void setFullColumnNames(boolean bl) {
        this.config.enableFullColumnNames(bl);
    }

    public void setFullSync(boolean bl) {
        this.config.enableFullSync(bl);
    }

    public void setIncrementalVacuum(int n2) {
        this.config.incrementalVacuum(n2);
    }

    public void setJournalMode(String string) {
        this.config.setJournalMode(SQLiteConfig.JournalMode.valueOf(string));
    }

    public void setJournalSizeLimit(int n2) {
        this.config.setJournalSizeLimit(n2);
    }

    public void setLegacyFileFormat(boolean bl) {
        this.config.useLegacyFileFormat(bl);
    }

    public void setLegacyAlterTable(boolean bl) {
        this.config.setLegacyAlterTable(bl);
    }

    public void setLockingMode(String string) {
        this.config.setLockingMode(SQLiteConfig.LockingMode.valueOf(string));
    }

    public void setPageSize(int n2) {
        this.config.setPageSize(n2);
    }

    public void setMaxPageCount(int n2) {
        this.config.setMaxPageCount(n2);
    }

    public void setReadUncommitted(boolean bl) {
        this.config.setReadUncommitted(bl);
    }

    public void setRecursiveTriggers(boolean bl) {
        this.config.enableRecursiveTriggers(bl);
    }

    public void setReverseUnorderedSelects(boolean bl) {
        this.config.enableReverseUnorderedSelects(bl);
    }

    public void setShortColumnNames(boolean bl) {
        this.config.enableShortColumnNames(bl);
    }

    public void setSynchronous(String string) {
        this.config.setSynchronous(SQLiteConfig.SynchronousMode.valueOf(string));
    }

    public void setTempStore(String string) {
        this.config.setTempStore(SQLiteConfig.TempStore.valueOf(string));
    }

    public void setTempStoreDirectory(String string) {
        this.config.setTempStoreDirectory(string);
    }

    public void setTransactionMode(String string) {
        this.config.setTransactionMode(string);
    }

    public void setUserVersion(int n2) {
        this.config.setUserVersion(n2);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.getConnection(null, null);
    }

    @Override
    public SQLiteConnection getConnection(String string, String string2) throws SQLException {
        Properties properties = this.config.toProperties();
        if (string != null) {
            properties.put("user", string);
        }
        if (string2 != null) {
            properties.put("pass", string2);
        }
        return JDBC.createConnection(this.url, properties);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.logger;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger");
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        this.logger = printWriter;
    }

    @Override
    public void setLoginTimeout(int n2) throws SQLException {
        this.loginTimeout = n2;
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        return clazz.isInstance(this);
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        return (T)this;
    }
}

