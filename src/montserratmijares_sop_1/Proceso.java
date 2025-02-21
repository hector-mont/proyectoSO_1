package montserratmijares_sop_1;

import java.util.concurrent.Semaphore;

public class Proceso extends Thread {

    private int id;
    private String nombre;
    private int instrucciones;
    private boolean cpuBound;
    private int ciclosExcepcion;
    private int ciclosCompletarExcepcion;
    private volatile String estado;
    private int ciclosEjecutados;
    private int tiempoEspera;
    private Semaphore semaforoExcepcion;
    private int programCounter;
    private int mar;
    private Planificador planificador;

    public Proceso(int id, String nombre, int instrucciones, boolean cpuBound, int ciclosExcepcion, int ciclosCompletarExcepcion, Planificador planificador) {
        this.id = id;
        this.nombre = nombre;
        this.instrucciones = instrucciones;
        this.cpuBound = cpuBound;
        this.ciclosExcepcion = ciclosExcepcion;
        this.ciclosCompletarExcepcion = ciclosCompletarExcepcion;
        this.estado = "Ready";
        this.ciclosEjecutados = 0;
        this.tiempoEspera = 0;
        this.semaforoExcepcion = new Semaphore(1);
        this.programCounter = 0;
        this.mar = 0;
        this.planificador = planificador;
    }

    @Override
    public void run() {
        while (instrucciones > 0) {
            if (estado.equals("Running")) {
                System.out.println(nombre + " ejecutando instrucción " + (programCounter + 1) + ". Instrucciones restantes: " + instrucciones);
                instrucciones--;
                ciclosEjecutados++;
                programCounter++;
                mar++;

                if (!cpuBound && ciclosExcepcion > 0 && ciclosEjecutados % ciclosExcepcion == 0) {
                    manejarExcepcionIO();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                esperarCambioEstado();
            }
        }
        setEstado("Terminated");
        System.out.println(nombre + " ha terminado.");
    }

    private void manejarExcepcionIO() {
        try {
            semaforoExcepcion.acquire();
            setEstado("Blocked");
            System.out.println(nombre + " generó una excepción de E/S.");
            planificador.moverABloqueados(this);

            Thread.sleep(ciclosCompletarExcepcion * 1000);

            setEstado("Ready");
            planificador.agregarProceso(this);
            System.out.println(nombre + " ha completado la excepción de E/S.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Proceso interrumpido: " + e.getMessage());
        } finally {
            semaforoExcepcion.release();
        }
    }

    private void esperarCambioEstado() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void incrementarTiempoEspera() {
        this.tiempoEspera++;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public boolean haTerminado() {
        return instrucciones == 0;
    }

    public void setInstrucciones(int instrucciones) {
        this.instrucciones = instrucciones;
    }

    public int getProcesoId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getInstrucciones() {
        return instrucciones;
    }

    public boolean esCpuBound() {
        return cpuBound;
    }

    public int getCiclosExcepcion() {
        return ciclosExcepcion;
    }

    public int getCiclosCompletarExcepcion() {
        return ciclosCompletarExcepcion;
    }

    public synchronized String getEstado() {
        return estado;
    }

    public synchronized void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCiclosEjecutados() {
        return ciclosEjecutados;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getMar() {
        return mar;
    }
}
