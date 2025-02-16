/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

/**
 *
 * @author Etol
 */
public class Cola {
    private Proceso[] procesos;
    private int capacidad;
    private int principio;
    private int fin;
    private int size;
    
    public Cola(int capacidad){
        this.capacidad = capacidad;
        this.procesos = new Proceso[capacidad];
        this.principio = 0;
        this.fin = -1;
        this.size = 0;
    }
    
    public void agrefar(Proceso proceso){
        if(size == capacidad){
            throw new IllegalStateException("La cola esta llena");
        }
        fin =(fin + 1) % capacidad;
        procesos[fin] = proceso;
        size++;
    }
    
    public Proceso poll(){
        if (size == 0){
            throw new IllegalStateException("la cola esta vacia");
        }
        Proceso proceso = procesos [principio];
        principio = (principio + 1) % capacidad;
        size--;
        return proceso;
    }
    
    public Proceso peek(){
        if(size == 0){
            throw new IllegalStateException("La cola esta vacia");
        }
        return procesos[principio];
    }
    
    public boolean Vacia(){
        return size == 0;
    }
    
    public int cantidad(){
        return size;
    }
    
}
