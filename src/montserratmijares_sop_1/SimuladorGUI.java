/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimuladorGUI extends JFrame {
    private JTextArea logArea;
    private JComboBox<String> algoritmoPlanificacionComboBox;
    private JTextField duracionCicloTextField;
    private JTextField numProcesadoresTextField;
    private JTextField numInstruccionesTextField;
    private JComboBox<String> tipoProcesoComboBox;
    private JTextField ciclosExcepcionTextField;
    private JTextField ciclosCompletarExcepcionTextField;
    private JButton iniciarSimulacionButton;
    private JButton detenerSimulacionButton;
    private JButton guardarConfiguracionButton;
    private JButton cargarConfiguracionButton;
    private JTextArea colaListosArea;
    private JTextArea colaBloqueadosArea;
    private JTextArea cpuInfoArea;
    private Controlador controlador;

    public SimuladorGUI(Controlador controlador) {
        this.controlador = controlador;
        setTitle("Simulador de Planificación de Procesos");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de control
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 2));

        // Duración del ciclo
        controlPanel.add(new JLabel("Duración del Ciclo (ms):"));
        duracionCicloTextField = new JTextField();
        controlPanel.add(duracionCicloTextField);

        // Número de procesadores
        controlPanel.add(new JLabel("Número de Procesadores:"));
        numProcesadoresTextField = new JTextField();
        controlPanel.add(numProcesadoresTextField);

        // Número de instrucciones
        controlPanel.add(new JLabel("Número de Instrucciones:"));
        numInstruccionesTextField = new JTextField();
        controlPanel.add(numInstruccionesTextField);

        // Tipo de proceso (CPU bound o I/O bound)
        controlPanel.add(new JLabel("Tipo de Proceso:"));
        tipoProcesoComboBox = new JComboBox<>(new String[]{"CPU Bound", "I/O Bound"});
        controlPanel.add(tipoProcesoComboBox);

        // Ciclos para excepción
        controlPanel.add(new JLabel("Ciclos para Excepción:"));
        ciclosExcepcionTextField = new JTextField();
        controlPanel.add(ciclosExcepcionTextField);

        // Ciclos para completar excepción
        controlPanel.add(new JLabel("Ciclos para Completar Excepción:"));
        ciclosCompletarExcepcionTextField = new JTextField();
        controlPanel.add(ciclosCompletarExcepcionTextField);

        // Botones
        iniciarSimulacionButton = new JButton("Iniciar Simulación");
        detenerSimulacionButton = new JButton("Detener Simulación");
        guardarConfiguracionButton = new JButton("Guardar Configuración");
        cargarConfiguracionButton = new JButton("Cargar Configuración");

        controlPanel.add(iniciarSimulacionButton);
        controlPanel.add(detenerSimulacionButton);
        controlPanel.add(guardarConfiguracionButton);
        controlPanel.add(cargarConfiguracionButton);

        add(controlPanel, BorderLayout.NORTH);

        // Panel de información
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 3));

        // Área de cola de listos
        colaListosArea = new JTextArea();
        colaListosArea.setEditable(false);
        infoPanel.add(new JScrollPane(colaListosArea));

        // Área de cola de bloqueados
        colaBloqueadosArea = new JTextArea();
        colaBloqueadosArea.setEditable(false);
        infoPanel.add(new JScrollPane(colaBloqueadosArea));

        // Área de información de CPUs
        cpuInfoArea = new JTextArea();
        cpuInfoArea.setEditable(false);
        infoPanel.add(new JScrollPane(cpuInfoArea));

        add(infoPanel, BorderLayout.CENTER);

        // Área de log
        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Listeners
        iniciarSimulacionButton.addActionListener(e -> {
            if (controlador != null) {
                controlador.iniciarSimulacion();
            } else {
                System.err.println("Controlador no inicializado.");
            }
        });

        detenerSimulacionButton.addActionListener(e -> {
            if (controlador != null) {
                controlador.detenerSimulacion();
            } else {
                System.err.println("Controlador no inicializado.");
            }
        });

        guardarConfiguracionButton.addActionListener(e -> {
            if (controlador != null) {
                controlador.guardarConfiguracion();
            } else {
                System.err.println("Controlador no inicializado.");
            }
        });

        cargarConfiguracionButton.addActionListener(e -> {
            if (controlador != null) {
                controlador.cargarConfiguracion();
            } else {
                System.err.println("Controlador no inicializado.");
            }
        });
    }

    // Métodos para obtener los valores de los campos
    public String getDuracionCiclo() {
        return duracionCicloTextField.getText();
    }

    public String getNumProcesadores() {
        return numProcesadoresTextField.getText();
    }

    public String getNumInstrucciones() {
        return numInstruccionesTextField.getText();
    }

    public String getTipoProceso() {
        return (String) tipoProcesoComboBox.getSelectedItem();
    }

    public String getCiclosExcepcion() {
        return ciclosExcepcionTextField.getText();
    }

    public String getCiclosCompletarExcepcion() {
        return ciclosCompletarExcepcionTextField.getText();
    }

    // Métodos para establecer los valores de los campos
    public void setDuracionCiclo(String duracion) {
        duracionCicloTextField.setText(duracion);
    }

    public void setNumProcesadores(String numProcesadores) {
        numProcesadoresTextField.setText(numProcesadores);
    }

    public void setNumInstrucciones(String numInstrucciones) {
        numInstruccionesTextField.setText(numInstrucciones);
    }

    public void setTipoProceso(String tipoProceso) {
        tipoProcesoComboBox.setSelectedItem(tipoProceso);
    }

    public void setCiclosExcepcion(String ciclosExcepcion) {
        ciclosExcepcionTextField.setText(ciclosExcepcion);
    }

    public void setCiclosCompletarExcepcion(String ciclosCompletarExcepcion) {
        ciclosCompletarExcepcionTextField.setText(ciclosCompletarExcepcion);
    }

    // Métodos para actualizar la interfaz
    public void actualizarLog(String mensaje) {
        logArea.append(mensaje + "\n");
    }

    public void actualizarColaListos(String info) {
        colaListosArea.setText(info);
    }

    public void actualizarColaBloqueados(String info) {
        colaBloqueadosArea.setText(info);
    }

    public void actualizarCPUInfo(String info) {
        cpuInfoArea.setText(info);
    }

    // Método para establecer el controlador
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }
}



