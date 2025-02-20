/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package montserratmijares_sop_1;

/**
 *
 * @author Etol
 */
public class MontserratMijares_SOP_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Crear el planificador con una política específica (por ejemplo, "FCFS")
        Planificador planificador = new Planificador(5, "FCFS", 10);

        // Crear algunos procesos
        Proceso proceso1 = new Proceso(1, "Proceso1", 10, true, 0, 0); // CPU-bound
        Proceso proceso2 = new Proceso(2, "Proceso2", 8, false, 3, 2); // I/O-bound
        Proceso proceso3 = new Proceso(3, "Proceso3", 6, true, 0, 0); // CPU-bound

        // Agregar procesos al planificador
        planificador.agregarProceso(proceso1);
        planificador.agregarProceso(proceso2);
        planificador.agregarProceso(proceso3);

        // Simular la ejecución de procesos
        simular(planificador);
    }

    public static void simular(Planificador planificador) {
        int ciclos = 0;
        while (true) {
            System.out.println("\n--- Ciclo " + ciclos + " ---");

            // Obtener el siguiente proceso según la política de planificación
            Proceso proceso = planificador.siguienteProceso();
            if (proceso != null) {
                System.out.println("Ejecutando: " + proceso.getNombre() + " (Instrucciones restantes: " + proceso.getInstrucciones() + ")");
                proceso.run(); // Ejecutar el proceso
            } else {
                System.out.println("No hay procesos en la cola de listos.");
            }

            // Verificar si hay procesos bloqueados que puedan volver a la cola de listos
            planificador.verificarBloqueados();

            // Mostrar el estado de las colas
            mostrarColas(planificador);

            // Incrementar el contador de ciclos
            ciclos++;

            // Simular un ciclo de reloj (1 segundo)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Detener la simulación si no hay más procesos en las colas
            if (planificador.cola.Vacia() && planificador.colaBloqueados.Vacia()) {
                System.out.println("Todos los procesos han terminado.");
                break;
            }
        }
    }

    public static void mostrarColas(Planificador planificador) {
        System.out.println("\nEstado de las colas:");
        
        // Mostrar cola de listos
        System.out.println("Cola de listos:");
        Proceso[] listos = planificador.cola.getProcesos();
        for (Proceso proceso : listos) {
            System.out.println("- " + proceso.getNombre() + " (Instrucciones: " + proceso.getInstrucciones() + ", Estado: " + proceso.getEstado() + ")");
        }

        // Mostrar cola de bloqueados
        System.out.println("Cola de bloqueados:");
        Proceso[] bloqueados = planificador.colaBloqueados.getProcesos();
        for (Proceso proceso : bloqueados) {
            System.out.println("- " + proceso.getNombre() + " (Instrucciones: " + proceso.getInstrucciones() + ", Estado: " + proceso.getEstado() + ")");
        }
    }
    
}
