package es.cic25.proy009.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Arbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único del árbol

    @Version
    private Long version; // Control de versiones para concurrencia optimista

    @Column(name = "nombre")
    private String nombre; // Nombre del árbol

    @Column(name = "tipo")
    private String tipo; // Tipo de árbol (ej: Frutal, Deciduo)

    @Column(name = "edad")
    private int edad; // Edad del árbol

    @Column(name = "ubicacion")
    private String ubicacion; // Ubicación del árbol

    @OneToMany(
        mappedBy = "arbol",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
        orphanRemoval = true
    )
    @JsonManagedReference
    private List<Rama> ramas = new ArrayList<>(); // Lista de ramas asociadas al árbol

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
        this.ramas = ramas != null ? ramas : new ArrayList<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arbol other = (Arbol) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Arbol [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", edad=" + edad + ", ubicacion="
                + ubicacion + ", ramas=" + ramas + "]";
    }

}