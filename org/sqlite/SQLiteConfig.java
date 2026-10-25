/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.sqlite.JDBC;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteConnectionConfig;
import org.sqlite.SQLiteLimits;
import org.sqlite.SQLiteOpenMode;

public class SQLiteConfig {
    public static final String DEFAULT_DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final int DEFAULT_MAX_LENGTH = 1000000000;
    private static final int DEFAULT_MAX_COLUMN = 2000;
    private static final int DEFAULT_MAX_SQL_LENGTH = 1000000;
    private static final int DEFAULT_MAX_FUNCTION_ARG = 100;
    private static final int DEFAULT_MAX_ATTACHED = 10;
    private static final int DEFAULT_MAX_PAGE_COUNT = 0x3FFFFFFF;
    private final Properties pragmaTable;
    private int openModeFlag = 0;
    private int busyTimeout;
    private boolean explicitReadOnly;
    private final SQLiteConnectionConfig defaultConnectionConfig;
    private static final String[] OnOff = new String[]{"true", "false"};
    static final Set<String> pragmaSet = new TreeSet<String>();

    public SQLiteConfig() {
        this(new Properties());
    }

    public SQLiteConfig(Properties object) {
        this.pragmaTable = object;
        object = this.pragmaTable.getProperty(Pragma.OPEN_MODE.pragmaName);
        if (object != null) {
            this.openModeFlag = Integer.parseInt((String)object);
        } else {
            this.setOpenMode(SQLiteOpenMode.READWRITE);
            this.setOpenMode(SQLiteOpenMode.CREATE);
        }
        SQLiteConfig sQLiteConfig = this;
        sQLiteConfig.setSharedCache(Boolean.parseBoolean(sQLiteConfig.pragmaTable.getProperty(Pragma.SHARED_CACHE.pragmaName, "false")));
        this.setOpenMode(SQLiteOpenMode.OPEN_URI);
        SQLiteConfig sQLiteConfig2 = this;
        sQLiteConfig2.setBusyTimeout(Integer.parseInt(sQLiteConfig2.pragmaTable.getProperty(Pragma.BUSY_TIMEOUT.pragmaName, "3000")));
        this.defaultConnectionConfig = SQLiteConnectionConfig.fromPragmaTable(this.pragmaTable);
        this.explicitReadOnly = Boolean.parseBoolean(this.pragmaTable.getProperty(Pragma.JDBC_EXPLICIT_READONLY.pragmaName, "false"));
    }

    public SQLiteConnectionConfig newConnectionConfig() {
        return this.defaultConnectionConfig.copyConfig();
    }

    public Connection createConnection(String string) throws SQLException {
        return JDBC.createConnection(string, this.toProperties());
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void apply(Connection object) throws SQLException {
        Object object2;
        HashSet<String> hashSet = new HashSet<String>();
        for (Pragma pragma : Pragma.values()) {
            hashSet.add(pragma.pragmaName);
        }
        if (object instanceof SQLiteConnection) {
            object2 = (SQLiteConnection)object;
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_ATTACHED, this.parseLimitPragma(Pragma.LIMIT_ATTACHED, 10));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_COLUMN, this.parseLimitPragma(Pragma.LIMIT_COLUMN, 2000));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_COMPOUND_SELECT, this.parseLimitPragma(Pragma.LIMIT_COMPOUND_SELECT, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_EXPR_DEPTH, this.parseLimitPragma(Pragma.LIMIT_EXPR_DEPTH, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_FUNCTION_ARG, this.parseLimitPragma(Pragma.LIMIT_FUNCTION_ARG, 100));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_LENGTH, this.parseLimitPragma(Pragma.LIMIT_LENGTH, 1000000000));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_LIKE_PATTERN_LENGTH, this.parseLimitPragma(Pragma.LIMIT_LIKE_PATTERN_LENGTH, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_SQL_LENGTH, this.parseLimitPragma(Pragma.LIMIT_SQL_LENGTH, 1000000));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_TRIGGER_DEPTH, this.parseLimitPragma(Pragma.LIMIT_TRIGGER_DEPTH, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_VARIABLE_NUMBER, this.parseLimitPragma(Pragma.LIMIT_VARIABLE_NUMBER, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_VDBE_OP, this.parseLimitPragma(Pragma.LIMIT_VDBE_OP, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_WORKER_THREADS, this.parseLimitPragma(Pragma.LIMIT_WORKER_THREADS, -1));
            ((SQLiteConnection)object2).setLimit(SQLiteLimits.SQLITE_LIMIT_PAGE_COUNT, this.parseLimitPragma(Pragma.LIMIT_PAGE_COUNT, 0x3FFFFFFF));
        }
        hashSet.remove(Pragma.OPEN_MODE.pragmaName);
        hashSet.remove(Pragma.SHARED_CACHE.pragmaName);
        hashSet.remove(Pragma.LOAD_EXTENSION.pragmaName);
        hashSet.remove(Pragma.DATE_PRECISION.pragmaName);
        hashSet.remove(Pragma.DATE_CLASS.pragmaName);
        hashSet.remove(Pragma.DATE_STRING_FORMAT.pragmaName);
        hashSet.remove(Pragma.PASSWORD.pragmaName);
        hashSet.remove(Pragma.HEXKEY_MODE.pragmaName);
        hashSet.remove(Pragma.LIMIT_ATTACHED.pragmaName);
        hashSet.remove(Pragma.LIMIT_COLUMN.pragmaName);
        hashSet.remove(Pragma.LIMIT_COMPOUND_SELECT.pragmaName);
        hashSet.remove(Pragma.LIMIT_EXPR_DEPTH.pragmaName);
        hashSet.remove(Pragma.LIMIT_FUNCTION_ARG.pragmaName);
        hashSet.remove(Pragma.LIMIT_LENGTH.pragmaName);
        hashSet.remove(Pragma.LIMIT_LIKE_PATTERN_LENGTH.pragmaName);
        hashSet.remove(Pragma.LIMIT_SQL_LENGTH.pragmaName);
        hashSet.remove(Pragma.LIMIT_TRIGGER_DEPTH.pragmaName);
        hashSet.remove(Pragma.LIMIT_VARIABLE_NUMBER.pragmaName);
        hashSet.remove(Pragma.LIMIT_VDBE_OP.pragmaName);
        hashSet.remove(Pragma.LIMIT_WORKER_THREADS.pragmaName);
        hashSet.remove(Pragma.LIMIT_PAGE_COUNT.pragmaName);
        hashSet.remove(Pragma.JDBC_EXPLICIT_READONLY.pragmaName);
        object2 = object.createStatement();
        try {
            String string;
            if (this.pragmaTable.containsKey(Pragma.PASSWORD.pragmaName) && (string = this.pragmaTable.getProperty(Pragma.PASSWORD.pragmaName)) != null && !string.isEmpty()) {
                void var6_15;
                String string2 = this.pragmaTable.getProperty(Pragma.HEXKEY_MODE.pragmaName);
                if (HexKeyMode.SSE.name().equalsIgnoreCase(string2)) {
                    String string3 = "pragma hexkey = '%s'";
                } else if (HexKeyMode.SQLCIPHER.name().equalsIgnoreCase(string2)) {
                    String string4 = "pragma key = \"x'%s'\"";
                } else {
                    String string5 = "pragma key = '%s'";
                }
                object2.execute(String.format((String)var6_15, string.replace("'", "''")));
                object2.execute("select 1 from sqlite_schema");
            }
            for (Object object3 : this.pragmaTable.keySet()) {
                String string6 = object3.toString();
                if (!hashSet.contains(string6) || (object = this.pragmaTable.getProperty(string6)) == null) continue;
                object2.execute(String.format("pragma %s=%s", string6, object));
            }
            if (object2 == null) return;
        }
        catch (Throwable throwable) {
            if (object2 == null) throw throwable;
            object2.close();
            throw throwable;
        }
        object2.close();
    }

    private void set(Pragma pragma, boolean bl) {
        this.setPragma(pragma, Boolean.toString(bl));
    }

    private void set(Pragma pragma, int n2) {
        this.setPragma(pragma, Integer.toString(n2));
    }

    private boolean getBoolean(Pragma pragma, String string) {
        return Boolean.parseBoolean(this.pragmaTable.getProperty(pragma.pragmaName, string));
    }

    private int parseLimitPragma(Pragma object, int n2) {
        if (!this.pragmaTable.containsKey(object.pragmaName)) {
            return n2;
        }
        object = this.pragmaTable.getProperty(object.pragmaName);
        try {
            return Integer.parseInt((String)object);
        }
        catch (NumberFormatException numberFormatException) {
            return n2;
        }
    }

    public boolean isEnabledSharedCache() {
        return this.getBoolean(Pragma.SHARED_CACHE, "false");
    }

    public boolean isEnabledLoadExtension() {
        return this.getBoolean(Pragma.LOAD_EXTENSION, "false");
    }

    public int getOpenModeFlags() {
        return this.openModeFlag;
    }

    public void setPragma(Pragma pragma, String string) {
        this.pragmaTable.put(pragma.pragmaName, string);
    }

    public Properties toProperties() {
        this.pragmaTable.setProperty(Pragma.OPEN_MODE.pragmaName, Integer.toString(this.openModeFlag));
        this.pragmaTable.setProperty(Pragma.TRANSACTION_MODE.pragmaName, this.defaultConnectionConfig.getTransactionMode().getValue());
        this.pragmaTable.setProperty(Pragma.DATE_CLASS.pragmaName, this.defaultConnectionConfig.getDateClass().getValue());
        this.pragmaTable.setProperty(Pragma.DATE_PRECISION.pragmaName, this.defaultConnectionConfig.getDatePrecision().getValue());
        this.pragmaTable.setProperty(Pragma.DATE_STRING_FORMAT.pragmaName, this.defaultConnectionConfig.getDateStringFormat());
        this.pragmaTable.setProperty(Pragma.JDBC_EXPLICIT_READONLY.pragmaName, this.explicitReadOnly ? "true" : "false");
        return this.pragmaTable;
    }

    static DriverPropertyInfo[] getDriverPropertyInfo() {
        Object[] objectArray = Pragma.values();
        objectArray = new DriverPropertyInfo[objectArray.length];
        int n2 = 0;
        for (Pragma pragma : Pragma.values()) {
            DriverPropertyInfo driverPropertyInfo = new DriverPropertyInfo(pragma.pragmaName, null);
            new DriverPropertyInfo(pragma.pragmaName, null).choices = pragma.choices;
            driverPropertyInfo.description = pragma.description;
            driverPropertyInfo.required = false;
            objectArray[n2++] = driverPropertyInfo;
        }
        return objectArray;
    }

    public boolean isExplicitReadOnly() {
        return this.explicitReadOnly;
    }

    public void setExplicitReadOnly(boolean bl) {
        this.explicitReadOnly = bl;
    }

    public void setOpenMode(SQLiteOpenMode sQLiteOpenMode) {
        this.openModeFlag |= sQLiteOpenMode.flag;
    }

    public void resetOpenMode(SQLiteOpenMode sQLiteOpenMode) {
        this.openModeFlag &= ~sQLiteOpenMode.flag;
    }

    public void setSharedCache(boolean bl) {
        this.set(Pragma.SHARED_CACHE, bl);
    }

    public void enableLoadExtension(boolean bl) {
        this.set(Pragma.LOAD_EXTENSION, bl);
    }

    public void setReadOnly(boolean bl) {
        if (bl) {
            this.setOpenMode(SQLiteOpenMode.READONLY);
            this.resetOpenMode(SQLiteOpenMode.CREATE);
            this.resetOpenMode(SQLiteOpenMode.READWRITE);
            return;
        }
        this.setOpenMode(SQLiteOpenMode.READWRITE);
        this.setOpenMode(SQLiteOpenMode.CREATE);
        this.resetOpenMode(SQLiteOpenMode.READONLY);
    }

    public void setCacheSize(int n2) {
        this.set(Pragma.CACHE_SIZE, n2);
    }

    public void enableCaseSensitiveLike(boolean bl) {
        this.set(Pragma.CASE_SENSITIVE_LIKE, bl);
    }

    @Deprecated
    public void enableCountChanges(boolean bl) {
        this.set(Pragma.COUNT_CHANGES, bl);
    }

    public void setDefaultCacheSize(int n2) {
        this.set(Pragma.DEFAULT_CACHE_SIZE, n2);
    }

    public void deferForeignKeys(boolean bl) {
        this.set(Pragma.DEFER_FOREIGN_KEYS, bl);
    }

    @Deprecated
    public void enableEmptyResultCallBacks(boolean bl) {
        this.set(Pragma.EMPTY_RESULT_CALLBACKS, bl);
    }

    private static String[] toStringArray(PragmaValue[] pragmaValueArray) {
        String[] stringArray = new String[pragmaValueArray.length];
        for (int i2 = 0; i2 < pragmaValueArray.length; ++i2) {
            stringArray[i2] = pragmaValueArray[i2].getValue();
        }
        return stringArray;
    }

    public void setEncoding(Encoding encoding) {
        this.setPragma(Pragma.ENCODING, encoding.typeName);
    }

    public void enforceForeignKeys(boolean bl) {
        this.set(Pragma.FOREIGN_KEYS, bl);
    }

    @Deprecated
    public void enableFullColumnNames(boolean bl) {
        this.set(Pragma.FULL_COLUMN_NAMES, bl);
    }

    public void enableFullSync(boolean bl) {
        this.set(Pragma.FULL_SYNC, bl);
    }

    public void incrementalVacuum(int n2) {
        this.set(Pragma.INCREMENTAL_VACUUM, n2);
    }

    public void setJournalMode(JournalMode journalMode) {
        this.setPragma(Pragma.JOURNAL_MODE, journalMode.name());
    }

    public void setJournalSizeLimit(int n2) {
        this.set(Pragma.JOURNAL_SIZE_LIMIT, n2);
    }

    public void useLegacyFileFormat(boolean bl) {
        this.set(Pragma.LEGACY_FILE_FORMAT, bl);
    }

    public void setLegacyAlterTable(boolean bl) {
        this.set(Pragma.LEGACY_ALTER_TABLE, bl);
    }

    public void setLockingMode(LockingMode lockingMode) {
        this.setPragma(Pragma.LOCKING_MODE, lockingMode.name());
    }

    public void setPageSize(int n2) {
        this.set(Pragma.PAGE_SIZE, n2);
    }

    public void setMaxPageCount(int n2) {
        this.set(Pragma.MAX_PAGE_COUNT, n2);
    }

    public void setReadUncommitted(boolean bl) {
        this.set(Pragma.READ_UNCOMMITTED, bl);
    }

    public void enableRecursiveTriggers(boolean bl) {
        this.set(Pragma.RECURSIVE_TRIGGERS, bl);
    }

    public void enableReverseUnorderedSelects(boolean bl) {
        this.set(Pragma.REVERSE_UNORDERED_SELECTS, bl);
    }

    public void enableShortColumnNames(boolean bl) {
        this.set(Pragma.SHORT_COLUMN_NAMES, bl);
    }

    public void setSynchronous(SynchronousMode synchronousMode) {
        this.setPragma(Pragma.SYNCHRONOUS, synchronousMode.name());
    }

    public void setHexKeyMode(HexKeyMode hexKeyMode) {
        this.setPragma(Pragma.HEXKEY_MODE, hexKeyMode.name());
    }

    public void setTempStore(TempStore tempStore) {
        this.setPragma(Pragma.TEMP_STORE, tempStore.name());
    }

    public void setTempStoreDirectory(String string) {
        this.setPragma(Pragma.TEMP_STORE_DIRECTORY, String.format("'%s'", string));
    }

    public void setUserVersion(int n2) {
        this.set(Pragma.USER_VERSION, n2);
    }

    public void setApplicationId(int n2) {
        this.set(Pragma.APPLICATION_ID, n2);
    }

    public void setTransactionMode(TransactionMode transactionMode) {
        this.defaultConnectionConfig.setTransactionMode(transactionMode);
    }

    public void setTransactionMode(String string) {
        this.setTransactionMode(TransactionMode.getMode(string));
    }

    public TransactionMode getTransactionMode() {
        return this.defaultConnectionConfig.getTransactionMode();
    }

    public void setDatePrecision(String string) {
        this.defaultConnectionConfig.setDatePrecision(DatePrecision.getPrecision(string));
    }

    public void setDateClass(String string) {
        this.defaultConnectionConfig.setDateClass(DateClass.getDateClass(string));
    }

    public void setDateStringFormat(String string) {
        this.defaultConnectionConfig.setDateStringFormat(string);
    }

    public void setBusyTimeout(int n2) {
        this.setPragma(Pragma.BUSY_TIMEOUT, Integer.toString(n2));
        this.busyTimeout = n2;
    }

    public int getBusyTimeout() {
        return this.busyTimeout;
    }

    static /* synthetic */ String[] access$000() {
        return OnOff;
    }

    static /* synthetic */ String[] access$100(PragmaValue[] pragmaValueArray) {
        return SQLiteConfig.toStringArray(pragmaValueArray);
    }

    static {
        for (Pragma pragma : Pragma.values()) {
            pragmaSet.add(pragma.pragmaName);
        }
    }

    public static enum DateClass implements PragmaValue
    {
        INTEGER,
        TEXT,
        REAL;


        @Override
        public final String getValue() {
            return this.name();
        }

        public static DateClass getDateClass(String string) {
            return DateClass.valueOf(string.toUpperCase());
        }
    }

    public static enum DatePrecision implements PragmaValue
    {
        SECONDS,
        MILLISECONDS;


        @Override
        public final String getValue() {
            return this.name();
        }

        public static DatePrecision getPrecision(String string) {
            return DatePrecision.valueOf(string.toUpperCase());
        }
    }

    public static enum TransactionMode implements PragmaValue
    {
        DEFERRED,
        IMMEDIATE,
        EXCLUSIVE;


        @Override
        public final String getValue() {
            return this.name();
        }

        public static TransactionMode getMode(String string) {
            return TransactionMode.valueOf(string.toUpperCase());
        }
    }

    public static enum HexKeyMode implements PragmaValue
    {
        NONE,
        SSE,
        SQLCIPHER;


        @Override
        public final String getValue() {
            return this.name();
        }
    }

    public static enum TempStore implements PragmaValue
    {
        DEFAULT,
        FILE,
        MEMORY;


        @Override
        public final String getValue() {
            return this.name();
        }
    }

    public static enum SynchronousMode implements PragmaValue
    {
        OFF,
        NORMAL,
        FULL;


        @Override
        public final String getValue() {
            return this.name();
        }
    }

    public static enum LockingMode implements PragmaValue
    {
        NORMAL,
        EXCLUSIVE;


        @Override
        public final String getValue() {
            return this.name();
        }
    }

    public static enum JournalMode implements PragmaValue
    {
        DELETE,
        TRUNCATE,
        PERSIST,
        MEMORY,
        WAL,
        OFF;


        @Override
        public final String getValue() {
            return this.name();
        }
    }

    public static enum Encoding implements PragmaValue
    {
        UTF8("'UTF-8'"),
        UTF16("'UTF-16'"),
        UTF16_LITTLE_ENDIAN("'UTF-16le'"),
        UTF16_BIG_ENDIAN("'UTF-16be'"),
        UTF_8(UTF8),
        UTF_16(UTF16),
        UTF_16LE(UTF16_LITTLE_ENDIAN),
        UTF_16BE(UTF16_BIG_ENDIAN);

        public final String typeName;

        private Encoding(String string2) {
            this.typeName = string2;
        }

        private Encoding(Encoding encoding) {
            this.typeName = encoding.getValue();
        }

        @Override
        public final String getValue() {
            return this.typeName;
        }

        public static Encoding getEncoding(String string) {
            return Encoding.valueOf(string.replaceAll("-", "_").toUpperCase());
        }
    }

    static interface PragmaValue {
        public String getValue();
    }

    public static enum Pragma {
        OPEN_MODE("open_mode", "Database open-mode flag", null),
        SHARED_CACHE("shared_cache", "Enable SQLite Shared-Cache mode, native driver only", SQLiteConfig.access$000()),
        LOAD_EXTENSION("enable_load_extension", "Enable SQLite load_extension() function, native driver only", SQLiteConfig.access$000()),
        CACHE_SIZE("cache_size", "Maximum number of database disk pages that SQLite will hold in memory at once per open database file", null),
        MMAP_SIZE("mmap_size", "Maximum number of bytes that are set aside for memory-mapped I/O on a single database", null),
        CASE_SENSITIVE_LIKE("case_sensitive_like", "Installs a new application-defined LIKE function that is either case sensitive or insensitive depending on the value", SQLiteConfig.access$000()),
        COUNT_CHANGES("count_changes", "Deprecated", SQLiteConfig.access$000()),
        DEFAULT_CACHE_SIZE("default_cache_size", "Deprecated", null),
        DEFER_FOREIGN_KEYS("defer_foreign_keys", "When the defer_foreign_keys PRAGMA is on, enforcement of all foreign key constraints is delayed until the outermost transaction is committed. The defer_foreign_keys pragma defaults to OFF so that foreign key constraints are only deferred if they are created as \"DEFERRABLE INITIALLY DEFERRED\". The defer_foreign_keys pragma is automatically switched off at each COMMIT or ROLLBACK. Hence, the defer_foreign_keys pragma must be separately enabled for each transaction. This pragma is only meaningful if foreign key constraints are enabled, of course.", SQLiteConfig.access$000()),
        EMPTY_RESULT_CALLBACKS("empty_result_callback", "Deprecated", SQLiteConfig.access$000()),
        ENCODING("encoding", "Set the encoding that the main database will be created with if it is created by this session", SQLiteConfig.access$100(Encoding.values())),
        FOREIGN_KEYS("foreign_keys", "Set the enforcement of foreign key constraints", SQLiteConfig.access$000()),
        FULL_COLUMN_NAMES("full_column_names", "Deprecated", SQLiteConfig.access$000()),
        FULL_SYNC("fullsync", "Whether or not the F_FULLFSYNC syncing method is used on systems that support it. Only Mac OS X supports F_FULLFSYNC.", SQLiteConfig.access$000()),
        INCREMENTAL_VACUUM("incremental_vacuum", "Causes up to N pages to be removed from the freelist. The database file is truncated by the same amount. The incremental_vacuum pragma has no effect if the database is not in auto_vacuum=incremental mode or if there are no pages on the freelist. If there are fewer than N pages on the freelist, or if N is less than 1, or if the \"(N)\" argument is omitted, then the entire freelist is cleared.", null),
        JOURNAL_MODE("journal_mode", "Set the journal mode for databases associated with the current database connection", SQLiteConfig.access$100(JournalMode.values())),
        JOURNAL_SIZE_LIMIT("journal_size_limit", "Limit the size of rollback-journal and WAL files left in the file-system after transactions or checkpoints", null),
        LEGACY_ALTER_TABLE("legacy_alter_table", "Use legacy alter table behavior", SQLiteConfig.access$000()),
        LEGACY_FILE_FORMAT("legacy_file_format", "No-op", SQLiteConfig.access$000()),
        LOCKING_MODE("locking_mode", "Set the database connection locking-mode", SQLiteConfig.access$100(LockingMode.values())),
        PAGE_SIZE("page_size", "Set the page size of the database. The page size must be a power of two between 512 and 65536 inclusive.", null),
        MAX_PAGE_COUNT("max_page_count", "Set the maximum number of pages in the database file", null),
        READ_UNCOMMITTED("read_uncommitted", "Set READ UNCOMMITTED isolation", SQLiteConfig.access$000()),
        RECURSIVE_TRIGGERS("recursive_triggers", "Set the recursive trigger capability", SQLiteConfig.access$000()),
        REVERSE_UNORDERED_SELECTS("reverse_unordered_selects", "When enabled, this PRAGMA causes many SELECT statements without an ORDER BY clause to emit their results in the reverse order from what they normally would", SQLiteConfig.access$000()),
        SECURE_DELETE("secure_delete", "When secure_delete is on, SQLite overwrites deleted content with zeros", new String[]{"true", "false", "fast"}),
        SHORT_COLUMN_NAMES("short_column_names", "Deprecated", SQLiteConfig.access$000()),
        SYNCHRONOUS("synchronous", "Set the \"synchronous\" flag", SQLiteConfig.access$100(SynchronousMode.values())),
        TEMP_STORE("temp_store", "When temp_store is DEFAULT (0), the compile-time C preprocessor macro SQLITE_TEMP_STORE is used to determine where temporary tables and indices are stored. When temp_store is MEMORY (2) temporary tables and indices are kept as if they were in pure in-memory databases. When temp_store is FILE (1) temporary tables and indices are stored in a file. The temp_store_directory pragma can be used to specify the directory containing temporary files when FILE is specified. When the temp_store setting is changed, all existing temporary tables, indices, triggers, and views are immediately deleted.", SQLiteConfig.access$100(TempStore.values())),
        TEMP_STORE_DIRECTORY("temp_store_directory", "Deprecated", null),
        USER_VERSION("user_version", "Set the value of the user-version integer at offset 60 in the database header. The user-version is an integer that is available to applications to use however they want. SQLite makes no use of the user-version itself.", null),
        APPLICATION_ID("application_id", "Set the 32-bit signed big-endian \"Application ID\" integer located at offset 68 into the database header. Applications that use SQLite as their application file-format should set the Application ID integer to a unique integer so that utilities such as file(1) can determine the specific file type rather than just reporting \"SQLite3 Database\"", null),
        LIMIT_LENGTH("limit_length", "The maximum size of any string or BLOB or table row, in bytes.", null),
        LIMIT_SQL_LENGTH("limit_sql_length", "The maximum length of an SQL statement, in bytes.", null),
        LIMIT_COLUMN("limit_column", "The maximum number of columns in a table definition or in the result set of a SELECT or the maximum number of columns in an index or in an ORDER BY or GROUP BY clause.", null),
        LIMIT_EXPR_DEPTH("limit_expr_depth", "The maximum depth of the parse tree on any expression.", null),
        LIMIT_COMPOUND_SELECT("limit_compound_select", "The maximum number of terms in a compound SELECT statement.", null),
        LIMIT_VDBE_OP("limit_vdbe_op", "The maximum number of instructions in a virtual machine program used to implement an SQL statement. If sqlite3_prepare_v2() or the equivalent tries to allocate space for more than this many opcodes in a single prepared statement, an SQLITE_NOMEM error is returned.", null),
        LIMIT_FUNCTION_ARG("limit_function_arg", "The maximum number of arguments on a function.", null),
        LIMIT_ATTACHED("limit_attached", "The maximum number of attached databases.", null),
        LIMIT_LIKE_PATTERN_LENGTH("limit_like_pattern_length", "The maximum length of the pattern argument to the LIKE or GLOB operators.", null),
        LIMIT_VARIABLE_NUMBER("limit_variable_number", "The maximum index number of any parameter in an SQL statement.", null),
        LIMIT_TRIGGER_DEPTH("limit_trigger_depth", "The maximum depth of recursion for triggers.", null),
        LIMIT_WORKER_THREADS("limit_worker_threads", "The maximum number of auxiliary worker threads that a single prepared statement may start.", null),
        LIMIT_PAGE_COUNT("limit_page_count", "The maximum number of pages allowed in a single database file.", null),
        TRANSACTION_MODE("transaction_mode", "Set the transaction mode", SQLiteConfig.access$100(TransactionMode.values())),
        DATE_PRECISION("date_precision", "\"seconds\": Read and store integer dates as seconds from the Unix Epoch (SQLite standard).\n\"milliseconds\": (DEFAULT) Read and store integer dates as milliseconds from the Unix Epoch (Java standard).", SQLiteConfig.access$100(DatePrecision.values())),
        DATE_CLASS("date_class", "\"integer\": (Default) store dates as number of seconds or milliseconds from the Unix Epoch\n\"text\": store dates as a string of text\n\"real\": store dates as Julian Dates", SQLiteConfig.access$100(DateClass.values())),
        DATE_STRING_FORMAT("date_string_format", "Format to store and retrieve dates stored as text. Defaults to \"yyyy-MM-dd HH:mm:ss.SSS\"", null),
        BUSY_TIMEOUT("busy_timeout", "Sets a busy handler that sleeps for a specified amount of time when a table is locked", null),
        HEXKEY_MODE("hexkey_mode", "Mode of the secret key", SQLiteConfig.access$100(HexKeyMode.values())),
        PASSWORD("password", "Database password", null),
        JDBC_EXPLICIT_READONLY("jdbc.explicit_readonly", "Set explicit read only transactions", null);

        public final String pragmaName;
        public final String[] choices;
        public final String description;

        private Pragma(String string2) {
            this(string2, null);
        }

        private Pragma(String string2, String[] stringArray) {
            this(string2, null, stringArray);
        }

        private Pragma(String string2, String string3, String[] stringArray) {
            this.pragmaName = string2;
            this.description = string3;
            this.choices = stringArray;
        }

        public final String getPragmaName() {
            return this.pragmaName;
        }
    }
}

