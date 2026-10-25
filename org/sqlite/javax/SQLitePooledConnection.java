/*
 * Decompiled with CFR 0.152.
 */
package org.sqlite.javax;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4PooledConnection;

public class SQLitePooledConnection
extends JDBC4PooledConnection {
    protected SQLiteConnection physicalConn;
    protected volatile Connection handleConn;
    protected List<ConnectionEventListener> listeners = new ArrayList<ConnectionEventListener>();

    protected SQLitePooledConnection(SQLiteConnection sQLiteConnection) {
        this.physicalConn = sQLiteConnection;
    }

    public SQLiteConnection getPhysicalConn() {
        return this.physicalConn;
    }

    @Override
    public void close() throws SQLException {
        if (this.handleConn != null) {
            this.listeners.clear();
            this.handleConn.close();
        }
        if (this.physicalConn != null) {
            try {
                this.physicalConn.close();
                return;
            }
            finally {
                this.physicalConn = null;
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.handleConn != null) {
            this.handleConn.close();
        }
        this.handleConn = (Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Connection.class}, new InvocationHandler(){
            boolean isClosed;

            @Override
            public Object invoke(Object object, Method object2, Object[] objectArray) throws Throwable {
                try {
                    object = ((Method)object2).getName();
                    if ("close".equals(object)) {
                        object2 = new ConnectionEvent(SQLitePooledConnection.this);
                        for (int i2 = SQLitePooledConnection.this.listeners.size() - 1; i2 >= 0; --i2) {
                            SQLitePooledConnection.this.listeners.get(i2).connectionClosed((ConnectionEvent)object2);
                        }
                        if (!SQLitePooledConnection.this.physicalConn.getAutoCommit()) {
                            SQLitePooledConnection.this.physicalConn.rollback();
                        }
                        SQLitePooledConnection.this.physicalConn.setAutoCommit(true);
                        this.isClosed = true;
                        return null;
                    }
                    if ("isClosed".equals(object)) {
                        if (!this.isClosed) {
                            this.isClosed = (Boolean)((Method)object2).invoke(SQLitePooledConnection.this.physicalConn, objectArray);
                        }
                        return this.isClosed;
                    }
                    if (this.isClosed) {
                        throw new SQLException("Connection is closed");
                    }
                    return ((Method)object2).invoke(SQLitePooledConnection.this.physicalConn, objectArray);
                }
                catch (SQLException sQLException) {
                    if ("database connection closed".equals(sQLException.getMessage())) {
                        object2 = new ConnectionEvent(SQLitePooledConnection.this, sQLException);
                        for (int i3 = SQLitePooledConnection.this.listeners.size() - 1; i3 >= 0; --i3) {
                            SQLitePooledConnection.this.listeners.get(i3).connectionErrorOccurred((ConnectionEvent)object2);
                        }
                    }
                    throw sQLException;
                }
                catch (InvocationTargetException invocationTargetException) {
                    object = invocationTargetException;
                    throw invocationTargetException.getCause();
                }
            }
        });
        return this.handleConn;
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.listeners.add(connectionEventListener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.listeners.remove(connectionEventListener);
    }

    public List<ConnectionEventListener> getListeners() {
        return this.listeners;
    }
}

