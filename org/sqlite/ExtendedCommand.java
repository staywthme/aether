/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.core.DB;

public class ExtendedCommand {
    public static SQLExtension parse(String string) throws SQLException {
        if (string == null) {
            return null;
        }
        if (string.length() > 5 && string.substring(0, 6).toLowerCase().equals("backup")) {
            return BackupCommand.parse(string);
        }
        if (string.length() > 6 && string.substring(0, 7).toLowerCase().equals("restore")) {
            return RestoreCommand.parse(string);
        }
        return null;
    }

    public static String removeQuotation(String string) {
        if (string == null) {
            return string;
        }
        if (string.startsWith("\"") && string.endsWith("\"") || string.startsWith("'") && string.endsWith("'")) {
            return string.substring(1, string.length() - 1);
        }
        return string;
    }

    public static class RestoreCommand
    implements SQLExtension {
        public final String targetDB;
        public final String srcFile;
        private static Pattern restoreCmd = Pattern.compile("restore(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+from\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);

        public RestoreCommand(String string, String string2) {
            this.targetDB = string;
            this.srcFile = string2;
        }

        public static RestoreCommand parse(String string) throws SQLException {
            Object object;
            if (string != null && ((Matcher)(object = restoreCmd.matcher(string))).matches()) {
                string = ExtendedCommand.removeQuotation(((Matcher)object).group(2));
                object = ExtendedCommand.removeQuotation(((Matcher)object).group(3));
                if (string == null || string.length() == 0) {
                    string = "main";
                }
                return new RestoreCommand(string, (String)object);
            }
            throw new SQLException("syntax error: " + string);
        }

        @Override
        public void execute(DB dB) throws SQLException {
            int n2 = dB.restore(this.targetDB, this.srcFile, null);
            if (n2 != SQLiteErrorCode.SQLITE_OK.code) {
                throw DB.newSQLException(n2, "Restore failed");
            }
        }
    }

    public static class BackupCommand
    implements SQLExtension {
        public final String srcDB;
        public final String destFile;
        private static Pattern backupCmd = Pattern.compile("backup(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+to\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);

        public BackupCommand(String string, String string2) {
            this.srcDB = string;
            this.destFile = string2;
        }

        public static BackupCommand parse(String string) throws SQLException {
            Object object;
            if (string != null && ((Matcher)(object = backupCmd.matcher(string))).matches()) {
                string = ExtendedCommand.removeQuotation(((Matcher)object).group(2));
                object = ExtendedCommand.removeQuotation(((Matcher)object).group(3));
                if (string == null || string.length() == 0) {
                    string = "main";
                }
                return new BackupCommand(string, (String)object);
            }
            throw new SQLException("syntax error: " + string);
        }

        @Override
        public void execute(DB dB) throws SQLException {
            int n2 = dB.backup(this.srcDB, this.destFile, null);
            if (n2 != SQLiteErrorCode.SQLITE_OK.code) {
                throw DB.newSQLException(n2, "Backup failed");
            }
        }
    }

    public static interface SQLExtension {
        public void execute(DB var1) throws SQLException;
    }
}

