/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc3;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConnection;
import org.sqlite.core.CoreDatabaseMetaData;
import org.sqlite.core.CoreStatement;
import org.sqlite.util.QueryUtils;
import org.sqlite.util.StringUtils;

public abstract class JDBC3DatabaseMetaData
extends CoreDatabaseMetaData {
    private static String driverName;
    private static String driverVersion;
    protected static final Pattern TYPE_INTEGER;
    protected static final Pattern TYPE_VARCHAR;
    protected static final Pattern TYPE_FLOAT;
    private static final Map<String, Integer> RULE_MAP;
    protected static final Pattern PK_UNNAMED_PATTERN;
    protected static final Pattern PK_NAMED_PATTERN;

    public JDBC3DatabaseMetaData(SQLiteConnection sQLiteConnection) {
        super(sQLiteConnection);
    }

    @Override
    public Connection getConnection() {
        return this.conn;
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        return Integer.parseInt(this.conn.libversion().split("\\.")[0]);
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        return Integer.parseInt(this.conn.libversion().split("\\.")[1]);
    }

    @Override
    public int getDriverMajorVersion() {
        return Integer.parseInt(driverVersion.split("\\.")[0]);
    }

    @Override
    public int getDriverMinorVersion() {
        return Integer.parseInt(driverVersion.split("\\.")[1]);
    }

    @Override
    public int getJDBCMajorVersion() {
        return 4;
    }

    @Override
    public int getJDBCMinorVersion() {
        return 2;
    }

    @Override
    public int getDefaultTransactionIsolation() {
        return 8;
    }

    @Override
    public int getMaxBinaryLiteralLength() {
        return 0;
    }

    @Override
    public int getMaxCatalogNameLength() {
        return 0;
    }

    @Override
    public int getMaxCharLiteralLength() {
        return 0;
    }

    @Override
    public int getMaxColumnNameLength() {
        return 0;
    }

    @Override
    public int getMaxColumnsInGroupBy() {
        return 0;
    }

    @Override
    public int getMaxColumnsInIndex() {
        return 0;
    }

    @Override
    public int getMaxColumnsInOrderBy() {
        return 0;
    }

    @Override
    public int getMaxColumnsInSelect() {
        return 0;
    }

    @Override
    public int getMaxColumnsInTable() {
        return 0;
    }

    @Override
    public int getMaxConnections() {
        return 0;
    }

    @Override
    public int getMaxCursorNameLength() {
        return 0;
    }

    @Override
    public int getMaxIndexLength() {
        return 0;
    }

    @Override
    public int getMaxProcedureNameLength() {
        return 0;
    }

    @Override
    public int getMaxRowSize() {
        return 0;
    }

    @Override
    public int getMaxSchemaNameLength() {
        return 0;
    }

    @Override
    public int getMaxStatementLength() {
        return 0;
    }

    @Override
    public int getMaxStatements() {
        return 0;
    }

    @Override
    public int getMaxTableNameLength() {
        return 0;
    }

    @Override
    public int getMaxTablesInSelect() {
        return 0;
    }

    @Override
    public int getMaxUserNameLength() {
        return 0;
    }

    @Override
    public int getResultSetHoldability() {
        return 2;
    }

    @Override
    public int getSQLStateType() {
        return 2;
    }

    @Override
    public String getDatabaseProductName() {
        return "SQLite";
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        return this.conn.libversion();
    }

    @Override
    public String getDriverName() {
        return driverName;
    }

    @Override
    public String getDriverVersion() {
        return driverVersion;
    }

    @Override
    public String getExtraNameCharacters() {
        return "";
    }

    @Override
    public String getCatalogSeparator() {
        return ".";
    }

    @Override
    public String getCatalogTerm() {
        return "catalog";
    }

    @Override
    public String getSchemaTerm() {
        return "schema";
    }

    @Override
    public String getProcedureTerm() {
        return "not_implemented";
    }

    @Override
    public String getSearchStringEscape() {
        return "\\";
    }

    @Override
    public String getIdentifierQuoteString() {
        return "\"";
    }

    @Override
    public String getSQLKeywords() {
        return "ABORT,ACTION,AFTER,ANALYZE,ATTACH,AUTOINCREMENT,BEFORE,CASCADE,CONFLICT,DATABASE,DEFERRABLE,DEFERRED,DESC,DETACH,EXCLUSIVE,EXPLAIN,FAIL,GLOB,IGNORE,INDEX,INDEXED,INITIALLY,INSTEAD,ISNULL,KEY,LIMIT,NOTNULL,OFFSET,PLAN,PRAGMA,QUERY,RAISE,REGEXP,REINDEX,RENAME,REPLACE,RESTRICT,TEMP,TEMPORARY,TRANSACTION,VACUUM,VIEW,VIRTUAL";
    }

    @Override
    public String getNumericFunctions() {
        return "";
    }

    @Override
    public String getStringFunctions() {
        return "";
    }

    @Override
    public String getSystemFunctions() {
        return "";
    }

    @Override
    public String getTimeDateFunctions() {
        return "DATE,TIME,DATETIME,JULIANDAY,STRFTIME";
    }

    @Override
    public String getURL() {
        return this.conn.getUrl();
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public boolean allProceduresAreCallable() {
        return false;
    }

    @Override
    public boolean allTablesAreSelectable() {
        return true;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() {
        return false;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() {
        return false;
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() {
        return false;
    }

    @Override
    public boolean deletesAreDetected(int n2) {
        return false;
    }

    @Override
    public boolean insertsAreDetected(int n2) {
        return false;
    }

    @Override
    public boolean isCatalogAtStart() {
        return true;
    }

    @Override
    public boolean locatorsUpdateCopy() {
        return false;
    }

    @Override
    public boolean nullPlusNonNullIsNull() {
        return true;
    }

    @Override
    public boolean nullsAreSortedAtEnd() {
        return !this.nullsAreSortedAtStart();
    }

    @Override
    public boolean nullsAreSortedAtStart() {
        return true;
    }

    @Override
    public boolean nullsAreSortedHigh() {
        return true;
    }

    @Override
    public boolean nullsAreSortedLow() {
        return !this.nullsAreSortedHigh();
    }

    @Override
    public boolean othersDeletesAreVisible(int n2) {
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int n2) {
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int n2) {
        return false;
    }

    @Override
    public boolean ownDeletesAreVisible(int n2) {
        return false;
    }

    @Override
    public boolean ownInsertsAreVisible(int n2) {
        return false;
    }

    @Override
    public boolean ownUpdatesAreVisible(int n2) {
        return false;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() {
        return false;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() {
        return true;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() {
        return false;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() {
        return false;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() {
        return false;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() {
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() {
        return false;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() {
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() {
        return true;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() {
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() {
        return false;
    }

    @Override
    public boolean supportsColumnAliasing() {
        return true;
    }

    @Override
    public boolean supportsConvert() {
        return false;
    }

    @Override
    public boolean supportsConvert(int n2, int n3) {
        return false;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() {
        return false;
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() {
        return true;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() {
        return false;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() {
        return false;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() {
        return true;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() {
        return true;
    }

    @Override
    public boolean supportsCoreSQLGrammar() {
        return true;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() {
        return false;
    }

    @Override
    public boolean supportsLimitedOuterJoins() {
        return true;
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        String[] stringArray = this.conn.libversion().split("\\.");
        return Integer.parseInt(stringArray[0]) >= 3 && Integer.parseInt(stringArray[1]) >= 39;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override
    public boolean supportsGroupBy() {
        return true;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() {
        return false;
    }

    @Override
    public boolean supportsGroupByUnrelated() {
        return false;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() {
        return false;
    }

    @Override
    public boolean supportsLikeEscapeClause() {
        return false;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() {
        return true;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() {
        return false;
    }

    @Override
    public boolean supportsMultipleOpenResults() {
        return false;
    }

    @Override
    public boolean supportsMultipleResultSets() {
        return false;
    }

    @Override
    public boolean supportsMultipleTransactions() {
        return true;
    }

    @Override
    public boolean supportsNamedParameters() {
        return true;
    }

    @Override
    public boolean supportsNonNullableColumns() {
        return true;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() {
        return false;
    }

    @Override
    public boolean supportsOrderByUnrelated() {
        return false;
    }

    @Override
    public boolean supportsOuterJoins() {
        return true;
    }

    @Override
    public boolean supportsPositionedDelete() {
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() {
        return false;
    }

    @Override
    public boolean supportsResultSetConcurrency(int n2, int n3) {
        return n2 == 1003 && n3 == 1007;
    }

    @Override
    public boolean supportsResultSetHoldability(int n2) {
        return n2 == 2;
    }

    @Override
    public boolean supportsResultSetType(int n2) {
        return n2 == 1003;
    }

    @Override
    public boolean supportsSavepoints() {
        return true;
    }

    @Override
    public boolean supportsSchemasInDataManipulation() {
        return false;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() {
        return false;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() {
        return false;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() {
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() {
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() {
        return false;
    }

    @Override
    public boolean supportsStatementPooling() {
        return false;
    }

    @Override
    public boolean supportsStoredProcedures() {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() {
        return false;
    }

    @Override
    public boolean supportsSubqueriesInExists() {
        return true;
    }

    @Override
    public boolean supportsSubqueriesInIns() {
        return true;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() {
        return false;
    }

    @Override
    public boolean supportsTableCorrelationNames() {
        return false;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int n2) {
        return n2 == 8;
    }

    @Override
    public boolean supportsTransactions() {
        return true;
    }

    @Override
    public boolean supportsUnion() {
        return true;
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    @Override
    public boolean updatesAreDetected(int n2) {
        return false;
    }

    @Override
    public boolean usesLocalFilePerTable() {
        return false;
    }

    @Override
    public boolean usesLocalFiles() {
        return true;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.conn.isReadOnly();
    }

    @Override
    public ResultSet getAttributes(String string, String string2, String string3, String string4) throws SQLException {
        if (this.getAttributes == null) {
            this.getAttributes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as ATTR_NAME, null as DATA_TYPE, null as ATTR_TYPE_NAME, null as ATTR_SIZE, null as DECIMAL_DIGITS, null as NUM_PREC_RADIX, null as NULLABLE, null as REMARKS, null as ATTR_DEF, null as SQL_DATA_TYPE, null as SQL_DATETIME_SUB, null as CHAR_OCTET_LENGTH, null as ORDINAL_POSITION, null as IS_NULLABLE, null as SCOPE_CATALOG, null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE limit 0;");
        }
        return this.getAttributes.executeQuery();
    }

    @Override
    public ResultSet getBestRowIdentifier(String string, String string2, String string3, int n2, boolean bl) throws SQLException {
        if (this.getBestRowIdentifier == null) {
            this.getBestRowIdentifier = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
        }
        return this.getBestRowIdentifier.executeQuery();
    }

    @Override
    public ResultSet getColumnPrivileges(String string, String string2, String string3, String string4) throws SQLException {
        if (this.getColumnPrivileges == null) {
            this.getColumnPrivileges = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as COLUMN_NAME, null as GRANTOR, null as GRANTEE, null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
        }
        return this.getColumnPrivileges.executeQuery();
    }

    @Override
    public ResultSet getColumns(String object, String string, String string2, String string3) throws SQLException {
        this.checkOpen();
        StringBuilder stringBuilder = new StringBuilder(700);
        stringBuilder.append("select null as TABLE_CAT, null as TABLE_SCHEM, tblname as TABLE_NAME, cn as COLUMN_NAME, ct as DATA_TYPE, tn as TYPE_NAME, colSize as COLUMN_SIZE, ").append("2000000000 as BUFFER_LENGTH, colDecimalDigits as DECIMAL_DIGITS, 10   as NUM_PREC_RADIX, colnullable as NULLABLE, null as REMARKS, colDefault as COLUMN_DEF, ").append("0    as SQL_DATA_TYPE, 0    as SQL_DATETIME_SUB, 2000000000 as CHAR_OCTET_LENGTH, ordpos as ORDINAL_POSITION, (case colnullable when 0 then 'NO' when 1 then 'YES' else '' end)").append("    as IS_NULLABLE, null as SCOPE_CATALOG, null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE, ").append("(case colautoincrement when 0 then 'NO' when 1 then 'YES' else '' end) as IS_AUTOINCREMENT, (case colgenerated when 0 then 'NO' when 1 then 'YES' else '' end) as IS_GENERATEDCOLUMN from (");
        boolean bl = false;
        ResultSet resultSet = null;
        try {
            resultSet = this.getTables((String)object, string, string2, null);
            while (resultSet.next()) {
                boolean bl2;
                object = resultSet.getString(3);
                Statement statement = this.conn.createStatement();
                Object object2 = null;
                try {
                    statement = this.conn.createStatement();
                    object2 = statement.executeQuery("SELECT LIKE('%autoincrement%', LOWER(sql)) FROM sqlite_schema WHERE LOWER(name) = LOWER('" + this.escape((String)object) + "') AND TYPE IN ('table', 'view')");
                    object2.next();
                    bl2 = object2.getInt(1) == 1;
                }
                finally {
                    if (object2 != null) {
                        try {
                            object2.close();
                        }
                        catch (Exception exception) {
                            LogHolder.logger.error("Could not close ResultSet", exception);
                        }
                    }
                    if (statement != null) {
                        try {
                            statement.close();
                        }
                        catch (Exception exception) {
                            LogHolder.logger.error("Could not close statement", exception);
                        }
                    }
                }
                object2 = "PRAGMA table_xinfo('" + this.escape((String)object) + "')";
                Statement statement2 = this.conn.createStatement();
                try {
                    object2 = statement2.executeQuery((String)object2);
                    try {
                        int n2 = 0;
                        while (object2.next()) {
                            int n3;
                            String string4 = object2.getString(2);
                            String string5 = object2.getString(3);
                            String string6 = object2.getString(4);
                            String string7 = object2.getString(5);
                            int n4 = "1".equals(object2.getString(6));
                            String string8 = object2.getString(7);
                            int n5 = 2;
                            if (string6 != null) {
                                int n6 = n5 = string6.equals("0") ? 1 : 0;
                            }
                            if (bl) {
                                stringBuilder.append(" union all ");
                            }
                            bl = true;
                            int n7 = 2000000000;
                            int n8 = 10;
                            string5 = string5 == null ? "TEXT" : string5.toUpperCase();
                            int n9 = 0;
                            if (n4 != 0 && bl2) {
                                n9 = 1;
                            }
                            if (TYPE_INTEGER.matcher(string5).find()) {
                                n4 = 4;
                                n8 = 0;
                            } else if (TYPE_VARCHAR.matcher(string5).find()) {
                                n4 = 12;
                                n8 = 0;
                            } else {
                                n4 = TYPE_FLOAT.matcher(string5).find() ? 6 : 12;
                            }
                            int n10 = string5.indexOf(40);
                            if (n10 > 0) {
                                n3 = string5.indexOf(41, n10);
                                if (n3 > 0) {
                                    String string9;
                                    String string10;
                                    int n11 = string5.indexOf(44, n10);
                                    if (n11 > 0) {
                                        string10 = string5.substring(n10 + 1, n11);
                                        string9 = string5.substring(n11 + 1, n3);
                                    } else {
                                        string10 = string5.substring(n10 + 1, n3);
                                        string9 = null;
                                    }
                                    try {
                                        int n12 = Integer.parseUnsignedInt(string10);
                                        if (string9 != null) {
                                            n8 = Integer.parseUnsignedInt(string9);
                                            n7 = n12 + n8;
                                        } else {
                                            n8 = 0;
                                            n7 = n12;
                                        }
                                    }
                                    catch (NumberFormatException numberFormatException) {}
                                }
                                string5 = string5.substring(0, n10).trim();
                            }
                            n3 = "2".equals(string8) ? 1 : 0;
                            stringBuilder.append("select ").append(n2 + 1).append(" as ordpos, ").append(n5).append(" as colnullable,").append(n4).append(" as ct, ").append(n7).append(" as colSize, ").append(n8).append(" as colDecimalDigits, '").append((String)object).append("' as tblname, '").append(this.escape(string4)).append("' as cn, '").append(this.escape(string5)).append("' as tn, ").append(JDBC3DatabaseMetaData.quote(string7 == null ? null : this.escape(string7))).append(" as colDefault,").append(n9).append(" as colautoincrement,").append(n3).append(" as colgenerated");
                            if (string3 != null) {
                                stringBuilder.append(" where upper(cn) like upper('").append(this.escape(string3)).append("') ESCAPE '").append(this.getSearchStringEscape()).append("'");
                            }
                            ++n2;
                        }
                    }
                    finally {
                        if (object2 == null) continue;
                        object2.close();
                    }
                }
                finally {
                    if (statement2 == null) continue;
                    statement2.close();
                }
            }
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (Exception exception) {
                    LogHolder.logger.error("Could not close ResultSet", exception);
                }
            }
        }
        if (bl) {
            stringBuilder.append(") order by TABLE_SCHEM, TABLE_NAME, ORDINAL_POSITION;");
        } else {
            stringBuilder.append("select null as ordpos, null as colnullable, null as ct, null as colsize, null as colDecimalDigits, null as tblname, null as cn, null as tn, null as colDefault, null as colautoincrement, null as colgenerated) limit 0;");
        }
        object = this.conn.createStatement();
        return ((CoreStatement)object).executeQuery(stringBuilder.toString(), true);
    }

    @Override
    public ResultSet getCrossReference(String string, String string2, String string3, String string4, String string5, String string6) throws SQLException {
        if (string3 == null) {
            return this.getExportedKeys(string4, string5, string6);
        }
        if (string6 == null) {
            return this.getImportedKeys(string, string2, string3);
        }
        string = "select " + JDBC3DatabaseMetaData.quote(string) + " as PKTABLE_CAT, " + JDBC3DatabaseMetaData.quote(string2) + " as PKTABLE_SCHEM, " + JDBC3DatabaseMetaData.quote(string3) + " as PKTABLE_NAME, '' as PKCOLUMN_NAME, " + JDBC3DatabaseMetaData.quote(string4) + " as FKTABLE_CAT, " + JDBC3DatabaseMetaData.quote(string5) + " as FKTABLE_SCHEM, " + JDBC3DatabaseMetaData.quote(string6) + " as FKTABLE_NAME, '' as FKCOLUMN_NAME, -1 as KEY_SEQ, 3 as UPDATE_RULE, 3 as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, 5" + " as DEFERRABILITY limit 0 ";
        return ((CoreStatement)((Object)this.conn.createStatement())).executeQuery(string, true);
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        if (this.getSchemas == null) {
            this.getSchemas = this.conn.prepareStatement("select null as TABLE_SCHEM, null as TABLE_CATALOG limit 0;");
        }
        return this.getSchemas.executeQuery();
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        if (this.getCatalogs == null) {
            this.getCatalogs = this.conn.prepareStatement("select null as TABLE_CAT limit 0;");
        }
        return this.getCatalogs.executeQuery();
    }

    @Override
    public ResultSet getPrimaryKeys(String object, String stringArray, String string) throws SQLException {
        object = new PrimaryKeyFinder(string);
        stringArray = ((PrimaryKeyFinder)object).getColumns();
        Statement statement = this.conn.createStatement();
        StringBuilder stringBuilder = new StringBuilder(512);
        stringBuilder.append("select null as TABLE_CAT, null as TABLE_SCHEM, '").append(this.escape(string)).append("' as TABLE_NAME, cn as COLUMN_NAME, ks as KEY_SEQ, pk as PK_NAME from (");
        if (stringArray == null) {
            stringBuilder.append("select null as cn, null as pk, 0 as ks) limit 0;");
            return ((CoreStatement)((Object)statement)).executeQuery(stringBuilder.toString(), true);
        }
        if ((object = ((PrimaryKeyFinder)object).getName()) != null) {
            object = "'" + (String)object + "'";
        }
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            if (i2 > 0) {
                stringBuilder.append(" union ");
            }
            JDBC3DatabaseMetaData jDBC3DatabaseMetaData = this;
            stringBuilder.append("select ").append((String)object).append(" as pk, '").append(jDBC3DatabaseMetaData.escape(jDBC3DatabaseMetaData.unquoteIdentifier(stringArray[i2]))).append("' as cn, ").append(i2 + 1).append(" as ks");
        }
        return ((CoreStatement)((Object)statement)).executeQuery(stringBuilder.append(") order by cn;").toString(), true);
    }

    @Override
    public ResultSet getExportedKeys(String string, String string2, String object) throws SQLException {
        Iterator iterator2;
        PrimaryKeyFinder primaryKeyFinder = new PrimaryKeyFinder((String)object);
        String[] stringArray = primaryKeyFinder.getColumns();
        Statement statement = this.conn.createStatement();
        string = string != null ? JDBC3DatabaseMetaData.quote(string) : null;
        string2 = string2 != null ? JDBC3DatabaseMetaData.quote(string2) : null;
        StringBuilder stringBuilder = new StringBuilder(512);
        String string3 = null;
        int n2 = 0;
        if (stringArray != null) {
            String string4;
            Object object2;
            iterator2 = statement.executeQuery("select name from sqlite_schema where type = 'table'");
            try {
                object2 = new ArrayList<String>();
                while (iterator2.next()) {
                    string4 = iterator2.getString(1);
                    ((ArrayList)object2).add(string4);
                    if (!string4.equalsIgnoreCase((String)object)) continue;
                    string3 = string4;
                }
            }
            finally {
                if (iterator2 != null) {
                    iterator2.close();
                }
            }
            iterator2 = ((ArrayList)object2).iterator();
            while (iterator2.hasNext()) {
                string4 = (String)iterator2.next();
                object = new ImportedKeyFinder(string4);
                object = ((ImportedKeyFinder)object).getFkList();
                object = object.iterator();
                while (object.hasNext()) {
                    object2 = (ImportedKeyFinder.ForeignKey)object.next();
                    String string5 = ((ImportedKeyFinder.ForeignKey)object2).getPkTableName();
                    if (string5 == null || !string5.equalsIgnoreCase(string3)) continue;
                    for (int i2 = 0; i2 < ((ImportedKeyFinder.ForeignKey)object2).getColumnMappingCount(); ++i2) {
                        int n3 = i2 + 1;
                        Object object3 = ((ImportedKeyFinder.ForeignKey)object2).getColumnMapping(i2);
                        String string6 = object3[1];
                        string6 = string6 == null ? "" : string6;
                        object3 = object3[0];
                        object3 = object3 == null ? "" : object3;
                        boolean bl = false;
                        Object object4 = stringArray;
                        int n4 = stringArray.length;
                        for (int i3 = 0; i3 < n4; ++i3) {
                            String string7 = object4[i3];
                            if (string7 == null || !string7.equalsIgnoreCase(string6)) continue;
                            bl = true;
                            break;
                        }
                        object4 = bl && primaryKeyFinder.getName() != null ? primaryKeyFinder.getName() : "";
                        stringBuilder.append(n2 > 0 ? " union all select " : "select ").append(n3).append(" as ks, '").append(this.escape(string4)).append("' as fkt, '").append(this.escape((String)object3)).append("' as fcn, '").append(this.escape(string6)).append("' as pcn, '").append(this.escape((String)object4)).append("' as pkn, ").append(RULE_MAP.get(((ImportedKeyFinder.ForeignKey)object2).getOnUpdate())).append(" as ur, ").append(RULE_MAP.get(((ImportedKeyFinder.ForeignKey)object2).getOnDelete())).append(" as dr, ");
                        String string8 = ((ImportedKeyFinder.ForeignKey)object2).getFkName();
                        if (string8 != null) {
                            stringBuilder.append("'").append(this.escape(string8)).append("' as fkn");
                        } else {
                            stringBuilder.append("'' as fkn");
                        }
                        ++n2;
                    }
                }
            }
        }
        boolean bl = n2 > 0;
        iterator2 = new StringBuilder(512);
        ((StringBuilder)((Object)iterator2)).append("select ").append(string).append(" as PKTABLE_CAT, ").append(string2).append(" as PKTABLE_SCHEM, ").append(JDBC3DatabaseMetaData.quote(string3)).append(" as PKTABLE_NAME, ").append(bl ? "pcn" : "''").append(" as PKCOLUMN_NAME, ").append(string).append(" as FKTABLE_CAT, ").append(string2).append(" as FKTABLE_SCHEM, ").append(bl ? "fkt" : "''").append(" as FKTABLE_NAME, ").append(bl ? "fcn" : "''").append(" as FKCOLUMN_NAME, ").append(bl ? "ks" : "-1").append(" as KEY_SEQ, ").append(bl ? "ur" : "3").append(" as UPDATE_RULE, ").append(bl ? "dr" : "3").append(" as DELETE_RULE, ").append(bl ? "fkn" : "''").append(" as FK_NAME, ").append(bl ? "pkn" : "''").append(" as PK_NAME, 5").append(" as DEFERRABILITY ");
        if (bl) {
            ((StringBuilder)((Object)iterator2)).append("from (").append((CharSequence)stringBuilder).append(") ORDER BY FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, KEY_SEQ");
        } else {
            ((StringBuilder)((Object)iterator2)).append("limit 0");
        }
        return ((CoreStatement)((Object)statement)).executeQuery(((StringBuilder)((Object)iterator2)).toString(), true);
    }

    private StringBuilder appendDummyForeignKeyList(StringBuilder stringBuilder) {
        stringBuilder.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, 3").append(" as ur, 3").append(" as dr,  '' as fkn, ").append(" '' as pkn ) limit 0;");
        return stringBuilder;
    }

    @Override
    public ResultSet getImportedKeys(String object, String list2, String string) throws SQLException {
        Statement statement = this.conn.createStatement();
        StringBuilder stringBuilder = new StringBuilder(700);
        stringBuilder.append("select ").append(JDBC3DatabaseMetaData.quote((String)object)).append(" as PKTABLE_CAT, ").append(JDBC3DatabaseMetaData.quote((String)((Object)list2))).append(" as PKTABLE_SCHEM, ptn as PKTABLE_NAME, pcn as PKCOLUMN_NAME, ").append(JDBC3DatabaseMetaData.quote((String)object)).append(" as FKTABLE_CAT, ").append(JDBC3DatabaseMetaData.quote((String)((Object)list2))).append(" as FKTABLE_SCHEM, ").append(JDBC3DatabaseMetaData.quote(string)).append(" as FKTABLE_NAME, fcn as FKCOLUMN_NAME, ks as KEY_SEQ, ur as UPDATE_RULE, dr as DELETE_RULE, fkn as FK_NAME, pkn as PK_NAME, ").append(5).append(" as DEFERRABILITY from (");
        try {
            object = statement.executeQuery("pragma foreign_key_list('" + this.escape(string) + "');");
        }
        catch (SQLException sQLException) {
            stringBuilder = this.appendDummyForeignKeyList(stringBuilder);
            return ((CoreStatement)((Object)statement)).executeQuery(stringBuilder.toString(), true);
        }
        list2 = new ImportedKeyFinder(string);
        list2 = ((ImportedKeyFinder)((Object)list2)).getFkList();
        int n2 = 0;
        while (object.next()) {
            Object object2;
            int n3 = object.getInt(2) + 1;
            int n4 = object.getInt(1);
            String string2 = object.getString(3);
            String string3 = object.getString(4);
            String string4 = object.getString(5);
            String string5 = null;
            try {
                object2 = new PrimaryKeyFinder(string2);
                string5 = ((PrimaryKeyFinder)object2).getName();
                if (string4 == null) {
                    string4 = ((PrimaryKeyFinder)object2).getColumns()[0];
                }
            }
            catch (SQLException sQLException) {}
            object2 = object.getString(6);
            String string6 = object.getString(7);
            if (n2 > 0) {
                stringBuilder.append(" union all ");
            }
            String string7 = null;
            if (list2.size() > n4) {
                string7 = list2.get(n4).getFkName();
            }
            stringBuilder.append("select ").append(n3).append(" as ks,'").append(this.escape(string2)).append("' as ptn, '").append(this.escape(string3)).append("' as fcn, '").append(this.escape(string4)).append("' as pcn,case '").append(this.escape((String)object2)).append("' when 'NO ACTION' then ").append(3).append(" when 'CASCADE' then 0").append(" when 'RESTRICT' then 1").append(" when 'SET NULL' then 2").append(" when 'SET DEFAULT' then 4").append(" end as ur, case '").append(this.escape(string6)).append("' when 'NO ACTION' then ").append(3).append(" when 'CASCADE' then 0").append(" when 'RESTRICT' then 1").append(" when 'SET NULL' then 2").append(" when 'SET DEFAULT' then 4").append(" end as dr, ").append(string7 == null ? "''" : JDBC3DatabaseMetaData.quote(string7)).append(" as fkn, ").append(string5 == null ? "''" : JDBC3DatabaseMetaData.quote(string5)).append(" as pkn");
            ++n2;
        }
        object.close();
        if (n2 == 0) {
            stringBuilder = this.appendDummyForeignKeyList(stringBuilder);
        } else {
            stringBuilder.append(") ORDER BY PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, KEY_SEQ;");
        }
        return ((CoreStatement)((Object)statement)).executeQuery(stringBuilder.toString(), true);
    }

    @Override
    public ResultSet getIndexInfo(String object, String object2, String iterator2, boolean bl, boolean bl2) throws SQLException {
        String string;
        object2 = this.conn.createStatement();
        StringBuilder stringBuilder = new StringBuilder(500);
        stringBuilder.append("select null as TABLE_CAT, null as TABLE_SCHEM, '").append(this.escape((String)((Object)iterator2))).append("' as TABLE_NAME, un as NON_UNIQUE, null as INDEX_QUALIFIER, n as INDEX_NAME, ").append(Integer.toString(3)).append(" as TYPE, op as ORDINAL_POSITION, cn as COLUMN_NAME, null as ASC_OR_DESC, 0 as CARDINALITY, 0 as PAGES, null as FILTER_CONDITION from (");
        object = object2.executeQuery("pragma index_list('" + this.escape((String)((Object)iterator2)) + "');");
        iterator2 = new ArrayList();
        while (object.next()) {
            ((ArrayList)((Object)iterator2)).add(new ArrayList());
            Iterator iterator3 = iterator2;
            ((ArrayList)((ArrayList)((Object)iterator3)).get(((ArrayList)((Object)iterator3)).size() - 1)).add(object.getString(2));
            Iterator iterator4 = iterator2;
            ((ArrayList)((ArrayList)((Object)iterator4)).get(((ArrayList)((Object)iterator4)).size() - 1)).add(object.getInt(3));
        }
        object.close();
        if (((ArrayList)((Object)iterator2)).size() == 0) {
            stringBuilder.append("select null as un, null as n, null as op, null as cn) limit 0;");
            return ((CoreStatement)object2).executeQuery(stringBuilder.toString(), true);
        }
        iterator2 = ((ArrayList)((Object)iterator2)).iterator();
        ArrayList<String> arrayList = new ArrayList<String>();
        while (iterator2.hasNext()) {
            ArrayList arrayList2 = (ArrayList)iterator2.next();
            string = arrayList2.get(0).toString();
            object = object2.executeQuery("pragma index_info('" + this.escape(string) + "');");
            while (object.next()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                String string2 = object.getString(3);
                stringBuilder2.append("select ").append(1 - (Integer)arrayList2.get(1)).append(" as un,'").append(this.escape(string)).append("' as n,").append(object.getInt(1) + 1).append(" as op,");
                if (string2 == null) {
                    stringBuilder2.append("null");
                } else {
                    stringBuilder2.append("'").append(this.escape(string2)).append("'");
                }
                stringBuilder2.append(" as cn");
                arrayList.add(stringBuilder2.toString());
            }
            object.close();
        }
        string = StringUtils.join(arrayList, " union all ");
        return ((CoreStatement)object2).executeQuery(stringBuilder.append(string).append(");").toString(), true);
    }

    @Override
    public ResultSet getProcedureColumns(String string, String string2, String string3, String string4) throws SQLException {
        if (this.getProcedureColumns == null) {
            this.getProcedureColumns = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as COLUMN_NAME, null as COLUMN_TYPE, null as DATA_TYPE, null as TYPE_NAME, null as PRECISION, null as LENGTH, null as SCALE, null as RADIX, null as NULLABLE, null as REMARKS limit 0;");
        }
        return this.getProcedureColumns.executeQuery();
    }

    @Override
    public ResultSet getProcedures(String string, String string2, String string3) throws SQLException {
        if (this.getProcedures == null) {
            this.getProcedures = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as UNDEF1, null as UNDEF2, null as UNDEF3, null as REMARKS, null as PROCEDURE_TYPE limit 0;");
        }
        return this.getProcedures.executeQuery();
    }

    @Override
    public ResultSet getSuperTables(String string, String string2, String string3) throws SQLException {
        if (this.getSuperTables == null) {
            this.getSuperTables = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as SUPERTABLE_NAME limit 0;");
        }
        return this.getSuperTables.executeQuery();
    }

    @Override
    public ResultSet getSuperTypes(String string, String string2, String string3) throws SQLException {
        if (this.getSuperTypes == null) {
            this.getSuperTypes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, null as SUPERTYPE_NAME limit 0;");
        }
        return this.getSuperTypes.executeQuery();
    }

    @Override
    public ResultSet getTablePrivileges(String string, String string2, String string3) throws SQLException {
        if (this.getTablePrivileges == null) {
            this.getTablePrivileges = this.conn.prepareStatement("select  null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as GRANTOR, null GRANTEE,  null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
        }
        return this.getTablePrivileges.executeQuery();
    }

    @Override
    public synchronized ResultSet getTables(String charSequence, String string2, String string3, String[] stringArray) throws SQLException {
        this.checkOpen();
        string3 = string3 == null || "".equals(string3) ? "%" : this.escape(string3);
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("SELECT\n");
        ((StringBuilder)charSequence).append("  NULL AS TABLE_CAT,\n");
        ((StringBuilder)charSequence).append("  NULL AS TABLE_SCHEM,\n");
        ((StringBuilder)charSequence).append("  NAME AS TABLE_NAME,\n");
        ((StringBuilder)charSequence).append("  TYPE AS TABLE_TYPE,\n");
        ((StringBuilder)charSequence).append("  NULL AS REMARKS,\n");
        ((StringBuilder)charSequence).append("  NULL AS TYPE_CAT,\n");
        ((StringBuilder)charSequence).append("  NULL AS TYPE_SCHEM,\n");
        ((StringBuilder)charSequence).append("  NULL AS TYPE_NAME,\n");
        ((StringBuilder)charSequence).append("  NULL AS SELF_REFERENCING_COL_NAME,\n");
        ((StringBuilder)charSequence).append("  NULL AS REF_GENERATION\n");
        ((StringBuilder)charSequence).append("FROM\n");
        ((StringBuilder)charSequence).append("  (\n");
        ((StringBuilder)charSequence).append("    SELECT\n");
        ((StringBuilder)charSequence).append("      'sqlite_schema' AS NAME,\n");
        ((StringBuilder)charSequence).append("      'SYSTEM TABLE' AS TYPE");
        ((StringBuilder)charSequence).append("    UNION ALL\n");
        ((StringBuilder)charSequence).append("    SELECT\n");
        ((StringBuilder)charSequence).append("      NAME,\n");
        ((StringBuilder)charSequence).append("      UPPER(TYPE) AS TYPE\n");
        ((StringBuilder)charSequence).append("    FROM\n");
        ((StringBuilder)charSequence).append("      sqlite_schema\n");
        ((StringBuilder)charSequence).append("    WHERE\n");
        ((StringBuilder)charSequence).append("      NAME NOT LIKE 'sqlite\\_%' ESCAPE '\\'\n");
        ((StringBuilder)charSequence).append("      AND UPPER(TYPE) IN ('TABLE', 'VIEW')\n");
        ((StringBuilder)charSequence).append("    UNION ALL\n");
        ((StringBuilder)charSequence).append("    SELECT\n");
        ((StringBuilder)charSequence).append("      NAME,\n");
        ((StringBuilder)charSequence).append("      'GLOBAL TEMPORARY' AS TYPE\n");
        ((StringBuilder)charSequence).append("    FROM\n");
        ((StringBuilder)charSequence).append("      sqlite_temp_master\n");
        ((StringBuilder)charSequence).append("    UNION ALL\n");
        ((StringBuilder)charSequence).append("    SELECT\n");
        ((StringBuilder)charSequence).append("      NAME,\n");
        ((StringBuilder)charSequence).append("      'SYSTEM TABLE' AS TYPE\n");
        ((StringBuilder)charSequence).append("    FROM\n");
        ((StringBuilder)charSequence).append("      sqlite_schema\n");
        ((StringBuilder)charSequence).append("    WHERE\n");
        ((StringBuilder)charSequence).append("      NAME LIKE 'sqlite\\_%' ESCAPE '\\'\n");
        ((StringBuilder)charSequence).append("  )\n");
        ((StringBuilder)charSequence).append(" WHERE TABLE_NAME LIKE '");
        ((StringBuilder)charSequence).append(string3);
        ((StringBuilder)charSequence).append("' ESCAPE '");
        ((StringBuilder)charSequence).append(this.getSearchStringEscape());
        ((StringBuilder)charSequence).append("'");
        if (stringArray != null && stringArray.length != 0) {
            ((StringBuilder)charSequence).append(" AND TABLE_TYPE IN (");
            ((StringBuilder)charSequence).append(Arrays.stream(stringArray).map(string -> "'" + string.toUpperCase() + "'").collect(Collectors.joining(",")));
            ((StringBuilder)charSequence).append(")");
        }
        ((StringBuilder)charSequence).append(" ORDER BY TABLE_TYPE, TABLE_NAME;");
        return ((CoreStatement)((Object)this.conn.createStatement())).executeQuery(((StringBuilder)charSequence).toString(), true);
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        this.checkOpen();
        String string = "SELECT 'TABLE' AS TABLE_TYPE UNION SELECT 'VIEW' AS TABLE_TYPE UNION SELECT 'SYSTEM TABLE' AS TABLE_TYPE UNION SELECT 'GLOBAL TEMPORARY' AS TABLE_TYPE;";
        if (this.getTableTypes == null) {
            this.getTableTypes = this.conn.prepareStatement(string);
        }
        this.getTableTypes.clearParameters();
        return this.getTableTypes.executeQuery();
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        if (this.getTypeInfo == null) {
            String string = QueryUtils.valuesQuery(Arrays.asList("TYPE_NAME", "DATA_TYPE", "PRECISION", "LITERAL_PREFIX", "LITERAL_SUFFIX", "CREATE_PARAMS", "NULLABLE", "CASE_SENSITIVE", "SEARCHABLE", "UNSIGNED_ATTRIBUTE", "FIXED_PREC_SCALE", "AUTO_INCREMENT", "LOCAL_TYPE_NAME", "MINIMUM_SCALE", "MAXIMUM_SCALE", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "NUM_PREC_RADIX"), Arrays.asList(Arrays.asList("BLOB", 2004, 0, null, null, null, 1, 0, 3, 1, 0, 0, null, 0, 0, 0, 0, 10), Arrays.asList("INTEGER", 4, 0, null, null, null, 1, 0, 3, 0, 0, 1, null, 0, 0, 0, 0, 10), Arrays.asList("NULL", 0, 0, null, null, null, 1, 0, 3, 1, 0, 0, null, 0, 0, 0, 0, 10), Arrays.asList("REAL", 7, 0, null, null, null, 1, 0, 3, 0, 0, 0, null, 0, 0, 0, 0, 10), Arrays.asList("TEXT", 12, 0, null, null, null, 1, 1, 3, 1, 0, 0, null, 0, 0, 0, 0, 10))) + " order by DATA_TYPE";
            this.getTypeInfo = this.conn.prepareStatement(string);
        }
        this.getTypeInfo.clearParameters();
        return this.getTypeInfo.executeQuery();
    }

    @Override
    public ResultSet getUDTs(String string, String string2, String string3, int[] nArray) throws SQLException {
        if (this.getUDTs == null) {
            this.getUDTs = this.conn.prepareStatement("select  null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME,  null as CLASS_NAME,  null as DATA_TYPE, null as REMARKS, null as BASE_TYPE limit 0;");
        }
        this.getUDTs.clearParameters();
        return this.getUDTs.executeQuery();
    }

    @Override
    public ResultSet getVersionColumns(String string, String string2, String string3) throws SQLException {
        if (this.getVersionColumns == null) {
            this.getVersionColumns = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
        }
        return this.getVersionColumns.executeQuery();
    }

    @Override
    @Deprecated
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
    }

    public Struct createStruct(String string, Object[] objectArray) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not yet implemented by SQLite JDBC driver");
    }

    @Override
    public ResultSet getFunctionColumns(String string, String string2, String string3, String string4) throws SQLException {
        throw new SQLFeatureNotSupportedException("Not yet implemented by SQLite JDBC driver");
    }

    @Override
    public void finalize() throws Throwable {
        this.close();
    }

    private String unquoteIdentifier(String string) {
        if (string == null) {
            return string;
        }
        if ((string = string.trim()).length() > 2 && (string.startsWith("`") && string.endsWith("`") || string.startsWith("\"") && string.endsWith("\"") || string.startsWith("[") && string.endsWith("]"))) {
            string = string.substring(1, string.length() - 1);
        }
        return string;
    }

    static {
        try (InputStream inputStream2 = JDBC3DatabaseMetaData.class.getClassLoader().getResourceAsStream("sqlite-jdbc.properties");){
            if (inputStream2 == null) {
                throw new IOException("Cannot load sqlite-jdbc.properties from jar");
            }
            Properties properties = new Properties();
            properties.load(inputStream2);
            driverName = properties.getProperty("name");
            driverVersion = properties.getProperty("version");
        }
        catch (Exception exception) {
            driverName = "SQLite JDBC";
            driverVersion = "3.0.0-UNKNOWN";
        }
        TYPE_INTEGER = Pattern.compile(".*(INT|BOOL).*");
        TYPE_VARCHAR = Pattern.compile(".*(CHAR|CLOB|TEXT|BLOB).*");
        TYPE_FLOAT = Pattern.compile(".*(REAL|FLOA|DOUB|DEC|NUM).*");
        RULE_MAP = new HashMap<String, Integer>();
        RULE_MAP.put("NO ACTION", 3);
        RULE_MAP.put("CASCADE", 0);
        RULE_MAP.put("RESTRICT", 1);
        RULE_MAP.put("SET NULL", 2);
        RULE_MAP.put("SET DEFAULT", 4);
        PK_UNNAMED_PATTERN = Pattern.compile(".*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
        PK_NAMED_PATTERN = Pattern.compile(".*CONSTRAINT\\s*(.*?)\\s*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
    }

    static class LogHolder {
        private static final Logger logger = LoggerFactory.getLogger(JDBC3DatabaseMetaData.class);

        private LogHolder() {
        }
    }

    class ImportedKeyFinder {
        private final Pattern FK_NAMED_PATTERN = Pattern.compile("CONSTRAINT\\s*\"?([A-Za-z_][A-Za-z\\d_]*)?\"?\\s*FOREIGN\\s+KEY\\s*\\((.*?)\\)", 34);
        private final String fkTableName;
        private final List<ForeignKey> fkList = new ArrayList<ForeignKey>();

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public ImportedKeyFinder(String list2) throws SQLException {
            if (list2 == null || ((String)((Object)list2)).trim().length() == 0) {
                throw new SQLException("Invalid table name: '" + list2 + "'");
            }
            this.fkTableName = list2;
            ImportedKeyFinder importedKeyFinder = this;
            list2 = importedKeyFinder.getForeignKeyNames(importedKeyFinder.fkTableName);
            try (Statement statement = ((JDBC3DatabaseMetaData)JDBC3DatabaseMetaData.this).conn.createStatement();){
                JDBC3DatabaseMetaData.this = statement.executeQuery("pragma foreign_key_list('" + ((JDBC3DatabaseMetaData)JDBC3DatabaseMetaData.this).escape(this.fkTableName.toLowerCase()) + "')");
                try {
                    int n2 = -1;
                    int n3 = 0;
                    ForeignKey foreignKey = null;
                    while (JDBC3DatabaseMetaData.this.next()) {
                        int n4 = JDBC3DatabaseMetaData.this.getInt(1);
                        String string = JDBC3DatabaseMetaData.this.getString(3);
                        String string2 = JDBC3DatabaseMetaData.this.getString(4);
                        String string3 = JDBC3DatabaseMetaData.this.getString(5);
                        String string4 = JDBC3DatabaseMetaData.this.getString(6);
                        String string5 = JDBC3DatabaseMetaData.this.getString(7);
                        String string6 = JDBC3DatabaseMetaData.this.getString(8);
                        String string7 = null;
                        if (list2.size() > n3) {
                            string7 = list2.get(n3);
                        }
                        if (n4 != n2) {
                            foreignKey = new ForeignKey(string7, string, this.fkTableName, string4, string5, string6);
                            this.fkList.add(foreignKey);
                            n2 = n4;
                            ++n3;
                        }
                        if (foreignKey == null) continue;
                        foreignKey.addColumnMapping(string2, string3);
                    }
                }
                finally {
                    if (JDBC3DatabaseMetaData.this != null) {
                        JDBC3DatabaseMetaData.this.close();
                    }
                }
                if (statement == null) return;
            }
        }

        private List<String> getForeignKeyNames(String object) throws SQLException {
            ArrayList<String> arrayList = new ArrayList<String>();
            if (object == null) {
                return arrayList;
            }
            try (Statement statement = JDBC3DatabaseMetaData.this.conn.createStatement();){
                object = statement.executeQuery("select sql from sqlite_schema where lower(name) = lower('" + JDBC3DatabaseMetaData.this.escape((String)object) + "')");
                try {
                    if (object.next()) {
                        Matcher matcher = this.FK_NAMED_PATTERN.matcher(object.getString(1));
                        while (matcher.find()) {
                            arrayList.add(matcher.group(1));
                        }
                    }
                }
                finally {
                    if (object != null) {
                        object.close();
                    }
                }
            }
            Collections.reverse(arrayList);
            return arrayList;
        }

        public String getFkTableName() {
            return this.fkTableName;
        }

        public List<ForeignKey> getFkList() {
            return this.fkList;
        }

        class ForeignKey {
            private final String fkName;
            private final String pkTableName;
            private final String fkTableName;
            private final List<String> fkColNames = new ArrayList<String>();
            private final List<String> pkColNames = new ArrayList<String>();
            private final String onUpdate;
            private final String onDelete;
            private final String match;

            ForeignKey(String string, String string2, String string3, String string4, String string5, String string6) {
                this.fkName = string;
                this.pkTableName = string2;
                this.fkTableName = string3;
                this.onUpdate = string4;
                this.onDelete = string5;
                this.match = string6;
            }

            public String getFkName() {
                return this.fkName;
            }

            void addColumnMapping(String string, String string2) {
                this.fkColNames.add(string);
                this.pkColNames.add(string2);
            }

            public String[] getColumnMapping(int n2) {
                return new String[]{this.fkColNames.get(n2), this.pkColNames.get(n2)};
            }

            public int getColumnMappingCount() {
                return this.fkColNames.size();
            }

            public String getPkTableName() {
                return this.pkTableName;
            }

            public String getFkTableName() {
                return this.fkTableName;
            }

            public String getOnUpdate() {
                return this.onUpdate;
            }

            public String getOnDelete() {
                return this.onDelete;
            }

            public String getMatch() {
                return this.match;
            }

            public String toString() {
                return "ForeignKey [fkName=" + this.fkName + ", pkTableName=" + this.pkTableName + ", fkTableName=" + this.fkTableName + ", pkColNames=" + this.pkColNames + ", fkColNames=" + this.fkColNames + "]";
            }
        }
    }

    class PrimaryKeyFinder {
        String table;
        String pkName = null;
        String[] pkColumns = null;

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public PrimaryKeyFinder(String object) throws SQLException {
            this.table = object;
            if ("sqlite_schema".equals(object) || "sqlite_master".equals(object)) {
                return;
            }
            if (object == null || ((String)object).trim().length() == 0) {
                throw new SQLException("Invalid table name: '" + this.table + "'");
            }
            try (Statement statement = JDBC3DatabaseMetaData.this.conn.createStatement();){
                try (ResultSet resultSet = statement.executeQuery("select sql from sqlite_schema where lower(name) = lower('" + JDBC3DatabaseMetaData.this.escape((String)object) + "') and type in ('table', 'view')");){
                    if (!resultSet.next()) {
                        throw new SQLException("Table not found: '" + (String)object + "'");
                    }
                    Matcher matcher = PK_NAMED_PATTERN.matcher(resultSet.getString(1));
                    if (matcher.find()) {
                        JDBC3DatabaseMetaData jDBC3DatabaseMetaData2 = JDBC3DatabaseMetaData.this;
                        this.pkName = jDBC3DatabaseMetaData2.unquoteIdentifier(jDBC3DatabaseMetaData2.escape(matcher.group(1)));
                        this.pkColumns = matcher.group(2).split(",");
                    } else {
                        matcher = PK_UNNAMED_PATTERN.matcher(resultSet.getString(1));
                        if (matcher.find()) {
                            this.pkColumns = matcher.group(1).split(",");
                        }
                    }
                    if (this.pkColumns == null) {
                        object = statement.executeQuery("pragma table_info('" + JDBC3DatabaseMetaData.this.escape((String)object) + "');");
                        try {
                            while (object.next()) {
                                if (!object.getBoolean(6)) continue;
                                this.pkColumns = new String[]{object.getString(2)};
                            }
                        }
                        finally {
                            if (object != null) {
                                object.close();
                            }
                        }
                    }
                    if (this.pkColumns != null) {
                        for (int i2 = 0; i2 < this.pkColumns.length; ++i2) {
                            this.pkColumns[i2] = JDBC3DatabaseMetaData.this.unquoteIdentifier(this.pkColumns[i2]);
                        }
                    }
                }
                if (statement == null) return;
            }
        }

        public String getName() {
            return this.pkName;
        }

        public String[] getColumns() {
            return this.pkColumns;
        }
    }
}

