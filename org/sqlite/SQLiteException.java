/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite;

import java.sql.SQLException;
import org.sqlite.SQLiteErrorCode;

public class SQLiteException
extends SQLException {
    private SQLiteErrorCode resultCode;

    public SQLiteException(String string, SQLiteErrorCode sQLiteErrorCode) {
        super(string, null, sQLiteErrorCode.code & 0xFF);
        this.resultCode = sQLiteErrorCode;
    }

    public SQLiteErrorCode getResultCode() {
        return this.resultCode;
    }
}

