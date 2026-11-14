package models;

import java.math.BigDecimal;

import exceptions.SinPedidosException;

public interface Facturable {
    BigDecimal CalcularImporte() throws SinPedidosException;  
} 
