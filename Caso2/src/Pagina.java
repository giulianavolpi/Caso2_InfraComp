public class Pagina {
    
    private String id;
    private boolean referenciado;
    private boolean modificado;

    public Pagina(String id){
        this.id= id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean isReferenciado() {
        return referenciado;
    }
    public void setReferenciado(boolean referenciado) {
        this.referenciado = referenciado;
    }
    public boolean isModificado() {
        return modificado;
    }
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    
}
