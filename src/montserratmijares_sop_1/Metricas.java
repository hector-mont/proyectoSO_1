package montserratmijares_sop_1;

public class Metricas {
    private int tiempoTotalEspera;
    private int tiempoTotalEjecucion;
    private int procesosCompletados;

    public Metricas() {
        this.tiempoTotalEspera = 0;
        this.tiempoTotalEjecucion = 0;
        this.procesosCompletados = 0;
        System.out.println("Métricas inicializadas.");
    }

    public void registrarEspera(int tiempo) {
        tiempoTotalEspera += tiempo;
        System.out.println("Tiempo de espera registrado: " + tiempo + " unidades. Total: " + tiempoTotalEspera);
    }

    public void registrarEjecucion(int tiempo) {
        tiempoTotalEjecucion += tiempo;
        System.out.println("Tiempo de ejecución registrado: " + tiempo + " unidades. Total: " + tiempoTotalEjecucion);
    }

    public void incrementarProcesosCompletados() {
        procesosCompletados++;
        System.out.println("Proceso completado. Total de procesos completados: " + procesosCompletados);
    }

    public double getTiempoEsperaPromedio() {
        if (procesosCompletados == 0) {
            System.out.println("No hay procesos completados para calcular el tiempo de espera promedio.");
            return 0.0;
        }
        double promedio = (double) tiempoTotalEspera / procesosCompletados;
        System.out.println("Tiempo de espera promedio: " + promedio);
        return promedio;
    }

    public double getTiempoEjecucionPromedio() {
        if (procesosCompletados == 0) {
            System.out.println("No hay procesos completados para calcular el tiempo de ejecución promedio.");
            return 0.0;
        }
        double promedio = (double) tiempoTotalEjecucion / procesosCompletados;
        System.out.println("Tiempo de ejecución promedio: " + promedio);
        return promedio;
    }

    public int getTiempoTotalEspera() {
        return tiempoTotalEspera;
    }

    public int getTiempoTotalEjecucion() {
        return tiempoTotalEjecucion;
    }

    public int getProcesosCompletados() {
        return procesosCompletados;
    }

    public void reiniciarMetricas() {
        tiempoTotalEspera = 0;
        tiempoTotalEjecucion = 0;
        procesosCompletados = 0;
        System.out.println("Métricas reiniciadas.");
    }
}