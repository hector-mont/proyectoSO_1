package montserratmijares_sop_1;

import java.util.concurrent.Semaphore;

public class Planificador {

    private Cola cola;
    private Cola colaBloqueados;
    private Cola colaTerminados;
    private int tiempoMaxEjecucion;
    private String politica;
    private Semaphore semaforo;
    private Metricas metricas;

    public Planificador(int tiempoMaxEjecucion, String politica, int capacidadCola) {
        this.cola = new Cola(capacidadCola);
        this.colaBloqueados = new Cola(capacidadCola);
        this.colaTerminados = new Cola(capacidadCola); // Inicializar cola de terminados
        this.tiempoMaxEjecucion = tiempoMaxEjecucion;
        this.politica = politica;
        this.semaforo = new Semaphore(1);
        this.metricas = new Metricas();
        System.out.println("Planificador creado con política: " + politica);
    }

    public Metricas getMetricas() {
        return metricas;
    }

    public synchronized void agregarProceso(Proceso proceso) {
        try {
            semaforo.acquire();
            if (proceso.getEstado().equals("Ready")) {
                cola.agregar(proceso);
                System.out.println("Proceso " + proceso.getNombre() + " agregado a la cola.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al agregar proceso: " + e.getMessage());
        } finally {
            semaforo.release();
        }
    }

    public synchronized void moverABloqueados(Proceso proceso) {
        try {
            semaforo.acquire();
            colaBloqueados.agregar(proceso);
            System.out.println("Proceso " + proceso.getNombre() + " movido a la cola de bloqueados.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al mover proceso a bloqueados: " + e.getMessage());
        } finally {
            semaforo.release();
        }
    }

    public synchronized void verificarBloqueados() {
        try {
            semaforo.acquire();
            if (!colaBloqueados.estaVacia()) {
                Proceso proceso = colaBloqueados.poll();
                if (proceso != null) {
                    proceso.setEstado("Ready");
                    cola.agregar(proceso);
                    System.out.println("Proceso " + proceso.getNombre() + " ha sido movido a la cola de listos.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al verificar bloqueados: " + e.getMessage());
        } finally {
            semaforo.release();
        }
    }

    public synchronized Proceso siguienteProceso() {
        Proceso proceso = null;
        try {
            semaforo.acquire();
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
                    System.err.println("Política no soportada: " + politica);
            }

            if (proceso != null) {
                if (proceso.haTerminado()) {
                    colaTerminados.agregar(proceso); // Mover a la lista de terminados
                    System.out.println("Proceso " + proceso.getNombre() + " ha sido movido a la cola de terminados.");
                    proceso = null; // No asignar un proceso terminado
                } else {
                    proceso.setEstado("Running");
                    System.out.println("Proceso " + proceso.getNombre() + " seleccionado para ejecución con política " + politica);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }
        return proceso;
    }

    // Nuevo método para obtener la lista de procesos terminados
    public synchronized Proceso[] getProcesosTerminados() {
        return colaTerminados.getProcesos();
    }

    private Proceso siguienteProcesoFCFS() {
        return cola.poll();
    }

    private Proceso siguienteProcesoRoundRobin() {
        Proceso proceso = cola.poll();
        if (proceso != null) {
            if (proceso.haTerminado()) {
                System.out.println("Proceso " + proceso.getNombre() + " ya terminó y no se vuelve a encolar.");
            } else if (proceso.getInstrucciones() > tiempoMaxEjecucion) {
                proceso.setInstrucciones(proceso.getInstrucciones() - tiempoMaxEjecucion);
                cola.agregar(proceso);
                System.out.println("Proceso " + proceso.getNombre() + " devuelto a la cola en RoundRobin.");
            }
        }
        return proceso;
    }

    private Proceso siguienteProcesoSPN() {
        Proceso[] procesos = cola.getProcesos();
        Proceso seleccionado = null;
        for (Proceso proceso : procesos) {
            if (seleccionado == null || proceso.getInstrucciones() < seleccionado.getInstrucciones()) {
                seleccionado = proceso;
            }
        }
        if (seleccionado != null) {
            cola.remove(seleccionado);
        }
        return seleccionado;
    }

    private Proceso siguienteProcesoSRT() {
        Proceso[] procesos = cola.getProcesos();
        Proceso seleccionado = null;
        for (Proceso proceso : procesos) {
            if (seleccionado == null || proceso.getInstrucciones() < seleccionado.getInstrucciones()) {
                seleccionado = proceso;
            }
        }
        if (seleccionado != null) {
            cola.remove(seleccionado);
        }
        return seleccionado;
    }

    private Proceso siguienteProcesoHRRN() {
        Proceso[] procesos = cola.getProcesos();
        Proceso seleccionado = null;
        double mayorRatio = -1;

        for (Proceso proceso : procesos) {
            double responseRatio = (double) (proceso.getTiempoEspera() + proceso.getInstrucciones()) / proceso.getInstrucciones();
            if (seleccionado == null || responseRatio > mayorRatio) {
                seleccionado = proceso;
                mayorRatio = responseRatio;
            }
        }

        if (seleccionado != null) {
            cola.remove(seleccionado);
        }
        return seleccionado;
    }

    private synchronized void incrementarTiempoEsperaCola() {
        Proceso[] procesos = cola.getProcesos();
        for (Proceso proceso : procesos) {
            proceso.incrementarTiempoEspera();
        }
    }

    public String getColaListosToString() {
        StringBuilder sb = new StringBuilder("Cola de Listos:\n");
        for (Proceso proceso : cola.getProcesos()) {
            sb.append(proceso.getNombre()).append(" - ").append(proceso.getEstado()).append("\n");
        }
        return sb.toString();
    }

    public String getColaBloqueadosToString() {
        StringBuilder sb = new StringBuilder("Cola de Bloqueados:\n");
        for (Proceso proceso : colaBloqueados.getProcesos()) {
            sb.append(proceso.getNombre()).append(" - ").append(proceso.getEstado()).append("\n");
        }
        return sb.toString();
    }

    public Proceso[] getColaListos() {
        return cola.getProcesos();
    }
    
    public int getNumDeProcesos() {
        return cola.getSize(); // Devuelve el tamaño de la cola de listos
    }


    public Proceso[] getColaBloqueados() {
        return colaBloqueados.getProcesos();
    }
    // Método para cambiar la política de planificación
    public void setPolitica(String politica) {
        this.politica = politica;
        System.out.println("Política de planificación cambiada a: " + politica);
    }
}
