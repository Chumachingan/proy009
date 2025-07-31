package es.cic25.proy009.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.model.Rama;
import es.cic25.proy009.repository.ArbolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ArbolControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArbolRepository arbolRepository;

    @BeforeEach
    void limpiarBD() {
        arbolRepository.deleteAll();
    }

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

        // Comprobación adicional: la rama tiene el árbol asignado en la BD
        List<Arbol> arboles = arbolRepository.findAll();
        assertFalse(arboles.isEmpty());
        Arbol arbolBD = arboles.get(0);
        assertFalse(arbolBD.getRamas().isEmpty());
        assertNotNull(arbolBD.getRamas().get(0).getArbol());

        mockMvc.perform(get("/arboles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Olivo"));
    }

    @Test
    void borrarArbol() throws Exception {
        Arbol arbol = new Arbol("Pino", "Conífera", 30, "Montaña");
        Rama rama = new Rama();
        rama.setNombre("RamaPino");
        rama.setLongitud(1.2);
        rama.setColor("Verde");
        rama.setPrincipal(true);
        rama.setArbol(arbol); 
        arbol.setRamas(List.of(rama));
        arbol = arbolRepository.save(arbol);

        Long arbolId = arbol.getId();

        mockMvc.perform(delete("/arboles/" + arbolId))
                .andExpect(status().isOk());

        // Verifica que el árbol ya no existe
        assertFalse(arbolRepository.findById(arbolId).isPresent());

        // Verifica que no quedan ramas asociadas a ese árbol
        boolean ramasExisten = arbolRepository.findAll().stream()
            .flatMap(a -> a.getRamas().stream())
            .anyMatch(r -> r.getArbol() != null && r.getArbol().getId().equals(arbolId));
        assertFalse(ramasExisten, "Las ramas asociadas deberían eliminarse en cascada");
    }

    
}
