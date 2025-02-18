/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

/**
 *
 * @author danie
 */
public class CPU implements Runnable{
        private int id;
    private Planificador planificador;
    private Proceso procesoActual;
    private boolean activa;

    public CPU(int id, Planificador planificador) {
        this.id = id;
        this.planificador = planificador;
        this.activa = true;
    }

    @Override
    public void run() {
        while (activa) {
            if (procesoActual == null || procesoActual.getEstado().equals("Terminated")) {
                procesoActual = planificador.siguienteProceso();
                if (procesoActual != null) {
                    procesoActual.setEstado("Running");
                }
            }

            if (procesoActual != null) {
                procesoActual.run(); // Ejecutar el proceso
                if (procesoActual.getEstado().equals("Blocked")) {
                    planificador.moverABloqueados(procesoActual);
                    procesoActual = null;
                }
            }

            try {
                Thread.sleep(100); // Simular un ciclo de reloj
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void detener() {
        this.activa = false;
    }
}

