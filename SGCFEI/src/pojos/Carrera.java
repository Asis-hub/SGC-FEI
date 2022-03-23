/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author asisr
 */
public class Carrera {
    
    private Integer idCarrera;
    
    private String nombre;
    
    private Integer idFacultad;

    public Carrera(Integer idCarrera, String nombre, Integer idFacultad) {
        this.idCarrera = idCarrera;
        this.nombre = nombre;
        this.idFacultad = idFacultad;
    }

    public Carrera() {
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(Integer idFacultad) {
        this.idFacultad = idFacultad;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
