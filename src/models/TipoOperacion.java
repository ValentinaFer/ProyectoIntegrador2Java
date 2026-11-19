package models;

public enum TipoOperacion {
    ALTA_PEDIDO,
    ASIGNACION_PEDIDO,
    CAMBIO_ESTADO;

    @Override
    public String toString() {
        switch (this) {
            case ALTA_PEDIDO:
                return "Alta de Pedido";
            case ASIGNACION_PEDIDO:
                return "Asignacion de Pedido";
            case CAMBIO_ESTADO:
                return "Cambio de estado";
            default:
                return null;
        }
    }
}
