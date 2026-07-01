package Entities;
public class Categoria {
    
    private String nombre;
    private String descripcion;
    private String color;

    public Categoria(String nombre, String descripcion, String color) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.color = color;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public void cambiarNombre(String nuevoNombre){
        
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {            
            throw new IllegalArgumentException("El nombre esta vacio o es invalido");
        }else{ 
            this.nombre = nuevoNombre;   
        } 
    }
    
    
    
}
