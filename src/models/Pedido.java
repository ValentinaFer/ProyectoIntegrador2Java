package models;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import exceptions.PedidoInvalidoException;

public abstract class Pedido implements Rastreable{
    private int codigo;
    private Cliente cliente;
    private String destino;
    private double pesoKg;
    private double distanciaKm;
    private LocalDateTime fechaHoraIngreso;
    private EstadoPedido estado;

    protected Pedido(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm, LocalDateTime fechaHoraIngreso, EstadoPedido estado) throws PedidoInvalidoException{
        setCodigo(codigo);
        setCliente(cliente);
        setDestino(destino);
        setPesoKg(pesoKg);
        setDistanciaKm(distanciaKm);
        setfechaHoraIngreso(fechaHoraIngreso);
        setEstado(estado);
    }

    @Override
    public String toString() {
        return "[Pedido cod. " + (codigo != 0? codigo: "...") + "\nCliente: " + (cliente != null ? cliente.getNombre(): "Sin Cliente") + "\nDestino: " + (destino != null?destino:"Sin destino.") + "\nPeso: " + pesoKg
                + "KG\nDistancia: " + distanciaKm + "KM\nFechaHoraIngreso:" + (fechaHoraIngreso!=null?fechaHoraIngreso: "Sin fecha/hora") + "\nEstado: " + estado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codigo;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    protected Pedido(Cliente cliente, String destino, double pesoKg, double distanciaKm) throws PedidoInvalidoException{
        setCliente(cliente);
        setDestino(destino);
        setDistanciaKm(distanciaKm);
        setPesoKg(pesoKg);
    }

    protected Pedido(){
        
    }

    public void setCodigo(int codigo){
        if (codigo <= 0){
            throw new IllegalArgumentException("El codigo del pedido no puede ser negativo o cero.");
        }
        this.codigo = codigo;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public void setDestino(String destino) throws PedidoInvalidoException{
        if (destino.isBlank()){
            throw new PedidoInvalidoException("El destino del pedido no puede estar vacio.");
        }
        this.destino = destino;
    }

    public void setPesoKg(double pesoKg) throws PedidoInvalidoException{
        if (pesoKg <= 0){
            throw new PedidoInvalidoException("El peso del pedido no puede ser negativo o cero.");
        }
        this.pesoKg = pesoKg;
    }

    public void setDistanciaKm(double distanciaKm) throws PedidoInvalidoException{
        if (distanciaKm <= 0){
            throw new PedidoInvalidoException("La distancia del pedido no puede ser negativa o cero.");
        }
        this.distanciaKm = distanciaKm;
    }

    public void setfechaHoraIngreso(LocalDateTime fechaHoraIngreso){
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public void setEstado(EstadoPedido estadoPedido){
        this.estado = estadoPedido;
    }

    public int getCodigo(){
        return this.codigo;
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public String getDestino(){
        return this.destino;
    }

    public double getPesoKg(){
        return this.pesoKg;
    }

    public double getDistanciaKm(){
        return this.distanciaKm;
    }

    public LocalDateTime getfechaHoraIngreso(){
        return this.fechaHoraIngreso;
    }

    public EstadoPedido getEstado(){
        return this.estado;
    }
    
    @Override
    public String obtenerEstadoActual() {
        return this.getEstado().toString();
    }

    public abstract BigDecimal calcularCosto(Vehiculo vehiculo);

}


