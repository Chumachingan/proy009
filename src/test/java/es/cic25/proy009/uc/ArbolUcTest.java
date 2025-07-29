package es.cic25.proy009.uc;

import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.service.ArbolService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArbolUcTest {
    @Test
    void crearArbolDelegadoEnService() {
        ArbolService service = mock(ArbolService.class);
        ArbolUc uc = new ArbolUc(service);

        Arbol arbol = new Arbol("Cerezo", "Frutal", 8, "Jardín");
        when(service.crear(arbol)).thenReturn(arbol);

        Arbol resultado = uc.crearArbol(arbol);
        assertEquals("Cerezo", resultado.getNombre());
    }

    @Test
    void obtenerTodosDelegadoEnService() {
        ArbolService service = mock(ArbolService.class);
        ArbolUc uc = new ArbolUc(service);

        when(service.listar()).thenReturn(List.of(new Arbol("Olmo", "Deciduo", 12, "Plaza")));
        List<Arbol> arboles = uc.obtenerTodos();
        assertFalse(arboles.isEmpty());
    }

    @Test
    void obtenerPorIdDelegadoEnService() {
        ArbolService service = mock(ArbolService.class);
        ArbolUc uc = new ArbolUc(service);

        Arbol arbol = new Arbol("Sauce", "Deciduo", 25, "Río");
        when(service.buscarPorId(2L)).thenReturn(Optional.of(arbol));

        Optional<Arbol> resultado = uc.obtenerPorId(2L);
        assertTrue(resultado.isPresent());
        assertEquals("Sauce", resultado.get().getNombre());
    }

    @Test
    void borrarArbolDelegadoEnService() {
        ArbolService service = mock(ArbolService.class);
        ArbolUc uc = new ArbolUc(service);

        uc.borrarArbol(3L);
        verify(service, times(1)).borrar(3L);
    }
}