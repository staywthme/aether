/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc3;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.CorePreparedStatement;
import org.sqlite.core.DB;
import org.sqlite.jdbc3.JDBC3Connection;

public abstract class JDBC3PreparedStatement
extends CorePreparedStatement {
    public JDBC3PreparedStatement(SQLiteConnection sQLiteConnection, String string) throws SQLException {
        super(sQLiteConnection, string);
    }

    public void clearParameters() throws SQLException {
        this.checkOpen();
        this.pointer.safeRunConsume(DB::clear_bindings);
        if (this.batch != null) {
            for (int i2 = this.batchPos; i2 < this.batchPos + this.paramCount; ++i2) {
                this.batch[i2] = null;
            }
        }
    }

    public boolean execute() throws SQLException {
        this.checkOpen();
        this.rs.close();
        this.pointer.safeRunConsume(DB::reset);
        this.exhaustedResults = false;
        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
        }
        JDBC3PreparedStatement jDBC3PreparedStatement = this;
        return jDBC3PreparedStatement.withConnectionTimeout(() -> {
            boolean bl = false;
            try {
                Object object = this.conn;
                synchronized (object) {
                    JDBC3PreparedStatement jDBC3PreparedStatement = this;
                    this.resultsWaiting = this.conn.getDatabase().execute(jDBC3PreparedStatement, jDBC3PreparedStatement.batch);
                    this.updateGeneratedKeys();
                    bl = true;
                    this.updateCount = this.getDatabase().changes();
                }
                object = 0 != this.columnCount;
                return object;
            }
            catch (Throwable throwable) {
                if (!bl && !this.pointer.isClosed()) {
                    this.pointer.safeRunConsume(DB::reset);
                }
                throw throwable;
            }
        });
    }

    public ResultSet executeQuery() throws SQLException {
        this.checkOpen();
        if (this.columnCount == 0) {
            throw new SQLException("Query does not return results");
        }
        this.rs.close();
        this.pointer.safeRunConsume(DB::reset);
        this.exhaustedResults = false;
        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
        }
        JDBC3PreparedStatement jDBC3PreparedStatement = this;
        return jDBC3PreparedStatement.withConnectionTimeout(() -> {
            try {
                JDBC3PreparedStatement jDBC3PreparedStatement = this;
                this.resultsWaiting = this.conn.getDatabase().execute(jDBC3PreparedStatement, jDBC3PreparedStatement.batch);
            }
            catch (Throwable throwable) {
                if (!this.pointer.isClosed()) {
                    this.pointer.safeRunInt(DB::reset);
                }
                throw throwable;
            }
            return this.getResultSet();
        });
    }

    public int executeUpdate() throws SQLException {
        return (int)this.executeLargeUpdate();
    }

    public long executeLargeUpdate() throws SQLException {
        this.checkOpen();
        if (this.columnCount != 0) {
            throw new SQLException("Query returns results");
        }
        this.rs.close();
        this.pointer.safeRunConsume(DB::reset);
        this.exhaustedResults = false;
        if (this.conn instanceof JDBC3Connection) {
            ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
        }
        JDBC3PreparedStatement jDBC3PreparedStatement = this;
        return jDBC3PreparedStatement.withConnectionTimeout(() -> {
            SQLiteConnection sQLiteConnection = this.conn;
            synchronized (sQLiteConnection) {
                JDBC3PreparedStatement jDBC3PreparedStatement = this;
                long l2 = this.conn.getDatabase().executeUpdate(jDBC3PreparedStatement, jDBC3PreparedStatement.batch);
                this.updateGeneratedKeys();
                return l2;
            }
        });
    }

    public void addBatch() throws SQLException {
        this.checkOpen();
        this.batchPos += this.paramCount;
        ++this.batchQueryCount;
        if (this.batch == null) {
            this.batch = new Object[this.paramCount];
        }
        if (this.batchPos + this.paramCount > this.batch.length) {
            Object[] objectArray = new Object[this.batch.length << 1];
            System.arraycopy(this.batch, 0, objectArray, 0, this.batch.length);
            this.batch = objectArray;
        }
        System.arraycopy(this.batch, this.batchPos - this.paramCount, this.batch, this.batchPos, this.paramCount);
    }

    public ParameterMetaData getParameterMetaData() {
        return (ParameterMetaData)((Object)this);
    }

    public int getParameterCount() throws SQLException {
        this.checkOpen();
        return this.paramCount;
    }

    public String getParameterClassName(int n2) throws SQLException {
        this.checkOpen();
        return "java.lang.String";
    }

    public String getParameterTypeName(int n2) throws SQLException {
        this.checkIndex(n2);
        return JDBCType.valueOf(this.getParameterType(n2)).getName();
    }

    public int getParameterType(int n2) throws SQLException {
        this.checkIndex(n2);
        Object object = this.batch[n2 - 1];
        if (object == null) {
            return 0;
        }
        if (object instanceof Integer || object instanceof Short || object instanceof Boolean) {
            return 4;
        }
        if (object instanceof Long) {
            return -5;
        }
        if (object instanceof Double || object instanceof Float) {
            return 7;
        }
        return 12;
    }

    public int getParameterMode(int n2) {
        return 1;
    }

    public int getPrecision(int n2) {
        return 0;
    }

    public int getScale(int n2) {
        return 0;
    }

    public int isNullable(int n2) {
        return 1;
    }

    public boolean isSigned(int n2) {
        return true;
    }

    public Statement getStatement() {
        return this;
    }

    public void setBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        this.batch(n2, bigDecimal == null ? null : bigDecimal.toString());
    }

    private byte[] readBytes(InputStream inputStream2, int n2) throws SQLException {
        if (n2 < 0) {
            throw new SQLException("Error reading stream. Length should be non-negative");
        }
        byte[] byArray = new byte[n2];
        try {
            int n3;
            for (int i2 = 0; i2 < n2; i2 += n3) {
                n3 = inputStream2.read(byArray, i2, n2 - i2);
                if (n3 != -1) continue;
                throw new IOException("End of stream has been reached");
            }
            return byArray;
        }
        catch (IOException iOException) {
            SQLException sQLException = new SQLException("Error reading stream");
            sQLException.initCause(iOException);
            throw sQLException;
        }
    }

    public void setBinaryStream(int n2, InputStream inputStream2, int n3) throws SQLException {
        if (inputStream2 == null && n3 == 0) {
            this.setBytes(n2, null);
        }
        this.setBytes(n2, this.readBytes(inputStream2, n3));
    }

    public void setAsciiStream(int n2, InputStream inputStream2, int n3) throws SQLException {
        this.setUnicodeStream(n2, inputStream2, n3);
    }

    public void setUnicodeStream(int n2, InputStream object, int n3) throws SQLException {
        if (object == null && n3 == 0) {
            this.setString(n2, null);
        }
        try {
            this.setString(n2, new String(this.readBytes((InputStream)object, n3), "UTF-8"));
            return;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            object = new SQLException("UTF-8 is not supported");
            ((Throwable)object).initCause(unsupportedEncodingException);
            throw object;
        }
    }

    public void setBoolean(int n2, boolean bl) throws SQLException {
        this.setInt(n2, bl ? 1 : 0);
    }

    public void setByte(int n2, byte by) throws SQLException {
        this.setInt(n2, by);
    }

    public void setBytes(int n2, byte[] byArray) throws SQLException {
        this.batch(n2, byArray);
    }

    public void setDouble(int n2, double d2) throws SQLException {
        this.batch(n2, d2);
    }

    public void setFloat(int n2, float f2) throws SQLException {
        this.batch(n2, Float.valueOf(f2));
    }

    public void setInt(int n2, int n3) throws SQLException {
        this.batch(n2, n3);
    }

    public void setLong(int n2, long l2) throws SQLException {
        this.batch(n2, new Long(l2));
    }

    public void setNull(int n2, int n3) throws SQLException {
        this.setNull(n2, n3, null);
    }

    public void setNull(int n2, int n3, String string) throws SQLException {
        this.batch(n2, null);
    }

    public void setObject(int n2, Object object) throws SQLException {
        if (object == null) {
            this.batch(n2, null);
            return;
        }
        if (object instanceof java.util.Date) {
            this.setDateByMilliseconds(n2, ((java.util.Date)object).getTime(), Calendar.getInstance());
            return;
        }
        if (object instanceof Long) {
            this.batch(n2, object);
            return;
        }
        if (object instanceof Integer) {
            this.batch(n2, object);
            return;
        }
        if (object instanceof Short) {
            this.batch(n2, new Integer(((Short)object).intValue()));
            return;
        }
        if (object instanceof Float) {
            this.batch(n2, object);
            return;
        }
        if (object instanceof Double) {
            this.batch(n2, object);
            return;
        }
        if (object instanceof Boolean) {
            this.setBoolean(n2, (Boolean)object);
            return;
        }
        if (object instanceof byte[]) {
            this.batch(n2, object);
            return;
        }
        if (object instanceof BigDecimal) {
            this.setBigDecimal(n2, (BigDecimal)object);
            return;
        }
        this.batch(n2, object.toString());
    }

    public void setObject(int n2, Object object, int n3) throws SQLException {
        this.setObject(n2, object);
    }

    public void setObject(int n2, Object object, int n3, int n4) throws SQLException {
        this.setObject(n2, object);
    }

    public void setShort(int n2, short s2) throws SQLException {
        this.setInt(n2, s2);
    }

    public void setString(int n2, String string) throws SQLException {
        this.batch(n2, string);
    }

    public void setCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        try {
            int n4;
            StringBuffer stringBuffer = new StringBuffer();
            char[] cArray = new char[8192];
            while ((n4 = reader.read(cArray)) > 0) {
                stringBuffer.append(cArray, 0, n4);
            }
            this.setString(n2, stringBuffer.toString());
            return;
        }
        catch (IOException iOException) {
            throw new SQLException("Cannot read from character stream, exception message: " + iOException.getMessage());
        }
    }

    public void setDate(int n2, Date date) throws SQLException {
        this.setDate(n2, date, Calendar.getInstance());
    }

    public void setDate(int n2, Date date, Calendar calendar) throws SQLException {
        if (date == null) {
            this.setObject(n2, null);
            return;
        }
        this.setDateByMilliseconds(n2, date.getTime(), calendar);
    }

    public void setTime(int n2, Time time) throws SQLException {
        this.setTime(n2, time, Calendar.getInstance());
    }

    public void setTime(int n2, Time time, Calendar calendar) throws SQLException {
        if (time == null) {
            this.setObject(n2, null);
            return;
        }
        this.setDateByMilliseconds(n2, time.getTime(), calendar);
    }

    public void setTimestamp(int n2, Timestamp timestamp) throws SQLException {
        this.setTimestamp(n2, timestamp, Calendar.getInstance());
    }

    public void setTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        if (timestamp == null) {
            this.setObject(n2, null);
            return;
        }
        this.setDateByMilliseconds(n2, timestamp.getTime(), calendar);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        this.checkOpen();
        return (ResultSetMetaData)((Object)this.rs);
    }

    @Override
    protected SQLException unsupported() {
        return new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
    }

    protected SQLException invalid() {
        return new SQLException("method cannot be called on a PreparedStatement");
    }

    public void setArray(int n2, Array array) throws SQLException {
        throw this.unsupported();
    }

    public void setBlob(int n2, Blob blob) throws SQLException {
        throw this.unsupported();
    }

    public void setClob(int n2, Clob clob) throws SQLException {
        throw this.unsupported();
    }

    public void setRef(int n2, Ref ref) throws SQLException {
        throw this.unsupported();
    }

    public void setURL(int n2, URL uRL) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public boolean execute(String string) throws SQLException {
        throw this.invalid();
    }

    @Override
    public boolean execute(String string, int n2) throws SQLException {
        throw this.invalid();
    }

    @Override
    public boolean execute(String string, int[] nArray) throws SQLException {
        throw this.invalid();
    }

    @Override
    public boolean execute(String string, String[] stringArray) throws SQLException {
        throw this.invalid();
    }

    @Override
    public int executeUpdate(String string) throws SQLException {
        throw this.invalid();
    }

    @Override
    public int executeUpdate(String string, int n2) throws SQLException {
        throw this.invalid();
    }

    @Override
    public int executeUpdate(String string, int[] nArray) throws SQLException {
        throw this.invalid();
    }

    @Override
    public int executeUpdate(String string, String[] stringArray) throws SQLException {
        throw this.invalid();
    }

    @Override
    public long executeLargeUpdate(String string) throws SQLException {
        throw this.invalid();
    }

    @Override
    public long executeLargeUpdate(String string, int n2) throws SQLException {
        throw this.invalid();
    }

    @Override
    public long executeLargeUpdate(String string, int[] nArray) throws SQLException {
        throw this.invalid();
    }

    @Override
    public long executeLargeUpdate(String string, String[] stringArray) throws SQLException {
        throw this.invalid();
    }

    @Override
    public ResultSet executeQuery(String string) throws SQLException {
        throw this.invalid();
    }

    @Override
    public void addBatch(String string) throws SQLException {
        throw this.invalid();
    }
}

