/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import hospital.Interface.Vent;

import static java.lang.Thread.sleep;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;

/**
 *
 * @author marco
 */
public class main {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic he
        String id;
        Queue<Pacientes> colaRecepcion = new ConcurrentLinkedQueue<Pacientes>();
        Queue<String> colaVacunas = new ConcurrentLinkedQueue<String>();
        Queue<Pacientes> colaReaccion = new ConcurrentLinkedQueue<Pacientes>();
        Semaphore sem = new Semaphore(0);
        Semaphore sem2 = new Semaphore(0);
        Semaphore sem3 = new Semaphore(0);
        Semaphore semEspera = new Semaphore(0);
        
        
        Vacunacion vacunacion = new Vacunacion(colaVacunas);
        Recepcion recepcion = new Recepcion(colaRecepcion, vacunacion);
        Descanso descanso = new Descanso(semEspera);
        Observacion observacion = new Observacion(colaReaccion);
        Contenedor contenedor = new Contenedor (recepcion,vacunacion,descanso, observacion);
        server ser = new server (contenedor);
        
        ser.start();
        
        
        vacunacion.inicializarPuestos();
        observacion.inicializarObservacion();
        
        Auxiliares auxiliar1 = new Auxiliares("A1", recepcion, vacunacion, sem2);
        Auxiliares auxiliar2 = new Auxiliares("A2", recepcion, vacunacion, sem2);
        auxiliar1.start();
        auxiliar2.start();
        
        for (int i = 0; i < 10; i++)// Crea los 10 sanitarios 
        {
            id = "S" + i;
            Sanitarios sanitario = new Sanitarios(id, vacunacion, descanso, sem,sem3, colaReaccion, observacion, semEspera);
            //System.out.print("\nSanitarios creandose:  " + id);
            sanitario.start();
        }
        
       
        for (int i = 0; i < 20; i++)// Crea los 2.000 pacientes en intervalos de 1 y 3 segundos 
        {
            try {
                id = "P" + i;
                sleep((int)(Math.random()* (3000 - 1000 + 1) + 1000));
                Pacientes paciente = new Pacientes(id,recepcion, vacunacion,observacion, sem2, sem, sem3);
                //System.out.print("\npacientes creandose:  " + id);
                paciente.start();
                
                
            } catch (InterruptedException e){
                System.out.println("Error creando pacientes: " + e.getMessage());}
            
        } 
         
        
        
     
    }
    
}
