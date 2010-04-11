package com.google.code.paster.util;

import com.google.code.paster.entity.KbItem;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.DefaultListModel;

/**
 * Wrapper for the SQLite database connection
 * Handles DB connections and CRUD operations
 *
 * Database has one table, kbitems, which has the following structure:
 *
 *   CREATE TABLE "kbitems" ("id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
 *                           "title" VARCHAR NOT NULL , "text" TEXT NOT NULL ) ;
 *
 * @author Bram Borggreve
 */
public class KbdataConnector {

    public static Connection kbConnection = null;
    public static HashMap<Integer, KbItem> kbItemMap = null;

    /**
     * Opens a Connection to the SQLite database
     *
     * @return Connection handler
     */
    static Connection makeConnection() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db/paster.sq3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Retrieve the model from the Database
     * @return
     */
    public DefaultListModel getModel() {
        DefaultListModel dlm = new DefaultListModel();
        kbItemMap = getItemMap();
        KbItem thisItem = null;
        for (int i = 0; i < kbItemMap.size(); i++) {
            thisItem = kbItemMap.get(i);
            dlm.add(i, kbItemMap.get(i));
        }
        return dlm;
    }

    /**
     * Retrieve the Items from the database
     * @return Hasmap with the items
     * @throws Exception
     */
    public static HashMap<Integer, KbItem> getData() throws Exception {
        Statement stat = kbConnection.createStatement();
        String query = "SELECT id, title, text FROM kbitems";
        ResultSet rs = stat.executeQuery(query);
        HashMap<Integer, KbItem> newKbMap = new HashMap<Integer, KbItem>();
        KbItem newKb = null;
        int i = 0;
        while (rs.next()) {
            int id = Integer.parseInt(rs.getString("id"));
            String title = rs.getString("title");
            String text = rs.getString("text");
            newKb = new KbItem(id, title, text);
            newKbMap.put(i, newKb);
            i++;
        }
        rs.close();
        return newKbMap;
    }

    /**
     * Retrieve a specific item from thje Items Hashmap
     * @param id
     * @return
     */
    public KbItem getItem(int id) {
        kbItemMap = getItemMap();
        return kbItemMap.get(id);
    }

    /**
     * Puts the KbItems in a HashMap
     * @return
     */
    private HashMap<Integer, KbItem> getItemMap() {
        HashMap<Integer, KbItem> itemMap = null;
        try {
            // Create the connection
            kbConnection = makeConnection();
            // Get the data
            itemMap = getData();
            // Close the connection
            kbConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemMap;
    }

    /**
     * Insert a new KbItem in the database
     * @param title new Title
     * @param text new Text
     */
    public void insertItem(String title, String text) {
        try {
            kbConnection = makeConnection();
            Statement stat = kbConnection.createStatement();
            text = text.replaceAll("'", "");
            String query = "INSERT INTO kbitems (title, text) VALUES ('" + title + "','" + text + "');";
            stat.executeUpdate(query);
            kbConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an existing KbItem
     *
     * @param id Item to Update
     * @param title Updated Title
     * @param text Updated Text
     */
    public void updateItem(int id, String title, String text) {
        try {
            kbConnection = makeConnection();
            Statement stat = kbConnection.createStatement();
            String query = "UPDATE kbitems SET title = '" + title + "' , text = '" + text + "'" + " WHERE id = " + id;
            stat.executeUpdate(query);
            kbConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete an existing KbItem
     *
     * @param delItem
     */
    public void deleteItem(KbItem delItem) {
        try {
            kbConnection = makeConnection();
            Statement stat = kbConnection.createStatement();
            String query = "DELETE FROM kbitems WHERE id = " + delItem.getId();
            stat.executeUpdate(query);
            kbConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
