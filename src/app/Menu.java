package app;

import java.math.BigDecimal;
import java.util.Scanner;

import models.ResultadoValidation;
import models.TipoPedido;
import models.TipoVehiculo;
import models.Validation;

public final class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public Menu() {

    }

    /**
     * Shows main menu options and returns the selection of the user;
     * 
     * @return {@link String} of selection of the user.
     *         <ol>
     *         <li>Gestionar Clientes</li>
     *         <li>Gestionar Repartidores</li>
     *         <li>Registrar Pedido</li>
     *         <li>Atender siguiente Pedido</li>
     *         <li>Registrar una entrega</li>
     *         <li>Deshacer la ultima operacion</li>
     *         <li>Ordenar y Listar Pedidos</li>
     *         <li>Buscar un Pedido por Codigo</li>
     *         <li>Intercalar los pedidos de dos turnos</li>
     *         <li>SALIR</li>
     *         </ol>
     */
    public String getMainMenuOptions() {
        System.out.println("===[MENU CITYLINK]===");
        System.out.println("[1]. Gestionar Clientes;");
        System.out.println("[2]. Gestionar Repartidores;");
        System.out.println("[3]. Registrar Pedido;"); // encola y registra operacion en pila
        System.out.println("[4]. Atender siguiente Pedido;"); // en base a cola(urgentes primero, luego normales)
        System.out.println("[5]. Registrar una entrega;"); // se guarda en historial de Repartidor
        System.out.println("[6]. Deshacer la ultima operacion;"); // pila
        System.out.println("[7]. Ordenar y Listar Pedidos;"); // Eleccion de criterio(costo || distancia || fecha)
        System.out.println("[8]. Buscar un Pedido por Codigo;"); // lineal o binary
        System.out.println("[9]. Intercalar los pedidos de dos turnos;"); // facturable
        System.out.println("[10]. SALIR.");
        return scanner.nextLine();
    }

    /**
     * Shows Client management menu and returns selected option.
     * 
     * @return {@link String} of selection of the user.
     *         <ol>
     *         <li>Ingresar nuevo cliente</li>
     *         <li>Modificar un Cliente</li>
     *         <li>Eliminar un cliente</li>
     *         <li>Ver todos los clientes</li>
     *         <li>Volver al Menu principal.</li>
     *         </ol>
     */
    public String getClientMenuOption() {
        System.out.println("===[GESTION CLIENTE]===");
        System.out.println("[1]. Ingresar nuevo cliente;");
        System.out.println("[2]. Modificar un cliente;");
        System.out.println("[3]. Eliminar un cliente;");
        System.out.println("[4]. Ver todos los clientes;");
        System.out.println("[5]. Volver al Menu principal.");
        return scanner.nextLine();
    }

    /**
     * Shows Repartidores management menu and returns selected option
     * @return {@link String} of selection of the user.
     *         <ol>
     *         <li>Ingresar nuevo Repartidor</li>
     *         <li>Eliminar un repartidor</li>
     *         <li>Buscar Repartidor por Legajo</li>
     *         <li>Ver todo los Repartidores</li>
     *         <li>Volver al Menu principal.</li>
     *         </ol>
     */
    public String getDeliveryPersMenuOption(){
        System.out.println("===[GESTION REPARTIDOR]===");
        System.out.println("[1]. Ingresar nuevo Repartidor;");
        System.out.println("[2]. Eliminar un repartidor;");
        System.out.println("[3]. Buscar Repartidor por Legajo;");
        System.out.println("[4]. Ver todo los Repartidores;");
        System.out.println("[5]. Volver al Menu principal.");
        return scanner.nextLine();
    }

    /**
     * Shows Repartidor editable fields menu and returns
     * selected option.
     * 
     * @return {@link String} of selection of the user.
     *         <ol>
     *         <li>Legajo</li>
     *         <li>Nombre</li>
     *         <li>Vehiculo</li>
     *         <li>CANCELAR</li>
     *         <li>GUARDAR</li>
     *         </ol>
     */
    public String getDeliveryPersFields(){
        System.out.println("==[REPARTIDOR]==");
        System.out.println("[1]. Legajo;");
        System.out.println("[2]. Nombre;");
        System.out.println("[3]. Vehiculo;");
        System.out.println("[4]. CANCELAR;");
        System.out.println("[5]. GUARDAR.");
        System.out.println("Ingrese el campo a modificar: ");
        return scanner.nextLine();
    }

    /**
     * Shows Client editable fields menu and returns
     * selected option.
     * 
     * @return {@link String} of selection of the user.
     *         <ol>
     *         <li>Nombre</li>
     *         <li>Telefono</li>
     *         <li>Eliminar un cliente</li>
     *         <li>CANCELAR</li>
     *         <li>GUARDAR</li>
     *         </ol>
     */
    public String getClientFieldsOption() {
        System.out.println("===[CLIENTE]===");
        System.out.println("[1]. Nombre;");
        System.out.println("[2]. Telefono;");
        System.out.println("[3]. Zona Ciudad;");
        System.out.println("[4]. CANCELAR;");
        System.out.println("[5]. GUARDAR.");
        System.out.println("Ingrese el campo a modificar:");
        return scanner.nextLine();
    }

    /**
     * Asks the user for a valid name, returns the validated string based from corresponding {@link Validation} method.
     * @return String non-empty and under 100 characters.
     */
    public String askValidNombre() {
        String option;
        ResultadoValidation status;
        while (true) {
            System.out.println("Ingrese el nombre:");
            option = scanner.nextLine();
            status = Validation.getNombreStatus(option);
            if (status == ResultadoValidation.EMPTY) {
                System.out.println("[UY!] El nombre no puede estar vacio.");
            } else if (status == ResultadoValidation.TOO_LONG) {
                System.out.println("[UY!] El nombre no puede superar los 100 caracteres.");
            } else {
                return option;
            }
        }
    }

    /**
     * Asks the user for a valid Legajo, returns the validated string based from corresponding {@link Validation} method.
     * @return String non-empty and under 100 characters.
     */
    public String askLegajo() {
        String option;
        ResultadoValidation status;
        while (true) {
            System.out.println("Ingrese el legajo del repartidor: ");
            option = scanner.nextLine();
            status = Validation.getLegajoStatus(option);
            if (status == ResultadoValidation.EMPTY) {
                System.out.println("[UY!] El legajo no puede estar vacio.");
            } else if (status == ResultadoValidation.TOO_LONG) {
                System.out.println("[UY!] El legajo no puede superar los 100 caracteres.");
            } else {
                return option;
            }
        }
    }

    public TipoVehiculo askTipoVehiculo() {
        String option;
        while (true) {
            System.out.println("[1]. Camioneta;");
            System.out.println("[2]. Moto;");
            System.out.println("[3]. Bicicleta;");
            System.out.println("Ingrese el tipo de vehiculo: ");
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    return TipoVehiculo.CAMIONETA;
                case "2":
                    return TipoVehiculo.MOTO;
                case "3":
                    return TipoVehiculo.BICICLETA;
                default:
                    System.out.println("[UY!]. Debe ingresar un tipo de vehiculo valido.");
                    break;
            }
        }
    }

    public String askPatente() {
        String patente;
        ResultadoValidation status;
        while (true) {
            System.out.println("Ingrese la patente del vehiculo:");
            patente = scanner.nextLine();
            status = Validation.getPatenteStatus(patente);
            if (status == ResultadoValidation.EMPTY) {
                System.out.println("[UY!]. La patente del vehiculo no puede estar vacia.");
            } else if (status == ResultadoValidation.TOO_LONG) {
                System.out.println("[UY!]. La patente del vehiculo no puede superar los 10 caracteres de largo.");
            } else {
                return patente;
            }
        }
    }

    public double askCapacidad() {
        String option;
        while (true) {
            System.out.println("Ingrese la capacidad de carga del vehiculo(en Kg):");
            option = scanner.nextLine();
            try {
                Double capacidad = Double.parseDouble(option);
                if (!Double.isFinite(capacidad) || capacidad <= 0) {
                    System.out.println("[UY!]. Debe ingresar un valor numerico positivo y valido.");
                } else {
                    return capacidad;
                }
            } catch (NumberFormatException e) {
                System.out.println("[UY!]. Debe ingresar un valor numerico valido.");
            }
        }
    }

    public double askVelocidad() {
        String option;
        while (true) {
            System.out.println("Ingrese la velocidad promedio del vehiculo(Km/h): ");
            option = scanner.nextLine();
            try {
                Double velocidad = Double.parseDouble(option);
                if (!Double.isFinite(velocidad) || velocidad <= 0) {
                    System.out.println("[UY!]. Debe ingresar un valor numerico positivo y valido.");
                } else {
                    return velocidad;
                }
            } catch (NumberFormatException e) {
                System.out.println("[UY!]. Debe ingresar un valor numerico valido.");
            }
        }
    }

    public BigDecimal askCostoPorKm() {
        String option;
        while (true) {
            System.out.println("Ingrese el costo por kilometro del vehiculo:");
            option = scanner.nextLine();
            try {
                BigDecimal costo = new BigDecimal(option);
                if (costo.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("[UY!]. Debe ingresar un valor numerico positivo y valido");
                } else {
                    return costo;
                }
            } catch (NumberFormatException e) {
                System.out.println("[UY!]. Debe ingresar un valor valido.");
            }
        }
    }

    public String getVehicleFieldOption(){
        System.out.println("---[MENU VEHICULO DE REPARTIDOR]---");
            System.out.println("[1]. Tipo de vehiculo;");
            System.out.println("[2]. Patente;");
            System.out.println("[3]. Capacidad(kg);");
            System.out.println("[4]. Velocidad(km/h);");
            System.out.println("[5]. Costo por Kilometro;");
            System.out.println("[6]. CANCELAR;");
            System.out.println("[7]. GUARDAR;");
            System.out.println("Ingrese el campo a modificar:");
            return scanner.nextLine();
    }

    /**
     * Asks the user for a valid Telefono, returns the validated string based from corresponding {@link Validation} method.
     * @return String non-empty, under 15 characters and with numeric values only.
     */
    public String askTelefono() {
        String option;
        ResultadoValidation status;
        while (true) {
            System.out.println("Ingrese el numero de telefono del cliente:");
            option = scanner.nextLine();
            status = Validation.getTelefonoStatus(option);
            if (status == ResultadoValidation.EMPTY) {
                System.out.println("[UY!] El numero de telefono del cliente no puede estar vacio.");
            } else if (status == ResultadoValidation.TOO_LONG) {
                System.out
                        .println("[UY!] El numero de telefono del cliente no debe superar los 15 caracteres de largo.");
            } else if (status == ResultadoValidation.NAN) {
                System.out.println(
                        "[UY!] El numero de telefono del cliente debe contener caracteres numericos unicamente.");
            } else {
                return option;
            }
        }
    }

    /**
     * Asks the user for a valid ZonaCiudad, returns the validated string based from corresponding {@link Validation} method.
     * @return String non-empty, under 300 characters.
     */
    public String askZonaCiudad() {
        String option;
        ResultadoValidation status;
        while (true) {
            System.out.println("Ingrese la Zona de Ciudad del cliente:");
            option = scanner.nextLine();
            status = Validation.getZonaCiudadStatus(option);
            if (status == ResultadoValidation.EMPTY) {
                System.out.println("[UY!] La zona de ciudad del cliente no puede estar vacia.");
            } else if (status == ResultadoValidation.TOO_LONG) {
                System.out.println("[UY!] La zona de ciudad del cliente no puede superar los 300 caracteres.");
            } else {
                return option;
            }
        }
    }

    /**
     * Asks for code of client, returns it in String
     * @return the {@link String} for the code introduced, or empty String if choosing to go back 
     */
    public String askCode(){
        System.out.println("Ingrese un codigo o deje vacio para volver al menu: ");
        return scanner.nextLine();
    }

    public String askEstaSeguro(){
        System.out.println("Esta seguro que desea continuar con esta accion?(s/n)");
        return scanner.nextLine();
    }

    public String waitEnter(){
        return scanner.nextLine();
    }

    public String askCriteria(){
        System.out.println("[Criterios de ordenamiento]");
        System.out.println("[1]. Por codigo;");
        System.out.println("[2]. Por Costo;");
        System.out.println("[3]. Por Distancia;");
        System.out.println("[4]. Por Fecha;");
        System.out.println("[5]. Volver al menu;");
        System.out.println("Ingrese el criterio de ordenamiento: ");
        return scanner.nextLine();
    }

    public String askLegajoRepartidor() {
        System.out.println("Ingrese el legajo del repartidor o deje vacio para volver: ");
        return scanner.nextLine();
    }

    public double askPeso() {
        String option;
        while (true) {
            System.out.println("Ingrese el peso del Pedido(KG): ");
            option = scanner.nextLine();
            try {
                double peso = Double.parseDouble(option);
                if (!Double.isFinite(peso) || peso <= 0) {
                    System.out.println("[UY!]. Debe ingresar un valor numerico positivo y valido.");
                } else {
                    return peso;
                }
            } catch (NumberFormatException e) {
                System.out.println("[UY!]. Debe ingresar un valor valido.");
            }
        }
    }

    public double askDistancia() {
        String option;
        while (true) {
            System.out.println("Ingrese la distancia del Pedido(KM): ");
            option = scanner.nextLine();
            try {
                double distancia = Double.parseDouble(option);
                if (!Double.isFinite(distancia) || distancia <= 0) {
                    System.out.println("[UY!]. Debe ingresar un valor numerico positivo y valido");
                } else {
                    return distancia;
                }
            } catch (NumberFormatException e) {
                System.out.println("[UY!]. Debe ingresar un valor valido.");
            }
        }
    }

    public String askDestino() {
        String option;
        ResultadoValidation status;
        while (true) {
            System.out.println("Ingrese el Destino del pedido:");
            option = scanner.nextLine();
            status = Validation.getDestinoStatus(option);
            if (status == ResultadoValidation.EMPTY) {
                System.out.println("[UY!]. El destino del pedido no puede estar vacio.");
            } else if (status == ResultadoValidation.TOO_LONG) {
                System.out.println("[UY!]. El destino no puede superar los 100 caracteres de largo.");
            } else {
                return option;
            }
        }
    }

    public TipoPedido askTipoPedido() {
        String option;
        while (true) {
            System.out.println("[1]. Pedido Norma;");
            System.out.println("[2]. Pedido Fragil;");
            System.out.println("[3]. Pedido Urgente;");
            System.out.println("Ingrese el tipo de pedido: ");
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    return TipoPedido.PEDIDO_NORMAL;
                case "2":
                    return TipoPedido.PEDIDO_FRAGIL;
                case "3":
                    return TipoPedido.PEDIDO_URGENTE;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

}

