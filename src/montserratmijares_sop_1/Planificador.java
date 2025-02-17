/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

/**
 *
 * @author Etol
 */
public class Planificador {
    private Cola cola;
    private int tiempoMaxEjecucion;
    private String politica;
    
    public Planificador(int tiempoMaxEjecucion, String politica, int capacidadCola ){
        this.cola = new Cola(capacidadCola);
        this.tiempoMaxEjecucion = tiempoMaxEjecucion;
        this.politica = politica;
    }
    
    public void agregarProceso(Proceso proceso){
        cola.agregar(proceso);
    }
    
    public Proceso siguienteProceso(){
       switch (politica) {
            case "FCFS":
                return siguienteProcesoFCFS();
            case "RoundRobin":
                return siguienteProcesoRoundRobin();
            case "SPM":
                return siguienteProcesoSPM();
            case "SRT":
                return siguienteProcesoSRT();
            case "HRDN":
                return siguienteProcesoHRDN();
            default:
                throw new IllegalArgumentException("Política no soportada: " + politica);
        }
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


}
