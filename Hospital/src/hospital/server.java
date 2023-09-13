/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marco
 */
public class server extends Thread{
    private ServerSocket servidor;
    private Socket cliente;
    private DataInputStream entrada;
    private DataOutputStream salida;
    private String mensaje,respuesta;
    Contenedor contenedor;
    private ExecutorService ppl ;
    
    
    public server(Contenedor contenedor){
        this.ppl = Executors.newCachedThreadPool();
        this.contenedor= contenedor;
       
    }
    
    public void run(){
        try{
            servidor=new ServerSocket(5000); //Creación sel socket servidor
            System.out.println("Servidor abierto...");
            while(true){
                cliente=servidor.accept(); //Espera conexión del cliente
                ppl.execute(new HiloSockect(cliente, contenedor));
                
                System.out.println("Conexión desde: "+
                cliente.getInetAddress().getHostName()+ "("+cliente.getPort()+")"); //info cliente
 
                
            }
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
