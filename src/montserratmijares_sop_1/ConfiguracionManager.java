/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

import java.io.*;

public class ConfiguracionManager {

    // Guardar configuración en un archivo TXT
    public void guardarConfiguracion(String rutaArchivo, String[] configuracion) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (String linea : configuracion) {
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("Configuración guardada en: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar la configuración: " + e.getMessage());
        }
    }

    // Cargar configuración desde un archivo TXT
    public String[] cargarConfiguracion(String rutaArchivo) {
        String[] configuracion = new String[6]; // 6 parámetros
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            for (int i = 0; i < configuracion.length; i++) {
                configuracion[i] = reader.readLine();
            }
            return configuracion;
        } catch (IOException e) {
            System.err.println("Error al cargar la configuración: " + e.getMessage());
            return null;
        }
    }
}
