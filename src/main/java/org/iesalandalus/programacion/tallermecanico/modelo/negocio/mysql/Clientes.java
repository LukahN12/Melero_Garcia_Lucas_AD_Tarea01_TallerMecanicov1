package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mysql;

import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;

import java.util.ArrayList;
import java.util.List;

public class Clientes implements IClientes {
    private List<Cliente> coleccionClientes;
    private static Clientes instancia;

    private Clientes() {
        coleccionClientes = new ArrayList<>();
    }

    public static Clientes getInstancia() {
        if (instancia == null) {
            instancia = new Clientes();
        }
        return instancia;
    }

    @Override
    public void comenzar() {

    }

    @Override
    public void terminar() {

    }

    @Override
    public List<Cliente> get() {
        return List.of();
    }

    @Override
    public void insertar(Cliente cliente) throws TallerMecanicoExcepcion {

    }

    @Override
    public Cliente modificar(Cliente cliente, String nombre, String telefono) throws TallerMecanicoExcepcion {
        return null;
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        return null;
    }

    @Override
    public void borrar(Cliente cliente) throws TallerMecanicoExcepcion {

    }
}
