/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package montserratmijares_sop_1;

/**
 *
 * @author Etol
 */
public class Cola {
    private Proceso[] procesos;
    private int capacidad;
    private int principio;
    private int fin;
    private int size;

    public Cola(int capacidad) {
        this.capacidad = capacidad;
        this.procesos = new Proceso[capacidad];
        this.principio = 0;
        this.fin = -1;
        this.size = 0;
    }

    // Método para obtener todos los procesos en la cola
    public Proceso[] getProcesos() {
        Proceso[] listaProcesos = new Proceso[size]; // Crear un arreglo del tamaño de la cola
        for (int i = 0; i < size; i++) {
            int index = (principio + i) % capacidad; // Calcular el índice circular
            listaProcesos[i] = procesos[index]; // Copiar el proceso al arreglo
        }
        return listaProcesos; // Devolver el arreglo de procesos
    }

    // Resto de los métodos de la clase Cola...
    public void agregar(Proceso proceso) {
        if (size == capacidad) {
            throw new IllegalStateException("La cola esta llena");
        }
        fin = (fin + 1) % capacidad;
        procesos[fin] = proceso;
        size++;
    }

    public Proceso poll() {
        if (size == 0) {
            throw new IllegalStateException("la cola esta vacia");
        }
        Proceso proceso = procesos[principio];
        principio = (principio + 1) % capacidad;
        size--;
        return proceso;
    }

    public Proceso peek() {
        if (size == 0) {
            throw new IllegalStateException("La cola esta vacia");
        }
        return procesos[principio];
    }

    public void remove(Proceso proceso) {
        if (size == 0) {
            throw new IllegalStateException("La cola está vacía");
        }

        int index = -1;
        for (int i = 0; i < size; i++) {
            int actual = (principio + i) % capacidad;
            if (procesos[actual] == proceso) {
                index = actual;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException("El proceso no está en la cola");
        }
        for (int i = index; i != fin; i = (i + 1) % capacidad) {
            int next = (i + 1) % capacidad;
            procesos[i] = procesos[next];
        }

        procesos[fin] = null;
        fin = (fin - 1 + capacidad) % capacidad;
        size--;
    }

    public boolean Vacia() {
        return size == 0;
    }

    public int cantidad() {
        return size;
    }
}
