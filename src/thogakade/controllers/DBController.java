/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import thogakade.Shared.Shared;
import thogakade.db.DBConnection;
import thogakade.db.Handle;
import thogakade.model.Customer;
import thogakade.model.DownloadData;
import thogakade.model.Link;
import thogakade.model.SettingsData;

/**
 *
 * @author cmjd
 */
public class DBController {

    public static int addCustomer(Customer customer) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        Object[] data = {customer.getId(), customer.getName(), customer.getAddress(), customer.getSalary()};
        String query = "INSERT INTO Customer VALUES(?,?,?,?)";
        return Handle.setData(connection, query, data);
    }

    public static Customer searchCustomer(String id) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM Customer WHERE id='" + "'";
        ResultSet rst = Handle.getData(connection, query);
        Customer c = new Customer();
        while (rst.next()) {
            c.setId(rst.getString(1));
            c.setName(rst.getString(2));
            c.setAddress(rst.getString(3));
            c.setSalary(rst.getDouble(4));
        }
        return c;

    }

    public int addSttings(SettingsData obj) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        SettingsData dataobj = getMaxId();
        Integer id = 0;
        if (dataobj != null && dataobj.getId() != null) {
            id = dataobj.getId();
        }
        id = id + 1;
        Object[] data = {id, obj.getKey(), obj.getValue()};
        String query = "INSERT INTO SYSTEM_SETTINGS_TABLE VALUES(?,?,?)";
        return Handle.setData(connection, query, data);
    }

    public int updatettings(SettingsData obj) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "Update SYSTEM_SETTINGS_TABLE set value='" + obj.getValue() + "'";
        return Handle.updateData(connection, query, null);
    }

    public SettingsData searchSetting(String id) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM SYSTEM_SETTINGS_TABLE WHERE key='" + id + "'";
        ResultSet rst = Handle.getData(connection, query);
        SettingsData c = new SettingsData();
        while (rst.next()) {
            c.setId(rst.getInt(1));
            c.setKey(rst.getString(2));
            c.setValue(rst.getString(3));
        }
        return c;

    }

    public SettingsData getMaxId() throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM SYSTEM_SETTINGS_TABLE Order by id desc limit 1";
        ResultSet rst = Handle.getData(connection, query);
        SettingsData c = new SettingsData();
        while (rst.next()) {
            c.setId(rst.getInt(1));
            c.setKey(rst.getString(2));
            c.setValue(rst.getString(3));
        }
        return c;

    }

    public SettingsData getMaxIdFOrDLDATA() throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM DOWNLOAD_DATA Order by id desc limit 1";
        ResultSet rst = Handle.getData(connection, query);
        SettingsData c = new SettingsData();
        while (rst.next()) {
            c.setId(rst.getInt(1));
            c.setKey(rst.getString(2));
            c.setValue(rst.getString(3));
        }
        return c;

    }

    public Link getMaxIdForLinks() throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM file_download_link Order by id desc limit 1";
        ResultSet rst = Handle.getData(connection, query);
        Link c = new Link();
        while (rst.next()) {
            c.setId(rst.getInt(1));
        }
        return c;

    }

    public ArrayList<Link> getLinks() throws ClassNotFoundException, SQLException {
        ArrayList<Link> linklist = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM file_download_link Order by id desc";
        ResultSet rst = Handle.getData(connection, query);
        while (rst.next()) {
            Link c = new Link();
            c.setLink(rst.getString(2));
            c.setStatus(rst.getString(3));
            linklist.add(c);
        }
        return linklist;

    }

    public ArrayList<Link> getLinksForSchedule() throws ClassNotFoundException, SQLException {
        ArrayList<Link> linklist = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM file_download_link where type=2 and status='Pending' Order by id desc";
        ResultSet rst = Handle.getData(connection, query);
        while (rst.next()) {
            Link c = new Link();
            c.setLink(rst.getString(2));
            c.setStatus(rst.getString(3));
            linklist.add(c);
        }
        return linklist;

    }
 public ArrayList<Link> getLinksForNormal(String time) throws ClassNotFoundException, SQLException {
        ArrayList<Link> linklist = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM file_download_link where type=1  and downloadtime='" + time + "' and status='Pending'  Order by id desc";
        ResultSet rst = Handle.getData(connection, query);
        while (rst.next()) {
            Link c = new Link();
            c.setLink(rst.getString(2));
            c.setStatus(rst.getString(3));
            linklist.add(c);
        }
        return linklist;

    }
    public ArrayList<Link> getLinksForSchedule(String time) throws ClassNotFoundException, SQLException {
        ArrayList<Link> linklist = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM file_download_link where type=2 and downloadtime='" + time + "' Order by id desc";
        ResultSet rst = Handle.getData(connection, query);
        while (rst.next()) {
            Link c = new Link();
            c.setLink(rst.getString(2));
            c.setStatus(rst.getString(3));
            linklist.add(c);
        }
        return linklist;

    }
     public ArrayList<Link> getLinksForNormalbytime(String time) throws ClassNotFoundException, SQLException {
        ArrayList<Link> linklist = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM file_download_link where type=1 and downloadtime='" + time + "' Order by id desc";
        ResultSet rst = Handle.getData(connection, query);
        while (rst.next()) {
            Link c = new Link();
            c.setLink(rst.getString(2));
            c.setStatus(rst.getString(3));
            linklist.add(c);
        }
        return linklist;

    }

    public void addLinks(ArrayList<Link> objlist) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        Link dataobj = getMaxIdForLinks();
        Integer id = 0;
        if (dataobj != null && dataobj.getId() != null) {
            id = dataobj.getId();
        }
        if (objlist != null) {
            for (int i = 0; i < objlist.size(); i++) {
                Link obj = objlist.get(i);
                id = id + 1;
                Object[] data = {id, obj.getLink(), obj.getStatus(), obj.getType(), obj.getDownloadtime()};
                String query = "INSERT INTO file_download_link VALUES(?,?,?,?,?)";
                Handle.setData(connection, query, data);
            }
        }

    }

    public void updateLinks(ArrayList<Link> objlist) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        if (objlist != null) {
            for (int i = 0; i < objlist.size(); i++) {
                Link obj = objlist.get(i);
                String query = "Update file_download_link set status='" + obj.getStatus() + "'";
                Handle.updateData(connection, query, null);
            }
        }

    }

    public void updateLink(Link object) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        if (object != null) {
            String query = "Update file_download_link set status='" + object.getStatus() + "'";
            Handle.updateData(connection, query, null);
        }

    }

    public void updateDownloadDetails(String sheduleDatetime) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "Update DOWNLOAD_DATA set status='" + Shared.STATUS_COMPLETED + "' where datetime='" + sheduleDatetime + "'";
        Handle.updateData(connection, query, null);

    }

    public void addDownloadDetails(Date sheduleDatetime) throws SQLException {
        try {
            Connection connection = DBConnection.getConnection();
            SettingsData dataobj = getMaxIdFOrDLDATA();
            Integer id = 0;
            if (dataobj != null && dataobj.getId() != null) {
                id = dataobj.getId();
            }
            id = id + 1;
            Object[] data = {id, sheduleDatetime, Shared.STATUS_PENDING};
            String query = "INSERT INTO DOWNLOAD_DATA VALUES(?,?,?)";
            Handle.setData(connection, query, data);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DownloadData getDownloadDetails(String scheduletime) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM DOWNLOAD_DATA  where status='" + Shared.STATUS_COMPLETED + "' and  datetime='" + scheduletime + "'";
        ResultSet rst = Handle.getData(connection, query);
        DownloadData c = new DownloadData();
        while (rst.next()) {
            c.setId(rst.getInt(1));
            c.setStatus(rst.getString(3));
            c.setDateTime(rst.getString(2));
        }
        return c;
    }

}
