package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import exceptions.SinPedidosException;

public class Repartidor implements Facturable, Rastreable {

    private String legajo;
    private String nombre;
    private Vehiculo vehiculo;
    private List<Pedido> pedidosAsignados = new ArrayList<>();

    public Repartidor(String legajo, String nombre, Vehiculo vehiculo) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.vehiculo = vehiculo;
    }

    public Repartidor() {
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        ResultadoValidation status = Validation.getLegajoStatus(legajo);
        if (status == ResultadoValidation.EMPTY) {
            throw new IllegalArgumentException("El legajo del repartidor no puede estar vacio.");
        } else if (status == ResultadoValidation.TOO_LONG) {
            throw new IllegalArgumentException("El legajo del repartidor no puede superar los 100 caracteres.");
        }
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        ResultadoValidation status = Validation.getNombreStatus(nombre);
        if (status == ResultadoValidation.EMPTY) {
            throw new IllegalArgumentException("El nombre del repartidor no puede estar vacio.");
        } else if (status == ResultadoValidation.TOO_LONG) {
            throw new IllegalArgumentException("El nombre del repartidor no puede exceder los 100 caracteres.");
        }
        this.nombre = nombre;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public List<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public String toStringSimple(){
        return "Legajo: " + (this.getLegajo() != null? this.getLegajo():"Sin legajo") + ", Nombre: " + (this.getNombre() != null ? this.getNombre(): "Sin nombre");
    }

    @Override
    public String toString() { //make prettier(and right) later..
        String string = "";
        string = string + "[Repartidor legajo nro. "+(this.getLegajo()!= null?this.getLegajo():"Sin legajo")+"]\n";
        string = string + "Nombre: "+ (this.getNombre() != null ? this.getNombre(): "Sin nombre")+"\n";
        Vehiculo vehiculo = this.getVehiculo();
        if (vehiculo != null){
            string = string + vehiculo.toStringSimple();
        } else {
            string = string + "Sin vehiculo";
        }
        if (pedidosAsignados.size() > 0){
            string = string + pedidosAsignados;
        }
        return string;
    }

    @Override
    public BigDecimal CalcularImporte() throws SinPedidosException {
        if (pedidosAsignados.isEmpty()){
            throw new SinPedidosException("El repartidor no tiene pedidos");
        }
        BigDecimal totalImporte = BigDecimal.ZERO;
        for (Pedido pedido : pedidosAsignados) {
            if (pedido.getEstado() == EstadoPedido.ENTREGADO){
                totalImporte = totalImporte.add(pedido.calcularCosto(this.getVehiculo()));
            }
        }
        if (totalImporte.compareTo(BigDecimal.ZERO) <= 0){
            throw new SinPedidosException("El repartidor no tiene pedidos Entregados aún.");
        }
        return totalImporte.multiply(new BigDecimal("0.1"));
    }

    /**
     * Returns copy of the pedidos with the estado received in parameters.
     * 
     * @param estado to be filtered by.
     * 
     * @return List of {@link Pedido} filtered by the estado.
     */
    public List<Pedido> getPedidos(EstadoPedido estado){
        List<Pedido> copyPedidos = new ArrayList<>();
        for (Pedido pedido : pedidosAsignados) {
            if (pedido.getEstado() == estado){
                copyPedidos.add(pedido);
            }
        }
        return copyPedidos;
    }

    //Asumiendo que un Repartidor NO esta disponible si tiene alguno de sus pedidos en estado EN_CAMINO.
    public boolean isAvailable() {
        for (Pedido pedido : pedidosAsignados) {
            if (pedido.getEstado() == EstadoPedido.EN_CAMINO) {
                return false;
            }
        }
        return true;
    }

    public boolean addPedido(Pedido pedido) {
        if (pedido == null){ //maybe check more stuff for validation here?
            throw new IllegalArgumentException("Debe ingresar un pedido valido.");
        }
        pedidosAsignados.add(pedido);
        return true;
    }

    @Override
    public String obtenerEstadoActual() {
        return isAvailable() ? "Disponible" : "No Disponible";
    }

}
