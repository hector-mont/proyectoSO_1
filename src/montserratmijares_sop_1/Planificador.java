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

    public Cola cola;
    public Cola colaBloqueados;
    public int tiempoMaxEjecucion;
    public String politica;
    public Semaphore semaforo;

    public Planificador(int tiempoMaxEjecucion, String politica, int capacidadCola) {
        this.cola = new Cola(capacidadCola);
        this.colaBloqueados = new Cola(capacidadCola); // Inicializar la cola de bloqueados
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

    public void moverABloqueados(Proceso proceso) {
        try {
            semaforo.acquire();
            colaBloqueados.agregar(proceso);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforo.release();
        }
    }

    public void verificarBloqueados() {
        try {
            semaforo.acquire();
            Proceso proceso = colaBloqueados.poll();
            if (proceso != null && proceso.getEstado().equals("Ready")) {
                cola.agregar(proceso);
            }
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

            // Incrementar el tiempo de espera de todos los procesos en la cola de listos
            incrementarTiempoEsperaCola();

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
                case "HRRN":
                    proceso = siguienteProcesoHRRN();
                    break;
                default:
                    throw new IllegalArgumentException("Política no soportada: " + politica);
            }

            // Establecer el estado del proceso a "Running" antes de devolverlo
            if (proceso != null) {
                proceso.setEstado("Running");
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

    private Proceso siguienteProcesoRoundRobin() {
        Proceso proceso = cola.poll();
        if (proceso != null && proceso.getInstrucciones() > tiempoMaxEjecucion) {
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

    private Proceso siguienteProcesoHRRN() {
        Proceso procesoMayorResponseRatio = null;
        Proceso[] procesosEnCola = cola.getProcesos(); // Obtener todos los procesos en la cola
        for (Proceso proceso : procesosEnCola) {
            int tiempoEspera = proceso.getTiempoEspera();
            int tiempoEjecucion = proceso.getInstrucciones();
            double responseRatio = (tiempoEspera + tiempoEjecucion) / (double) tiempoEjecucion;

            if (procesoMayorResponseRatio == null || responseRatio > (procesoMayorResponseRatio.getTiempoEspera() + procesoMayorResponseRatio.getInstrucciones()) / (double) procesoMayorResponseRatio.getInstrucciones()) {
                procesoMayorResponseRatio = proceso;
            }
        }
        return procesoMayorResponseRatio;
    }

    // Método para incrementar el tiempo de espera de todos los procesos en la cola de listos
    private void incrementarTiempoEsperaCola() {
        Proceso[] procesosEnCola = cola.getProcesos(); // Obtener todos los procesos en la cola
        for (Proceso proceso : procesosEnCola) {
            proceso.incrementarTiempoEspera(); // Incrementar el tiempo de espera de cada proceso
        }
    }
}
