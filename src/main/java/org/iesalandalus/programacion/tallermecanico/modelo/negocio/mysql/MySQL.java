package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private static final String HOST = "174.129.78.34";
    private static final String ESQUEMA = "tallerMecanico";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root";
    private static Connection conexion;

    private MySQL(){

    }

    public static Connection getConnection(){
        if (conexion != null) {
            return conexion;
        }

        try
        {
            conexion = DriverManager.getConnection("jdbc:mysql://"+HOST+"/"+ESQUEMA,USUARIO,CONTRASENA);
            System.out.println("Conexion realizada con exito");
        }
        catch (SQLException e) {
            System.out.println("ERROR MySQL:  "+ e);
        }

        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
                conexion = null;
                System.out.println("Conexión a MySQL cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("ERROR MySQL: "+ e.toString());
        }
    }

}
