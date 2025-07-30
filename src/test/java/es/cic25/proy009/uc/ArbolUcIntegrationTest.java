package es.cic25.proy009.uc;

// Test de integración para la creación de un árbol con ramas usando MockMvc
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.model.Rama;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ArbolUcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearArbolConRamas() throws Exception {
        // Crea un árbol con una rama y verifica que se crea correctamente
        Arbol arbol = new Arbol();
        arbol.setNombre("Álamo");
        arbol.setTipo("Deciduo");
        arbol.setEdad(12);
        arbol.setUbicacion("Río");

        Rama rama = new Rama();
        rama.setNombre("RamaPrincipal");
        rama.setLongitud(2.3);
        rama.setColor("Marrón");
        rama.setPrincipal(true);

        arbol.setRamas(List.of(rama));

        String arbolJson = objectMapper.writeValueAsString(arbol);

        mockMvc.perform(post("/arboles")
                .contentType("application/json")
                .content(arbolJson))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(result -> {
                assertNotNull(
                    objectMapper.readTree(result.getResponse().getContentAsString()).get("id"),
                    "El árbol debería haberse creado y devuelto su id");
            });
    }
}
