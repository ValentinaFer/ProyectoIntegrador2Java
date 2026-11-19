package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Entregas {
    private int codigo;
    private Pedido pedido;
    private LocalDateTime fechaHoraEntrega;
    private BigDecimal costoFinal;
    private Double tiempoEstimado;
    
    public Entregas(int codigo, Pedido pedido, LocalDateTime fechaHoraEntrega, BigDecimal costoFinal,
            Double tiempoEstimado) {
        this.codigo = codigo;
        this.pedido = pedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.costoFinal = costoFinal;
        this.tiempoEstimado = tiempoEstimado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0){
            throw new IllegalArgumentException("El codigo no puede ser negativo o cero.");
        }
        this.codigo = codigo;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public BigDecimal getCostoFinal() {
        return costoFinal;
    }

    public void setCostoFinal(BigDecimal costoFinal) {
        this.costoFinal = costoFinal;
    }

    public Double getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Double tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    
    
}
