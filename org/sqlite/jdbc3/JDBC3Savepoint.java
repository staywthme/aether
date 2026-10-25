/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.jdbc3;

import java.sql.SQLException;
import java.sql.Savepoint;

public class JDBC3Savepoint
implements Savepoint {
    final int id;
    final String name;

    JDBC3Savepoint(int n2) {
        this.id = n2;
        this.name = null;
    }

    JDBC3Savepoint(int n2, String string) {
        this.id = n2;
        this.name = string;
    }

    @Override
    public int getSavepointId() throws SQLException {
        return this.id;
    }

    @Override
    public String getSavepointName() throws SQLException {
        if (this.name == null) {
            return String.format("SQLITE_SAVEPOINT_%s", this.id);
        }
        return this.name;
    }
}

