package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Objects;

public class JsonUtils {
    public static ObjectMapper creadorMapas(){
        // configuración global del mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // Escribe las fechas como texto.
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.findAndRegisterModules();
        return mapper;
    }
}
