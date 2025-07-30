package es.cic25.proy009.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.cic25.proy009.controller.CreacionSecurityException;
import es.cic25.proy009.controller.ModificacionSecurityException;

// Manejo global de excepciones para los controladores REST
@RestControllerAdvice
public class ControllerAdvice {

    // Maneja excepciones de modificación no permitida
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ModificacionSecurityException.class)
    public void controlModificacion() {
        // Manejo de excepciones de modificación
    }

    // Maneja excepciones de creación no permitida
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CreacionSecurityException.class)
    public void controlCreacion() {
        // Manejo de excepciones de creación
    }
}