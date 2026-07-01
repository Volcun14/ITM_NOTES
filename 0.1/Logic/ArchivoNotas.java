package Logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import Entities.Nota;
import Entities.Categoria;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ArchivoNotas {
    
    private String rutaCarpeta;

    public ArchivoNotas(String rutaCarpeta) {
        this.rutaCarpeta = rutaCarpeta;
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }
    
    public boolean guardarNota(Nota nota){
        
        if(nota == null){
            throw new IllegalArgumentException("Nota no puede ser null");
        }
        
        try{
            
            File archivo = new File(rutaCarpeta + "/" + nota.getId()+ ".txt");
            FileWriter escritor = new FileWriter(archivo);
            escritor.write("TITULO:" + nota.getTitulo()+"\n");
            escritor.write("CONTENIDO:" + nota.getContenido()+"\n");
            escritor.write("FAVORITA:" + nota.isFavorita()+"\n");
            escritor.write("CATEGORIA:" + (nota.getCategoria() != null ? nota.getCategoria().getNombre() : "null")+"\n");
            escritor.write("COLOR:" + (nota.getCategoria() != null ? nota.getCategoria().getColor(): "null")+"\n");
            escritor.write("FECHA_CREACION:" + nota.getFechaCreacion()+"\n");
            escritor.write("FECHA_ACTUALIZACION:" + nota.getFechaActualizacion()+"\n");
            escritor.close();
            return true;
        } catch(IOException e){
            return false;
        }
    }
    
    public List<Nota> cargarNotas(){
        List<Nota> notas = new ArrayList<>();
        try{
            File carpeta = new File(rutaCarpeta);
            File[] archivos = carpeta.listFiles();

            if (archivos == null || archivos.length == 0){
                    return notas;
            }

            for(File archivoread : archivos){

                BufferedReader lector = new BufferedReader(new FileReader(archivoread));
                String titulo = lector.readLine().replace("TITULO:", "");      
                String contenido = lector.readLine().replace("CONTENIDO:", "");
                Boolean favorita = Boolean.parseBoolean(lector.readLine().replace("FAVORITA:", ""));
                String categoriaNombre = lector.readLine().replace("CATEGORIA:", "");
                String categoriaColor = lector.readLine().replace("COLOR:", "");
                String fechaCreacionSTR = lector.readLine().replace("FECHA_CREACION:", "");
                String fechaActualizacionSTR = lector.readLine().replace("FECHA_ACTUALIZACION:", "");
                lector.close();

                String id = archivoread.getName().replace(".txt", "");
                
                Categoria categoria = null;
                if (!categoriaNombre.equals("null")) {
                    categoria = new Categoria(categoriaNombre, "", categoriaColor);
                }
                
                SimpleDateFormat formato = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date fechaCreacion = formato.parse(fechaCreacionSTR);
                Date fechaActualizacion = formato.parse(fechaActualizacionSTR);
                
                Nota nota = new Nota(id, categoria, titulo, contenido, fechaCreacion, fechaActualizacion, null);
                nota.setFavorita(favorita);
                notas.add(nota);
            }   
        }catch(IOException | ParseException e){
            return notas;
        }
        return notas;
    }
    
    public boolean eliminarArchivo(String id){
        
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID no puede ser null o vacio");
        }
        try{
        File archivo = new File(rutaCarpeta+"/"+id+".txt");
        if (archivo.exists()) {
            archivo.delete();
            return true;
        }
        return false;
        } catch(Exception e){
        return false;
        }
    }
    
    public boolean actualizarArchivo(Nota nota){
        
        if (nota == null) {
        throw new IllegalArgumentException("La nota no puede ser null");
        }
        try {
            File archivo = new File(rutaCarpeta + "/" + nota.getId() + ".txt");
            if (!archivo.exists()) {
                return false;
            }
            FileWriter escritor = new FileWriter(archivo);
            escritor.write("TITULO:" + nota.getTitulo() + "\n");
            escritor.write("CONTENIDO:" + nota.getContenido() + "\n");
            escritor.write("FAVORITA:" + nota.isFavorita() + "\n");
            escritor.write("CATEGORIA:" + (nota.getCategoria() != null ? nota.getCategoria().getNombre() : "null") + "\n");
            escritor.write("COLOR:" + (nota.getCategoria() != null ? nota.getCategoria().getColor() : "null") + "\n");
            escritor.write("FECHA_CREACION:" + nota.getFechaCreacion() + "\n");
            escritor.write("FECHA_ACTUALIZACION:" + nota.getFechaActualizacion() + "\n");
            escritor.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
