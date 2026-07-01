package Entities;

import java.util.Date;

public class Nota {
    
    private String id;
    private String titulo;
    private String contenido;
    private Categoria categoria;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private Boolean favorita;

    public Nota(String id, Categoria categoria, String titulo, String contenido, Date fechaCreacion, Date fechaActualizacion,Boolean favorita) {
        this.id = id;
        this.categoria = categoria;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.favorita = false;
    }
    
    public void Editar(String nuevoContenido){
        
        if (nuevoContenido == null || nuevoContenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El conteido esta vacio");
        }else{
            this.contenido = nuevoContenido;
            this.fechaActualizacion = new Date();
        }
        
    }
    
    public String MostrarResumen(){
        
        String resumen;
        
        if (contenido.length() > 50) {
            resumen = contenido.substring(0, 50) + "...";
        }else{
            resumen = contenido;
        }
        
        return "Titulo: " + titulo + " - " + resumen; 
    }
    
    public void MarcarFavorita(){
        
        this.favorita = !this.favorita;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public boolean isFavorita() {
        return favorita;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }
    
    
    
}
