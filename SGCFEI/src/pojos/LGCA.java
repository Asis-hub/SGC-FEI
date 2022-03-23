
package pojos;

public class LGCA {
    
    private Integer idLGCA;
    private String LGCA;
    private String clave;
    private String grado;
    private String desAdscripcion;
    private Integer idResponsable;
    private String responsable;
    private String correo;

    public LGCA(Integer idLGCA, String LGCA, String clave, String grado, String desAdscripcion, Integer idResponsable, String responsable, String correo) {
        this.idLGCA = idLGCA;
        this.LGCA = LGCA;
        this.clave = clave;
        this.grado = grado;
        this.desAdscripcion = desAdscripcion;
        this.idResponsable = idResponsable;
        this.responsable = responsable;
        this.correo = correo;
    }

    public LGCA() {
    }

    public Integer getIdLGCA() {
        return idLGCA;
    }

    public void setIdLGCA(Integer idLGCA) {
        this.idLGCA = idLGCA;
    }

    public String getLGCA() {
        return LGCA;
    }

    public void setLGCA(String LGCA) {
        this.LGCA = LGCA;
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

    public String getDesAdscripcion() {
        return desAdscripcion;
    }

    public void setDesAdscripcion(String desAdscripcion) {
        this.desAdscripcion = desAdscripcion;
    }

    public Integer getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    @Override
    public String toString() {
        return LGCA;
    } 
}
