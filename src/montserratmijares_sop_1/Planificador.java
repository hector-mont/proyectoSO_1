package montserratmijares_sop_1;

import java.util.concurrent.Semaphore;

public class Planificador {

    public Cola cola;
    public Cola colaBloqueados;
    public int tiempoMaxEjecucion;
    public String politica;
    public Semaphore semaforo;
    public Metricas metricas;

    public Planificador(int tiempoMaxEjecucion, String politica, int capacidadCola) {
        this.cola = new Cola(capacidadCola);
        this.colaBloqueados = new Cola(capacidadCola);
        this.tiempoMaxEjecucion = tiempoMaxEjecucion;
        this.politica = politica;
        this.semaforo = new Semaphore(1);
        this.metricas = new Metricas();
        System.out.println("Planificador creado con política: " + politica);
    }
    
    public Metricas getMetricas() {
        return metricas;
    }

    public void agregarProceso(Proceso proceso) {
        try {
            semaforo.acquire();
            cola.agregar(proceso);
            System.out.println("Proceso " + proceso.getNombre() + " agregado a la cola de listos.");
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
            System.out.println("Proceso " + proceso.getNombre() + " movido a la cola de bloqueados.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforo.release();
        }
    }

    public void verificarBloqueados() {
        try {
            semaforo.acquire();
            if (!colaBloqueados.estaVacia()) {
                Proceso proceso = colaBloqueados.poll();
                if (proceso != null && proceso.getEstado().equals("Ready")) {
                    cola.agregar(proceso);
                    System.out.println("Proceso " + proceso.getNombre() + " ha sido movido a la cola de listos.");
                }
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
                System.out.println("Proceso " + proceso.getNombre() + " seleccionado para ejecución con política " + politica);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaforo.release();
        }
        return proceso;
    }

    public boolean debeCambiarProceso(Proceso procesoActual) {
        boolean cambiar = false;
        switch (politica) {
            case "RoundRobin":
                cambiar = procesoActual.getCiclosEjecutados() >= tiempoMaxEjecucion;
                break;
            case "SRT":
                cambiar = !cola.estaVacia() && cola.peek().getInstrucciones() < procesoActual.getInstrucciones();
                break;
            default:
                cambiar = false;
        }
        if (cambiar) {
            System.out.println("Cambiando de proceso según política " + politica);
        }
        return cambiar;
    }

    private Proceso siguienteProcesoFCFS() {
        Proceso proceso = cola.poll();
        if (proceso != null) {
            System.out.println("Seleccionado proceso " + proceso.getNombre() + " con política FCFS.");
        }
        return proceso;
    }

    private Proceso siguienteProcesoRoundRobin() {
    Proceso proceso = cola.poll();
    if (proceso != null && proceso.getInstrucciones() > tiempoMaxEjecucion) {
        proceso.setInstrucciones(proceso.getInstrucciones() - tiempoMaxEjecucion); // Usa setInstrucciones
        cola.agregar(proceso);
        System.out.println("Proceso " + proceso.getNombre() + " devuelto a la cola con política RoundRobin.");
    }
    return proceso;
}

    private Proceso siguienteProcesoSPN() {
        Proceso procesoMenorInstrucciones = null;

        for (int i = 0; i < cola.getSize(); i++) {
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
        if (procesoMenorInstrucciones != null) {
            System.out.println("Seleccionado proceso " + procesoMenorInstrucciones.getNombre() + " con política SPN.");
        }
        return procesoMenorInstrucciones;
    }

    private Proceso siguienteProcesoSRT() {
        Proceso procesoMenorInstruccionesRestantes = null;
        for (int i = 0; i < cola.getSize(); i++) {
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
        if (procesoMenorInstruccionesRestantes != null) {
            System.out.println("Seleccionado proceso " + procesoMenorInstruccionesRestantes.getNombre() + " con política SRT.");
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
        if (procesoMayorResponseRatio != null) {
            System.out.println("Seleccionado proceso " + procesoMayorResponseRatio.getNombre() + " con política HRRN.");
        }
        return procesoMayorResponseRatio;
    }

    // Método para incrementar el tiempo de espera de todos los procesos en la cola de listos
    private void incrementarTiempoEsperaCola() {
        Proceso[] procesosEnCola = cola.getProcesos(); // Obtener todos los procesos en la cola
        for (Proceso proceso : procesosEnCola) {
            proceso.incrementarTiempoEspera(); // Incrementar el tiempo de espera de cada proceso
            System.out.println("Tiempo de espera incrementado para el proceso " + proceso.getNombre());
        }
    }
}