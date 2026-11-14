package models;
import java.math.BigDecimal;

public abstract class Vehiculo {
    private String patente;
    private double capacidadKg;
    private double velocidadKmH;
    private BigDecimal costoPorKm;

    protected Vehiculo(String patente, double capacidadKg, double velocidadKmH, BigDecimal costoPorKm){
        setPatente(patente);
        setCapacidadKg(capacidadKg);
        setVelocidadKmH(velocidadKmH);
        setCostoPorKm(costoPorKm);
    }

    public void setPatente(String patente){
        ResultadoValidation status = Validation.getPatenteStatus(patente);
        if (status == ResultadoValidation.EMPTY){
            throw new IllegalArgumentException("La patente del vehiculo no puede estar vacia.");
        } else if (status == ResultadoValidation.TOO_LONG){
            throw new IllegalArgumentException("La patente del vehiculo no puede contener mas de 10 caracteres.");
        }
        this.patente = patente;
    }

    public void setCapacidadKg(double capacidadKg){
        if (!Double.isFinite(capacidadKg) || capacidadKg <= 0){
            throw new IllegalArgumentException("La capacidad del vehiculo no puede ser negativa ni cero.");
        }
        this.capacidadKg = capacidadKg;
    }

    public void setVelocidadKmH(double velocidadKmH){
        if (!Double.isFinite(velocidadKmH) || velocidadKmH <= 0){
            throw new IllegalArgumentException("La velocidad media del vehiculo no puede ser negativa ni cero.");
        }
        this.velocidadKmH = velocidadKmH;
    }

    public void setCostoPorKm(BigDecimal costoPorKm){
        if (costoPorKm == null || costoPorKm.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("La velocidad media del vehiculo no puede ser negativa ni cero.");
        }
        this.costoPorKm = costoPorKm;
    }

    public String getPatente(){
        return this.patente;
    }

    public double getCapacidadKg(){
        return this.capacidadKg;
    }

    public double getVelocidadKmH(){
        return this.velocidadKmH;
    }

    public BigDecimal getCostoPorKm(){
        return this.costoPorKm;
    }

    public abstract double calcularTiempoEntrega(double km);

    /**
     * Retorna si el vehiculo puede transportar un pedido
     * 
     * Un vehiculo no puede transportar un pedido si el peso del mismo supera la capacidad del vehiculo.
     * 
     * @param pedido sobre el que se evaluará si el vehiculo puede transportarlo.
     * @throws IllegalStateException si el pedido o el vehiculo no tienen valores validos sobre los que evaluar la condición. 
     */
    public boolean puedeTransportar(Pedido pedido){
        if (pedido.getPesoKg() <= 0){
            throw new IllegalStateException("El pedido no tiene un peso valido");
        } else if (this.getCapacidadKg() <= 0){
            throw new IllegalStateException("El vehiculo no tiene una capacidad de carga valida.");
        }
        return pedido.getPesoKg() <= this.getCapacidadKg();
    }

    public String toStringSimple(){
        return "Patente: "+ patente + ", Capacidad(KM): " + capacidadKg;
    }

    @Override
    public String toString() {
        return "[Vehiculo patente " + patente + "]\nCapacidad(Kg): " + capacidadKg + "\nVelocidad(Km/h):" + velocidadKmH
                + "\nCosto por Km: " + costoPorKm;
    };
    
}
