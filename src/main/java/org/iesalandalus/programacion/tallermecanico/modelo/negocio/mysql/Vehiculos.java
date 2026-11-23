package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mysql;

import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Vehiculos implements IVehiculos {
    private static Vehiculos instancia;
    private Connection conexion;

    private Vehiculos() {
    }

    public static Vehiculos getInstancia() {
        if (instancia == null) {
            instancia = new Vehiculos();
        }
        return instancia;
    }

    @Override
    public void comenzar() {
        conexion = MySQL.getConnection();
    }

    @Override
    public void terminar() {
        MySQL.cerrarConexion();
    }

    @Override
    public List<Vehiculo> get() {
        if (conexion == null){
            System.out.println("ES NULA!");
        }

        List<Vehiculo> listaVehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            while (resultado.next()) {
                Vehiculo vehiculo = new Vehiculo(resultado.getString("nombre"),
                        resultado.getString("dni"),
                        resultado.getString("telefono"));
                listaVehiculos.add(vehiculo);
            }
            resultado.close();
            sentencia.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return listaVehiculos;
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        if (buscar(vehiculo) != null) {
            System.out.println("El vehiculo a insertar existe: " + vehiculo.matricula());
        } else {
            String sql = "INSERT INTO vehiculos (marca,matricula,modelo) VALUES (?, ?, ?)";
            try {
                System.out.println("Sentencia: " + sql);
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, vehiculo.marca());
                sentencia.setString(2, vehiculo.matricula());
                sentencia.setString(3, vehiculo.modelo());
                int filas = sentencia.executeUpdate();
                System.out.println("Insertado:" + filas);
                sentencia.close();
            } catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        Vehiculo buscado = null;
        if (vehiculo == null) {
            System.out.println("No se puede buscar un vehiculo nulo");
        } else {
            String sql = "SELECT * FROM vehiculos WHERE matricula = ?" ;
            try {
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1,vehiculo.matricula());
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    buscado = new Vehiculo(resultado.getString("marca"),
                            resultado.getString("matricula"),
                            resultado.getString("modelo"));
                }
                resultado.close();

                sentencia.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return buscado;
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        if (buscar(vehiculo) == null) {
            System.out.println("El vehiculo a borrar no existe: " + vehiculo.matricula());
        } else {
            String sql = "DELETE from vehiculos where matricula = " + vehiculo.matricula();
            try {
                System.out.println("Sentencia: " + sql);
                Statement sentencia = conexion.createStatement();
                int filas = sentencia.executeUpdate(sql);
                sentencia.close();
                System.out.println("Borrado:" + filas);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

