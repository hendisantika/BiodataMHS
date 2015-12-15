/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hendi.koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Uchiha Itachi
 */
public class DatabaseConnection {
    public static Connection getKoneksi(){
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/akademik";
        String user = "root";
        String password = "";
        if(con == null){
            try {
                Class.forName(driver);
                con = DriverManager.getConnection(url, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Terjadi error!\nDengan Pesan :" 
                        + e.getMessage());
            }
        }
        return con;
    } 
    
}
