package app;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.TreeMap;

import models.Cliente;
import models.EstadoAtencion;
import models.EstadoPedido;
import models.Operacion;
import models.Pedido;
import models.PedidoUrgente;
import models.Repartidor;
import models.ResultadoAtencion;
import models.TipoOperacion;

//"Contiene los arreglos y las operaciones del sistema"
public class CityLink {
    private TreeMap<Integer, Cliente> clientes = new TreeMap<>();
    private Queue<Pedido> pedidosNormales = new ArrayDeque<>();
    private Queue<Pedido> pedidosUrgentes = new ArrayDeque<>();
    private List<Repartidor> repartidores = new ArrayList<>();
    private Deque<Operacion> operaciones = new ArrayDeque<>();
    // operaciones: para deshacer alta de pedido, asignacion y cambio de estado
    private int nextClienteId = 1;
    private int nextPedidoId = 1;

    public List<Repartidor> getRepartidores() {
        return this.repartidores;
    }

    public boolean isRepartidorInList(Repartidor repartidor) {
        return repartidores.contains(repartidor);
    }

    public boolean addNewRepartidor(Repartidor repartidor) {
        if (isRepartidorInList(repartidor)) {
            throw new IllegalArgumentException("Ya existe un repartidor con el numero de legajo ingresado.");
        }
        return repartidores.add(repartidor);
    }

    public TreeMap<Integer, Cliente> getClientes() {
        return this.clientes;
    }

    /**
     * modifies the client passed by parameter, searched by the code of it and
     * modifying the found client, if found, field by field.
     * 
     * @param cliente - the {@link Cliente} with the code to search in the map, and
     *                the fields to change.
     * @throws {@link NoSuchElementException} if the Client to modify is not found.
     */
    public void modifyClient(Cliente cliente) {
        if (!clientes.containsKey(cliente.getCodigo())) {
            throw new NoSuchElementException("Cliente no encontrado!");
        }
        Cliente c = clientes.get(cliente.getCodigo());
        c.setNombre(cliente.getNombre());
        c.setTelefono(cliente.getTelefono());
        c.setZonaCiudad(cliente.getZonaCiudad());
    }

    public void addNewClient(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe ingresar un Cliente valido");
        }
        cliente.setCodigo(nextClienteId++);
        clientes.put(cliente.getCodigo(), cliente);
    }

    /**
     * Returns the Client deleted from the list or null if nothing was deleted.
     * 
     * @param code - the code of the client to remove.
     * @return the {@link Cliente} removed or {@code null} if nothing was found to
     *         be removed
     */
    public Cliente deleteClient(int code) {
        return clientes.remove(code);
    }

    /**
     * returns a shallow copy of the client with the code received by parameter
     * 
     * @param code - the code of the client to look for
     * @return the {@link Client} found by the code or {@code null} if not found
     */
    public Cliente getCliente(int code) {
        Cliente c = clientes.get(code);
        if (c == null)
            return null;
        return new Cliente(c.getCodigo(), c.getNombre(), c.getTelefono(), c.getZonaCiudad());
    }

    /**
     * returns a Repartidor with the received legajo
     * 
     * @return {@link Repartidor} if one was found, else {@code null}
     */
    public Repartidor getRepartidor(String legajo) {
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getLegajo().equalsIgnoreCase(legajo)) {
                return repartidor;
            }
        }
        return null;
    }

    // asumiendo que un repartidor no puede ser eliminado si tiene pedidos
    // Pendientes o En camino.
    public boolean canDeleteRepartidor(String legajo) {
        Repartidor repartidor = getRepartidor(legajo);
        List<Pedido> pedidosAsig = repartidor.getPedidosAsignados();
        for (Pedido pedido : pedidosAsig) {
            if (pedido.getEstado() == EstadoPedido.PENDIENTE || pedido.getEstado() == EstadoPedido.EN_CAMINO) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deletes the Repartidor with the legajo passed.
     * 
     * @return {@code boolean} true if deleted, false if not found in the list
     * @throws {@link IllegalStateException} if the Repartidor cannot be deleted due
     *                to assigned Pedidos, {@link NoSuchElementException} if the
     *                Repartidor wasn't found.
     */
    public boolean deleteRepartidor(String legajo) {
        if (!canDeleteRepartidor(legajo)) {
            throw new IllegalStateException("El repartidor no puede ser eliminado debido a pedidos asignados.");
        }
        Repartidor repartidor = getRepartidor(legajo);
        if (repartidor == null) {
            throw new NoSuchElementException("Repartidor no encontrado");
        }
        return repartidores.remove(repartidor);
    }

    /**
     * Adds the new Pedido, setting the proper state, dateTime and the code, and
     * registers the operation
     * 
     * @return {@code boolean} for whether the operation happened successfully or
     *         not.
     */
    public boolean addNewPedido(Pedido pedido) {
        pedido.setCodigo(nextPedidoId++);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setfechaHoraIngreso(LocalDateTime.now());
        boolean added;
        if (pedido instanceof PedidoUrgente) {
            added = pedidosUrgentes.offer(pedido);
        } else {
            added = pedidosNormales.offer(pedido);
        }
        if (added) {
            operaciones.push(new Operacion(TipoOperacion.ALTA_PEDIDO, pedido, null, null));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the next pedido to attend to without taking it from the queues
     * 
     * @return {@link Pedido} - the pedido or {@code null} if none was found.
     */
    public Pedido getNextPedido() {
        if (!pedidosUrgentes.isEmpty()) {
            return pedidosUrgentes.peek();
        } else {
            return pedidosNormales.peek();
        }
    }

    /**
     * Tends the next Pedido in list(urgents first, then following arrival order),
     * it assigns it to a proper Repartidor and registers the operation
     * 
     * @return {@link ResultadoAtencion} with a describing {@link EstadoAtencion}
     *         depending on the status of the operation; whether no pedidos was
     *         found, was found but no Repartidor was available, or whether the
     *         operation was successful.
     */
    public ResultadoAtencion atenderPedido() {
        Pedido pedido = getNextPedido();
        if (pedido == null) {
            return new ResultadoAtencion(EstadoAtencion.COLA_VACIA, null, null);
        }
        Repartidor repartidor = getProperRepartidor(pedido);

        if (repartidor != null) {
            repartidor.addPedido(pedido);
        } else {
            return new ResultadoAtencion(EstadoAtencion.SIN_REPARTIDOR_DESPONIBLE, pedido, null);
        }

        if (pedido instanceof PedidoUrgente) {
            pedidosUrgentes.poll(); // this might have a small timing issue (IF the app were to work with multiples
                                    // programs open and working at the same time, but this works now
                                    // because the app is sequential)
        } else {
            pedidosNormales.poll();
        }
        pedido.setEstado(EstadoPedido.EN_CAMINO);
        operaciones
                .push(new Operacion(TipoOperacion.ASIGNACION_PEDIDO, pedido, repartidor.getLegajo(), null));
        return new ResultadoAtencion(EstadoAtencion.ATENDIDO, pedido, repartidor);
    }

    /**
     * returns a Repartidor that can transport the Pedido based on the capacity of
     * the vehicle and the Repartidor availability
     * 
     * @param pedido - The pedido to get a repartidor for
     * @return {@link Repartidor} the Repartidor if one was found, or {@code null}
     *         if none was found capable or available.
     */
    private Repartidor getProperRepartidor(Pedido pedido) {
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getVehiculo().puedeTransportar(pedido) && repartidor.isAvailable()) {
                return repartidor;
            }
        }
        return null;
    }

    /**
     * Returns whether the current lists of pedidos have pedidos to tend to
     * 
     * @return {@code boolean}, the truth/false value if there are pedidor or not.
     */
    public boolean areTherePedidos() {
        return (!pedidosUrgentes.isEmpty() || !pedidosNormales.isEmpty());
    }

    public List<Repartidor> getRepartidoresConPedidos() {
        List<Repartidor> repartidoresPedidos = new ArrayList<>();
        for (Repartidor repartidor : repartidores) {
            if (!repartidor.getPedidosAsignados().isEmpty()) {
                repartidoresPedidos.add(repartidor);
            }
        }
        return repartidoresPedidos;
    }

    public boolean registrarEntrega(String legajo, int pedidoCodigo) {
        Repartidor repa = getRepartidor(legajo);
        if (repa == null) {
            throw new NoSuchElementException("Repartidor no encontrado.");
        }
        for (int i = 0; i < repa.getPedidosAsignados().size(); i++) {
            Pedido pedido = repa.getPedidosAsignados().get(i);
            if (pedido.getCodigo() == pedidoCodigo) {
                if (repa.getPedidosEntregados().add(repa.getPedidosAsignados().remove(i))) {
                    pedido.setEstado(EstadoPedido.ENTREGADO);
                    Operacion operacion = new Operacion(TipoOperacion.CAMBIO_ESTADO, pedido, legajo, EstadoPedido.EN_CAMINO);
                    operaciones.push(operacion);
                    return true;
                }
            }
        }
        return false;
    }

    public Operacion getLastOperacion() {
        return this.operaciones.peek();
    }

    public boolean undoLastOperacion() {
        Operacion operacion = this.operaciones.peek();
        if (operacion == null) {
            throw new IllegalStateException("No hay operaciones que deshacer.");
        }
        switch (operacion.getOperacion()) {
            case ALTA_PEDIDO:
                Pedido pedido = operacion.getPedido();
                boolean undone;
                if (pedido instanceof PedidoUrgente){
                    undone = this.pedidosUrgentes.remove(pedido);
                } else {
                    undone = this.pedidosNormales.remove(pedido);
                }
                if (undone){
                    operaciones.pop();
                }
                return undone;
            case ASIGNACION_PEDIDO:
                Repartidor repartidor = this.getRepartidor(operacion.getLegajoRepartidor());
                for (int i = 0; i < repartidor.getPedidosAsignados().size(); i++) {
                    Pedido pedidoAsig = repartidor.getPedidosAsignados().get(i);
                    if (pedidoAsig == operacion.getPedido()){
                        pedidoAsig.setEstado(EstadoPedido.PENDIENTE);
                        repartidor.getPedidosAsignados().remove(pedidoAsig);
                        if (pedidoAsig instanceof PedidoUrgente){
                            shoveInFront(pedidosUrgentes, pedidoAsig);
                        } else {
                            shoveInFront(pedidosNormales, pedidoAsig);
                        }
                        operaciones.pop();
                        return true;
                    }
                }
                return false;
            case CAMBIO_ESTADO:
                Repartidor repa = getRepartidor(operacion.getLegajoRepartidor());
                Pedido ped = operacion.getPedido();
                if (repa.getPedidosEntregados().contains(ped)){
                    ped.setEstado(operacion.getEstadoAnterior());
                    repa.getPedidosEntregados().remove(ped);
                    repa.getPedidosAsignados().add(ped);
                    operaciones.pop();
                    return true;
                } else {
                    return false;
                }
            default:
                System.out.println("Error");
                break;
        }
        return false;
    }

    private void shoveInFront(Queue<Pedido> queue, Pedido element){
        Queue<Pedido> temporaryQueue = new ArrayDeque<>();
        while (!queue.isEmpty()){
            temporaryQueue.add(queue.poll());
        }
        queue.add(element);
        while (!temporaryQueue.isEmpty()) {
            queue.add(temporaryQueue.poll());
        }
    }

    //returns a list with all the pedidos together: entregados, assigned, urgents, normal, etc.
    public List<Pedido> getPedidosList(){
        List<Pedido> listaPedidos = new ArrayList<>();
        for (Repartidor repart : repartidores) {
            if (!repart.getPedidosAsignados().isEmpty()){
                listaPedidos.addAll(repart.getPedidosAsignados());
            }
            if (!repart.getPedidosEntregados().isEmpty()){
                listaPedidos.addAll(repart.getPedidosEntregados());
            }
        }
        listaPedidos.addAll(this.pedidosNormales);
        listaPedidos.addAll(this.pedidosUrgentes);
        return listaPedidos;
    }

    //O(n^(2)) complejidad, O(1) memoria extra, sorting estable
    public void insertionSort(List<Pedido> pedidos, Comparator<Pedido> comparador){
        for (int i = 1; i < pedidos.size(); i++) {
            Pedido actual = pedidos.get(i); //gets the "second" element, the first is considered "sorted" in  way
            int j = i - 1;
            while (j >= 0 && comparador.compare(pedidos.get(j), actual) > 0) { //compares it with the previous one
                pedidos.set(j+1, pedidos.get(j)); //if previous is bigger, it inserts it one position to the right
                j--;
            }
            pedidos.set(j+1, actual); //inserts the actual where that previous one was, or leaves it where it was(j) if it was bigger than the previous one.
        }
    }

    //O(n log n) complejidad, O(n) memoria extra, sorting estable
    public void mergeSort(List<Pedido> pedidos, int inicio, int fin){
        if (inicio < fin){
            int medio = (inicio + fin) /2;
            mergeSort(pedidos, inicio, medio);
            mergeSort(pedidos, medio+1, fin);
            merge(pedidos, inicio, medio, fin);
        }
    }

    private void merge(List<Pedido> pedidos, int inicio, int medio, int fin){
        List<Pedido> izquierda = new ArrayList<>();
        List<Pedido> derecha = new ArrayList<>();
        for (int i = inicio; i <= medio; i++) {
            izquierda.add(pedidos.get(i));
        }
        for (int i = medio + 1; i <= fin; i++) {
            derecha.add(pedidos.get(i));
        }
        int i = 0;
        int j = 0;
        int k = inicio;
        while (i < izquierda.size() && j < derecha.size()) {
            if (izquierda.get(i).getCodigo() <= derecha.get(j).getCodigo()){
                pedidos.set(k, izquierda.get(i));
                i++;
            } else {
                pedidos.set(k, derecha.get(j));
                j++;
            }
            k++;
        }
        while (i < izquierda.size()) {
            pedidos.set(k, izquierda.get(i));
            i++;
            k++;
        }
        while(j < derecha.size()){
            pedidos.set(k, derecha.get(j));
            j++;
            k++;
        }
    }

    public List<Pedido> intercalar(List<Pedido> lista1, List<Pedido> lista2){
        List<Pedido> result = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i < lista1.size() && j < lista2.size()){
            if (lista1.get(i).getCodigo() <= lista2.get(j).getCodigo()){
                result.add(lista1.get(i));
                i++;
            } else {
                result.add(lista2.get(j));
                j++;
            }
        }
        while(i < lista1.size()){
            result.add(lista1.get(i));
            i++;
        }
        while (j < lista2.size()) {
            result.add(lista2.get(j));
            j++;
        }
        return result;
    }

    public Pedido getPedidoBinaria(int code){
        List<Pedido> pedidos = getPedidosList();
        this.mergeSort(pedidos, 0, pedidos.size()-1);
        int inicio = 0;
        int fin = pedidos.size()-1;
        while (inicio <= fin) {
            int medio = (inicio+fin)/2;
            int codigoMedio = pedidos.get(medio).getCodigo();
            if (codigoMedio == code){
                return pedidos.get(medio);
            }
            if (code < codigoMedio){
                fin = medio -1;
            } else {
                inicio = medio +1;
            }
        }
        return null;
    }

    public Queue<Pedido> getPedidosNormales() {
        return pedidosNormales;
    }

    public void setPedidosNormales(Queue<Pedido> pedidosNormales) {
        this.pedidosNormales = pedidosNormales;
    }

    public Queue<Pedido> getPedidosUrgentes() {
        return pedidosUrgentes;
    }

    public void setPedidosUrgentes(Queue<Pedido> pedidosUrgentes) {
        this.pedidosUrgentes = pedidosUrgentes;
    }
}
