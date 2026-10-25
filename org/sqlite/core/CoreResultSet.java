/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.core;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.core.Codes;
import org.sqlite.core.CoreStatement;
import org.sqlite.core.DB;

public abstract class CoreResultSet
implements Codes {
    public final CoreStatement stmt;
    public boolean emptyResultSet = false;
    public boolean open = false;
    public long maxRows;
    public String[] cols = null;
    public String[] colsMeta = null;
    public boolean[][] meta = null;
    public int limitRows;
    public int row = 0;
    public boolean pastLastRow = false;
    public int lastCol;
    public boolean closeStmt;
    protected Map<String, Integer> columnNameToIndex = null;

    public CoreResultSet(CoreStatement coreStatement) {
        this.stmt = coreStatement;
    }

    protected DB getDatabase() {
        return this.stmt.getDatabase();
    }

    protected SQLiteConnectionConfig getConnectionConfig() {
        return this.stmt.getConnectionConfig();
    }

    public boolean isOpen() {
        return this.open;
    }

    protected void checkOpen() throws SQLException {
        if (!this.open) {
            throw new SQLException("ResultSet closed");
        }
    }

    public int checkCol(int n2) throws SQLException {
        if (this.colsMeta == null) {
            throw new SQLException("SQLite JDBC: inconsistent internal state");
        }
        if (n2 <= 0 || n2 > this.colsMeta.length) {
            throw new SQLException("column " + n2 + " out of bounds [1," + this.colsMeta.length + "]");
        }
        return --n2;
    }

    protected int markCol(int n2) throws SQLException {
        this.checkCol(n2);
        this.lastCol = n2--;
        return n2;
    }

    public void checkMeta() throws SQLException {
        this.checkCol(1);
        if (this.meta == null) {
            this.meta = this.stmt.pointer.safeRun(DB::column_metadata);
        }
    }

    public void close() throws SQLException {
        this.cols = null;
        this.colsMeta = null;
        this.meta = null;
        this.limitRows = 0;
        this.row = 0;
        this.pastLastRow = false;
        this.lastCol = -1;
        this.columnNameToIndex = null;
        this.emptyResultSet = false;
        if (this.stmt.pointer.isClosed() || !this.open && !this.closeStmt) {
            return;
        }
        DB dB = this.stmt.getDatabase();
        DB dB2 = dB;
        dB2 = dB;
        synchronized (dB) {
            if (!this.stmt.pointer.isClosed()) {
                this.stmt.pointer.safeRunInt(DB::reset);
                if (this.closeStmt) {
                    this.closeStmt = false;
                    ((Statement)((Object)this.stmt)).close();
                }
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            this.open = false;
            return;
        }
    }

    protected Integer findColumnIndexInCache(String string) {
        if (this.columnNameToIndex == null) {
            return null;
        }
        return this.columnNameToIndex.get(string);
    }

    protected int addColumnIndexInCache(String string, int n2) {
        if (this.columnNameToIndex == null) {
            this.columnNameToIndex = new HashMap<String, Integer>(this.cols.length);
        }
        this.columnNameToIndex.put(string, n2);
        return n2;
    }
}

