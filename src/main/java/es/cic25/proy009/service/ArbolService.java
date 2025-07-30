package es.cic25.proy009.service;

import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.repository.ArbolRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Servicio de negocio para gestionar árboles y sus ramas
@Service
public class ArbolService {

    private final ArbolRepository arbolRepository;

    public ArbolService(ArbolRepository arbolRepository) {
        this.arbolRepository = arbolRepository;
    }

    // Devuelve la lista de todos los árboles
    public List<Arbol> listar() {
        return arbolRepository.findAll();
    }

    // Busca un árbol por su ID
    public Optional<Arbol> buscarPorId(Long id) {
        return arbolRepository.findById(id);
    }

    // Crea un árbol y asigna el árbol a cada rama antes de guardar
    public Arbol crear(Arbol arbol) {
        if (arbol.getRamas() != null) {
            arbol.getRamas().forEach(r -> r.setArbol(arbol));
        }
        return arbolRepository.save(arbol);
    }

    // Borra un árbol por su ID
    public void borrar(Long id) {
        arbolRepository.deleteById(id);
    }

    // Actualiza un árbol y sus ramas
    public Arbol actualizar(Long id, Arbol arbolActualizado) {
        return arbolRepository.findById(id).map(arbolExistente -> {
            arbolExistente.setNombre(arbolActualizado.getNombre());
            arbolExistente.setTipo(arbolActualizado.getTipo());
            arbolExistente.setEdad(arbolActualizado.getEdad());
            arbolExistente.setUbicacion(arbolActualizado.getUbicacion());
            // Refuerzo: asegura que la lista no es null
            if (arbolExistente.getRamas() == null) {
                arbolExistente.setRamas(new ArrayList<>());
            }
            arbolExistente.getRamas().clear();
            if (arbolActualizado.getRamas() != null) {
                arbolActualizado.getRamas().forEach(rama -> {
                    rama.setArbol(arbolExistente); // Esto es clave
                    rama.setId(null); // Refuerzo: fuerza a que siempre sean nuevas ramas
                    arbolExistente.getRamas().add(rama);
                });
            }
            return arbolRepository.save(arbolExistente);
        }).orElseThrow(() -> new RuntimeException("Árbol no encontrado"));
    }
}
