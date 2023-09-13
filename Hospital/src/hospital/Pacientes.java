/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import java.io.Serializable;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class Pacientes extends Thread implements Serializable{
    Vacunacion vacunacion;
    Recepcion recepcion;
    Observacion observacion;
   
    
    private String id;
    private int IdPuesto, IdPuestoObservacion;
    private boolean  reaccion ;
    private Semaphore sem2;
    private Semaphore sem;
    private Semaphore sem3;
    /**
     *
     * @param id
     * @param colaRecepcion
     */
    public Pacientes (String id, Recepcion recepcion, Vacunacion vacunacion,Observacion observacion,  Semaphore sem2 ,  Semaphore sem, Semaphore sem3){
        this.id = id;
        this.recepcion = recepcion;
        this.vacunacion = vacunacion;
        this.observacion = observacion;
       
      
        this.sem2 = sem2;
        this.sem = sem;
        this.sem3 = sem3;
    }
    
    
    
    public void setIdpuesto(int puesto){
        this.IdPuesto = puesto;
    }
    public String getID(){
        return id;
    }
    public void setIdpuestoObservacion(int puesto){
        this.IdPuestoObservacion = puesto;
    }
    public void setReaccion(boolean reaccion){
        this.reaccion= reaccion;
    }
    public int getdpuestoObservacion(){
        return IdPuestoObservacion;
    }
   
    public synchronized void run(){
        
        
        try {
            notifyAll();
            sleep((int) (Math.random()*(1000+500)));
            recepcion.procesarEnRecepcion(this);
            
            
            System.out.println("\n {PACIENTE} EL PACIENTE "  + id + "ESPERA QUE LO VERIFIQUEN");  
            sem2.acquire();//espera hasta que el paciente ha salido de la cola y ha sido verificado para continuar 
           
            
            vacunacion.entrarPaciente(this,IdPuesto);
            sem.release();
            vacunacion.salirPaciente(IdPuesto);
            System.out.println("\n {PACIENTE} EL PACIENTE " + id + " SE SALE DEL BOX " + IdPuesto );
            
            observacion.entrarPaciente(this);
            System.out.println("\n {PACIENTE} EL PACIENTE " + id + "ENTRA EN EL PUSTO DE OBSERVACION " + IdPuestoObservacion );
            
            sleep (10000);
           
            
            
            if (observacion.reaccion(this)==false){
                System.out.println("\n {PACIENTE} EL PACIENTE " + id + "NO TIENE NINGUN SINTOMA " );
                observacion.salirPaciente(IdPuestoObservacion);
                System.out.println("\n {PACIENTE} EL PACIENTE " + id + "SALE DEL PUSTO DE OBSERVACION " + IdPuestoObservacion );
            }else{
                System.out.println("\n {PACIENTE} EL PACIENTE " + id + " TIENE ALGUN SINTOMA " );
                sem3.acquire();//se espera hasta que el sanitario ayude al paciente para poder sacarle de la sala de observacion
                observacion.salirPaciente(IdPuestoObservacion);
                 System.out.println("\n {PACIENTE} EL PACIENTE " + id + "SALE DEL PUSTO DE OBSERVACION " + IdPuestoObservacion );
            }   
               
         
          
         
            
        

        } catch (InterruptedException ex) {
            Logger.getLogger(Pacientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
        
          
    
    
    

