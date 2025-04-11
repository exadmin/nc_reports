package org.qubership.reporter.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCUtils {
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                if (!resultSet.isClosed()) resultSet.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void close(PreparedStatement pstm) {
        if (pstm != null) {
            try {
                if (!pstm.isClosed()) pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<String> doSingleColumnSelect(Connection connection, String sqlQuery, Object ... binds) {
        List<String> result = new ArrayList<>();

        try (PreparedStatement pstm = connection.prepareStatement(sqlQuery)) {

            if (binds != null) {
                int bindIndex = 1;
                for (Object bind : binds) {
                    try {
                        // pstm.setString(bindIndex, bind);
                        if (bind instanceof String) pstm.setString(bindIndex, (String) bind);
                        if (bind instanceof Integer) pstm.setInt(bindIndex, (Integer) bind);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    bindIndex++;
                }
            }

            TheLogger.debug("Executing DML = " + sqlQuery + ", binds = " + Arrays.toString(binds));
            pstm.execute();

            try (ResultSet resultSet = pstm.getResultSet()) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        Object value = resultSet.getObject(1);
                        result.add(value == null ? null : value.toString());
                    }
                }
            }
        } catch (SQLException sqlEx) {
            throw new IllegalStateException(sqlEx);
        }

        return result;
    }

    public static void executeDDL(Connection jdbcConnection, String sql) {
        try (Statement ddlStm = jdbcConnection.createStatement()) {
            TheLogger.debug("Executing DDL = " + sql);
            ddlStm.execute(sql);
        } catch (SQLException sqlEx) {
            throw new IllegalStateException(sqlEx);
        }
    }

    /**
     * Executes select query which must return only one number.
     * @param jdbcConnection
     * @param sqlQuery
     * @param binds
     * @return
     */
    public static int selectInt(Connection jdbcConnection, String sqlQuery, Object... binds) {
        List<String> list = doSingleColumnSelect(jdbcConnection, sqlQuery, binds);
        if (list.size() != 1) throw new IllegalStateException("Query returned more that 1 value, actual number of values is " + list.size());

        String singleValue = list.get(0);
        if (singleValue == null) return 0;
        try {
            return Integer.parseInt(singleValue);
        } catch (NumberFormatException nfe) {
            TheLogger.error("Error while parsing number from string '" + singleValue + "'");
            return 0;
        }
    }
}
