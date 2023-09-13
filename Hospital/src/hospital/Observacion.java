/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import hospital.Interface.Vent;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Observacion implements Serializable {
    Sanitarios sanitarios;
    Pacientes paciente;
    
    private Pacientes [] array;
    Queue<Pacientes> colaReaccion;

    public Observacion(Queue<Pacientes> colaReaccion ) {
        
        this.array = new Pacientes [19];
        this.colaReaccion = colaReaccion;
    } 

    
    
    public void inicializarObservacion(){
        
         for (int i = 0; i < array.length; i++) {
            array [i] = null;  
        }
         
    }
    
    public void entrarPaciente(Pacientes paciente ){
        boolean dentro = false;
        int count = 0;
        
        while (dentro == false){
          
            if(array [count]==null){ 
               
               array [count] = paciente;
               //ventana.mostrarSalaObservacion (array);
               paciente.setIdpuestoObservacion (count);
               dentro = true; 
                
            }
            
            count ++;
          
            
        }
    }
    public void salirPaciente(int puesto){
       
        if (array [puesto]!= null){
            array [puesto]= null;
            //ventana.mostrarSalaObservacion (array);
        }
       
    }
    
        
    public boolean reaccion(Pacientes paciente) {
         
        boolean reaccion= false;

        int numAleatorio = (int) Math.floor(Math.random()*101);
        if ( numAleatorio < 5){ //si el num al azar es 1 o menor el paciente no ha sido citado y el hilo acaba
            colaReaccion.offer(paciente);
            reaccion = true;
            return reaccion;
        }
        return reaccion;
       
    }
    
    public void ayuda(Sanitarios sanitario){
        try {
            for (int i = 0; i < colaReaccion.size(); i++) {
                Pacientes pAux = colaReaccion.poll();
                int puesto = pAux.getdpuestoObservacion();
                System.out.println("\n {SANITARIO} EL SANITARIO " + sanitario.getID() + "ENTRA A AYUDAR AL PACIENTE " + pAux.getID() + "EN EL PUESTO DE OBSERVACION " + puesto );
                sleep((int)(Math.random()* (5000 - 2000 + 1) + 2000));
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Observacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public Pacientes [] getArrayObservacion(){
        return array;
    }
   
}