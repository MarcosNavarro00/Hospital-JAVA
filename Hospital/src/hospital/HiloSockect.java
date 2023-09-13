/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class HiloSockect implements Runnable{
    
    private Contenedor contenedor;
    private Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    
    public HiloSockect (Socket s , Contenedor contenedor){
        this.s = s;
        this.contenedor = contenedor;
    }

    
    @Override
    public void run() {
        
        try {
            oos = new ObjectOutputStream(s.getOutputStream());
          
            
            oos.writeObject(contenedor);
        } catch (IOException ex) {
            Logger.getLogger(HiloSockect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
