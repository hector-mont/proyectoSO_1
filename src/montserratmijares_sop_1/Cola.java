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
    public synchronized Proceso[] getProcesos() {
        Proceso[] listaProcesos = new Proceso[size];
        for (int i = 0; i < size; i++) {
            int index = (principio + i) % capacidad;
            listaProcesos[i] = procesos[index];
        }
        return listaProcesos;
    }

    // Método para agregar un proceso a la cola
    public synchronized void agregar(Proceso proceso) {
        if (size == capacidad) {
            System.err.println("No se puede agregar: la cola está llena.");
            return;
        }
        fin = (fin + 1) % capacidad;
        procesos[fin] = proceso;
        size++;
        System.out.println("Proceso " + proceso.getNombre() + " agregado a la cola. Tamaño actual: " + size);
    }

    // Método para eliminar y devolver el primer proceso de la cola
    public synchronized Proceso poll() {
        if (size == 0) {
            System.out.println("La cola está vacía. No hay procesos para eliminar.");
            return null;
        }
        Proceso proceso = procesos[principio];
        procesos[principio] = null;  // Liberar referencia
        principio = (principio + 1) % capacidad;
        size--;
        System.out.println("Proceso " + proceso.getNombre() + " eliminado de la cola. Tamaño actual: " + size);
        return proceso;
    }

    // Método para consultar el primer proceso de la cola sin eliminarlo
    public synchronized Proceso peek() {
        if (size == 0) {
            System.out.println("La cola está vacía. No hay procesos para consultar.");
            return null;
        }
        System.out.println("Primer proceso en la cola: " + procesos[principio].getNombre());
        return procesos[principio];
    }

    // Método para eliminar un proceso específico de la cola
    public synchronized void remove(Proceso proceso) {
        if (size == 0) {
            System.out.println("No se puede eliminar: la cola está vacía.");
            return;
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
            System.out.println("Proceso no encontrado en la cola.");
            return;
        }

        // Desplazar elementos solo si no es el último en la cola
        while (index != fin) {
            int next = (index + 1) % capacidad;
            procesos[index] = procesos[next];
            index = next;
        }

        procesos[fin] = null;
        fin = (fin - 1 + capacidad) % capacidad;
        size--;
        System.out.println("Proceso " + proceso.getNombre() + " eliminado de la cola. Tamaño actual: " + size);
    }

    // Método para verificar si la cola está vacía
    public synchronized boolean estaVacia() {
        return size == 0;
    }

    // Método para obtener el tamaño de la cola
    public synchronized int getSize() {
        return size;
    }
}
