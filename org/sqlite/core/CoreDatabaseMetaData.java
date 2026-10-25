/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.core;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import org.sqlite.SQLiteConnection;

public abstract class CoreDatabaseMetaData
implements DatabaseMetaData {
    public SQLiteConnection conn;
    protected PreparedStatement getTables = null;
    public PreparedStatement getTableTypes = null;
    public PreparedStatement getTypeInfo = null;
    public PreparedStatement getCatalogs = null;
    public PreparedStatement getSchemas = null;
    public PreparedStatement getUDTs = null;
    protected PreparedStatement getColumnsTblName = null;
    public PreparedStatement getSuperTypes = null;
    public PreparedStatement getSuperTables = null;
    public PreparedStatement getTablePrivileges = null;
    protected PreparedStatement getIndexInfo = null;
    public PreparedStatement getProcedures = null;
    public PreparedStatement getProcedureColumns = null;
    public PreparedStatement getAttributes = null;
    public PreparedStatement getBestRowIdentifier = null;
    public PreparedStatement getVersionColumns = null;
    public PreparedStatement getColumnPrivileges = null;
    protected static final Pattern PK_UNNAMED_PATTERN = Pattern.compile(".*\\sPRIMARY\\s+KEY\\s+\\((.*?,+.*?)\\).*", 34);
    protected static final Pattern PK_NAMED_PATTERN = Pattern.compile(".*\\sCONSTRAINT\\s+(.*?)\\s+PRIMARY\\s+KEY\\s+\\((.*?)\\).*", 34);

    public CoreDatabaseMetaData(SQLiteConnection sQLiteConnection) {
        this.conn = sQLiteConnection;
    }

    @Deprecated
    public abstract ResultSet getGeneratedKeys() throws SQLException;

    protected void checkOpen() throws SQLException {
        if (this.conn == null) {
            throw new SQLException("connection closed");
        }
    }

    public synchronized void close() throws SQLException {
        if (this.conn == null) {
            return;
        }
        try {
            if (this.getTables != null) {
                this.getTables.close();
            }
            if (this.getTableTypes != null) {
                this.getTableTypes.close();
            }
            if (this.getTypeInfo != null) {
                this.getTypeInfo.close();
            }
            if (this.getCatalogs != null) {
                this.getCatalogs.close();
            }
            if (this.getSchemas != null) {
                this.getSchemas.close();
            }
            if (this.getUDTs != null) {
                this.getUDTs.close();
            }
            if (this.getColumnsTblName != null) {
                this.getColumnsTblName.close();
            }
            if (this.getSuperTypes != null) {
                this.getSuperTypes.close();
            }
            if (this.getSuperTables != null) {
                this.getSuperTables.close();
            }
            if (this.getTablePrivileges != null) {
                this.getTablePrivileges.close();
            }
            if (this.getIndexInfo != null) {
                this.getIndexInfo.close();
            }
            if (this.getProcedures != null) {
                this.getProcedures.close();
            }
            if (this.getProcedureColumns != null) {
                this.getProcedureColumns.close();
            }
            if (this.getAttributes != null) {
                this.getAttributes.close();
            }
            if (this.getBestRowIdentifier != null) {
                this.getBestRowIdentifier.close();
            }
            if (this.getVersionColumns != null) {
                this.getVersionColumns.close();
            }
            if (this.getColumnPrivileges != null) {
                this.getColumnPrivileges.close();
            }
            this.getTables = null;
            this.getTableTypes = null;
            this.getTypeInfo = null;
            this.getCatalogs = null;
            this.getSchemas = null;
            this.getUDTs = null;
            this.getColumnsTblName = null;
            this.getSuperTypes = null;
            this.getSuperTables = null;
            this.getTablePrivileges = null;
            this.getIndexInfo = null;
            this.getProcedures = null;
            this.getProcedureColumns = null;
            this.getAttributes = null;
            this.getBestRowIdentifier = null;
            this.getVersionColumns = null;
            this.getColumnPrivileges = null;
            return;
        }
        finally {
            this.conn = null;
        }
    }

    protected static String quote(String string) {
        if (string == null) {
            return "null";
        }
        return String.format("'%s'", string);
    }

    public String escape(String string) {
        int n2 = string.length();
        StringBuilder stringBuilder = new StringBuilder(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (string.charAt(i2) == '\'') {
                stringBuilder.append('\'');
            }
            stringBuilder.append(string.charAt(i2));
        }
        return stringBuilder.toString();
    }

    protected void finalize() throws Throwable {
        this.close();
    }
}

