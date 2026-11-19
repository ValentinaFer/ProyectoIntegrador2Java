package models;

public class Operacion {
    private TipoOperacion operacion;
    private Pedido pedido;
    private String legajoRepartidor;
    private EstadoPedido estadoAnterior;
    
    @Override
    public String toString() {
        return "Operacion [operacion=" + operacion + ", pedido=" + (pedido!=null?pedido.getCodigo(): "Sin pedido") + ", legajoRepartidor=" + (legajoRepartidor != null ? legajoRepartidor:"Sin legajo")
                + ", estadoAnterior=" + (estadoAnterior != null? estadoAnterior: "Sin estado anterior") + "]";
    }
    public Operacion(TipoOperacion operacion, Pedido pedido, String legajoRepartidor, EstadoPedido estadoAnterior) {
        this.operacion = operacion;
        this.pedido = pedido;
        this.legajoRepartidor = legajoRepartidor;
        this.estadoAnterior = estadoAnterior;
    }
    public TipoOperacion getOperacion() {
        return operacion;
    }
    public void setOperacion(TipoOperacion operacion) {
        this.operacion = operacion;
    }
    public Pedido getPedido() {
        return pedido;
    }
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    public String getLegajoRepartidor() {
        return legajoRepartidor;
    }
    public void setLegajoRepartidor(String legajoRepartidor) {
        this.legajoRepartidor = legajoRepartidor;
    }
    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }
    public void setEstadoAnterior(EstadoPedido estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }


    
}
