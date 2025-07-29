package es.cic25.proy009.controller;

import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.repository.ArbolRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/arboles")
public class ArbolController {

    private final ArbolRepository arbolRepository;

    public ArbolController(ArbolRepository arbolRepository) {
        this.arbolRepository = arbolRepository;
    }

    @GetMapping
    public List<Arbol> listarArboles() {
        return arbolRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Arbol> obtenerArbol(@PathVariable Long id) {
        return arbolRepository.findById(id);
    }

    @PostMapping
    public Arbol crearArbol(@RequestBody Arbol arbol) {
        // Las ramas deben tener el arbol asignado
        if (arbol.getRamas() != null) {
            arbol.getRamas().forEach(r -> r.setArbol(arbol));
        }
        return arbolRepository.save(arbol);
    }

    @DeleteMapping("/{id}")
    public void borrarArbol(@PathVariable Long id) {
        arbolRepository.deleteById(id);
    }
}
