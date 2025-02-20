package montserratmijares_sop_1;

public class MontserratMijares_SOP_1 {

    public static void main(String[] args) {
        Planificador planificador = new Planificador(5, "FCFS", 10);

        // Crear algunos procesos
        Proceso proceso1 = new Proceso(1, "Proceso1", 10, true, 0, 0); // CPU-bound
        Proceso proceso2 = new Proceso(2, "Proceso2", 8, false, 3, 2); // I/O-bound
        Proceso proceso3 = new Proceso(3, "Proceso3", 6, true, 0, 0); // CPU-bound
        Proceso proceso4 = new Proceso(4, "Proceso4", 12, false, 4, 3); // I/O-bound

        // Agregar procesos al planificador
        planificador.agregarProceso(proceso1);
        planificador.agregarProceso(proceso2);
        planificador.agregarProceso(proceso3);
        planificador.agregarProceso(proceso4);

        // Crear CPUs
        CPU cpu1 = new CPU(1, planificador);
        CPU cpu2 = new CPU(2, planificador);

        // Iniciar las CPUs en hilos separados
        new Thread(cpu1).start();
        new Thread(cpu2).start();

        // Simular la ejecución de procesos
        simular(planificador, cpu1, cpu2);
    }

    public static void simular(Planificador planificador, CPU cpu1, CPU cpu2) {
        int ciclos = 0;
        while (true) {
            System.out.println("\n--- Ciclo " + ciclos + " ---");

            // Verificar si hay procesos bloqueados que puedan volver a la cola de listos
            planificador.verificarBloqueados();

            // Mostrar el estado de las colas
            mostrarColas(planificador);

            // Mostrar el estado de las CPUs
            mostrarEstadoCPUs(cpu1, cpu2);

            // Incrementar el contador de ciclos
            ciclos++;

            // Simular un ciclo de reloj (1 segundo)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Detener la simulación si no hay más procesos en las colas y las CPUs están inactivas
            if (planificador.cola.estaVacia() && planificador.colaBloqueados.estaVacia() && cpu1.getProcesoActual() == null && cpu2.getProcesoActual() == null) {
                System.out.println("Todos los procesos han terminado.");
                cpu1.detener();
                cpu2.detener();
                break;
            }
        }

        // Mostrar métricas finales
        mostrarMetricas(planificador);
    }

    public static void mostrarColas(Planificador planificador) {
        System.out.println("\nEstado de las colas:");

        // Mostrar cola de listos
        System.out.println("Cola de listos:");
        Proceso[] listos = planificador.cola.getProcesos();
        for (Proceso proceso : listos) {
            System.out.println("- " + proceso.getNombre() + 
                               " (Instrucciones: " + proceso.getInstrucciones() + 
                               ", Estado: " + proceso.getEstado() + 
                               ", Tiempo de espera: " + proceso.getTiempoEspera() + ")");
        }

        // Mostrar cola de bloqueados
        System.out.println("Cola de bloqueados:");
        Proceso[] bloqueados = planificador.colaBloqueados.getProcesos();
        for (Proceso proceso : bloqueados) {
            System.out.println("- " + proceso.getNombre() + 
                               " (Instrucciones: " + proceso.getInstrucciones() + 
                               ", Estado: " + proceso.getEstado() + 
                               ", Tiempo de espera: " + proceso.getTiempoEspera() + ")");
        }
    }

    public static void mostrarEstadoCPUs(CPU cpu1, CPU cpu2) {
        System.out.println("\nEstado de las CPUs:");

        // Mostrar estado de CPU 1
        Proceso procesoCPU1 = cpu1.getProcesoActual();
        if (procesoCPU1 != null) {
            System.out.println("CPU 1 está ejecutando: " + procesoCPU1.getNombre() + 
                               " (Instrucción actual: " + procesoCPU1.getProgramCounter() + 
                               ", Estado: " + procesoCPU1.getEstado() + ")");
        } else {
            System.out.println("CPU 1 está inactiva.");
        }

        // Mostrar estado de CPU 2
        Proceso procesoCPU2 = cpu2.getProcesoActual();
        if (procesoCPU2 != null) {
            System.out.println("CPU 2 está ejecutando: " + procesoCPU2.getNombre() + 
                               " (Instrucción actual: " + procesoCPU2.getProgramCounter() + 
                               ", Estado: " + procesoCPU2.getEstado() + ")");
        } else {
            System.out.println("CPU 2 está inactiva.");
        }
    }

    public static void mostrarMetricas(Planificador planificador) {
        System.out.println("\n--- Métricas finales ---");

        // Obtener las métricas del planificador
        Metricas metricas = planificador.getMetricas();

        // Calcular métricas
        double tiempoEsperaPromedio = metricas.getTiempoEsperaPromedio();
        double tiempoEjecucionPromedio = metricas.getTiempoEjecucionPromedio();
        int procesosCompletados = metricas.getProcesosCompletados();

        // Mostrar métricas
        System.out.println("Tiempo de espera promedio: " + tiempoEsperaPromedio);
        System.out.println("Tiempo de ejecución promedio: " + tiempoEjecucionPromedio);
        System.out.println("Total de procesos completados: " + procesosCompletados);
    }
}