/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc3;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.core.CoreResultSet;
import org.sqlite.core.CoreStatement;
import org.sqlite.core.DB;
import org.sqlite.date.FastDateFormat;

public abstract class JDBC3ResultSet
extends CoreResultSet {
    protected static final Pattern COLUMN_TYPENAME = Pattern.compile("([^\\(]*)");
    protected static final Pattern COLUMN_TYPECAST = Pattern.compile("cast\\(.*?\\s+as\\s+(.*?)\\s*\\)");
    protected static final Pattern COLUMN_PRECISION = Pattern.compile(".*?\\((.*?)\\)");

    public JDBC3ResultSet(CoreStatement coreStatement) {
        super(coreStatement);
    }

    public int findColumn(String string) throws SQLException {
        this.checkOpen();
        Integer n2 = this.findColumnIndexInCache(string);
        if (n2 != null) {
            return n2;
        }
        for (int i2 = 0; i2 < this.cols.length; ++i2) {
            if (!string.equalsIgnoreCase(this.cols[i2])) continue;
            return this.addColumnIndexInCache(string, i2 + 1);
        }
        throw new SQLException("no such column: '" + string + "'");
    }

    public boolean next() throws SQLException {
        if (!this.open || this.emptyResultSet || this.pastLastRow) {
            return false;
        }
        this.lastCol = -1;
        if (this.row == 0) {
            ++this.row;
            return true;
        }
        if (this.maxRows != 0L && (long)this.row == this.maxRows) {
            return false;
        }
        int n2 = this.stmt.pointer.safeRunInt(DB::step);
        switch (n2) {
            case 101: {
                this.pastLastRow = true;
                return false;
            }
            case 100: {
                ++this.row;
                return true;
            }
        }
        this.getDatabase().throwex(n2);
        return false;
    }

    public int getType() {
        return 1003;
    }

    public int getFetchSize() {
        return this.limitRows;
    }

    public void setFetchSize(int n2) throws SQLException {
        if (n2 < 0 || this.maxRows != 0L && (long)n2 > this.maxRows) {
            throw new SQLException("fetch size " + n2 + " out of bounds " + this.maxRows);
        }
        this.limitRows = n2;
    }

    public int getFetchDirection() throws SQLException {
        this.checkOpen();
        return 1000;
    }

    public void setFetchDirection(int n2) throws SQLException {
        this.checkOpen();
        if (n2 != 1000) {
            throw new SQLException("only FETCH_FORWARD direction supported");
        }
    }

    public boolean isAfterLast() {
        return this.pastLastRow && !this.emptyResultSet;
    }

    public boolean isBeforeFirst() {
        return !this.emptyResultSet && this.open && this.row == 0;
    }

    public boolean isFirst() {
        return this.row == 1;
    }

    public boolean isLast() throws SQLException {
        throw new SQLFeatureNotSupportedException("not supported by sqlite");
    }

    public int getRow() {
        return this.row;
    }

    public boolean wasNull() throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(this.lastCol)) == 5;
    }

    public BigDecimal getBigDecimal(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.checkCol(n2))) {
            case 5: {
                return null;
            }
            case 1: {
                return BigDecimal.valueOf(this.safeGetLongCol(n2));
            }
        }
        String string = this.safeGetColumnText(n2);
        try {
            return new BigDecimal(string);
        }
        catch (NumberFormatException numberFormatException) {
            throw new SQLException("Bad value for type BigDecimal : " + string);
        }
    }

    public BigDecimal getBigDecimal(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getBigDecimal(jDBC3ResultSet.findColumn(string));
    }

    public boolean getBoolean(int n2) throws SQLException {
        return this.getInt(n2) != 0;
    }

    public boolean getBoolean(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getBoolean(jDBC3ResultSet.findColumn(string));
    }

    public InputStream getBinaryStream(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray != null) {
            return new ByteArrayInputStream(byArray);
        }
        return null;
    }

    public InputStream getBinaryStream(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getBinaryStream(jDBC3ResultSet.findColumn(string));
    }

    public byte getByte(int n2) throws SQLException {
        return (byte)this.getInt(n2);
    }

    public byte getByte(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getByte(jDBC3ResultSet.findColumn(string));
    }

    public byte[] getBytes(int n2) throws SQLException {
        return this.stmt.pointer.safeRun((dB, l2) -> dB.column_blob(l2, this.markCol(n2)));
    }

    public byte[] getBytes(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getBytes(jDBC3ResultSet.findColumn(string));
    }

    public Reader getCharacterStream(int n2) throws SQLException {
        String string = this.getString(n2);
        if (string == null) {
            return null;
        }
        return new StringReader(string);
    }

    public Reader getCharacterStream(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getCharacterStream(jDBC3ResultSet.findColumn(string));
    }

    public Date getDate(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 5: {
                return null;
            }
            case 3: {
                String string = this.safeGetColumnText(n2);
                if ("".equals(string)) {
                    return null;
                }
                try {
                    return new Date(this.getConnectionConfig().getDateFormat().parse(string).getTime());
                }
                catch (Exception exception) {
                    throw new SQLException("Error parsing date", exception);
                }
            }
            case 2: {
                JDBC3ResultSet jDBC3ResultSet2 = this;
                return new Date(jDBC3ResultSet2.julianDateToCalendar(jDBC3ResultSet2.safeGetDoubleCol(n2)).getTimeInMillis());
            }
        }
        return new Date(this.safeGetLongCol(n2) * this.getConnectionConfig().getDateMultiplier());
    }

    public Date getDate(int n2, Calendar cloneable) throws SQLException {
        this.requireCalendarNotNull((Calendar)cloneable);
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 5: {
                return null;
            }
            case 3: {
                String string = this.safeGetColumnText(n2);
                if ("".equals(string)) {
                    return null;
                }
                try {
                    cloneable = FastDateFormat.getInstance(this.getConnectionConfig().getDateStringFormat(), ((Calendar)cloneable).getTimeZone());
                    return new Date(((FastDateFormat)cloneable).parse(string).getTime());
                }
                catch (Exception exception) {
                    throw new SQLException("Error parsing time stamp", exception);
                }
            }
            case 2: {
                JDBC3ResultSet jDBC3ResultSet2 = this;
                return new Date(jDBC3ResultSet2.julianDateToCalendar(jDBC3ResultSet2.safeGetDoubleCol(n2), (Calendar)cloneable).getTimeInMillis());
            }
        }
        ((Calendar)cloneable).setTimeInMillis(this.safeGetLongCol(n2) * this.getConnectionConfig().getDateMultiplier());
        return new Date(((Calendar)cloneable).getTime().getTime());
    }

    public Date getDate(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getDate(jDBC3ResultSet.findColumn(string), Calendar.getInstance());
    }

    public Date getDate(String string, Calendar calendar) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getDate(jDBC3ResultSet.findColumn(string), calendar);
    }

    public double getDouble(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        if (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2)) == 5) {
            return 0.0;
        }
        return this.safeGetDoubleCol(n2);
    }

    public double getDouble(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getDouble(jDBC3ResultSet.findColumn(string));
    }

    public float getFloat(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        if (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2)) == 5) {
            return 0.0f;
        }
        return (float)this.safeGetDoubleCol(n2);
    }

    public float getFloat(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getFloat(jDBC3ResultSet.findColumn(string));
    }

    public int getInt(int n2) throws SQLException {
        return this.stmt.pointer.safeRunInt((dB, l2) -> dB.column_int(l2, this.markCol(n2)));
    }

    public int getInt(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getInt(jDBC3ResultSet.findColumn(string));
    }

    public long getLong(int n2) throws SQLException {
        return this.safeGetLongCol(n2);
    }

    public long getLong(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getLong(jDBC3ResultSet.findColumn(string));
    }

    public short getShort(int n2) throws SQLException {
        return (short)this.getInt(n2);
    }

    public short getShort(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getShort(jDBC3ResultSet.findColumn(string));
    }

    public String getString(int n2) throws SQLException {
        return this.safeGetColumnText(n2);
    }

    public String getString(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getString(jDBC3ResultSet.findColumn(string));
    }

    public Time getTime(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 5: {
                return null;
            }
            case 3: {
                String string = this.safeGetColumnText(n2);
                if ("".equals(string)) {
                    return null;
                }
                try {
                    return new Time(this.getConnectionConfig().getDateFormat().parse(string).getTime());
                }
                catch (Exception exception) {
                    throw new SQLException("Error parsing time", exception);
                }
            }
            case 2: {
                JDBC3ResultSet jDBC3ResultSet2 = this;
                return new Time(jDBC3ResultSet2.julianDateToCalendar(jDBC3ResultSet2.safeGetDoubleCol(n2)).getTimeInMillis());
            }
        }
        return new Time(this.safeGetLongCol(n2) * this.getConnectionConfig().getDateMultiplier());
    }

    public Time getTime(int n2, Calendar cloneable) throws SQLException {
        this.requireCalendarNotNull((Calendar)cloneable);
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 5: {
                return null;
            }
            case 3: {
                String string = this.safeGetColumnText(n2);
                if ("".equals(string)) {
                    return null;
                }
                try {
                    cloneable = FastDateFormat.getInstance(this.getConnectionConfig().getDateStringFormat(), ((Calendar)cloneable).getTimeZone());
                    return new Time(((FastDateFormat)cloneable).parse(string).getTime());
                }
                catch (Exception exception) {
                    throw new SQLException("Error parsing time", exception);
                }
            }
            case 2: {
                JDBC3ResultSet jDBC3ResultSet2 = this;
                return new Time(jDBC3ResultSet2.julianDateToCalendar(jDBC3ResultSet2.safeGetDoubleCol(n2), (Calendar)cloneable).getTimeInMillis());
            }
        }
        ((Calendar)cloneable).setTimeInMillis(this.safeGetLongCol(n2) * this.getConnectionConfig().getDateMultiplier());
        return new Time(((Calendar)cloneable).getTime().getTime());
    }

    public Time getTime(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getTime(jDBC3ResultSet.findColumn(string));
    }

    public Time getTime(String string, Calendar calendar) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getTime(jDBC3ResultSet.findColumn(string), calendar);
    }

    public Timestamp getTimestamp(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 5: {
                return null;
            }
            case 3: {
                String string = this.safeGetColumnText(n2);
                if ("".equals(string)) {
                    return null;
                }
                try {
                    return new Timestamp(this.getConnectionConfig().getDateFormat().parse(string).getTime());
                }
                catch (Exception exception) {
                    throw new SQLException("Error parsing time stamp", exception);
                }
            }
            case 2: {
                JDBC3ResultSet jDBC3ResultSet2 = this;
                return new Timestamp(jDBC3ResultSet2.julianDateToCalendar(jDBC3ResultSet2.safeGetDoubleCol(n2)).getTimeInMillis());
            }
        }
        return new Timestamp(this.safeGetLongCol(n2) * this.getConnectionConfig().getDateMultiplier());
    }

    public Timestamp getTimestamp(int n2, Calendar cloneable) throws SQLException {
        this.requireCalendarNotNull((Calendar)cloneable);
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 5: {
                return null;
            }
            case 3: {
                String string = this.safeGetColumnText(n2);
                if ("".equals(string)) {
                    return null;
                }
                try {
                    cloneable = FastDateFormat.getInstance(this.getConnectionConfig().getDateStringFormat(), ((Calendar)cloneable).getTimeZone());
                    return new Timestamp(((FastDateFormat)cloneable).parse(string).getTime());
                }
                catch (Exception exception) {
                    throw new SQLException("Error parsing time stamp", exception);
                }
            }
            case 2: {
                JDBC3ResultSet jDBC3ResultSet2 = this;
                return new Timestamp(jDBC3ResultSet2.julianDateToCalendar(jDBC3ResultSet2.safeGetDoubleCol(n2)).getTimeInMillis());
            }
        }
        ((Calendar)cloneable).setTimeInMillis(this.safeGetLongCol(n2) * this.getConnectionConfig().getDateMultiplier());
        return new Timestamp(((Calendar)cloneable).getTime().getTime());
    }

    public Timestamp getTimestamp(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getTimestamp(jDBC3ResultSet.findColumn(string));
    }

    public Timestamp getTimestamp(String string, Calendar calendar) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getTimestamp(jDBC3ResultSet.findColumn(string), calendar);
    }

    public Object getObject(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 1: {
                long l2 = this.getLong(n2);
                if (l2 > Integer.MAX_VALUE || l2 < Integer.MIN_VALUE) {
                    return new Long(l2);
                }
                return new Integer((int)l2);
            }
            case 2: {
                return new Double(this.getDouble(n2));
            }
            case 4: {
                return this.getBytes(n2);
            }
            case 5: {
                return null;
            }
        }
        return this.getString(n2);
    }

    public Object getObject(String string) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        return jDBC3ResultSet.getObject(jDBC3ResultSet.findColumn(string));
    }

    public Statement getStatement() {
        return (Statement)((Object)this.stmt);
    }

    public String getCursorName() {
        return null;
    }

    public SQLWarning getWarnings() {
        return null;
    }

    public void clearWarnings() {
    }

    public ResultSetMetaData getMetaData() {
        return (ResultSetMetaData)((Object)this);
    }

    public String getCatalogName(int n2) throws SQLException {
        return this.safeGetColumnTableName(n2);
    }

    public String getColumnClassName(int n2) throws SQLException {
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.markCol(n2))) {
            case 1: {
                long l2 = this.getLong(n2);
                if (l2 > Integer.MAX_VALUE || l2 < Integer.MIN_VALUE) {
                    return "java.lang.Long";
                }
                return "java.lang.Integer";
            }
            case 2: {
                return "java.lang.Double";
            }
            case 4: 
            case 5: {
                return "java.lang.Object";
            }
        }
        return "java.lang.String";
    }

    public int getColumnCount() throws SQLException {
        this.checkCol(1);
        return this.colsMeta.length;
    }

    public int getColumnDisplaySize(int n2) {
        return Integer.MAX_VALUE;
    }

    public String getColumnLabel(int n2) throws SQLException {
        return this.getColumnName(n2);
    }

    public String getColumnName(int n2) throws SQLException {
        return this.safeGetColumnName(n2);
    }

    public int getColumnType(int n2) throws SQLException {
        String string = this.getColumnTypeName(n2);
        JDBC3ResultSet jDBC3ResultSet = this;
        int n3 = jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.checkCol(n2));
        if (n3 == 1 || n3 == 5) {
            if ("BOOLEAN".equals(string)) {
                return 16;
            }
            if ("TINYINT".equals(string)) {
                return -6;
            }
            if ("SMALLINT".equals(string) || "INT2".equals(string)) {
                return 5;
            }
            if ("BIGINT".equals(string) || "INT8".equals(string) || "UNSIGNED BIG INT".equals(string)) {
                return -5;
            }
            if ("DATE".equals(string) || "DATETIME".equals(string)) {
                return 91;
            }
            if ("TIMESTAMP".equals(string)) {
                return 93;
            }
            if (n3 == 1 || "INT".equals(string) || "INTEGER".equals(string) || "MEDIUMINT".equals(string)) {
                long l2 = this.getLong(n2);
                if (l2 > Integer.MAX_VALUE || l2 < Integer.MIN_VALUE) {
                    return -5;
                }
                return 4;
            }
        }
        if (n3 == 2 || n3 == 5) {
            if ("DECIMAL".equals(string)) {
                return 3;
            }
            if ("DOUBLE".equals(string) || "DOUBLE PRECISION".equals(string)) {
                return 8;
            }
            if ("NUMERIC".equals(string)) {
                return 2;
            }
            if ("REAL".equals(string)) {
                return 7;
            }
            if (n3 == 2 || "FLOAT".equals(string)) {
                return 6;
            }
        }
        if (n3 == 3 || n3 == 5) {
            if ("CHARACTER".equals(string) || "NCHAR".equals(string) || "NATIVE CHARACTER".equals(string) || "CHAR".equals(string)) {
                return 1;
            }
            if ("CLOB".equals(string)) {
                return 2005;
            }
            if ("DATE".equals(string) || "DATETIME".equals(string)) {
                return 91;
            }
            if ("TIMESTAMP".equals(string)) {
                return 93;
            }
            if (n3 == 3 || "VARCHAR".equals(string) || "VARYING CHARACTER".equals(string) || "NVARCHAR".equals(string) || "TEXT".equals(string)) {
                return 12;
            }
        }
        if (n3 == 4 || n3 == 5) {
            if ("BINARY".equals(string)) {
                return -2;
            }
            if (n3 == 4 || "BLOB".equals(string)) {
                return 2004;
            }
        }
        return 2;
    }

    public String getColumnTypeName(int n2) throws SQLException {
        String string = this.getColumnDeclType(n2);
        if (string != null) {
            Matcher matcher = COLUMN_TYPENAME.matcher(string);
            matcher.find();
            return matcher.group(1).toUpperCase(Locale.ENGLISH);
        }
        JDBC3ResultSet jDBC3ResultSet = this;
        switch (jDBC3ResultSet.safeGetColumnType(jDBC3ResultSet.checkCol(n2))) {
            case 1: {
                return "INTEGER";
            }
            case 2: {
                return "FLOAT";
            }
            case 4: {
                return "BLOB";
            }
            case 3: {
                return "TEXT";
            }
        }
        return "NUMERIC";
    }

    public int getPrecision(int n2) throws SQLException {
        Object object = this.getColumnDeclType(n2);
        if (object != null) {
            if (((Matcher)(object = COLUMN_PRECISION.matcher((CharSequence)object))).find()) {
                return Integer.parseInt(((Matcher)object).group(1).split(",")[0].trim());
            }
            return 0;
        }
        return 0;
    }

    private String getColumnDeclType(int n2) throws SQLException {
        String string = this.stmt.pointer.safeRun((dB, l2) -> dB.column_decltype(l2, this.checkCol(n2)));
        if (string == null) {
            Matcher matcher = COLUMN_TYPECAST.matcher(this.safeGetColumnName(n2));
            string = matcher.find() ? matcher.group(1) : null;
        }
        return string;
    }

    public int getScale(int n2) throws SQLException {
        Object object = this.getColumnDeclType(n2);
        if (object != null && (object = COLUMN_PRECISION.matcher((CharSequence)object)).find() && ((String[])(object = object.group(1).split(","))).length == 2) {
            return Integer.parseInt(object[1].trim());
        }
        return 0;
    }

    public String getSchemaName(int n2) {
        return "";
    }

    public String getTableName(int n2) throws SQLException {
        String string = this.safeGetColumnTableName(n2);
        if (string == null) {
            return "";
        }
        return string;
    }

    public int isNullable(int n2) throws SQLException {
        this.checkMeta();
        if (this.meta[this.checkCol(n2)][0]) {
            return 0;
        }
        return 1;
    }

    public boolean isAutoIncrement(int n2) throws SQLException {
        this.checkMeta();
        return this.meta[this.checkCol(n2)][2];
    }

    public boolean isCaseSensitive(int n2) {
        return true;
    }

    public boolean isCurrency(int n2) {
        return false;
    }

    public boolean isDefinitelyWritable(int n2) {
        return true;
    }

    public boolean isReadOnly(int n2) {
        return false;
    }

    public boolean isSearchable(int n2) {
        return true;
    }

    public boolean isSigned(int n2) throws SQLException {
        String string = this.getColumnTypeName(n2);
        return "NUMERIC".equals(string) || "INTEGER".equals(string) || "REAL".equals(string);
    }

    public boolean isWritable(int n2) {
        return true;
    }

    public int getConcurrency() {
        return 1007;
    }

    public boolean rowDeleted() {
        return false;
    }

    public boolean rowInserted() {
        return false;
    }

    public boolean rowUpdated() {
        return false;
    }

    private Calendar julianDateToCalendar(Double d2) {
        return this.julianDateToCalendar(d2, Calendar.getInstance());
    }

    private Calendar julianDateToCalendar(Double d2, Calendar calendar) {
        int n2;
        if (d2 == null) {
            return null;
        }
        double d3 = d2 + 0.5;
        int n3 = (int)d3;
        double d4 = d3 - (double)n3;
        if (n3 >= 2299161) {
            n2 = (int)(((double)n3 - 1867216.25) / 36524.25);
            n3 = n3 + 1 + n2 - (int)((double)n2 / 4.0);
        }
        n2 = n3 + 1524;
        n3 = (int)(((double)n2 - 122.1) / 365.25);
        int n4 = (int)(365.25 * (double)n3);
        int n5 = (int)((double)(n2 - n4) / 30.6001);
        int n6 = n5 - ((double)n5 < 13.5 ? 1 : 13);
        n3 -= (double)n6 > 2.5 ? 4716 : 4715;
        double d5 = (double)(n2 - n4 - (int)(30.6001 * (double)n5)) + d4;
        n2 = (int)d5;
        double d6 = d5 - (double)n2;
        n4 = (int)(d6 * 24.0);
        double d7 = d6 * 24.0 - (double)n4;
        n5 = (int)(d7 * 60.0);
        double d8 = d7 * 60.0 - (double)n5;
        int n7 = (int)(d8 * 60.0);
        double d9 = d8 * 60.0 - (double)n7;
        int n8 = (int)(d9 * 1000.0);
        calendar.set(n3, n6 - 1, n2, n4, n5, n7);
        calendar.set(14, n8);
        if (n3 <= 0) {
            calendar.set(0, 0);
            calendar.set(1, -(n3 - 1));
        }
        return calendar;
    }

    private void requireCalendarNotNull(Calendar calendar) throws SQLException {
        if (calendar == null) {
            throw new SQLException("Expected a calendar instance.", new IllegalArgumentException());
        }
    }

    protected int safeGetColumnType(int n2) throws SQLException {
        return this.stmt.pointer.safeRunInt((dB, l2) -> dB.column_type(l2, n2));
    }

    private long safeGetLongCol(int n2) throws SQLException {
        return this.stmt.pointer.safeRunLong((dB, l2) -> dB.column_long(l2, this.markCol(n2)));
    }

    private double safeGetDoubleCol(int n2) throws SQLException {
        return this.stmt.pointer.safeRunDouble((dB, l2) -> dB.column_double(l2, this.markCol(n2)));
    }

    private String safeGetColumnText(int n2) throws SQLException {
        return this.stmt.pointer.safeRun((dB, l2) -> dB.column_text(l2, this.markCol(n2)));
    }

    private String safeGetColumnTableName(int n2) throws SQLException {
        return this.stmt.pointer.safeRun((dB, l2) -> dB.column_table_name(l2, this.checkCol(n2)));
    }

    private String safeGetColumnName(int n2) throws SQLException {
        return this.stmt.pointer.safeRun((dB, l2) -> dB.column_name(l2, this.checkCol(n2)));
    }
}

