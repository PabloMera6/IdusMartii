package org.springframework.samples.petclinic.chat;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Mensaje extends BaseEntity{

    @NotBlank
    @Size(min=1, max=100)
    private String contenido;

    @ManyToOne
    private Jugador jugador;


    public Mensaje() {
        this.contenido = "";
        this.jugador = null;
    }
    
}