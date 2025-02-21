package montserratmijares_sop_1;

import javax.swing.SwingUtilities;

public class MontserratMijares_SOP_1 {

     public static void main(String[] args) {
        // Inicializar el planificador y las CPUs
        ConfiguracionManager configuracionManager = new ConfiguracionManager();
        Planificador planificador = new Planificador(10, "FCFS", 100); // Inicializa tu planificador
        CPU[] cpus = new CPU[2]; // Supongamos que tienes 2 CPUs
        for (int i = 0; i < cpus.length; i++) {
            cpus[i] = new CPU(i, planificador);
        }

        // Inicializar el controlador y conectarlo con la interfaz gráfica
        Controlador controlador = new Controlador(null, configuracionManager, planificador, cpus);

        // Inicializar la interfaz gráfica
        SimuladorGUI gui = new SimuladorGUI(controlador);

        // Establecer el controlador en la interfaz gráfica
        controlador.setGui(gui); // Asegúrate de que Controlador tenga un método para establecer la GUI

        // Mostrar la interfaz gráfica
        gui.setVisible(true);

        // Iniciar las CPUs
        for (CPU cpu : cpus) {
            cpu.iniciar();
        }
    }
}
