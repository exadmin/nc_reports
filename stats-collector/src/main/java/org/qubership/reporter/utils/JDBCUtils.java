package org.qubership.reporter.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

    public static List<String> doSingleColumnSelect(Connection connection, String sqlQuery, String ... binds) {
        List<String> result = new ArrayList<>();

        try (PreparedStatement pstm = connection.prepareStatement(sqlQuery)) {

            if (binds != null) {
                int bindIndex = 1;
                for (String bind : binds) {
                    pstm.setString(bindIndex, bind);
                    bindIndex++;
                }
            }

            pstm.execute();

            try (ResultSet resultSet = pstm.getResultSet()) {
                if (resultSet != null) {
                    while (resultSet.next()) {
                        result.add(resultSet.getString(1));
                    }
                }
            }
        } catch (SQLException sqlEx) {
            throw new IllegalStateException(sqlEx);
        }

        return result;
    }
}
