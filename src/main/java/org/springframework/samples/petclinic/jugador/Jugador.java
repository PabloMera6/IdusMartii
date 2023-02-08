package org.springframework.samples.petclinic.jugador;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.model.AuditableEntity;
import org.springframework.samples.petclinic.partida.Participacion;
import org.springframework.samples.petclinic.partida.Partida;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Jugador extends AuditableEntity {
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username",referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn
    private RolType rol;

    private boolean estaEnPartida;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "jugadores")
    public List<Partida> partidas;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Participacion> participaciones;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    public List<Jugador> amigoDe;

    
    public Boolean yaElegido; 

    public Integer getPartidasJugadas() {
        List<Partida> partidasJugadas = getPartidas().stream().filter(x->!x.getParticipaciones().isEmpty()).filter(x->x.getActiva() ==false).collect(Collectors.toList());
        return partidasJugadas.size();
    }
    
    public Integer getPartidasGanadas() {
        Integer res = 0;
        List<Partida> partidas = getPartidas().stream().filter(x->!x.getParticipaciones().isEmpty()).filter(x->x.getActiva() ==false).collect(Collectors.toList());
        List<Participacion> participaciones = getParticipaciones();
        for (Partida partida : partidas) {
            for (Participacion participacion : participaciones) {
                if (participacion.getFaccionApoyada() !=null
                        &&partida.getParticipaciones().contains(participacion)
                        && partida.getFaccionGanadora()!=null
                        && partida.getFaccionGanadora().equals(participacion.getFaccionApoyada())) {
                    res++;
                }
            }
        }
        return res;
    }
    
    public Integer getVictoriasComoLeal() {
        Integer res = 0;
        List<Partida> partidas = getPartidas();
        List<Participacion> participaciones = getParticipaciones();
        for (Partida partida : partidas) {
            for (Participacion participacion : participaciones) {
                if (partida.getParticipaciones().contains(participacion)
                        && participacion.getFaccionApoyada()!=null
                        && partida.getFaccionGanadora()!=null
                        && participacion.getFaccionApoyada().getName().equals("Leal")
                        && partida.getFaccionGanadora().equals(participacion.getFaccionApoyada())) {
                    res++;
                }
            }
        }
        return res;
    }
    
    public Integer getVictoriasComoTraidor() {
        Integer res = 0;
        List<Partida> partidas = getPartidas();
        List<Participacion> participaciones = getParticipaciones();
        for (Partida partida : partidas) {
            for (Participacion participacion : participaciones) {
                if (partida.getParticipaciones().contains(participacion)
                        && participacion.getFaccionApoyada()!=null
                        && partida.getFaccionGanadora()!=null
                        && participacion.getFaccionApoyada().getName().equals("Traidor")
                        && partida.getFaccionGanadora().equals(participacion.getFaccionApoyada())) {
                    res++;
                }
            }
        }
        return res;
    }
    
    public Integer getVictoriasComoMercader() {
        Integer res = 0;
        List<Partida> partidas = getPartidas();
        List<Participacion> participaciones = getParticipaciones();
        for (Partida partida : partidas) {
            for (Participacion participacion : participaciones) {
                if (partida.getParticipaciones().contains(participacion)
                        && participacion.getFaccionApoyada()!=null
                        && partida.getFaccionGanadora()!=null
                        && participacion.getFaccionApoyada().getName().equals("Mercader")
                        && partida.getFaccionGanadora().equals(participacion.getFaccionApoyada())) {
                    res++;
                }
            }
        }
        return res;
    }
    
    public Long getTiempoJugado() {
        Long res = 0L;
        List<Partida> partidasJ = getPartidas().stream().filter(x->!x.getParticipaciones().isEmpty()).filter(x->x.getActiva() ==false).collect(Collectors.toList());
        for (Partida partida : partidasJ) {
            if(Long.valueOf(partida.getTiempo())!=null && !partida.getActiva()){
                res += partida.getTiempo();
            }
        }
        return res;
    }
    
    public String getFaccionFavorita() {
        Integer leal = 0;
        Integer traidor = 0;
        Integer mercader = 0;
        String res = "";
        List<Participacion> participaciones = getParticipaciones();
        if(!participaciones.isEmpty()){
            for (Participacion participacion : participaciones) {
                if(participacion.getFaccionApoyada()!=null){
                    if (participacion.getFaccionApoyada().getName().equals("Leal")) {
                        leal++;
                    } else if (participacion.getFaccionApoyada().getName().equals("Traidor")) {
                        traidor++;
                    } else {
                        mercader++;
                    }
                }
            }
            Integer max = Math.max(leal, traidor);
            if (Math.max(max, mercader) == leal) {
                res = "Leal";
            } else if (Math.max(max, mercader) == traidor) {
                res = "Traidor";
            } else {
                res = "Mercader";
            }
        }
        return res;
    }

    public Participacion getParticipacionEnPartida(Partida p){
        Participacion res = null;
        for(Participacion part: this.participaciones){
            if(p.getParticipaciones().contains(part)){
                res = part;
            }
        }
        return res;
    }

    public Integer getNumeroAmigos(){
        Integer res = 0;
        for(Jugador a : this.amigoDe){
            if(a.getAmigoDe().contains(this)){
                res++;
            }
        }
        return res;
    }
    public Integer getnNumeroSeguidos(){
        return this.amigoDe.size();
    }
    public Integer getNumeroDeSeguidores(List<Jugador> jugadores){
        Integer res = 0;
        for(Jugador j: jugadores){
            if(j.getAmigoDe().contains(this)){
                res ++;
            }
        }
        return res;
    }
}