/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import hospital.Interface.Vent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.Semaphore;


/**
 *
 * @author marco
 */
public class Sanitarios extends Thread implements Serializable{
    Descanso descanso;
    Vacunacion  vacunacion;
    Observacion observacion;
    Sanitarios sani;
    private String id;
    private int puesto;
    private Semaphore sem;
    private Semaphore sem3;
    private boolean ayuda, cerrado;
    Queue<Pacientes> colaReaccion;
    private Semaphore semEspera;
   
    
    public Sanitarios (String id, Vacunacion  vacunacion, Descanso descanso ,Semaphore sem,Semaphore sem3, Queue<Pacientes> colaReaccion, Observacion observacion, Semaphore semEspera){
        this.id = id;
        this.vacunacion = vacunacion;
        this.descanso = descanso;
        this.observacion = observacion;
        this.colaReaccion = colaReaccion;
        puesto = -1;
        this.sem = sem;
        this.sem3 = sem3;
        this.semEspera = semEspera;
        cerrado = false;
    }
  
    public String getID(){
        return id;
    }
    public void setAyuda(boolean ayuda){
        this.ayuda = ayuda;
    }
    public void setCerrado(boolean cerrado){
        this.cerrado = cerrado;
    }
    public void setSanitario(Sanitarios sani){
        this.sani = sani;
    }

    public synchronized void run(){
        try{
            sleep((int) (Math.random()*(1000+500)));
           
           
            descanso.entradaIni(this);           
             puesto = vacunacion.obtenerPuestoSanitario();
             System.out.println("\n[SANITARIO] Sanitario puesto asignado: "+ puesto +" id "+ id);
             vacunacion.entrarSanitario(this,puesto);
            while (true){
               
                
                
              
                if (cerrado){
                   
                    
                    descanso.cerradoBox(sani);
                    vacunacion.abrirPuesto(puesto);
                    puesto = -1;
                }
                
                if (vacunacion.getNumVacunados()==15){
                    vacunacion.salirSanitario(puesto, this);
                    descanso.entradaDescanso(this);
                    puesto = -1;
                    vacunacion.setNumVacunados (0);
                    
                  
                    semEspera.release();
                    if (!colaReaccion.isEmpty()){//ENTOCNES ALGUN PACIENTE HA REACCIONADO MAL 
                        observacion.ayuda(this);
                        sem3.release();//DESPIERTA AL HILO QUE ESTA PACIENTE PORQUE EL SANITARIO YA LE HA CHEQUEADO Y PUEDE IRSE A CASA 
                    }
                    
                   
                }
               
                if (puesto == -1){//SIGNFICA QUE EL SANITARIO HA SALIDO DEL PUESTO Y NECESITA UNA NUEVO 
                   
                    puesto = vacunacion.obtenerPuestoSanitario();
                    System.out.println("\n[SANITARIO]EL SANITARIO ENTRA AL PUESTO: "+ puesto +" id "+ id);
                    vacunacion.entrarSanitario(this,puesto);    
                }
               
                
                
                sem.acquire();//EL SANITARIO SOLO PUEDE VACUNAR SI HAY ALGUN PACIENTE EN EL PUESTO 
                vacunacion.vacunacion(this);//VACUNA AL PACIENTE DEL PUESTO
                System.out.println("El paciente ha sido vacunado por : "+ id + " con la vacuna ");
               
                
               
            }
            
            
        }catch (InterruptedException e){
            System.out.println("[SANITARIO] Error sanitario -> "+ e.getMessage());
        }
    }
    
    
   
   
    
   
}
