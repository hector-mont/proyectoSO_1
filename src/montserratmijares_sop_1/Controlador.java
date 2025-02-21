/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

import javax.swing.Timer;

public class Controlador {
    private SimuladorGUI gui;
    private ConfiguracionManager configuracionManager;
    private Planificador planificador;
    private CPU[] cpus;

    public Controlador(SimuladorGUI gui, ConfiguracionManager configuracionManager, Planificador planificador, CPU[] cpus) {
        this.gui = gui;
        this.configuracionManager = configuracionManager;
        this.planificador = planificador;
        this.cpus = cpus;

        // Actualizar la interfaz cada segundo
        Timer timer = new Timer(1000, e -> actualizarInterfaz());
        timer.start();
    }
    
    public void setGui(SimuladorGUI gui) {
        this.gui = gui;
    }

    public void iniciarSimulacion() {
        // Obtener los valores de la interfaz
        String duracionCiclo = gui.getDuracionCiclo();
        String numProcesadores = gui.getNumProcesadores();
        String numInstrucciones = gui.getNumInstrucciones();
        String tipoProceso = gui.getTipoProceso();
        String ciclosExcepcion = gui.getCiclosExcepcion();
        String ciclosCompletarExcepcion = gui.getCiclosCompletarExcepcion();

        // Crear un nuevo proceso con los parámetros ingresados
        int procesoId = planificador.getNumDeProcesos(); // Suponiendo que tienes un método para contar procesos
        Proceso proceso = new Proceso(procesoId, "Proceso" + procesoId, Integer.parseInt(numInstrucciones),
            tipoProceso.equals("CPU Bound"), Integer.parseInt(ciclosExcepcion),
            Integer.parseInt(ciclosCompletarExcepcion), planificador);

        // Agregar el proceso al planificador
        planificador.agregarProceso(proceso);

        // Iniciar la simulación
        gui.actualizarLog("Simulación iniciada con los siguientes parámetros:");
        gui.actualizarLog("Duración del ciclo: " + duracionCiclo + " ms");
        gui.actualizarLog("Número de procesadores: " + numProcesadores);
        gui.actualizarLog("Número de instrucciones: " + numInstrucciones);
        gui.actualizarLog("Tipo de proceso: " + tipoProceso);
        gui.actualizarLog("Ciclos para excepción: " + ciclosExcepcion);
        gui.actualizarLog("Ciclos para completar excepción: " + ciclosCompletarExcepcion);
    }

    public void detenerSimulacion() {
        gui.actualizarLog("Simulación detenida.");
    }

    public void guardarConfiguracion() {
        String[] configuracion = {
            gui.getDuracionCiclo(),
            gui.getNumProcesadores(),
            gui.getNumInstrucciones(),
            gui.getTipoProceso(),
            gui.getCiclosExcepcion(),
            gui.getCiclosCompletarExcepcion()
        };

        configuracionManager.guardarConfiguracion("configuracion.txt", configuracion);
        gui.actualizarLog("Configuración guardada en configuracion.txt");
    }

    public void cargarConfiguracion() {
        String[] configuracion = configuracionManager.cargarConfiguracion("configuracion.txt");
        if (configuracion != null) {
            gui.setDuracionCiclo(configuracion[0]);
            gui.setNumProcesadores(configuracion[1]);
            gui.setNumInstrucciones(configuracion[2]);
            gui.setTipoProceso(configuracion[3]);
            gui.setCiclosExcepcion(configuracion[4]);
            gui.setCiclosCompletarExcepcion(configuracion[5]);
            gui.actualizarLog("Configuración cargada desde configuracion.txt");
        }
    }

    public void cambiarAlgoritmoPlanificacion(String algoritmo) {
        planificador.setPolitica(algoritmo);
        gui.actualizarLog("Algoritmo de planificación cambiado a: " + algoritmo);
    }

    public void actualizarInterfaz() {
        // Actualizar la cola de listos
        gui.actualizarColaListos(planificador.getColaListosToString());

        // Actualizar la cola de bloqueados
        gui.actualizarColaBloqueados(planificador.getColaBloqueadosToString());

        // Actualizar la información de las CPUs
        StringBuilder cpuInfo = new StringBuilder("Información de CPUs:\n");
        for (CPU cpu : cpus) {
            Proceso proceso = cpu.getProcesoActual();
            if (proceso != null) {
                cpuInfo.append("CPU ").append(cpu.getCpuId()).append(": ")
                        .append(proceso.getNombre()).append(" - ")
                        .append(proceso.getEstado()).append("\n");
            } else {
                cpuInfo.append("CPU ").append(cpu.getCpuId()).append(": Inactiva\n");
            }
        }
        gui.actualizarCPUInfo(cpuInfo.toString());
    }
}