package es.cic25.proy009.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.model.Rama;
import es.cic25.proy009.repository.ArbolRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArbolControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArbolRepository arbolRepository;

    @Test
    void crearYObtenerArbol() throws Exception {
        Arbol arbol = new Arbol("Olivo", "Frutal", 50, "Campo");
        Rama rama = new Rama();
        rama.setNombre("RamaOlivo");
        rama.setLongitud(1.5);
        rama.setColor("Verde");
        rama.setPrincipal(true);

        arbol.setRamas(List.of(rama));

        mockMvc.perform(post("/arboles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(arbol)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ramas[0].nombre").value("RamaOlivo"));

        mockMvc.perform(get("/arboles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Olivo"));
    }

    @Test
    void borrarArbol() throws Exception {
        Arbol arbol = new Arbol("Pino", "Conífera", 30, "Montaña");
        arbol = arbolRepository.save(arbol);

        mockMvc.perform(delete("/arboles/" + arbol.getId()))
                .andExpect(status().isOk());
    }
}
