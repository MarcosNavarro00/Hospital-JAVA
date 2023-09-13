/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import java.io.Serializable;

/**
 *
 * @author marco
 */
public class Contenedor implements Serializable{
    
    private Recepcion recepcion;
    private Vacunacion vacunacion;
    private Descanso descanso;
    private Observacion observacion;
   
    
    public Contenedor(Recepcion recepcion, Vacunacion vacunacion,Descanso descanso, Observacion observacion){
        this.recepcion = recepcion;
        this.vacunacion = vacunacion;
        this.descanso = descanso;
        this.observacion = observacion;
        
    }
    public Recepcion getRecepcion(){
        return recepcion;
    }
    public Vacunacion getVacunacion() {
        return vacunacion;
    }
    public Descanso getDescanso(){
        return descanso;
    }
    public Observacion getObservacion(){
        return observacion;
    }
  
    
}
