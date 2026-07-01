package Logic;

import Entities.Nota;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;

public class GestorNotas {
    
    private int contadorid;
    private List<Nota> listaNotas;
    private Stack<Nota> pilaEliminadas;
    private Queue<Nota> colaRecordatorios;
    private ArchivoNotas archivoNotas;
    private HistorialAcciones historial;
    
    public GestorNotas() {
        this.contadorid = 1;
        this.listaNotas = new ArrayList<>();
        this.pilaEliminadas = new Stack<>();
        this.colaRecordatorios = new LinkedList<>();
        this.archivoNotas = new ArchivoNotas("notas");
        this.historial = new HistorialAcciones(new Stack<>(), new Stack<>());

        // Cargar notas existentes al iniciar
        this.listaNotas = archivoNotas.cargarNotas();

        // Ajustar contadorid al ultimo id usado
        for (Nota nota : listaNotas) {
            String id = nota.getId().replace("N", "");
            try {
                int num = Integer.parseInt(id);
                if (num >= contadorid) {
                    contadorid = num + 1;
                }
            } catch (NumberFormatException e) {
                // si el id no es numerico lo ignora
            }
        }
    }
    
    public String generarId(){
        
        String id = "N" +String.format("%02d", contadorid);
        contadorid++;
        return id;
    }
    
    public void agregarNota(Nota nota){
        
        if (nota == null) {
            
            throw new IllegalArgumentException("La nota no puede ser null"); 
        }
        
        listaNotas.add(nota);
        archivoNotas.guardarNota(nota);
        historial.resgistrarAcciones("AGREGAR");
    }
    
    public boolean editarNota(String id, String titulo, String contenido){
        
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id no puede ser null o estar vacio");  
        }
        
        if (contenido == null || contenido.trim().isEmpty()){
           throw new IllegalArgumentException("Contenido no puede ser null o estar vacio");            
        }
        
        if (titulo == null || contenido.trim().isEmpty()){
           throw new IllegalArgumentException("Contenido no puede ser null o estar vacio");            
        }
        
        for (Nota notatemp : listaNotas) {
            
            if (notatemp.getId().equals(id)) {
                notatemp.setTitulo(titulo);
                notatemp.Editar(contenido);
                archivoNotas.actualizarArchivo(notatemp);
                historial.resgistrarAcciones("EDITAR");
                return true;
            }
        }
       return false; 
    }
    
    public boolean eliminarNota(String id){
        
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID no puede ser null o vacio");
        }
        
        Iterator<Nota> iterator = listaNotas.iterator();
        while(iterator.hasNext()){
            Nota nota = iterator.next();
            if (nota.getId().equals(id)) {
              pilaEliminadas.push(nota);
              iterator.remove();
              archivoNotas.eliminarArchivo(id);
              historial.resgistrarAcciones("ELIMINAR");
              return true;
            }
        }
        return false;
    }
    
    public Nota recuperarNota(){
        
        if(pilaEliminadas.isEmpty()){
            return null;
        }
        
        Nota nota = pilaEliminadas.pop();
        listaNotas.add(nota);
        archivoNotas.guardarNota(nota);
        historial.resgistrarAcciones("RECUPERAR");
        return nota;
    }
    
    public List<Nota> buscarNotas(String criterio){
        
        if(criterio == null || criterio.trim().isEmpty()){
            throw new IllegalArgumentException("Criiterio no puede ser null o vacio");
        }
        List<Nota> resultados = new ArrayList<>();
        
        for(Nota notatemp : listaNotas){
            if(notatemp.getTitulo().toLowerCase().contains(criterio.toLowerCase()) || notatemp.getContenido().toLowerCase().contains(criterio.toLowerCase())){
                resultados.add(notatemp);
            }
        }
       return resultados;
    }
    
    public List<Nota> cargarNotas(){
        listaNotas = archivoNotas.cargarNotas();
        return listaNotas;
    }
    
    public Nota ObtenerNotaPorId(String id) {
        for(Nota nota : listaNotas){
            if (nota.getId().equals(id)){
                return nota;
            }
        }
        return null;
    }
}
