package es.cic25.proy009.uc;

import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.service.ArbolService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// Componente de caso de uso para operaciones de Árbol
@Component
public class ArbolUc {

    private final ArbolService arbolService;

    public ArbolUc(ArbolService arbolService) {
        this.arbolService = arbolService;
    }

    public List<Arbol> obtenerTodos() {
        return arbolService.listar();
    }

    public Optional<Arbol> obtenerPorId(Long id) {
        return arbolService.buscarPorId(id);
    }

    public Arbol crearArbol(Arbol arbol) {
        return arbolService.crear(arbol);
    }

    public void borrarArbol(Long id) {
        arbolService.borrar(id);
    }
}
