package es.cic25.proy009.service;

import es.cic25.proy009.model.Arbol;
import es.cic25.proy009.repository.ArbolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbolService {

    private final ArbolRepository arbolRepository;

    public ArbolService(ArbolRepository arbolRepository) {
        this.arbolRepository = arbolRepository;
    }

    public List<Arbol> listar() {
        return arbolRepository.findAll();
    }

    public Optional<Arbol> buscarPorId(Long id) {
        return arbolRepository.findById(id);
    }

    public Arbol crear(Arbol arbol) {
        if (arbol.getRamas() != null) {
            arbol.getRamas().forEach(r -> r.setArbol(arbol));
        }
        return arbolRepository.save(arbol);
    }

    public void borrar(Long id) {
        arbolRepository.deleteById(id);
    }
}
