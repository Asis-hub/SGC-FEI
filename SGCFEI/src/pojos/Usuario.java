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
public class Usuario {
    
    private Integer idUsuario;
    private String username;
    private String password;
    private Integer idRol;
    private Integer idAcademico;
    private String academico;
    private String rol;
    private String correo;

    public Usuario(Integer idUsuario, String username, String password, Integer idRol, Integer idAcademico, String academico, String rol, String correo) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
        this.idAcademico = idAcademico;
        this.academico = academico;
        this.rol = rol;
        this.correo = correo;
    }

    public Usuario() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Integer getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(Integer idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getAcademico() {
        return academico;
    }

    public void setAcademico(String academico) {
        this.academico = academico;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    

    @Override
    public String toString() {
        return academico;
    }

}
