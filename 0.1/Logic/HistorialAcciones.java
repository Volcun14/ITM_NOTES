package Logic;

import java.util.Stack;

public class HistorialAcciones {
    
    private Stack<String> pilaAcciones;
    private Stack<String> pilaRestauradas;

    public HistorialAcciones(Stack<String> pilaAcciones, Stack<String> pilaRestauradas) {
        this.pilaAcciones = new Stack<>();
        this.pilaRestauradas = new Stack<>();
    }
    
    public boolean deshacer(){
        
        if (pilaAcciones.isEmpty()) {
            return false;
        }
        String accion = pilaAcciones.pop();
        pilaRestauradas.push(accion);
        return true;
    }
    
    public boolean restaurar(){
        
        if (pilaRestauradas.isEmpty()){
            return false;
        }
        
        String accion = pilaRestauradas.pop();
        pilaAcciones.push(accion);
        return true;
    }
    public void resgistrarAcciones(String accion){
        
        if(accion != null && !accion.trim().isEmpty()){
            pilaAcciones.push(accion);
        }
    }
}
