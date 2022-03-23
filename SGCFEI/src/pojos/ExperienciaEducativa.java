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
public class ExperienciaEducativa {
    
    private Integer idExperienciaEducativa;
    private Integer idMateria;
    private String nrc;
    private Integer idAcademia;
    private Integer idPrerequisito;
    private Integer idCorequisito;
    private String nombre;
    private String academia;
    private String materia;
    private String prerequisito;
    private String corequisito;

    public ExperienciaEducativa() {
        
    }

    public ExperienciaEducativa(Integer idExperienciaEducativa, Integer idMateria, String nrc, Integer idAcademia, Integer idPrerequisito, Integer idCorequisito, String nombre, String academia, String materia, String prerequisito, String corequisito) {
        this.idExperienciaEducativa = idExperienciaEducativa;
        this.idMateria = idMateria;
        this.nrc = nrc;
        this.idAcademia = idAcademia;
        this.idPrerequisito = idPrerequisito;
        this.idCorequisito = idCorequisito;
        this.nombre = nombre;
        this.academia = academia;
        this.materia = materia;
        this.prerequisito = prerequisito;
        this.corequisito = corequisito;
    }

    public Integer getIdExperienciaEducativa() {
        return idExperienciaEducativa;
    }

    public void setIdExperienciaEducativa(Integer idExperienciaEducativa) {
        this.idExperienciaEducativa = idExperienciaEducativa;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public Integer getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(Integer idAcademia) {
        this.idAcademia = idAcademia;
    }

    public Integer getIdPrerequisito() {
        return idPrerequisito;
    }

    public void setIdPrerequisito(Integer idPrerequisito) {
        this.idPrerequisito = idPrerequisito;
    }

    public Integer getIdCorequisito() {
        return idCorequisito;
    }

    public void setIdCorequisito(Integer idCorequisito) {
        this.idCorequisito = idCorequisito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAcademia() {
        return academia;
    }

    public void setAcademia(String academia) {
        this.academia = academia;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getPrerequisito() {
        return prerequisito;
    }

    public void setPrerequisito(String prerequisito) {
        this.prerequisito = prerequisito;
    }

    public String getCorequisito() {
        return corequisito;
    }

    public void setCorequisito(String corequisito) {
        this.corequisito = corequisito;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
    
}
