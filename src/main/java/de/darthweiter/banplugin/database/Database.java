package de.darthweiter.banplugin.database;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static final BasicConnectionPool connectionPool = BasicConnectionPool.create(10);
    private static final String COMMA = ",";
    private static final String SPACE = " ";
    private static final String QUESTION_MARK = "?";
    private static final String EQUAL = "=";
    private static final String SQL_UUID = "uuid";
    private static final String SQL_NAME = "name";
    private static final String SQL_LAST_IP = "last_ip";
    private static final String SQL_LAST_LOGIN = "last_login";
    public static final String SQL_IS_BANNED = "is_banned";
    public static final String SQL_BAN_REASON = "ban_reason";
    public static final String SQL_BAN_EXPIRES_TIME = "ban_expires_time";
    public static final String SQL_BAN_IS_PERMANENT = "is_permanent";
    private static final String SQL_BANNED_BY = "banned_by";

    private Database() {
    }

    public static Map<String, String> selectUUID(String uuid) {
        Map<String, String> result = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *")
                .append("FROM spieleruebersicht")
                .append(SPACE).append("WHERE").append(SPACE).append(SQL_UUID).append(EQUAL).append(QUESTION_MARK);

        try {
            Connection con = connectionPool.getConnection();
            PreparedStatement preStmt = con.prepareStatement(sql.toString());
            preStmt.setString(1, uuid);
            ResultSet rs = preStmt.executeQuery();

            if (rs.next()) {
                String isBanned = String.valueOf(rs.getBoolean(SQL_IS_BANNED));
                String reason = rs.getString(SQL_BAN_REASON);
                Timestamp ts = rs.getTimestamp(SQL_BAN_EXPIRES_TIME);
                String timestamp = "";
                String permanentBan = String.valueOf(rs.getBoolean(SQL_BAN_IS_PERMANENT));
                if (ts != null) {
                    timestamp = String.valueOf(rs.getTimestamp(SQL_BAN_EXPIRES_TIME).getTime());
                }
                result = new HashMap<>();
                result.put(SQL_IS_BANNED, isBanned);
                result.put(SQL_BAN_REASON, reason);
                result.put(SQL_BAN_EXPIRES_TIME, timestamp);
                result.put(SQL_BAN_IS_PERMANENT, permanentBan);
            }

            rs.close();
            preStmt.close();
            connectionPool.releaseConnection(con);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void updateBanInfosWithUUID(String uuid, String reason, Timestamp expires, boolean isBanned, boolean isPermanent, String nameOfExecutor) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE spieleruebersicht ").append("SET ")
                .append(SQL_IS_BANNED).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_BAN_REASON).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_BAN_EXPIRES_TIME).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_BAN_IS_PERMANENT).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_BANNED_BY).append(EQUAL).append(QUESTION_MARK).append(SPACE)
                .append("WHERE").append(SPACE).append(SQL_UUID).append(EQUAL).append(QUESTION_MARK);
        try {
            Connection con = connectionPool.getConnection();
            PreparedStatement preStmt = con.prepareStatement(sql.toString());
            preStmt.setBoolean(1, isBanned);
            preStmt.setString(2, reason);
            if (expires == null) {
                preStmt.setNull(3, Types.TIMESTAMP);
            } else {
                preStmt.setTimestamp(3, expires);
            }
            preStmt.setBoolean(4, isPermanent);
            preStmt.setString(5, nameOfExecutor);
            preStmt.setString(6, uuid);
            preStmt.executeUpdate();
            preStmt.close();
            connectionPool.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertNewEntryOrUpdate(String uuid, String name, String ipAddress, Timestamp lastLogin) {
        StringBuilder sql = new StringBuilder();

        sql.append("INSERT INTO spieleruebersicht ").
                append("(")
                .append(SQL_UUID).append(COMMA).append(SPACE)
                .append(SQL_NAME).append(COMMA).append(SPACE)
                .append(SQL_LAST_IP).append(COMMA).append(SPACE)
                .append(SQL_LAST_LOGIN).append(COMMA).append(SPACE)
                .append(SQL_IS_BANNED)
                .append(")")
                .append("VALUES (")
                .append(QUESTION_MARK).append(COMMA)
                .append(QUESTION_MARK).append(COMMA)
                .append(QUESTION_MARK).append(COMMA)
                .append(QUESTION_MARK).append(COMMA)
                .append(QUESTION_MARK)
                .append(")").append(SPACE)
                .append("ON DUPLICATE KEY UPDATE").append(SPACE)
                .append(SQL_NAME).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_LAST_IP).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_LAST_LOGIN).append(EQUAL).append(QUESTION_MARK).append(COMMA)
                .append(SQL_IS_BANNED).append(EQUAL).append(QUESTION_MARK);

        try {
            Connection con = connectionPool.getConnection();
            PreparedStatement preStmt = con.prepareStatement(sql.toString());
            preStmt.setString(1, uuid);
            preStmt.setString(2, name);
            preStmt.setString(3, ipAddress);
            preStmt.setTimestamp(4, lastLogin);
            preStmt.setBoolean(5, false);
            preStmt.setString(6, name);
            preStmt.setString(7, ipAddress);
            preStmt.setTimestamp(8, lastLogin);
            preStmt.setBoolean(9, false);
            preStmt.executeUpdate();
            preStmt.close();
            connectionPool.releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void shutdownPool() {
        try {
            connectionPool.shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

