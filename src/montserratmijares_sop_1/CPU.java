package montserratmijares_sop_1;

public class CPU implements Runnable {
    private int id;
    private Planificador planificador;
    private Proceso procesoActual;
    private boolean activa;

    public CPU(int id, Planificador planificador) {
        this.id = id;
        this.planificador = planificador;
        this.activa = true;
        this.procesoActual = null;
    }

    @Override
    public void run() {
        System.out.println("CPU " + id + " iniciada.");
        while (activa) {
            if (procesoActual == null || procesoActual.haTerminado()) {
                procesoActual = planificador.siguienteProceso();
                if (procesoActual != null) {
                    procesoActual.setEstado("Running");
                    System.out.println("CPU " + id + " ejecutando proceso: " + procesoActual.getNombre());
                }
            }

            if (procesoActual != null) {
                procesoActual.run(); // Ejecutar el proceso
                if (procesoActual.getEstado().equals("Blocked")) {
                    planificador.moverABloqueados(procesoActual);
                    procesoActual = null;
                } else if (procesoActual.haTerminado()) {
                    System.out.println("CPU " + id + " terminó el proceso: " + procesoActual.getNombre());
                    procesoActual = null;
                }
            }

            try {
                Thread.sleep(1000); // Simular un ciclo de reloj
            } catch (InterruptedException e) {
                System.err.println("CPU " + id + " interrumpida: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            }
        }
        System.out.println("CPU " + id + " detenida.");
    }

    public void detener() {
        this.activa = false;
        System.out.println("CPU " + id + " recibió señal de detención.");
    }

    public Proceso getProcesoActual() {
        return procesoActual;
    }
}