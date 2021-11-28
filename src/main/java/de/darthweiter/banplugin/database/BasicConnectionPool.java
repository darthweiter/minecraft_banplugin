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

    /**
     * Create the DB Connection.
     *
     * @return the DB Connection or null if an Error occurs.
     */
    private static Connection createConnection() {
        try {
            return DataSourceFactory.getMySQLDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates the Basic Connection tool.
     *
     * @param maxPoolSize the maximal number of Connection in the Pool.
     * @return the Connection Pool with n Connection.
     * @throws RuntimeException if there is no Connection in the Pool.
     */
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

    /**
     * Gets the first Idle Connection of the Pool, if the Pool is empty, a new Connection is created if maxPoolSize not
     * Reached.
     *
     * @return The SQL-Connection
     * @throws SQLException if the value supplied for timeout is less than 0
     */
    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty() && usedConnections.size() < maxPoolSize) {
            Connection con = createConnection();
            if (con != null) {
                connectionPool.add(createConnection());
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

    /**
     * Release the Connection and add it to the Pool;
     *
     * @param connection the current SQL-Connection
     * @return true if the Connection is released otherwise false.
     */
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
