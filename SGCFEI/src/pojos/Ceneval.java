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
public class Ceneval {
    
    private Integer idCeneval;
    private Integer idEstudiante;
    private Date fechaExamen;
    private String periodo;
    private float puntaje;
    private String estudiante;
    private String matricula;
    private String carrera;

    public Ceneval() {
        
    }

    public Ceneval(Integer idCeneval, Integer idEstudiante, Date fechaExamen, String periodo, float puntaje, String estudiante, String matricula, String carrera) {
        this.idCeneval = idCeneval;
        this.idEstudiante = idEstudiante;
        this.fechaExamen = fechaExamen;
        this.periodo = periodo;
        this.puntaje = puntaje;
        this.estudiante = estudiante;
        this.matricula = matricula;
        this.carrera = carrera;
    }

    public Integer getIdCeneval() {
        return idCeneval;
    }

    public void setIdCeneval(Integer idCeneval) {
        this.idCeneval = idCeneval;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Date getFechaExamen() {
        return fechaExamen;
    }

    public void setFechaExamen(Date fechaExamen) {
        this.fechaExamen = fechaExamen;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(float puntaje) {
        this.puntaje = puntaje;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    

    @Override
    public String toString() {
        return "Ceneval{" + "idCeneval=" + idCeneval + ", idEstudiante=" + idEstudiante + ", fechaExamen=" + fechaExamen + ", periodo=" + periodo + ", puntaje=" + puntaje + ", estudiante=" + estudiante + ", matricula=" + matricula + '}';
    }

     
}
