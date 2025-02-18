/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

/**
 *
 * @author danie
 */
public class Metricas {
    private int tiempoTotalEspera;
    private int tiempoTotalEjecucion;
    private int procesosCompletados;

    public void registrarEspera(int tiempo) {
        tiempoTotalEspera += tiempo;
    }

    public void registrarEjecucion(int tiempo) {
        tiempoTotalEjecucion += tiempo;
    }

    public void incrementarProcesosCompletados() {
        procesosCompletados++;
    }

    public double getTiempoEsperaPromedio() {
        return (double) tiempoTotalEspera / procesosCompletados;
    }

    public double getTiempoEjecucionPromedio() {
        return (double) tiempoTotalEjecucion / procesosCompletados;
    }
}
