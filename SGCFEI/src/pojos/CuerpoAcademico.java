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
public class CuerpoAcademico {
    
    private Integer idCuerpoAcademico;
    private String nombre;
    private String descripcion;
    private Integer idLGCA;
    private String responsable;
    private String nombreLGCA;
    private String clave;
    private String grado;
    private String lgcaDescripcion;
    private Integer idAcademia;
    private String nombreAcademia;

    public CuerpoAcademico(Integer idCuerpoAcademico, String nombre, String descripcion, Integer idLGCA, String responsable, String nombreLGCA, String clave, String grado, String lgcaDescripcion, Integer idAcademia, String nombreAcademia) {
        this.idCuerpoAcademico = idCuerpoAcademico;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idLGCA = idLGCA;
        this.responsable = responsable;
        this.nombreLGCA = nombreLGCA;
        this.clave = clave;
        this.grado = grado;
        this.lgcaDescripcion = lgcaDescripcion;
        this.idAcademia = idAcademia;
        this.nombreAcademia = nombreAcademia;
    }

    public CuerpoAcademico() {   
    }

    public Integer getIdCuerpoAcademico() {
        return idCuerpoAcademico;
    }

    public void setIdCuerpoAcademico(Integer idCuerpoAcademico) {
        this.idCuerpoAcademico = idCuerpoAcademico;
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

    public Integer getIdLGCA() {
        return idLGCA;
    }

    public void setIdLGCA(Integer idLGCA) {
        this.idLGCA = idLGCA;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getNombreLGCA() {
        return nombreLGCA;
    }

    public void setNombreLGCA(String nombreLGCA) {
        this.nombreLGCA = nombreLGCA;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getLgcaDescripcion() {
        return lgcaDescripcion;
    }

    public void setLgcaDescripcion(String lgcaDescripcion) {
        this.lgcaDescripcion = lgcaDescripcion;
    }

    public Integer getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(Integer idAcademia) {
        this.idAcademia = idAcademia;
    }

    public String getNombreAcademia() {
        return nombreAcademia;
    }

    public void setNombreAcademia(String nombreAcademia) {
        this.nombreAcademia = nombreAcademia;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
}

    