package models;

public enum TipoPedido{
        PEDIDO_FRAGIL,
        PEDIDO_NORMAL,
        PEDIDO_URGENTE;

        @Override
        public String toString() {
            switch (this) {
                case PEDIDO_FRAGIL:
                    return "Pedido Fragil";
                case PEDIDO_NORMAL:
                    return "Pedido Normal";
                case PEDIDO_URGENTE:
                    return "Pedido Urgente";
                default:
                    return null;
            }
        }
    }