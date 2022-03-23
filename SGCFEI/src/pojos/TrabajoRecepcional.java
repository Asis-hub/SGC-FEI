/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.sql.Date;

/**
 *
 * @author asisr
 */
public class TrabajoRecepcional {
    
    private Integer idTrabajoRecepcional;
    private String nombre;
    private Date fechaRegistro;
    private String descripcion;
    private Integer idEstudiante;
    private Integer idDirector;
    private Integer idCoDirector;
    private Integer idSinodal;
    private String estudiante;
    private String director;
    private String coDirector;
    private String sinodal;

    public TrabajoRecepcional() {
        
    }

    public TrabajoRecepcional(Integer idTrabajoRecepcional, String nombre, Date fechaRegistro, String descripcion, Integer idEstudiante, Integer idDirector, Integer idCoDirector, Integer idSinodal, String estudiante, String director, String coDirector, String sinodal) {
        this.idTrabajoRecepcional = idTrabajoRecepcional;
        this.nombre = nombre;
        this.fechaRegistro = fechaRegistro;
        this.descripcion = descripcion;
        this.idEstudiante = idEstudiante;
        this.idDirector = idDirector;
        this.idCoDirector = idCoDirector;
        this.idSinodal = idSinodal;
        this.estudiante = estudiante;
        this.director = director;
        this.coDirector = coDirector;
        this.sinodal = sinodal;
    }

    public Integer getIdTrabajoRecepcional() {
        return idTrabajoRecepcional;
    }

    public void setIdTrabajoRecepcional(Integer idTrabajoRecepcional) {
        this.idTrabajoRecepcional = idTrabajoRecepcional;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Integer getIdDirector() {
        return idDirector;
    }

    public void setIdDirector(Integer idDirector) {
        this.idDirector = idDirector;
    }

    public Integer getIdCoDirector() {
        return idCoDirector;
    }

    public void setIdCoDirector(Integer idCoDirector) {
        this.idCoDirector = idCoDirector;
    }

    public Integer getIdSinodal() {
        return idSinodal;
    }

    public void setIdSinodal(Integer idSinodal) {
        this.idSinodal = idSinodal;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCoDirector() {
        return coDirector;
    }

    public void setCoDirector(String coDirector) {
        this.coDirector = coDirector;
    }

    public String getSinodal() {
        return sinodal;
    }

    public void setSinodal(String sinodal) {
        this.sinodal = sinodal;
    }

    
    
}
