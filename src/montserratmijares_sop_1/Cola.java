package montserratmijares_sop_1;

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

    // Método para agregar un proceso a la cola
    public void agregar(Proceso proceso) {
        if (size == capacidad) {
            throw new IllegalStateException("La cola está llena");
        }
        fin = (fin + 1) % capacidad;
        procesos[fin] = proceso;
        size++;
        System.out.println("Proceso " + proceso.getNombre() + " agregado a la cola.");
    }

    // Método para eliminar y devolver el primer proceso de la cola
    public Proceso poll() {
        if (size == 0) {
            throw new IllegalStateException("La cola está vacía");
        }
        Proceso proceso = procesos[principio];
        principio = (principio + 1) % capacidad;
        size--;
        System.out.println("Proceso " + proceso.getNombre() + " eliminado de la cola.");
        return proceso;
    }

    // Método para consultar el primer proceso de la cola sin eliminarlo
    public Proceso peek() {
        if (size == 0) {
            throw new IllegalStateException("La cola está vacía");
        }
        System.out.println("Consultando el primer proceso de la cola: " + procesos[principio].getNombre());
        return procesos[principio];
    }

    // Método para eliminar un proceso específico de la cola
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
        System.out.println("Proceso " + proceso.getNombre() + " eliminado de la cola.");
    }

    // Método para verificar si la cola está vacía
    public boolean estaVacia() {
        return size == 0;
    }

    // Método para obtener el tamaño de la cola
    public int getSize() {
        return size;
    }
}