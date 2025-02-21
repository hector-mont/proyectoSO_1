package montserratmijares_sop_1;

public class CPU implements Runnable {

    private int id;
    private Planificador planificador;
    private Proceso procesoActual;
    private volatile boolean activa;
    private Thread thread;

    public CPU(int id, Planificador planificador) {
        this.id = id;
        this.planificador = planificador;
        this.activa = true;
        this.procesoActual = null;
        this.thread = new Thread(this);
    }

    public void iniciar() {
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("CPU " + id + " iniciada.");

        while (activa) {
            if (procesoActual == null) {
                synchronized (this) {
                    procesoActual = planificador.siguienteProceso();
                }

                if (procesoActual != null) {
                    procesoActual.setEstado("Running");
                    System.out.println("CPU " + id + " ejecutando proceso: " + procesoActual.getNombre());

                    // En vez de usar start(), ejecutamos run() en un nuevo hilo
                    Thread procesoThread = new Thread(procesoActual);
                    procesoThread.start();

                    try {
                        procesoThread.join(); // Esperar a que termine la ejecución del proceso
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("CPU " + id + " interrumpida mientras esperaba el proceso.");
                    }
                }
            }

            if (procesoActual != null) {
                synchronized (this) {
                    if (procesoActual.haTerminado()) {
                        System.out.println("CPU " + id + " terminó el proceso: " + procesoActual.getNombre());
                        procesoActual = null;
                    } else if (procesoActual.getEstado().equals("Blocked")) {
                        System.out.println("CPU " + id + " bloqueó el proceso: " + procesoActual.getNombre());
                        procesoActual = null;
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("CPU " + id + " interrumpida: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("CPU " + id + " detenida.");
    }

    public synchronized void detener() {
        this.activa = false;
        thread.interrupt(); // Interrumpir el hilo para salir del bucle de inmediato
    }

    public synchronized Proceso getProcesoActual() {
        return procesoActual;
    }

    public int getCpuId() {
        return id;
    }
}
