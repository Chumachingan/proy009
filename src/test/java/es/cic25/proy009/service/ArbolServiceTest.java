package es.cic25.proy009.service;

import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.model.Rama;
import es.cic25.proy009.repository.ArbolRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ArbolServiceTest {

    @Test
    void crearArbolConRamas() {
        ArbolRepository repo = mock(ArbolRepository.class);
        ArbolService service = new ArbolService(repo);

        Arbol arbol = new Arbol("Manzano", "Frutal", 15, "Huerto");
        Rama rama = new Rama();
        rama.setNombre("RamaManzano");
        rama.setLongitud(2.0);
        rama.setColor("Verde");
        rama.setPrincipal(true);
        rama.setArbol(arbol);
        arbol.setRamas(new java.util.ArrayList<>(List.of(rama)));

        when(repo.save(any(Arbol.class))).thenReturn(arbol);

        Arbol resultado = service.crear(arbol);
        assertEquals("Manzano", resultado.getNombre());
        assertEquals(1, resultado.getRamas().size());
    }

    @Test
    void listarArboles() {
        ArbolRepository repo = mock(ArbolRepository.class);
        ArbolService service = new ArbolService(repo);

        when(repo.findAll()).thenReturn(List.of(new Arbol("Roble", "Deciduo", 20, "Parque")));
        List<Arbol> arboles = service.listar();
        assertFalse(arboles.isEmpty());
    }

    @Test
    void buscarPorId() {
        ArbolRepository repo = mock(ArbolRepository.class);
        ArbolService service = new ArbolService(repo);

        Arbol arbol = new Arbol("Pino", "Conífera", 30, "Bosque");
        when(repo.findById(1L)).thenReturn(Optional.of(arbol));

        Optional<Arbol> resultado = service.buscarPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Pino", resultado.get().getNombre());
    }

    @Test
    void borrarArbol() {
        ArbolRepository repo = mock(ArbolRepository.class);
        ArbolService service = new ArbolService(repo);

        service.borrar(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void actualizarArbolEliminaYAgregaRamas() {
        ArbolRepository repo = mock(ArbolRepository.class);
        ArbolService service = new ArbolService(repo);

        Arbol arbolExistente = new Arbol("Pino", "Conífera", 30, "Bosque");
        Rama ramaVieja = new Rama();
        ramaVieja.setNombre("RamaVieja");
        ramaVieja.setLongitud(1.0);
        ramaVieja.setColor("Marrón");
        ramaVieja.setPrincipal(true);
        ramaVieja.setArbol(arbolExistente);
        arbolExistente.setRamas(new java.util.ArrayList<>(List.of(ramaVieja)));

        Arbol arbolActualizado = new Arbol("Pino", "Conífera", 31, "Bosque");
        Rama ramaNueva = new Rama();
        ramaNueva.setNombre("RamaNueva");
        ramaNueva.setLongitud(2.0);
        ramaNueva.setColor("Verde");
        ramaNueva.setPrincipal(false);
        arbolActualizado.setRamas(new java.util.ArrayList<>(List.of(ramaNueva)));

        when(repo.findById(anyLong())).thenReturn(Optional.of(arbolExistente));
        when(repo.save(any(Arbol.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Arbol resultado = service.actualizar(1L, arbolActualizado);

        assertEquals(1, resultado.getRamas().size());
        assertEquals("RamaNueva", resultado.getRamas().get(0).getNombre());
        assertEquals(31, resultado.getEdad());
    }
}
