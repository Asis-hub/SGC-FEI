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
public class Personal {
    
    private Integer idAcademico;
    private String numeroPersonal;
    private String nombre;
    private String apellidos;
    private String correo;

    public Personal(Integer idAcademico, String numeroPersonal, String nombre, String apellidos, String correo) {
        this.idAcademico = idAcademico;
        this.numeroPersonal = numeroPersonal;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public Personal() {
    }

    public Integer getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(Integer idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
   
}
