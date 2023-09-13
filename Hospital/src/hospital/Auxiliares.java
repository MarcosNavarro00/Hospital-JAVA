/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import hospital.Interface.Vent;
import java.io.Serializable;

import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
//

/**
 *
 * @author marco
 */
public class Auxiliares extends Thread implements Serializable {
    Recepcion recepcion;
    Vacunacion vacunacion;
    
    private String id;
    private final Queue<String> colaVacunas;
    private int verificados, numVacunas;
    private Semaphore sem;
    
    public Auxiliares (String id, Recepcion recepcion, Vacunacion vacunacion,  Semaphore sem ){
        this.id = id; 
        this.recepcion = recepcion;
        this.vacunacion = vacunacion;
        this.colaVacunas = vacunacion.getCola();
      
        this.verificados = 0;
        this.sem = sem;
        this.numVacunas = 0;
        
    }
    public String getID(){
        return id;
    }
   
  
    public synchronized void run(){
        try{
            
            if (id == "A1"){ //Primer auxiliar
                while (true){
                    if ( verificados == 10){ 
                        //System.out.println("\n[AUXILIAR] El auxiliar descansa id "+ id);
                        verificados = 0;
                        //ventana.mostrarAuxliar1("Descansando");
                        sleep((int) (Math.random() * (5000 - 3000 + 1) + 3000)); //el auxiliar descnasa entre 3 y 5 segundos

                    }
                    //System.out.println("\n[AUXILIAR] El auxiliar se ejecuta id "+id);
                    //ventana.mostrarAuxliar1(id);
                 
                 
                    if (recepcion.procesarPaciente()){//se verifica que el paciente esta citado
                        verificados++;
                        System.out.println("\n {SANITARIO} EL PACIENTE HA SIDO FERIFICADO ");
                        sem.release();// si es true el paciente sigue si ejecucion sino se queda parado
                    }else{
                       
                        verificados++;
                    }
                    
                }
            }
        }catch (InterruptedException e){
            System.out.println("[AUXILIAR]Error auxiliar -> "+ e.getMessage());
        }
        if (id == "A2") {//Segundo auxiliar
            for (int i = 0; i < 2000; i++)//el auxiliar esta ejecutandose hasta que crea 2000 vacunas
            {
                try {
                    String idVacunas = "V" + i;
                    System.out.println("\nVacunas creandose " + idVacunas);
                    if (numVacunas ==20){
                        System.out.println("\nEL Auxiliar que prepara las vacunas descansa tras haber preparado 20 " + idVacunas);
                            
                        sleep((int)(Math.random() * (4000 - 1000 + 1) + 1000));
                        numVacunas = 0;
                        //ventana.mostrarAuxliar2("Descansando");
                    }
                    sleep((int) (Math.random() * (2000 - 1000 + 1) + 1000));
                    colaVacunas.offer(idVacunas); //Añade el elemento
                    //ventana.mostrasVacunas(colaVacunas);
                    numVacunas++;
                    //ventana.mostrarAuxliar2(id);
                            
                    synchronized (colaVacunas) {
                        colaVacunas.notifyAll();
                    }
                            
                } catch (InterruptedException e) {
                    System.out.println("[AUXILIAR] Error creando vacunas: " + e.getMessage());
                }

            }
        }  
        
    }
  
}
