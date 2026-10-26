package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Clientes implements IClientes {

    static File jsonFile = new File("datos/json/clientes.json");
    private static final String RAIZ = "clientes";
    private static final String CLIENTE = "cliente";
    private static final String NOMBRE = "nombre";
    private static final String DNI = "dni";
    private static final String TELEFONO = "telefono";
    ObjectMapper mapper = new ObjectMapper();

    private final List<Cliente> coleccionClientes;
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

    private List<Cliente> leer() {
        Map json = Map.of();
        try
        {
            // Leer fichero JSON y mapearlo a un Map
            json = mapper.readValue(jsonFile, Map.class);
        }
        catch (StreamReadException e)
        {
            System.out.println(" ERROR: El fichero json " + jsonFile.getName() + " presenta errores de sintaxis");
            System.out.println(e.getMessage());
        }
        catch(DatabindException e)
        {
            System.out.println("ERROR: El fichero json " + jsonFile.getName() + " presenta errores relacionados con el mapeo.");
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(" ERROR de entrada/salida en el fichero " + jsonFile.getName() );
            System.out.println(e.getMessage());
        }

        for (Object cliente1 : json.values()){
            coleccionClientes.add((Cliente) cliente1);
        }

        return coleccionClientes;
    }

    @Override
    public void terminar() {

    }

    private void escribir(List<Cliente> clientes) {
        try
        {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, clientes);
        }
        catch ( StreamWriteException e)
        {
            System.out.println(" ERROR: El fichero json " + jsonFile.getName() + " presenta errores de sintaxis");
            System.out.println(e.getMessage());
        }
        catch(DatabindException e)
        {
            System.out.println("ERROR: El fichero json " + jsonFile.getName() + " presenta errores relacionados con el mapeo.");
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(" ERROR de entrada/salida en el fichero " + jsonFile.getName() );
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Cliente> get() {
        return new ArrayList<>(coleccionClientes);
    }

    @Override
    public void insertar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede insertar un cliente nulo.");
        if (coleccionClientes.contains(cliente)) {
            throw new TallerMecanicoExcepcion("Ya existe un cliente con ese DNI.");
        }
        coleccionClientes.add(cliente);
    }

    @Override
    public Cliente modificar(Cliente cliente, String nombre, String telefono) throws TallerMecanicoExcepcion {
        try
        {
            // Leer el JSON como una lista de objetos Empleado
            List<Cliente> listaEmpleados = mapper.readValue(jsonFile, new TypeReference<>() {});

            // ---- MODIFICAR UN CLIENTE - Nombre --
            for (Cliente cliente1 : listaEmpleados) {
                if (Objects.equals(cliente1.getDni(), cliente.getDni())) {
                    cliente.setNombre(nombre);
                }
            }

            // ---- MODIFICAR UN CLIENTE - Telefono --
            for (Cliente cliente1 : listaEmpleados) {
                if (Objects.equals(cliente1.getDni(), cliente.getDni())) {
                    cliente.setTelefono(telefono);
                }
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, listaEmpleados);
        }
        catch (StreamReadException | StreamWriteException e)
        {
            System.out.println(" ERROR: El fichero json " + jsonFile.getName() + " presenta errores de sintaxis");
            System.out.println(e.getMessage());
        }
        catch(DatabindException e)
        {
            System.out.println("ERROR: El fichero json " + jsonFile.getName() + " presenta errores relacionados con el mapeo.");
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(" ERROR de entrada/salida en el fichero " + jsonFile.getName() );
            System.out.println(e.getMessage());
        }
        return cliente;
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        Objects.requireNonNull(cliente, "No se puede buscar un cliente nulo.");
        int indice = coleccionClientes.indexOf(cliente);
        return (indice == -1) ? null : coleccionClientes.get(indice);
    }

    @Override
    public void borrar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "No se puede borrar un cliente nulo.");
        Iterator<Cliente> iterator = coleccionClientes.iterator();
        while (iterator.hasNext()) {
            Cliente cliente1 = iterator.next();
            if (cliente.getDni() == cliente1.getDni()) {
                iterator.remove();
            }
        }
        coleccionClientes.remove(cliente);
    }
}
