/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author cmjd
 */
public class Handle {

    public static int setData(Connection connection, String query, Object[] data) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        for (int i = 0; i < data.length; i++) {
            ps.setObject(i + 1, data[i]);
        }
        return ps.executeUpdate();
    }

    public static ResultSet getData(Connection connection, String query) throws SQLException {
        Statement stm = connection.createStatement();
        return stm.executeQuery(query);

    }

    public static int updateData(Connection connection, String query, Object[] data) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeUpdate(query);
    }
}
