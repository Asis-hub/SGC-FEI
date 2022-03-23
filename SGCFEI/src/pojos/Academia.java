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
public class Academia {
    
    private Integer idAcademia;
    private String nombre;
    private String descripcion;
    private Integer idCoordinador;
    private String coordinador;

    public Academia(Integer idAcademia, String nombre, String descripcion, Integer idCoordinador, String coordinador) {
        this.idAcademia = idAcademia;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCoordinador = idCoordinador;
        this.coordinador = coordinador;
    }

    public Academia() {
    }

    public Integer getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(Integer idAcademia) {
        this.idAcademia = idAcademia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCoordinador() {
        return idCoordinador;
    }

    public void setIdCoordinador(Integer idCoordinador) {
        this.idCoordinador = idCoordinador;
    }

    public String getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(String coordinador) {
        this.coordinador = coordinador;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
