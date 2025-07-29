package es.cic25.proy009.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Arbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private int edad;
    private String ubicacion;

    @OneToMany(mappedBy = "arbol", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rama> ramas;

    public Arbol() {
    }

    public Arbol(String nombre, String tipo, int edad, String ubicacion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.edad = edad;
        this.ubicacion = ubicacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Rama> getRamas() {
        return ramas;
    }

    public void setRamas(List<Rama> ramas) {
        this.ramas = ramas;
    }
}