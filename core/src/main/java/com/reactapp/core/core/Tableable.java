package com.reactapp.core.core;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.http.util.TextUtils.isBlank;

public abstract class Tableable {

    protected static final String DELETE_BASE_QUERY = "DELETE FROM %s WHERE %s";
    protected static final String INSERT_BASE_QUERY = "INSERT INTO %s (%s) values %s";
    protected static final String UPDATE_BASE_QUERY = "UPDATE %s set %s";
    protected static final String SELECT_BASE_QUERY = "SELECT %s FROM %s";
    protected static final String WHERE_CLAUSE = " WHERE ";

    public abstract String getTable();

    public String selectQuery(String... keys) {
        return this.selectQueryWithWhereClause("", keys);
    }

    public String selectQueryWithWhereClause(String whereClause, String... keys) {
        String sql = String.format(
                SELECT_BASE_QUERY,
                Arrays.stream(keys).collect(Collectors.joining(",")),
                getTable()
        );
        return isBlank(whereClause) ? sql : sql + WHERE_CLAUSE + whereClause;
    }

    public String insertQuery(String... keys) {
        return String.format(
                INSERT_BASE_QUERY,
                getTable(),
                Arrays.stream(keys).collect(Collectors.joining(",")),
                "(" + Arrays.stream(keys).map(v -> "?").collect(Collectors.joining(",")) + ")"
        );
    }

    public String updateQuery(String... keys) {
        return updateQueryWithWhereClause("id = ?", keys);
    }

    public String updateQueryWithWhereClause(String whereClause, String... keys) {
        String sql = String.format(
                UPDATE_BASE_QUERY,
                getTable(),
                Arrays.stream(keys).map(k -> k + "=?").collect(Collectors.joining(","))
        );
        return isBlank(whereClause) ? sql : sql + WHERE_CLAUSE + whereClause;
    }

    public String deleteQuery() {
        return this.deleteQuery("id=?");
    }

    public String deleteQuery(String whereClause) {
        return String.format(DELETE_BASE_QUERY, getTable(), whereClause);
    }

    /**
     * Just to be easier
     *
     * @param ps
     * @param params
     */
    public void paramsToPreparedStatement(PreparedStatement ps, Map<String, Object> params) {
        try {
            Integer i = 0;
            for (Object value : params.values()) {
                if (value instanceof Integer) {
                    ps.setInt(i + 1, (Integer) value);
                } else if (value instanceof String) {
                    ps.setString(i + 1, (String) value);
                } else if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
                    ps.setBigDecimal(i + 1, new BigDecimal(value.toString()));
                }
                i++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createSQLWithWhereClause(String sqlBase, Map<String, Object> params) {
        if (params.isEmpty()) return sqlBase;

        String whereKeys = params.keySet().stream().map(key -> key + "=?")
                .collect(Collectors.joining(" OR "));
        return sqlBase + WHERE_CLAUSE + whereKeys;
    }

    protected void close(Connection connection, PreparedStatement ps) {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
            if (ps != null && !ps.isClosed()) ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected PreparedStatement prepareUpdateStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

}
