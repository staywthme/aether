/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executor;
import org.sqlite.SQLiteCommitListener;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.SQLiteLimits;
import org.sqlite.SQLiteUpdateListener;
import org.sqlite.core.CoreDatabaseMetaData;
import org.sqlite.core.DB;
import org.sqlite.core.NativeDB;
import org.sqlite.jdbc4.JDBC4DatabaseMetaData;

public abstract class SQLiteConnection
implements Connection {
    private static final String RESOURCE_NAME_PREFIX = ":resource:";
    private final DB db;
    private CoreDatabaseMetaData meta = null;
    private final SQLiteConnectionConfig connectionConfig;
    private SQLiteConfig.TransactionMode currentTransactionMode;
    private boolean firstStatementExecuted = false;

    public SQLiteConnection(DB dB) {
        this.db = dB;
        this.connectionConfig = dB.getConfig().newConnectionConfig();
    }

    public SQLiteConnection(String string, String string2) throws SQLException {
        this(string, string2, new Properties());
    }

    public SQLiteConnection(String object, String string, Properties properties) throws SQLException {
        DB dB = null;
        try {
            this.db = dB = SQLiteConnection.open((String)object, string, properties);
            object = this.db.getConfig();
            this.connectionConfig = this.db.getConfig().newConnectionConfig();
            ((SQLiteConfig)object).apply(this);
            this.currentTransactionMode = this.getDatabase().getConfig().getTransactionMode();
            this.firstStatementExecuted = false;
            return;
        }
        catch (Throwable throwable) {
            try {
                if (dB != null) {
                    dB.close();
                }
            }
            catch (Exception exception) {
                throwable.addSuppressed(exception);
            }
            throw throwable;
        }
    }

    public SQLiteConfig.TransactionMode getCurrentTransactionMode() {
        return this.currentTransactionMode;
    }

    public void setCurrentTransactionMode(SQLiteConfig.TransactionMode transactionMode) {
        this.currentTransactionMode = transactionMode;
    }

    public void setFirstStatementExecuted(boolean bl) {
        this.firstStatementExecuted = bl;
    }

    public boolean isFirstStatementExecuted() {
        return this.firstStatementExecuted;
    }

    public SQLiteConnectionConfig getConnectionConfig() {
        return this.connectionConfig;
    }

    public CoreDatabaseMetaData getSQLiteDatabaseMetaData() throws SQLException {
        this.checkOpen();
        if (this.meta == null) {
            this.meta = new JDBC4DatabaseMetaData(this);
        }
        return this.meta;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.getSQLiteDatabaseMetaData();
    }

    public String getUrl() {
        return this.db.getUrl();
    }

    @Override
    public void setSchema(String string) throws SQLException {
    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
    }

    @Override
    public void setNetworkTimeout(Executor executor, int n2) throws SQLException {
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    protected void checkCursor(int n2, int n3, int n4) throws SQLException {
        if (n2 != 1003) {
            throw new SQLException("SQLite only supports TYPE_FORWARD_ONLY cursors");
        }
        if (n3 != 1007) {
            throw new SQLException("SQLite only supports CONCUR_READ_ONLY cursors");
        }
        if (n4 != 2) {
            throw new SQLException("SQLite only supports closing cursors at commit");
        }
    }

    protected void setTransactionMode(SQLiteConfig.TransactionMode transactionMode) {
        this.connectionConfig.setTransactionMode(transactionMode);
    }

    @Override
    public int getTransactionIsolation() {
        return this.connectionConfig.getTransactionIsolation();
    }

    @Override
    public void setTransactionIsolation(int n2) throws SQLException {
        this.checkOpen();
        switch (n2) {
            case 2: 
            case 4: 
            case 8: {
                this.getDatabase().exec("PRAGMA read_uncommitted = false;", this.getAutoCommit());
                break;
            }
            case 1: {
                this.getDatabase().exec("PRAGMA read_uncommitted = true;", this.getAutoCommit());
                break;
            }
            default: {
                throw new SQLException("Unsupported transaction isolation level: " + n2 + ". Must be one of TRANSACTION_READ_UNCOMMITTED, TRANSACTION_READ_COMMITTED, TRANSACTION_REPEATABLE_READ, or TRANSACTION_SERIALIZABLE in java.sql.Connection");
            }
        }
        this.connectionConfig.setTransactionIsolation(n2);
    }

    private static DB open(String string, String string2, Properties object) throws SQLException {
        Serializable serializable;
        Object object2 = new Properties();
        ((Properties)object2).putAll((Map<?, ?>)object);
        string2 = SQLiteConnection.extractPragmasFromFilename(string, string2, (Properties)object2);
        object = new SQLiteConfig((Properties)object2);
        if (!(string2.isEmpty() || ":memory:".equals(string2) || string2.startsWith("file:") || string2.contains("mode=memory"))) {
            Object object3;
            if (string2.startsWith(RESOURCE_NAME_PREFIX)) {
                object2 = string2.substring(10);
                object3 = Thread.currentThread().getContextClassLoader();
                serializable = ((ClassLoader)object3).getResource((String)object2);
                if (serializable == null) {
                    try {
                        serializable = new URL((String)object2);
                    }
                    catch (MalformedURLException malformedURLException) {
                        throw new SQLException(String.format("resource %s not found: %s", object2, malformedURLException));
                    }
                }
                try {
                    string2 = SQLiteConnection.extractResource((URL)serializable).getAbsolutePath();
                }
                catch (IOException iOException) {
                    throw new SQLException(String.format("failed to load %s: %s", object2, iOException));
                }
            }
            object2 = new File(string2).getAbsoluteFile();
            object3 = ((File)object2).getParentFile();
            if (object3 != null && !((File)object3).exists()) {
                for (Object object4 = object3; object4 != null && !((File)object4).exists(); object4 = ((File)object4).getParentFile()) {
                    object3 = object4;
                }
                throw new SQLException("path to '" + string2 + "': '" + object3 + "' does not exist");
            }
            try {
                if (!((File)object2).exists() && ((File)object2).createNewFile()) {
                    ((File)object2).delete();
                }
            }
            catch (Exception exception) {
                throw new SQLException("opening db: '" + string2 + "': " + exception.getMessage());
            }
            string2 = ((File)object2).getAbsolutePath();
        }
        try {
            NativeDB.load();
            object2 = new NativeDB(string, string2, (SQLiteConfig)object);
        }
        catch (Exception exception) {
            serializable = new SQLException("Error opening connection");
            serializable.initCause(exception);
            throw serializable;
        }
        ((DB)object2).open(string2, ((SQLiteConfig)object).getOpenModeFlags());
        return object2;
    }

    private static File extractResource(URL uRL) throws IOException {
        if (uRL.getProtocol().equals("file")) {
            try {
                return new File(uRL.toURI());
            }
            catch (URISyntaxException uRISyntaxException) {
                throw new IOException(uRISyntaxException.getMessage());
            }
        }
        Object object = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        String string = String.format("sqlite-jdbc-tmp-%s.db", UUID.randomUUID());
        if (((File)(object = new File((String)object, string))).exists()) {
            long l2;
            long l3 = uRL.openConnection().getLastModified();
            if (l3 < (l2 = ((File)object).lastModified())) {
                return object;
            }
            boolean bl = ((File)object).delete();
            if (!bl) {
                throw new IOException("failed to remove existing DB file: " + ((File)object).getAbsolutePath());
            }
        }
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.setUseCaches(false);
        try (InputStream inputStream2 = uRLConnection.getInputStream();){
            Files.copy(inputStream2, ((File)object).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Object object2 = object;
            return object2;
        }
    }

    public DB getDatabase() {
        return this.db;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        this.checkOpen();
        return this.connectionConfig.isAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean bl) throws SQLException {
        this.checkOpen();
        if (this.connectionConfig.isAutoCommit() == bl) {
            return;
        }
        this.connectionConfig.setAutoCommit(bl);
        if (this.getConnectionConfig().isAutoCommit()) {
            this.db.exec("commit;", bl);
            this.currentTransactionMode = null;
            return;
        }
        this.db.exec(this.transactionPrefix(), bl);
        this.currentTransactionMode = this.getConnectionConfig().getTransactionMode();
    }

    public int getBusyTimeout() {
        return this.db.getConfig().getBusyTimeout();
    }

    public void setBusyTimeout(int n2) throws SQLException {
        this.db.getConfig().setBusyTimeout(n2);
        this.db.busy_timeout(n2);
    }

    public void setLimit(SQLiteLimits sQLiteLimits, int n2) throws SQLException {
        if (n2 >= 0) {
            this.db.limit(sQLiteLimits.getId(), n2);
        }
    }

    public void getLimit(SQLiteLimits sQLiteLimits) throws SQLException {
        this.db.limit(sQLiteLimits.getId(), -1);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.db.isClosed();
    }

    @Override
    public void close() throws SQLException {
        if (this.isClosed()) {
            return;
        }
        if (this.meta != null) {
            this.meta.close();
        }
        this.db.close();
    }

    protected void checkOpen() throws SQLException {
        if (this.isClosed()) {
            throw new SQLException("database connection closed");
        }
    }

    public String libversion() throws SQLException {
        this.checkOpen();
        return this.db.libversion();
    }

    @Override
    public void commit() throws SQLException {
        this.checkOpen();
        if (this.connectionConfig.isAutoCommit()) {
            throw new SQLException("database in auto-commit mode");
        }
        this.db.exec("commit;", this.getAutoCommit());
        this.db.exec(this.transactionPrefix(), this.getAutoCommit());
        this.firstStatementExecuted = false;
        SQLiteConnection sQLiteConnection = this;
        sQLiteConnection.setCurrentTransactionMode(sQLiteConnection.getConnectionConfig().getTransactionMode());
    }

    @Override
    public void rollback() throws SQLException {
        this.checkOpen();
        if (this.connectionConfig.isAutoCommit()) {
            throw new SQLException("database in auto-commit mode");
        }
        this.db.exec("rollback;", this.getAutoCommit());
        this.db.exec(this.transactionPrefix(), this.getAutoCommit());
        this.firstStatementExecuted = false;
        SQLiteConnection sQLiteConnection = this;
        sQLiteConnection.setCurrentTransactionMode(sQLiteConnection.getConnectionConfig().getTransactionMode());
    }

    public void addUpdateListener(SQLiteUpdateListener sQLiteUpdateListener) {
        this.db.addUpdateListener(sQLiteUpdateListener);
    }

    public void removeUpdateListener(SQLiteUpdateListener sQLiteUpdateListener) {
        this.db.removeUpdateListener(sQLiteUpdateListener);
    }

    public void addCommitListener(SQLiteCommitListener sQLiteCommitListener) {
        this.db.addCommitListener(sQLiteCommitListener);
    }

    public void removeCommitListener(SQLiteCommitListener sQLiteCommitListener) {
        this.db.removeCommitListener(sQLiteCommitListener);
    }

    protected static String extractPragmasFromFilename(String string, String stringArray, Properties properties) throws SQLException {
        int n2 = stringArray.indexOf(63);
        if (n2 == -1) {
            return stringArray;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stringArray.substring(0, n2));
        int n3 = 0;
        stringArray = stringArray.substring(n2 + 1).split("&");
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            String string2 = stringArray[stringArray.length - 1 - n2].trim();
            if (string2.isEmpty()) continue;
            String[] stringArray2 = string2.split("=");
            String string3 = stringArray2[0].trim().toLowerCase();
            if (SQLiteConfig.pragmaSet.contains(string3)) {
                if (stringArray2.length == 1) {
                    throw new SQLException(String.format("Please specify a value for PRAGMA %s in URL %s", string3, string));
                }
                string2 = stringArray2[1].trim();
                if (string2.isEmpty() || properties.containsKey(string3)) continue;
                properties.setProperty(string3, string2);
                continue;
            }
            stringBuilder.append(n3 == 0 ? (char)'?' : '&');
            stringBuilder.append(string2);
            ++n3;
        }
        String string4 = stringBuilder.toString();
        return string4;
    }

    protected String transactionPrefix() {
        return this.connectionConfig.transactionPrefix();
    }

    public byte[] serialize(String string) throws SQLException {
        return this.db.serialize(string);
    }

    public void deserialize(String string, byte[] byArray) throws SQLException {
        this.db.deserialize(string, byArray);
    }
}

