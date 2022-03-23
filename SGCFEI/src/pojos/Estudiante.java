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
public class Estudiante {
    
    private Integer idEstudiante;
    private String nombre;
    private String matricula;
    private String correo;
    private Integer idCarrera;
    private String carrera;

    public Estudiante(Integer idEstudiante, String nombre, String matricula, String correo, Integer idCarrera, String carrera) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.matricula = matricula;
        this.correo = correo;
        this.idCarrera = idCarrera;
        this.carrera = carrera;
    }

    public Estudiante() {
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
    
    
}
