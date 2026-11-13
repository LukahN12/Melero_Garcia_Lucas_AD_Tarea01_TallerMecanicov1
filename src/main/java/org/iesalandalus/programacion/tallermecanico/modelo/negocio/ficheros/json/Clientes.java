package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
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

    static File jsonFile = new File("datos/ficheros/json/clientes.json");
    private static final String RAIZ = "clientes";
    private static final String CLIENTE = "cliente";
    private static final String NOMBRE = "nombre";
    private static final String DNI = "dni";
    private static final String TELEFONO = "telefono";
    ObjectMapper mapper = JsonUtils.creadorMapas();
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
        // TODO document why this method is empty
    }

    private List<Cliente> leer() {
        try
        {
            JsonNode rootNode = mapper.readTree(jsonFile);

            // Si el JSON es una lista
            if (rootNode.isArray()) {
                return mapper.convertValue(rootNode, new TypeReference<List<Cliente>>() {});
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public void terminar() {

    }

    private void escribir(List<Cliente> clientes) {
        try {
            if (clientes.size() == 1) {
                // Si solo hay una persona, escribir como objeto único
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(jsonFile, clientes.get(0));
            } else {
                // Si hay varias, escribir como lista
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(jsonFile, clientes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> get() {
        return leer();
    }

    @Override
    public void insertar(Cliente cliente) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        List<Cliente> clientes = leer();
        clientes.add(cliente);
        escribir(clientes);
    }

    @Override
    public Cliente modificar(Cliente cliente, String nombre, String telefono) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente, "El cliente no puede ser nulo");
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        Objects.requireNonNull(telefono, "El telefono no puede ser nulo");
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
        coleccionClientes = leer();
        int indice = coleccionClientes.indexOf(cliente);
        return (indice == -1) ? null : coleccionClientes.get(indice);
    }

    @Override
    public void borrar(Cliente cliente0) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(cliente0, "El cliente a borrar no puede ser nulo");
        List<Cliente> clientes = leer();
        clientes.removeIf(cliente1 -> Objects.equals(cliente1.getDni(), cliente0.getDni()));
        escribir(clientes);
    }
}
