package common;

import java.io.InputStream;
import java.sql.Connection;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InitDatabase {

    public static Connection getConnection(final boolean autoCommit) throws SQLException {
        final Connection conn = DriverManager.getConnection(System.getProperty("JDBC_CONNECTION_STRING"));
        conn.setAutoCommit(autoCommit);
        return conn;
    }

    public static void initDB() throws Exception {
        try (final Connection conn = InitDatabase.getConnection(false);
                final InputStream is = new FileInputStream("sql/schema.sql")) {
            if (is == null) {
                throw new RuntimeException("File not found");
            }
            DBUtil.executeSqlScript(conn, is);
            conn.commit();
        }
    }

    public static void main(final String... args) throws Exception {
        System.out.println("Initializing the database.");
        InitDatabase.initDB();
        System.out.println("Initialization complete.");
    }
}
