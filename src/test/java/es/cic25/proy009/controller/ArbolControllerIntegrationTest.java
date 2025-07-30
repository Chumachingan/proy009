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

    @Test
    void actualizarArbol() throws Exception {
        // Crea y guarda un árbol inicial con una rama
        Arbol arbol = new Arbol("Castaño", "Frutal", 40, "Bosque");
        Rama rama = new Rama();
        rama.setNombre("RamaCastaño");
        rama.setLongitud(1.0);
        rama.setColor("Marrón");
        rama.setPrincipal(true);
        arbol.setRamas(List.of(rama));
        arbol = arbolRepository.save(arbol);

        Long arbolId = arbol.getId();

        // Prepara el árbol actualizado con una nueva rama
        Arbol arbolActualizado = new Arbol("Castaño Actualizado", "Frutal", 41, "Bosque Norte");
        Rama nuevaRama = new Rama();
        nuevaRama.setNombre("RamaNueva");
        nuevaRama.setLongitud(2.5);
        nuevaRama.setColor("Verde");
        nuevaRama.setPrincipal(false);
        // Asigna explícitamente el árbol a la rama (opcional, refuerzo)
        // nuevaRama.setArbol(arbolActualizado); // Esto no es necesario para el JSON, pero ayuda en algunos contextos de test
        arbolActualizado.setRamas(List.of(nuevaRama));

        // Realiza la petición PUT para actualizar el árbol
        mockMvc.perform(put("/arboles/" + arbolId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(arbolActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Castaño Actualizado"))
                .andExpect(jsonPath("$.edad").value(41))
                .andExpect(jsonPath("$.ubicacion").value("Bosque Norte"))
                .andExpect(jsonPath("$.ramas[0].nombre").value("RamaNueva"))
                .andExpect(jsonPath("$.ramas[0].color").value("Verde"));
    }
}
