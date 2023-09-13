/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hospital;

import java.io.Serializable;

/**
 *
 * @author marco
 */
public class Puestos implements Serializable {
    Pacientes paciente;
    Sanitarios sanitario;
    private int puesto;
    private String idPaciente, idSanitario;
    private boolean activo;
    public Puestos(){
        this.paciente = null;
        this.sanitario = null;
        this.idPaciente = null;
        this.idSanitario = null;
        puesto = -1;
        activo = true;
    }
    
    public Object getPaciente(){
        return paciente;
    }
    public void setPaciente(Pacientes paciente){
        
        this.paciente = paciente;
        if (paciente != null){
            this.idPaciente = paciente.getID();
        }
    }
    
    public Object getSanitario(){
        return sanitario;
    }
    public void setSanitario(Sanitarios sanitario){
        this.sanitario = sanitario;
        if (sanitario != null){
            this.idSanitario = sanitario.getID();
        }
    }
    
    public void setPuesto(int puesto){
       this.puesto=puesto;
    }
    public int getPuesto(){
       return puesto; 
    }
    
    public String getIdPaciente(){
        if (paciente != null){
            return idPaciente;
        }
        return " ";
    }
    
    public String getIdSanitario(){
         if (sanitario != null){
            return idSanitario;
        }
        return " ";
        
    }
    public void setActivo(boolean activo){
       this.activo=activo;
    }
    public boolean getActivo(){
        return activo;
    }

    
    
}
