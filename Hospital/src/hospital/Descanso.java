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
import java.util.concurrent.Semaphore;

/**
 *
 * @author marco
 */
public class Descanso implements Serializable{
   
    private ArrayList<String> array = new ArrayList<String>();
     private Semaphore semEspera;
     
    public Descanso (Semaphore semEspera){
      
        this.semEspera = semEspera;
    }
    
    public void entradaIni(Sanitarios sanitario ) {
        try{
            array.add(sanitario.getID());
           // ventana.mostrarDescanso(array);
            sleep((int)(Math.random()* (3000 - 1000 + 1) + 1000));
            System.out.println("\nSanitario " + sanitario.getID() + " pasa a la sala de descanso");
            array.remove(sanitario.getID());
            //ventana.mostrarDescanso(array);
        }catch (InterruptedException e){
            System.out.println("Error en la sala de descanso -> "+ e.getMessage());
        }   
    }
    public void entradaDescanso(Sanitarios sanitario) {
        
        try{
            array.add(sanitario.getID());
            
            //ventana.mostrarDescanso(array);
            System.out.println("\nEl Sanitario despues de cerrar el puesto " + sanitario.getID()+ " pasa a la sala de descanso");
            sleep((int)(Math.random()* (8000 - 5000 + 1) + 5000));
            
            array.remove(sanitario.getID());
            //ventana.mostrarDescanso(array);
            
            
        }catch (InterruptedException e){
            System.out.println("Error en la sala de descanso -> "+ e.getMessage());
        }   
    }
    public void cerradoBox(Sanitarios sanitario){
        try{
            array.add(sanitario.getID());
            //ventana.mostrarDescanso(array);
            System.out.println("\nEl Sanitario despues de cerrar el puesto " + sanitario.getID()+ " pasa a la sala de descanso");
            semEspera.acquire();//HASTA QUE NO SALGA UN SANITARIO DEL PUESTO DE DESCANSO POR ESTE NO VA A SALIR DEL SUYO
            array.remove(sanitario.getID());
            //ventana.mostrarDescanso(array);
        }catch (InterruptedException e){
           System.out.println("Error en la sala de descanso -> "+ e.getMessage());
        }   
    }
    public ArrayList<String> getArray(){
        return array;
    }
    
    
}
