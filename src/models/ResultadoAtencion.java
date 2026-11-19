package models;

public class ResultadoAtencion {
    private EstadoAtencion estado;
    private Pedido pedido;
    private Repartidor repartidor;
    
    public ResultadoAtencion(EstadoAtencion estado, Pedido pedido, Repartidor repartidor) {
        this.estado = estado;
        this.pedido = pedido;
        this.repartidor = repartidor;
    }

    public EstadoAtencion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAtencion estado) {
        this.estado = estado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

}
