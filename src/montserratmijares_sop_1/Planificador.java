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
public class Planificador {
    private Cola cola;
    private int tiempoMaxEjecucion;
    private String politica;
     private Semaphore semaforo;
    
    public Planificador(int tiempoMaxEjecucion, String politica, int capacidadCola ){
        this.cola = new Cola(capacidadCola);
        this.tiempoMaxEjecucion = tiempoMaxEjecucion;
        this.politica = politica;
        this.semaforo = new Semaphore(1);
       
    }
    
      public void agregarProceso(Proceso proceso) {
        try {
            semaforo.acquire();
            cola.agregar(proceso);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforo.release(); 
        }
    }
public Proceso siguienteProceso() {
        Proceso proceso = null;
        try {
            semaforo.acquire(); 
            switch (politica) {
                case "FCFS":
                    proceso = siguienteProcesoFCFS();
                    break;
                case "RoundRobin":
                    proceso = siguienteProcesoRoundRobin();
                    break;
                case "SPN":
                    proceso = siguienteProcesoSPN();
                    break;
                case "SRT":
                    proceso = siguienteProcesoSRT();
                    break;
                case "HRDN":
                    proceso = siguienteProcesoHRDN();
                    break;
                default:
                    throw new IllegalArgumentException("Política no soportada: " + politica);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforo.release(); 
        }
        return proceso;
    }
    
  public boolean debeCambiarProceso(Proceso procesoActual) {
        switch (politica) {
            case "RoundRobin":
                return procesoActual.getCiclosEjecutados() >= tiempoMaxEjecucion;
            case "SRT":
                return !cola.Vacia() && cola.peek().getInstrucciones() < procesoActual.getInstrucciones();
            default:
                return false; 
        }
    }
  
  private Proceso siguienteProcesoFCFS() {
        return cola.poll(); 
    }
  
  private Proceso siguienteProcesoRoundRobin(){
      Proceso proceso = cola.poll();
      if(proceso != null && proceso.getInstrucciones() > tiempoMaxEjecucion){
          proceso.setInstrucciones(proceso.getInstrucciones() - tiempoMaxEjecucion);
          cola.agregar(proceso);
      }
      return proceso;
  }
  
   private Proceso siguienteProcesoSPN() {
        Proceso procesoMenorInstrucciones = null;
       
        for (int i = 0; i < cola.cantidad(); i++) {
            Proceso proceso = cola.poll();
            if (procesoMenorInstrucciones == null || proceso.getInstrucciones() < procesoMenorInstrucciones.getInstrucciones()) {
                if (procesoMenorInstrucciones != null) {
                    cola.agregar(procesoMenorInstrucciones);
                }
                procesoMenorInstrucciones = proceso;
            } else {
                cola.agregar(proceso);
            }
        }
        return procesoMenorInstrucciones;
    }

    private Proceso siguienteProcesoSRT() {
        Proceso procesoMenorInstruccionesRestantes = null;
        for (int i = 0; i < cola.cantidad(); i++) {
            Proceso proceso = cola.poll();
            if (procesoMenorInstruccionesRestantes == null || proceso.getInstrucciones() < procesoMenorInstruccionesRestantes.getInstrucciones()) {
                if (procesoMenorInstruccionesRestantes != null) {
                    cola.agregar(procesoMenorInstruccionesRestantes);
                }
                procesoMenorInstruccionesRestantes = proceso;
            } else {
                cola.agregar(proceso);
            }
        }
        return procesoMenorInstruccionesRestantes;
    }

    
}



