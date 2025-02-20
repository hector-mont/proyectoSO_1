package montserratmijares_sop_1;

import java.util.concurrent.Semaphore;

public class Proceso extends Thread {

    private int id;
    private String nombre;
    private int instrucciones;
    private boolean cpuBound;
    private int ciclosExcepcion;
    private int ciclosCompletarExcepcion;
    private String estado;
    private int ciclosEjecutados;
    private int tiempoEspera; // Tiempo de espera del proceso
    private Semaphore semaforoExcepcion;
    private int programCounter; // Program Counter (PC)
    private int mar; // Memory Address Register (MAR)

    public Proceso(int id, String nombre, int instrucciones, boolean cpuBound, int ciclosExcepcion, int ciclosCompletarExcepcion) {
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

                // Verificar si el proceso es I/O bound y genera una excepción
                if (!cpuBound && ciclosExcepcion > 0 && ciclosEjecutados % ciclosExcepcion == 0) {
                    try {
                        semaforoExcepcion.acquire();
                        estado = "Blocked"; // Cambiar el estado a "Blocked"
                        System.out.println(nombre + " generó una excepción de E/S.");
                        Thread.sleep(ciclosCompletarExcepcion * 1000); // Simular el tiempo de la excepción
                        estado = "Ready"; // Cambiar el estado a "Ready" después de completar la excepción
                        System.out.println(nombre + " ha completado la excepción de E/S.");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Proceso interrumpido: " + e.getMessage());
                    } finally {
                        semaforoExcepcion.release();
                    }
                }

                try {
                    Thread.sleep(1000); // Simular un ciclo de reloj
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Proceso interrumpido: " + e.getMessage());
                }
            } else {
                try {
                    Thread.sleep(1000); // Esperar si el proceso no está en estado "Running"
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Proceso interrumpido: " + e.getMessage());
                }
            }
        }
        estado = "Terminated";
        System.out.println(nombre + " ha terminado.");
    }

    // Método para incrementar el tiempo de espera
    public void incrementarTiempoEspera() {
        this.tiempoEspera++;
    }

    // Método para obtener el tiempo de espera
    public int getTiempoEspera() {
        return tiempoEspera;
    }

    // Método para verificar si el proceso ha terminado
    public boolean haTerminado() {
        return instrucciones == 0;
    }

    // Método para establecer las instrucciones restantes
    public void setInstrucciones(int instrucciones) {
        this.instrucciones = instrucciones;
    }

    // Getters y Setters
    @Override
    public long getId() {
        return id; // Sobrescribe el método de Thread
    }

    public int getProcesoId() {
        return id; // Método adicional para obtener el id como int
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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