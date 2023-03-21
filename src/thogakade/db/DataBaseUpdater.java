/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saranga
 */
public class DataBaseUpdater {

    public void runDbUpdate() {
        boolean success = true;

        if (!checkTableExsist("SYSTEM_SETTINGS_TABLE")) {
            String sql = "CREATE TABLE   SYSTEM_SETTINGS_TABLE "
                    + "(id serial not NULL, "
                    + " key VARCHAR(255), "
                    + " value VARCHAR(255), "
                    + " PRIMARY KEY ( id ))";
            success = excecuteQuery(sql);
            System.out.println("SETTINGS_TABLE  Inserted");
        }

        if (!checkTableExsist("FILE_DOWNLOAD_LINK")) {
            String sql = "CREATE TABLE   FILE_DOWNLOAD_LINK "
                    + "(id serial not NULL, "
                    + " link VARCHAR(255), "
                    + " status VARCHAR(255), "
                    + " PRIMARY KEY ( id ))";
            success = excecuteQuery(sql);
            System.out.println("download_link  Inserted");

        }

        if (checkTableExsist("FILE_DOWNLOAD_LINK")) {
            String sql = "ALTER TABLE FILE_DOWNLOAD_LINK ADD COLUMN IF NOT EXISTS type INTEGER;";
            success = excecuteQuery(sql);
            System.out.println("download_type  Inserted");

        }
        if (checkTableExsist("FILE_DOWNLOAD_LINK")) {
            String sql = "ALTER TABLE FILE_DOWNLOAD_LINK ADD COLUMN IF NOT EXISTS downloadtime VARCHAR(255);";
            success = excecuteQuery(sql);
            System.out.println("downloadtime  Inserted");

        }
        if (!checkTableExsist("DOWNLOAD_DATA")) {
            String sql = "CREATE TABLE   DOWNLOAD_DATA "
                    + "(id serial not NULL, "
                    + " datetime VARCHAR(255), "
                    + " status VARCHAR(255), "
                    + " PRIMARY KEY ( id ))";
            success = excecuteQuery(sql);
            System.out.println("DOWNLOAD_DATA  Inserted");

        }
    }

    private boolean checkTableExsist(String tablename) {
        boolean tableExists = false;
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rset = conn.getMetaData().getTables(null, null, tablename, null);
            if (rset.next()) {
                tableExists = true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseUpdater.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseUpdater.class.getName()).log(Level.SEVERE, null, ex);

        }
        return tableExists;
    }

    private boolean excecuteQuery(String sql) {
        boolean success = true;
        // JDBC driver name and database URL 
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver 

            //STEP 2: Open a connection 
            System.out.println("Connecting to database...");
            Connection conn = DBConnection.getConnection();
            //STEP 3: Execute a query 
            System.out.println("Creating table in  database...");
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment 
            stmt.close();
        } catch (SQLException se) {
            success = false;
            //Handle errors for JDBC 
            se.printStackTrace();
        } catch (Exception e) {
            success = false;
            //Handle errors for Class.forName 
            e.printStackTrace();
        } finally {
            //finally block used to close resources 
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            } // nothing we can do 
            //end finally try 
        } //end try 
        System.out.println("Goodbye!");
        return success;
    }
}
