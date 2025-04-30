/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeguii.koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Dliyaa'ul
 */
public class koneksi {
    private static Connection conn;
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "akubakulcs";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String DB_UNAME = "root";
    private static final String DB_PASS = "Opoae_123";

    public static Connection bukaKoneksi() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, DB_UNAME, DB_PASS);
            } catch (ClassNotFoundException e) {
                System.err.format("Class not found");
            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static PreparedStatement prepareStatement(String KaryawanQuery) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
