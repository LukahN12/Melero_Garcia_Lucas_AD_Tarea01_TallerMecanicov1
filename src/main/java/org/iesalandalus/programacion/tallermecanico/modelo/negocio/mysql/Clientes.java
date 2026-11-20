package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mysql;

import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Clientes implements IClientes {
    private static Clientes instancia;
    private Connection conexion;

    private Clientes() {
    }

    public static Clientes getInstancia() {
        if (instancia == null) {
            instancia = new Clientes();
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
    public List<Cliente> get() {
        if (conexion == null){
            System.out.println("ES NULA!");
        }

        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(sql);
            while (resultado.next()) {
                Cliente cliente = new Cliente(resultado.getString("nombre"),
                        resultado.getString("dni"),
                        resultado.getString("telefono"));
                listaClientes.add(cliente);
            }
            resultado.close();
            sentencia.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return listaClientes;
    }

    @Override
    public void insertar(Cliente cliente) throws TallerMecanicoExcepcion {
        int cantidad = 0;
        if (buscar(cliente) != null) {
            System.out.println("El cliente a insertar existe: " + cliente.getDni());
        } else {
            String sql = "INSERT INTO clientes (nombre, dni, telefono) VALUES (?, ?, ?)";
            try {
                System.out.println("Sentencia: " + sql);
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, cliente.getNombre());
                sentencia.setString(2, cliente.getDni());
                sentencia.setString(3, cliente.getTelefono());
                int filas = sentencia.executeUpdate();
                System.out.println("Insertado:" + filas);
                sentencia.close();
            } catch (SQLException e) {

                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public Cliente modificar(Cliente cliente, String nombre, String telefono) throws TallerMecanicoExcepcion {
        String sql = "UPDATE clientes set nombre " + nombre + " telefono " + telefono + "  where dni = " + cliente.getDni();
        try {
            System.out.println("Sentencia: " + sql);
            Statement sentencia = conexion.createStatement();
            int filas = sentencia.executeUpdate(sql);
            System.out.println("Actualizados:" + filas);
            sentencia.close();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        String sql2 = "SELECT * From cliente where dni = " + cliente.getDni();
        Cliente resultadoCliente = null;
        try {
            PreparedStatement sentencia = (PreparedStatement) conexion.prepareStatement(sql2);

            sentencia.setInt(1, 10);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
               resultadoCliente = Cliente.get(resultado.getString(2));
            }
            resultado.close();

            sentencia.close();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        return resultadoCliente;
    }

    @Override
    public Cliente buscar(Cliente cliente) {

        Cliente buscado = null;
        if (cliente == null) {
            System.out.println("No se puede buscar un cliente nulo");
        } else {
            String sql = "SELECT * FROM clientes WHERE dni = ?" ;
            try {
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1,cliente.getDni());
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    buscado = new Cliente(resultado.getString("nombre"),
                            resultado.getString("dni"),
                            resultado.getString("telefono"));
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
    public void borrar(Cliente cliente) throws TallerMecanicoExcepcion {
        if (buscar(cliente) == null) {
            System.out.println("El departamento a borrar no existe: " + cliente.getDni());
        } else {
                String sql = "DELETE from clientes where dni = " + cliente.getDni();
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
