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
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author marco
 */
public class Vacunacion implements Serializable{
    
    

    private int vacunados;
    Lock lock=new ReentrantLock();
    private HashMap <Integer, Puestos> puestos;
    private final Queue<String> colaVacunas;
    private boolean vacunado,dentro ;
    private Semaphore semEspera;
    
    
    public Vacunacion(Queue<String> colaVacunas){
        this.colaVacunas = colaVacunas;
        puestos = new HashMap <Integer, Puestos> ();
        vacunados = 0;
        vacunado = false;
        semEspera = new Semaphore(0);
      
    }
    public void inicializarPuestos(){
       for (int i= 0; i < 10 ; i++){
        
            puestos.put(i,new Puestos());
       }
       
   }
    
    public synchronized void entrarPaciente(Pacientes paciente, int numPuesto ) {
            
            
            Puestos p_aux;
       
            p_aux = puestos.get(numPuesto);//coge el objecto que hay en ese puesto 
            p_aux.setPaciente(paciente); //mete al paciente en el objeto 
            puestos.put(numPuesto,p_aux);
            //ventana.mostrarSalaVacunacion  (puestos);
            System.out.println("\n {PACIENTE} EL Paciente " + paciente.getID() + " SE METE EN EL BOX " + numPuesto);
            notifyAll();
            
    } 
       
 
    public  void entrarSanitario (Sanitarios sanitario, int numPuesto ) {
        
      
        Puestos p_aux;
       
        p_aux = puestos.get(numPuesto);//coge el objecto que hay en ese puesto 
        if (p_aux.getActivo()){
            p_aux.setSanitario(sanitario); //mete al paciente en el objeto 
            puestos.put(numPuesto,p_aux);
            //ventana.mostrarSalaVacunacion  (puestos);
            //System.out.println("\n EL SANITARIO " + sanitario + "SE METE EN EL BOX " + numPuesto); 
        }
    }
    
    public void salirPaciente( int numPuesto){
      
        try {
            Puestos p_aux;
            p_aux = puestos.get(numPuesto);
            
            semEspera.acquire();//hasta que el sanitario vacune al hilo no puede salir de la sala 
            if (p_aux.getPaciente()!= null && vacunado == true){
              
                p_aux.setPaciente(null);
                puestos.put(numPuesto, p_aux);
                
                //ventana.mostrarSalaVacunacion (puestos);
                
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Vacunacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void salirSanitario( int numPuesto, Sanitarios sanitario){
        Puestos p_aux;
        p_aux = puestos.get(numPuesto);
        
        if (p_aux.getSanitario()!= null){
            //System.out.println("\nEl sanitario " + sanitario+ " CIERRA SU BOX");
            p_aux.setSanitario(null);
            puestos.put(numPuesto,p_aux);
           // ventana.mostrarSalaVacunacion (puestos);
           
            
        }   
    }
    
    public synchronized int obtenerPuestoPaciente(){ //obtiene el puesto correspodiente al paciente si el box esta vacio
       
        int numPuesto = -1;
        for (Integer key : puestos.keySet()) {
            Puestos puestoAux = (Puestos) puestos.get(key);
            if (puestoAux != null){
                if ( puestoAux.getPaciente() ==  null && puestoAux.getSanitario() !=  null){ //EN ESE MOMENTO EL BOX TIENE QUE ESTA SIN PACIENTE PER CON SANITARIO
                    numPuesto = key;
                    //System.out.println("\nPuesto del paciente " + numPuesto);
                    
                    return numPuesto;
                }
        }   }
       
       return numPuesto;

    }
    public int obtenerPuestoSanitario(){ //obtiene el puesto correspodiente al sanitario si el box esta vacio
        lock.lock();
        
        int numPuesto = -1;
        for (Integer key : puestos.keySet()) {
            
            Puestos puestoAux = (Puestos) puestos.get(key);//SE OBTIENE EL OBJETO QUE HAY EN LA DETERMINADA KEY 
            if (puestoAux != null){
                if ( puestoAux.getSanitario() ==  null){
               
                    numPuesto = key;
                    lock.unlock();

                   return numPuesto;
                }
            } 
        }
       
        lock.unlock();
        
        return numPuesto;   
    }
    
    
    
    public boolean vacunacion (Sanitarios sanitario){
       vacunado = false;
        try {
            if (colaVacunas.size()>0){
                String vacuna = colaVacunas.poll();
                //ventana.mostrasVacunas (colaVacunas);
                sleep((int)(Math.random()* (5000 - 3000 + 1) + 3000));//tarda entre 3 y 5 seg en vacunar 
                vacunado = true;
             
                vacunados++;
                
                //System.out.println("NUMERO DE VACUNADOS " + vacunados);
                synchronized (colaVacunas) {// hace un wait por si la no hay vacunas dentro de la cola y espera a que se meten  mas
                    colaVacunas.wait();
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Vacunacion.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        
        semEspera.release();
        return vacunado;
    }
    
    public void abrirPuesto(int puesto){
        if (puesto != -1){
            Puestos puestoAux = (Puestos) puestos.get(puesto);
            
            if (puestoAux != null){
                puestoAux.setActivo(true);
            }
    }
    
    }
    public Queue<String> getCola(){
        return colaVacunas;
    }
   
    public int getNumVacunados(){
       return vacunados;
   }
    public void setNumVacunados(int vacunados){
       this.vacunados = vacunados;
   }
  
    public HashMap <Integer, Puestos> getMapa(){
       return puestos;
   }
}
    
    
    
    

