package de.darthweiter.banplugin.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool {

    private final int maxPoolSize;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();

    public BasicConnectionPool(List<Connection> connectionPool, int maxPoolSize) {
        this.connectionPool = connectionPool;
        this.maxPoolSize = maxPoolSize;
    }

    private static Connection createConnection() {
        try {
            return DataSourceFactory.getMySQLDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BasicConnectionPool create(int maxPoolSize) {
        int initial = maxPoolSize / 2;
        List<Connection> pool = new ArrayList<>(initial);
        for (int i = 0; i < initial; i++) {
            Connection con = createConnection();
            if (con != null) {
                pool.add(con);
            }
        }
        if (pool.isEmpty()) {
            throw new RuntimeException("Probleme beim erzeugen des Pools");
        }
        return new BasicConnectionPool(pool, maxPoolSize);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < maxPoolSize) {
                Connection con = createConnection();
                if (con != null) {
                    connectionPool.add(createConnection());
                }
            }
        }

        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);

        if (!connection.isValid(10)) {
            connection = createConnection();
        }
        if (connection != null) {
            usedConnections.add(connection);
        } else {
            connection = getConnection();
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }

}
