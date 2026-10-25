/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import org.sqlite.core.CoreStatement;
import org.sqlite.jdbc3.JDBC3ResultSet;
import org.sqlite.jdbc3.JDBC3Statement;
import org.sqlite.jdbc4.JDBC4Statement;

public class JDBC4ResultSet
extends JDBC3ResultSet
implements ResultSet,
ResultSetMetaData {
    public JDBC4ResultSet(CoreStatement coreStatement) {
        super(coreStatement);
    }

    @Override
    public void close() throws SQLException {
        boolean bl = this.isOpen();
        super.close();
        if (bl && this.stmt instanceof JDBC4Statement) {
            JDBC4Statement jDBC4Statement = (JDBC4Statement)this.stmt;
            if (jDBC4Statement.closeOnCompletion && !jDBC4Statement.isClosed()) {
                ((JDBC3Statement)jDBC4Statement).close();
            }
        }
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws ClassCastException {
        return clazz.cast(this);
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) {
        return clazz.isInstance(this);
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateRowId(int n2, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateRowId(String string, RowId rowId) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return !this.isOpen();
    }

    @Override
    public void updateNString(int n2, String string) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNString(String string, String string2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNClob(int n2, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNClob(String string, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateSQLXML(String string, SQLXML sQLXML) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public String getNString(int n2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public String getNString(String string) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        String string = this.getString(n2);
        return this.getNCharacterStreamInternal(string);
    }

    private Reader getNCharacterStreamInternal(String object) {
        if (object == null) {
            return null;
        }
        object = new StringReader((String)object);
        return object;
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        string = this.getString(string);
        return this.getNCharacterStreamInternal(string);
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNCharacterStream(String string, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateAsciiStream(String string, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBinaryStream(String string, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateCharacterStream(String string, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBlob(String string, InputStream inputStream2, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateClob(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateClob(String string, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNClob(int n2, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNClob(String string, Reader reader, long l2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNCharacterStream(String string, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateAsciiStream(String string, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBinaryStream(String string, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateCharacterStream(String string, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateBlob(String string, InputStream inputStream2) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateClob(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateClob(String string, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNClob(int n2, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void updateNClob(String string, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        if (clazz == null) {
            throw new SQLException("requested type cannot be null");
        }
        if (clazz == String.class) {
            return clazz.cast(this.getString(n2));
        }
        if (clazz == Boolean.class) {
            return clazz.cast(this.getBoolean(n2));
        }
        if (clazz == BigDecimal.class) {
            return clazz.cast(this.getBigDecimal(n2));
        }
        if (clazz == byte[].class) {
            return clazz.cast(this.getBytes(n2));
        }
        if (clazz == Date.class) {
            return clazz.cast(this.getDate(n2));
        }
        if (clazz == Time.class) {
            return clazz.cast(this.getTime(n2));
        }
        if (clazz == Timestamp.class) {
            return clazz.cast(this.getTimestamp(n2));
        }
        if (clazz == LocalDate.class) {
            block25: {
                try {
                    Date date = this.getDate(n2);
                    if (date == null) break block25;
                    return clazz.cast(date.toLocalDate());
                }
                catch (SQLException sQLException) {
                    return clazz.cast(LocalDate.parse(this.getString(n2)));
                }
            }
            return null;
        }
        if (clazz == LocalTime.class) {
            block26: {
                try {
                    Time time = this.getTime(n2);
                    if (time == null) break block26;
                    return clazz.cast(time.toLocalTime());
                }
                catch (SQLException sQLException) {
                    return clazz.cast(LocalTime.parse(this.getString(n2)));
                }
            }
            return null;
        }
        if (clazz == LocalDateTime.class) {
            block27: {
                try {
                    Timestamp timestamp = this.getTimestamp(n2);
                    if (timestamp == null) break block27;
                    return clazz.cast(timestamp.toLocalDateTime());
                }
                catch (SQLException sQLException) {
                    return clazz.cast(LocalDateTime.parse(this.getString(n2)));
                }
            }
            return null;
        }
        JDBC4ResultSet jDBC4ResultSet = this;
        int n3 = jDBC4ResultSet.safeGetColumnType(jDBC4ResultSet.markCol(n2));
        if (clazz == Double.class) {
            if (n3 == 1 || n3 == 2) {
                return clazz.cast(this.getDouble(n2));
            }
            throw new SQLException("Bad value for type Double");
        }
        if (clazz == Long.class) {
            if (n3 == 1 || n3 == 2) {
                return clazz.cast(this.getLong(n2));
            }
            throw new SQLException("Bad value for type Long");
        }
        if (clazz == Float.class) {
            if (n3 == 1 || n3 == 2) {
                return clazz.cast(Float.valueOf(this.getFloat(n2)));
            }
            throw new SQLException("Bad value for type Float");
        }
        if (clazz == Integer.class) {
            if (n3 == 1 || n3 == 2) {
                return clazz.cast(this.getInt(n2));
            }
            throw new SQLException("Bad value for type Integer");
        }
        throw this.unsupported();
    }

    @Override
    public <T> T getObject(String string, Class<T> clazz) throws SQLException {
        JDBC4ResultSet jDBC4ResultSet = this;
        return jDBC4ResultSet.getObject(jDBC4ResultSet.findColumn(string), clazz);
    }

    protected SQLException unsupported() {
        return new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public Array getArray(String string) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        String string = this.getString(n2);
        return this.getAsciiStreamInternal(string);
    }

    @Override
    public InputStream getAsciiStream(String string) throws SQLException {
        string = this.getString(string);
        return this.getAsciiStreamInternal(string);
    }

    private InputStream getAsciiStreamInternal(String object) {
        if (object == null) {
            return null;
        }
        try {
            object = new ByteArrayInputStream(((String)object).getBytes("ASCII"));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            return null;
        }
        return object;
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        throw this.unsupported();
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String string, int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        String string = this.getString(n2);
        if (string == null) {
            return null;
        }
        return new SqliteClob(string);
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        if ((string = this.getString(string)) == null) {
            return null;
        }
        return new SqliteClob(string);
    }

    public Object getObject(int n2, Map map) throws SQLException {
        throw this.unsupported();
    }

    public Object getObject(String string, Map map) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        return this.getAsciiStream(n2);
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        return this.getAsciiStream(string);
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public URL getURL(String string) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public boolean last() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public boolean previous() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public boolean relative(int n2) throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public boolean absolute(int n2) throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public void afterLast() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public void beforeFirst() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public boolean first() throws SQLException {
        throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void deleteRow() throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateArray(int n2, Array array) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateArray(String string, Array array) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream2, int n3) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateAsciiStream(String string, InputStream inputStream2, int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBigDecimal(String string, BigDecimal bigDecimal) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream2, int n3) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBinaryStream(String string, InputStream inputStream2, int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBlob(int n2, Blob blob) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBlob(String string, Blob blob) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBoolean(int n2, boolean bl) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBoolean(String string, boolean bl) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateByte(int n2, byte by) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateByte(String string, byte by) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBytes(int n2, byte[] byArray) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateBytes(String string, byte[] byArray) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateCharacterStream(String string, Reader reader, int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateClob(int n2, Clob clob) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateClob(String string, Clob clob) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateDate(int n2, Date date) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateDate(String string, Date date) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateDouble(int n2, double d2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateDouble(String string, double d2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateFloat(int n2, float f2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateFloat(String string, float f2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateInt(int n2, int n3) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateInt(String string, int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateLong(int n2, long l2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateLong(String string, long l2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateNull(int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateNull(String string) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateObject(int n2, Object object) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateObject(int n2, Object object, int n3) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateObject(String string, Object object) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateObject(String string, Object object, int n2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateRef(int n2, Ref ref) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateRef(String string, Ref ref) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateRow() throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateShort(int n2, short s2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateShort(String string, short s2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateString(int n2, String string) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateString(String string, String string2) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateTime(int n2, Time time) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateTime(String string, Time time) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateTimestamp(int n2, Timestamp timestamp) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void updateTimestamp(String string, Timestamp timestamp) throws SQLException {
        throw this.unsupported();
    }

    @Override
    public void refreshRow() throws SQLException {
        throw this.unsupported();
    }

    class SqliteClob
    implements NClob {
        private String data;

        protected SqliteClob(String string) {
            this.data = string;
        }

        @Override
        public void free() throws SQLException {
            this.data = null;
        }

        @Override
        public InputStream getAsciiStream() throws SQLException {
            return JDBC4ResultSet.this.getAsciiStreamInternal(this.data);
        }

        @Override
        public Reader getCharacterStream() throws SQLException {
            return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
        }

        @Override
        public Reader getCharacterStream(long l2, long l3) throws SQLException {
            return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
        }

        @Override
        public String getSubString(long l2, int n2) throws SQLException {
            int n3;
            if (this.data == null) {
                throw new SQLException("no data");
            }
            if (l2 < 1L) {
                throw new SQLException("Position must be greater than or equal to 1");
            }
            if (n2 < 0) {
                throw new SQLException("Length must be greater than or equal to 0");
            }
            int n4 = n3 = (int)l2 - 1;
            return this.data.substring(n4, Math.min(n4 + n2, this.data.length()));
        }

        @Override
        public long length() throws SQLException {
            if (this.data == null) {
                throw new SQLException("no data");
            }
            return this.data.length();
        }

        @Override
        public long position(String string, long l2) throws SQLException {
            JDBC4ResultSet.this.unsupported();
            return -1L;
        }

        @Override
        public long position(Clob clob, long l2) throws SQLException {
            JDBC4ResultSet.this.unsupported();
            return -1L;
        }

        @Override
        public OutputStream setAsciiStream(long l2) throws SQLException {
            JDBC4ResultSet.this.unsupported();
            return null;
        }

        @Override
        public Writer setCharacterStream(long l2) throws SQLException {
            JDBC4ResultSet.this.unsupported();
            return null;
        }

        @Override
        public int setString(long l2, String string) throws SQLException {
            JDBC4ResultSet.this.unsupported();
            return -1;
        }

        @Override
        public int setString(long l2, String string, int n2, int n3) throws SQLException {
            JDBC4ResultSet.this.unsupported();
            return -1;
        }

        @Override
        public void truncate(long l2) throws SQLException {
            JDBC4ResultSet.this.unsupported();
        }
    }
}

