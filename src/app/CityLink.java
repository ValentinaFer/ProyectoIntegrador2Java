package app;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeMap;

import exceptions.AlmacenamientoLlenoException;
import exceptions.CapacidadExcedidaException;
import exceptions.EstadoInvalidoException;
import models.Cliente;
import models.EstadoPedido;
import models.Pedido;
import models.Repartidor;
import models.Validation;

//"Contiene los arreglos y las operaciones del sistema"
public class CityLink {
    private TreeMap<Integer,Cliente> clientes = new TreeMap<>();
    private Pedido[] pedidos = new Pedido[100];
    private Repartidor[] repartidores = new Repartidor[10];
    private int pedidoLength = 0;
    private int repartidorLength = 0;
    private int nextClienteId = 1;
    private int nextPedidoId = 1;

    public Pedido[] getPedidos() {
        Pedido[] pedidosCpy = new Pedido[pedidoLength];
        for (int i = 0; i < pedidoLength; i++) {
            pedidosCpy[i] = pedidos[i];
        }
        return pedidosCpy;
    }

    public Repartidor[] getRepartidores() {
        Repartidor[] repartidoresCpy = new Repartidor[repartidorLength];
        for (int i = 0; i < repartidorLength; i++) {
            repartidoresCpy[i] = repartidores[i];
        }
        return repartidoresCpy;
    }

    public TreeMap<Integer,Cliente> getClientes(){
        return this.clientes;
    }

    public void modifyClient(Cliente cliente){
        if (!clientes.containsKey(cliente.getCodigo())){
            throw new IllegalArgumentException("Cliente no encontrado!");
        }
        Cliente c = clientes.get(cliente.getCodigo());
        c.setNombre(cliente.getNombre());
        c.setTelefono(cliente.getTelefono());
        c.setZonaCiudad(cliente.getZonaCiudad());
    }

    public void addNewClient(Cliente cliente){
        if (cliente == null){
            throw new IllegalArgumentException("Debe ingresar un Cliente valido");
        }
        cliente.setCodigo(nextClienteId++);
        clientes.put(cliente.getCodigo(), cliente);
    }

    /**
     * Returns the Client deleted from the list or null if nothing was deleted.
     * @param code - the code of the client to remove.
     * @return the {@link Cliente} removed or {@code null} if nothing was found to be removed
     */
    public Cliente deleteClient(int code){
        return clientes.remove(code); //can also return null if the previous object mapping to that key was null, but should never happen because I only allow Clients that are not null to be added into the map.
    }

    /**
     * returns a shallow copy of the client with the code received by parameter
     * @param code - the code of the client to look for
     * @return the {@link Client} found by the code or {@code null} if not found
     */
    public Cliente getCliente(int code){
        Cliente c = clientes.get(code);
        if (c == null) return null;
        return new Cliente(c.getCodigo(), c.getNombre(), c.getTelefono(), c.getZonaCiudad());
    }

    //since each Repartidor is identified by their Legajo, I should probably also add a method to make sure I don't add a repartidor with a repeated Legajo into the array.
    public void addNewRepartidor(Repartidor repartidor) throws AlmacenamientoLlenoException{
        if (repartidorLength >= repartidores.length){
            throw new AlmacenamientoLlenoException("Se ha llegado al limite de almacenamiento para nuevos Repartidores.");
        }
        repartidores[repartidorLength++] = repartidor;
    }

    public void addNewPedido(Pedido pedido) throws AlmacenamientoLlenoException{
        if (pedidoLength >= pedidos.length){
            throw new AlmacenamientoLlenoException("Se ha llegado al limite de almacenamiento para nuevos Pedidos.");
        }
        pedido.setCodigo(nextPedidoId++);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setfechaHoraIngreso(LocalDateTime.now());
        pedidos[pedidoLength++] = pedido;
    }

    public Cliente buscarCliente(int codigo){
        for (int i = 0; i < clienteLength; i++) {
            if (clientes[i].getCodigo() == codigo){
                Cliente cliente = clientes[i];
                return cliente;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String nombre){
        nombre = nombre.toUpperCase();
        for (int i = 0; i < clienteLength; i++) {
            if (clientes[i].getNombre().toUpperCase().contains(nombre)){
                Cliente cliente = clientes[i];
                return cliente;
            }
        }
        return null;
    }

    public Repartidor getRepartidor(int codPedido) {
        for (int i = 0; i < repartidorLength; i++) {
            if (repartidores[i].getPedidosAsigLength() > 0){
                Pedido[] pedidos = repartidores[i].getPedidosAsignados();
                for (int j = 0; j < repartidores[i].getPedidosAsigLength(); j++) {
                    if (pedidos[j].getCodigo() == codPedido){
                        Repartidor repartidor = repartidores[i];
                        return new Repartidor(repartidor.getLegajo(), repartidor.getNombre(), repartidor.getVehiculo(), repartidor.getPedidosAsignados(), repartidor.getPedidosAsigLength());
                    }
                }
            }
        }
        return null;
    }

    public Repartidor[] getRepartidorAvailable(){
        Repartidor[] repartidoresAva = new Repartidor[repartidorLength];
        int cont = 0;
        for (int i = 0; i < repartidorLength; i++) {
            if (repartidores[i].isAvailable()){
                repartidoresAva[cont++] = repartidores[i];
            }
        }
        return Arrays.copyOf(repartidoresAva, cont);
    }
    
    //Asumiendo que no tendria sentido asignar Pedidos que ya han sido ENTREGADAS o han sido CANCELADAS, o si estan ya EN_CAMINO.
    //Es decir, los Pedidos sin asignar serian los que se encuentran PENDIENTE
    public Pedido[] getPedidosSinAsignar(){
        Pedido[] pedidosSinA = new Pedido[pedidoLength];
        int cont = 0;
        for (int i = 0; i < pedidoLength; i++) {
            if (pedidos[i].getEstado() == EstadoPedido.PENDIENTE && getRepartidor(pedidos[i].getCodigo()) == null){
                pedidosSinA[cont++] = pedidos[i];
            }
        }
        return Arrays.copyOf(pedidosSinA, cont);
    }

    public Pedido getPedido(int cod){
        for (int i = 0; i < pedidoLength; i++) {
            if (pedidos[i].getCodigo() == cod){
                return pedidos[i];
            }
        }
        return null;
    }

    public Repartidor getRepartidor(String legajo){
        for (int i = 0; i < repartidorLength; i++) {
            if (repartidores[i].getLegajo().equalsIgnoreCase(legajo)){
                return repartidores[i];
            }
        }
        return null;
    }

    public boolean asignarPedido(int codPedido, String legajoRepartidor) throws CapacidadExcedidaException{
        Pedido pedido = getPedido(codPedido);
        Repartidor repartidor = getRepartidor(legajoRepartidor);
        if (!repartidor.isAvailable()){
            return false;
        }
        if (!repartidor.getVehiculo().puedeTransportar(pedido)){
            throw new CapacidadExcedidaException("El Repartidor elegido no puede transportar el Pedido seleccionado.");
        } else {
            repartidor.addPedido(pedido);
            return true;
        }
    }

    public Pedido[] getPedidosCambiables(){
        Pedido[] pedidosCam = new Pedido[pedidoLength];
        int cont = 0;
        for (int i = 0; i < pedidoLength; i++) {
            Pedido pedido = pedidos[i];
            if (pedido.getEstado() == EstadoPedido.PENDIENTE || (pedido.getEstado() == EstadoPedido.EN_CAMINO && getRepartidor(pedido.getCodigo()) != null)){
                pedidosCam[cont++] = pedidos[i];
            }
        }
        return Arrays.copyOf(pedidosCam, cont);
    } 

    public boolean cancelarPedido(int codPedido){
        Pedido pedido = getPedido(codPedido);
        if (pedido == null){
            return false;
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE){
            throw new EstadoInvalidoException("No puede cancelar un pedido que no este PENDIENTE.");
        } else {
            pedido.setEstado(EstadoPedido.CANCELADO);
        }
        return true;
    }

    public boolean cambiarEstadoPedido(int codPedido){
        Pedido pedido = getPedido(codPedido);
        if (pedido == null){
            return false; 
        }
        if (getRepartidor(codPedido) == null){
            throw new EstadoInvalidoException("No puede cambiar el estado del pedido porque no tiene un Repartidor asignado.");
        }
        EstadoPedido estadoCambiar;
        switch (pedido.getEstado()) {
            case PENDIENTE:
                estadoCambiar = EstadoPedido.EN_CAMINO;
                break;
            case EN_CAMINO:
                estadoCambiar = EstadoPedido.ENTREGADO;
            default:
                throw new EstadoInvalidoException("No puede cambiar el estado de los pedidos que ya estan cancelados o entregados");
        }
        pedido.setEstado(estadoCambiar);
        return true;
    }

}
