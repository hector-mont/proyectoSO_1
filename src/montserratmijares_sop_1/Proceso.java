/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Etol
 */
public class Proceso extends Thread {
    private int id;
    private String nombre;
    private int instrucciones;
    private boolean cpuBound;
    private int ciclosExcepcion;
    private int ciclosCompletarExcepcion;
    private String estado;
    private int ciclosEjecutados;
    private int tiempoEspera; 
    private Semaphore semaforoExcepcion;
    
    public Proceso(int id, String nombre, int instrucciones, boolean cpuBound, int ciclosExcepcion, int ciclosCompletarExcepcion){
        
        this.id = id;
        this.nombre = nombre;
        this.instrucciones = instrucciones;
        this.cpuBound = cpuBound;
        this.ciclosExcepcion = ciclosExcepcion;
        this.ciclosCompletarExcepcion = ciclosCompletarExcepcion;
        this.estado = "Ready";
        this.ciclosEjecutados = 0;
        this.semaforoExcepcion = new Semaphore(1);
    }
    
    
    @Override
    public void run(){
        while(instrucciones > 0 ){
            if (estado.equals("Running")){
                System.out.println(nombre + " ejecutando instruccion. Instrucciones restantes: " + instrucciones);
                instrucciones --;
                ciclosEjecutados ++;
                
                if(!cpuBound && ciclosExcepcion > 0 && ciclosEjecutados % ciclosExcepcion == 0){
                     try {
                        semaforoExcepcion.acquire(); 
                        estado = "Blocked";
                        System.out.println(nombre + " generó una excepción de E/S.");
                        Thread.sleep(ciclosCompletarExcepcion * 100); 
                        estado = "Ready";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaforoExcepcion.release(); 
                    }
                }

                try {
                    Thread.sleep(100); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
               
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
         estado = "Terminated";
        System.out.println(nombre + " ha terminado.");
    }
    
     
    
    public int getProcesoId() {return id;}
    public String getNombre(){return nombre;}
    public int getInstrucciones(){return instrucciones;}
    public boolean cpuBoound(){return cpuBound;}
    public int getCiclosExcepcion(){return ciclosExcepcion;}
    public int getCiclosCompletarExcepcion(){return ciclosCompletarExcepcion;}
    public String getEstado(){return estado;}
      
    public int getCiclosEjecutados(){return ciclosEjecutados;}
    public void setEstado(String estado){this.estado = estado;}
    public void setInstrucciones(int instrucciones) { this.instrucciones = instrucciones; }
    public void setCpuBound(boolean cpuBound) { this.cpuBound = cpuBound; }
    public void setCiclosExcepcion(int ciclosExcepcion) { this.ciclosExcepcion = ciclosExcepcion; }
    public void setCiclosCompletarExcepcion(int ciclosCompletarExcepcion) { this.ciclosCompletarExcepcion = ciclosCompletarExcepcion; }
    public void setCiclosEjecutados(int ciclosEjecutados) { this.ciclosEjecutados = ciclosEjecutados; }
  
    
}
