package models;

public enum TipoVehiculo {
    BICICLETA,
    MOTO,
    CAMIONETA;

    @Override
    public String toString() {
            switch (this) {
                case BICICLETA:
                    return "Bicicleta";
                case MOTO:
                    return "Moto";
                case CAMIONETA:
                    return "Camioneta";
                default:
                    return null;
            }
        }     
}
