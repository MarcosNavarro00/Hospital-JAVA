/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import hospital.Interface.Vent;
import java.io.Serializable;
import static java.lang.Thread.sleep;

import java.util.Queue;
import java.util.Random;

import java.util.concurrent.Semaphore;

/**
 *
 * @author marco
 */
public class Recepcion implements Serializable{
    
    private final Queue<Pacientes> colaRecepcion;
   
    private int countVerificados, count, puesto;
    private final Random r;
    Vacunacion vacunacion;
   
   
    
    public Recepcion(Queue<Pacientes> colaRecepcion,Vacunacion vacunacion){
        this.colaRecepcion = colaRecepcion;
        this.vacunacion = vacunacion;
      
        countVerificados = 0;
        r = new Random();
        count = 0;
      
        
        
    }
    
    public synchronized void procesarEnRecepcion(Pacientes paciente) throws InterruptedException{
        
        while (count==2000){
            System.out.println("\n {PACIENTE} (ESPERA): El hilo espera a que se salga algun hilo de la cola");
            wait(); //si se meten 2000 hilos en la cola el hilo espera a que se vaya alguno
        }
        
        colaRecepcion.offer(paciente);
         //ventana.mostrarPrimerPaciente(colaRecepcion);
        synchronized (colaRecepcion) {
            colaRecepcion.notifyAll();
        }
        System.out.println("\n {PACIENTE} El paciente : " + paciente.getID() + " se mete en la cola");
        
    
        count++;
        
        try {
            Thread.sleep(r.nextInt(5000));
        } catch (InterruptedException ex) {}
        
        notifyAll();// despierta al wait de procesarPaciente
        
    }
    
    public synchronized boolean procesarPaciente () throws InterruptedException{
       
        while (colaRecepcion.isEmpty()){
            System.out.println("\n {AUXILIAR} (ESPERA): El auxiliar espera a que a los pacientes vayan llegando  ");
            wait();//el hilo espera hasta que haya pacientes en la cola    
        }
      
      
        Pacientes pAux = colaRecepcion.poll(); //Saca el 1º elemento y lo borra
    
        //ventana.mostrarRecepcion(colaRecepcion);
        if (pAux != null) {//si se ha sacado correctamente el elemento de la cola  
            if (verificar ()){//se tiene que usar la fun verificar para poder extraer un paciente
                puesto = vacunacion.obtenerPuestoPaciente(); 
                
                while (puesto == -1){ 
                    System.out.println("\n {AUXILIAR} (ESPERA): El auxiliar espera a que le lleguen puestos vacios ");
                    wait();//espera a que haya un puesto libre y si no es asi se queda en el wait  
                    puesto = vacunacion.obtenerPuestoPaciente();
                }
                pAux.setIdpuesto(puesto);
                System.out.println("\n {AUXILIAR} Paciente " + pAux.getID() + " sale de la cola  y se dirigue al puesto : " + puesto);
                return true;
            }else{
                System.out.println("\n {AUXILIAR} Paciente " + pAux.getID() + " sale del hospital  porque no tiene cita " );
                return false;
            }
           
        }
        return false;
  
    }
        
    public boolean verificar () throws InterruptedException{
         
        boolean verificado= false;
        int numAleatorio = (int) Math.floor(Math.random()*101);
        sleep((int)(Math.random()* (1000 - 500 + 1) + 500));// Tarda entre 0.5 y 1 segundo en verificar
        if ( numAleatorio > 1){ //si el num al azar es 1 o menor el paciente no ha sido citado y el hilo acaba
            verificado = true;
            return verificado;
        }
        
        return verificado;
       
    }
    public Queue<Pacientes> getColaRecepcion(){
        return colaRecepcion;
    }

}
