package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.iesalandalus.programacion.tallermecanico.modelo.TallerMecanicoExcepcion;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
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
    static File jsonFile = new File("datos/ficheros/json/vehiculos.json");
    private static final String RAIZ = "vehiculos";
    private static final String VEHICULO = "vehiculo";
    private static final String MARCA = "marca";
    private static final String MODELO = "modelo";
    private static final String MATRICULA = "matricula";
    ObjectMapper mapper = JsonUtils.creadorMapas();

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
        try {
            if (vehiculos.size() == 1) {
                // Si solo hay una persona, escribir como objeto único
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(jsonFile, vehiculos.get(0));
            } else {
                // Si hay varias, escribir como lista
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(jsonFile, vehiculos);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);

            // Si el JSON es una lista
            if (rootNode.isArray()) {
                return mapper.convertValue(rootNode, new TypeReference<List<Vehiculo>>() {});
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    @Override
    public List<Vehiculo> get() {
        return leer();
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        List<Vehiculo> vehiculos = leer();
        vehiculos.add(vehiculo);
        escribir(vehiculos);
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "No se puede buscar un vehículo nulo.");
        int indice = coleccionVehiculos.indexOf(vehiculo);
        return (indice == -1) ? null : coleccionVehiculos.get(indice);
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws TallerMecanicoExcepcion {
        List<Vehiculo> vehiculos = leer();
        vehiculos.removeIf(vehiculo1 -> Objects.equals((vehiculo1.matricula()), vehiculo.matricula()));
        escribir(vehiculos);
    }
}
