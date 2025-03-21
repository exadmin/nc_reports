package org.qubership.reporter.utils;

import java.sql.Connection;

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
}
