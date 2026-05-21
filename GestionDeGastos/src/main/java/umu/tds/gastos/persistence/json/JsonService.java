package umu.tds.gastos.persistence.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonService {
    private final ObjectMapper mapper;

    public JsonService() {
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public <T> List<T> cargar(File f, TypeReference<List<T>> type) {
        if (!f.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(f, type);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public <T> void guardar(File f, List<T> lista) {
        f.getParentFile().mkdirs();
        try {
            mapper.writeValue(f, lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
