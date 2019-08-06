package com.base.files.common;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class DBUtil {

    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());

    public static void executeSqlScript(Connection conn, String query) throws SQLException, FileNotFoundException {
        InputStream stream = new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8));
        executeSqlScript(conn, stream);
    }
    private Date now = null;
    private int count = 1;
    final private PreparedStatement ps;

    public DBUtil(final PreparedStatement ps) {
        this.ps = ps;
    }

    public void addBatch() throws SQLException {
        ps.addBatch();
        count = 1;
    }

    public static void attemptClose(final Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void attemptClose(final Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void attemptClose(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Boolean getNullableBoolean(final ResultSet rs, final String colName) throws SQLException {
        final Boolean val = rs.getBoolean(colName);
        return rs.wasNull() ? null : val;
    }

    public static String getNullableString(final ResultSet rs, final String colName) throws SQLException {
        final String val = rs.getString(colName);
        return rs.wasNull() ? null : val;
    }

    public static Integer getNullableInt(final ResultSet rs, final String colName) throws SQLException {
        final Integer val = rs.getInt(colName);
        return rs.wasNull() ? null : val;
    }

    public static Long getNullableLong(final ResultSet rs, final String colName) throws SQLException {
        final Long val = rs.getLong(colName);
        return rs.wasNull() ? null : val;
    }
    
    public static Double getNullableDouble(final ResultSet rs, final String colName) throws SQLException {
        final Double val = rs.getDouble(colName);
        return rs.wasNull() ? null : val;
    }
    
    public static Float getNullableFloat(final ResultSet rs, final String colName) throws SQLException {
        final Float val = rs.getFloat(colName);
        return rs.wasNull() ? null : val;
    }

    public static byte[] getNullableBytes(final ResultSet rs, final String colName) throws SQLException {
        final byte[] val = rs.getBytes(colName);
        return rs.wasNull() ? null : val;
    }

    public static Date getNullableDate(final ResultSet rs, final String colName) throws SQLException {
        final java.sql.Timestamp val = rs.getTimestamp(colName);
        return rs.wasNull() ? null : new Date(val.getTime());
    }

    public static void attemptRollback(final Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setNowTimestamp() throws SQLException {
        if (this.now == null) {
            this.now = new Date();
        }
        this.setNullableTimestamp(this.now);
    }

    public void setNullableTimestamp() throws SQLException {
        this.setNullableTimestamp(null);
    }

    public static void loadPS(final PreparedStatement ps, final String... params) throws SQLException {
        final DBUtil dBUtil = new DBUtil(ps);
        for (final String str : params) {
            dBUtil.setNullableString(str);
        }
    }

    public static void loadPS(final PreparedStatement ps, final Long... params) throws SQLException {
        final DBUtil dBUtil = new DBUtil(ps);
        for (final Long longParam : params) {
            dBUtil.setNullableLong(longParam);
        }
    }

    public static void loadPS(final PreparedStatement ps, final Long longParam, final String... params) throws SQLException {
        final DBUtil dBUtil = new DBUtil(ps);
        dBUtil.setNullableLong(longParam);
        for (final String strParam : params) {
            dBUtil.setNullableString(strParam);
        }
    }

    public void setNullableBoolean(final Boolean val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.BOOLEAN);
        } else {
            ps.setBoolean(count, val);
        }
        count++;
    }

    public void setNullableString(final String val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.VARCHAR);
        } else {
            ps.setString(count, val);
        }
        count++;
    }

    public void setNullableLong(final Long val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.BIGINT);
        } else {
            ps.setLong(count, val);
        }
        count++;
    }

    public void setNullableInt(final Integer val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.INTEGER);
        } else {
            ps.setInt(count, val);
        }
        count++;
    }

    public void setNullableTimestamp(final java.util.Date val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.TIMESTAMP);
        } else {
            ps.setTimestamp(count, new java.sql.Timestamp(val.getTime()));
        }
        count++;
    }

    public void setNullableDouble(final Double val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.DOUBLE);
        } else {
            ps.setDouble(count, val);
        }
        count++;
    }

    public void setNullableFloat(final Float val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.FLOAT);
        } else {
            ps.setFloat(count, val);
        }
        count++;
    }

    public void setNullableBytes(final byte[] val) throws SQLException {
        if (val == null) {
            ps.setNull(count, java.sql.Types.BLOB);
        } else {
            ps.setBytes(count, val);
        }
        count++;
    }

    public static String questionMarks(int count) {
        if (count < 1) {
            throw new IllegalArgumentException();
        }
        String retVal = "?";
        for (int i = 1; i < count; i++) {
            retVal += ", ?";
        }
        return retVal;
    }

    public static void executeSqlScript(final Connection conn, final InputStream inputStream) throws SQLException {

        // Delimiter
        final String delimiter = ";";

        // Create scanner
        final Scanner scanner;
        scanner = new Scanner(inputStream).useDelimiter(delimiter);

        // Loop through the SQL file statements 
        Statement currentStatement = null;
        while (scanner.hasNext()) {

            // Get statement 
            final String next = scanner.next().replaceAll("^\\n+", "").replaceAll("\\n+$", "");

            if (next.equals("")) {
                System.out.println("Skipping empty");
                continue;
            } else {
                System.out.println("Executing: " + next);
            }

            String rawStatement = next + delimiter;
            try {
                // Execute statement
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            } finally {
                // Release resources
                if (currentStatement != null) {
                    DBUtil.attemptClose(currentStatement);
                }
                currentStatement = null;
            }
        }
    }

}
