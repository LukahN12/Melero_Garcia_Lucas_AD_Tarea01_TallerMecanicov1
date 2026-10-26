package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Vehiculos implements IVehiculos {
    static File jsonFile = new File("datos/json/vehiculos.json");
    private static final String RAIZ = "vehiculos";
    private static final String VEHICULO = "vehiculo";
    private static final String MARCA = "marca";
    private static final String MODELO = "modelo";
    private static final String MATRICULA = "matricula";
    ObjectMapper mapper = new ObjectMapper();

    private final List<Vehiculo> coleccionVehiculos;
    private static Vehiculos instancia;

    private Vehiculos() {
        coleccionVehiculos = new ArrayList<>();
    }

    public static Vehiculos getInstancia() {
        if (instancia == null) {
            instancia = new Vehiculos();
        }
        return instancia;
    }

    @Override
    public void comenzar() {

    }

    private void escribir(List<Vehiculo> vehiculos) {
        try
        {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, vehiculos);
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

    private Vehiculo getVehiculo(Element elemento) {
        String marca = elemento.getAttribute(MARCA);
        String modelo = elemento.getAttribute(MODELO);
        String matricula = elemento.getAttribute(MATRICULA);
        return new Vehiculo(marca, modelo, matricula);
    }

    @Override
    public void terminar() {

    }

    private List<Vehiculo> leer() {
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

        for (Object vehiculo1 : json.values()){
            coleccionVehiculos.add((Vehiculo) vehiculo1);
        }

        return coleccionVehiculos;
    }

    @Override
    public List<Vehiculo> get() {
        return new ArrayList<>(coleccionVehiculos);
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(vehiculo, "No se puede insertar un vehículo nulo.");
        if (coleccionVehiculos.contains(vehiculo)) {
            throw new TallerMecanicoExcepcion("Ya existe un vehículo con esa matrícula.");
        }
        coleccionVehiculos.add(vehiculo);
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "No se puede buscar un vehículo nulo.");
        int indice = coleccionVehiculos.indexOf(vehiculo);
        return (indice == -1) ? null : coleccionVehiculos.get(indice);
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        Objects.requireNonNull(vehiculo, "No se puede borrar un vehículo nulo.");
        if (!coleccionVehiculos.contains(vehiculo)) {
            throw new TallerMecanicoExcepcion("No existe ningún vehículo con esa matrícula.");
        }
        coleccionVehiculos.remove(vehiculo);
    }
}
