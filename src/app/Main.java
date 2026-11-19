package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import exceptions.PedidoInvalidoException;
import models.Bicicleta;
import models.Camioneta;
import models.Cliente;
import models.Moto;
import models.Operacion;
import models.Pedido;
import models.PedidoFragil;
import models.PedidoNormal;
import models.PedidoUrgente;
import models.Repartidor;
import models.ResultadoAtencion;
import models.ResultadoValidation;
import models.Validation;
import models.Vehiculo;
import models.TipoPedido;
import models.TipoVehiculo;

//"Contiene el menu y la lectura por teclado"
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CityLink gestor = new CityLink();
    private static final Menu menu = new Menu();

    public static void main(String[] args) {
        String option;
        Cliente c1 = new Cliente("Valentina", "111222333", "Centro");
        gestor.addNewClient(c1);
        gestor.addNewClient(new Cliente("Miguel", "222333444", "Sur"));
        gestor.addNewRepartidor(new Repartidor("123", "Peter", new Moto("123asd", 50, 50, new BigDecimal("100"))));
        gestor.addNewRepartidor(
                new Repartidor("001", "Peter", new Camioneta("654mnb", 100, 100, new BigDecimal("200"))));
        try {
            gestor.addNewPedido(new PedidoUrgente(c1, "Sur", 100, 100));
            gestor.addNewPedido(new PedidoNormal(c1, "Este", 60, 50));
            gestor.addNewPedido(new PedidoUrgente(c1, "Norte", 100, 100));
            gestor.addNewPedido(new PedidoFragil(c1, "Oeste", 10, 500));
        } catch (PedidoInvalidoException e) {
            e.printStackTrace();
        }
        while (true) {
            option = menu.getMainMenuOptions();
            switch (option) {
                case "1": // gestion cliente
                    clientManagement();
                    break;
                case "2": // gestion Repartidor
                    gestionarRepartidores();
                    break;
                case "3": // registrar Pedido
                    registrarPedido();
                    break;
                case "4": // atender pedido
                    atenderPedido();
                    break;
                case "5": // Registrar Entrega
                    registrarEntrega();
                    break;
                case "6": // deshacer ultima operacion
                    deshacerOperacion();
                    break;
                case "7": // Ordenar y Listar Pedidos (Eleccion de criterio(costo || distancia || fecha))
                    List<Pedido> pedidos = ordenarYListar();
                    for (Pedido pedido : pedidos) {
                        System.out.println(pedido);
                    }
                    break;
                case "8": // buscar pedido por codigo
                    Pedido pedido = buscarPedido();
                    if (pedido != null){
                        System.out.println(pedido);
                    }
                    break;
                case "9": // intercalar
                    intercalateLists();
                    break;
                case "10":
                    System.out.println("BYE-BYE!");
                    return;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    public static void intercalateLists(){ //and show
        List<Pedido> pedidosNormales = new ArrayList<>();
        pedidosNormales.addAll(gestor.getPedidosNormales());
        gestor.mergeSort(pedidosNormales, 0, pedidosNormales.size()-1);
        
        System.out.println("[PEDIDOS Normal/Fragil]");
        for (Pedido pedido : pedidosNormales) {
            System.out.println("[Pedido nro. "+(pedido.getCodigo())+"] pedido Normal/Fragil.");
        }

        List<Pedido> pedidosUrgentes = new ArrayList<>();
        pedidosUrgentes.addAll(gestor.getPedidosUrgentes());
        gestor.mergeSort(pedidosUrgentes, 0, pedidosUrgentes.size()-1);
        
        System.out.println("[PEDIDOS URGENTES]");
        for (Pedido pedido : pedidosUrgentes) {
            System.out.println("[Pedido nro. "+(pedido.getCodigo())+"] pedido urgente.");
        }

        List<Pedido> intercalados = gestor.intercalar(pedidosNormales, pedidosUrgentes);

        System.out.println("[PEDIDOS INTERCALADOS]");
        for (Pedido pedido : intercalados) {
            System.out.println("[Pedido nro. "+(pedido.getCodigo())+"] " + (pedido instanceof PedidoUrgente? "Pedido Urgente": "Pedido Normal/Fragil"));
        }
    }

    public static Pedido buscarPedido() {
        String option;
        while (true) {
            option = menu.askCode();
            if (option.isBlank()) {
                return null;
            }
            try {
                int code = Integer.parseInt(option);
                Pedido pedido = gestor.getPedidoBinaria(code);
                if (pedido == null) {
                    System.out.println("Pedido no encontrado");
                } else {
                    return pedido;
                }

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un valor valido para el codigo.");
            }
        }
    }

    public static List<Pedido> ordenarYListar() {
        String option;
        List<Pedido> pedidos = gestor.getPedidosList();
        while (true) {
            option = menu.askCriteria();
            switch (option) {
                case "1": // codigo
                    gestor.insertionSort(pedidos, Comparator.comparing(Pedido::getCodigo));
                    return pedidos;
                case "2": // costo
                    System.out.println("[En mantenimiento!]");
                    // me olvide que necesitaba el repartidor para calcular el costo :')
                    break;
                case "3": // distancia
                    gestor.insertionSort(pedidos, Comparator.comparing(Pedido::getDistanciaKm));
                    return pedidos;
                case "4": // fecha
                    gestor.insertionSort(pedidos, Comparator.comparing(Pedido::getfechaHoraIngreso));
                    return pedidos;
                case "5":
                    return null;
                default:
                    break;
            }
        }
    }

    public static void deshacerOperacion() {
        String option;
        while (true) {
            System.out.println("===[Ultima operacion]===");
            Operacion operacion = gestor.getLastOperacion();
            if (operacion == null) {
                System.out.println("No hay operacion que deshacer..");
                return;
            }
            System.out.println(operacion);
            System.out.println("Deshacer operacion:");
            option = menu.askEstaSeguro().toLowerCase();
            switch (option) {
                case "s":
                    if (gestor.undoLastOperacion()) {
                        System.out.println("Ultima operacion deshecha.");
                    } else {
                        System.out.println("[Error]. Sucedio un error inesperado.");
                    }
                    return;
                case "n":
                    return;
                default:
                    break;
            }
        }
    }

    public static void registrarEntrega() {
        Repartidor repartidor;
        while (true) {
            repartidor = getRepartidor();
            if (repartidor == null) {
                return;
            }
            if (repartidor.getPedidosAsignados().isEmpty()) {
                System.out.println("[UY]. El Repartidor no tiene Pedidos asignados!");
            } else {
                Pedido pedido = choosePedido(repartidor.getPedidosAsignados());
                if (pedido == null) {
                    return;
                } else {
                    if (gestor.registrarEntrega(repartidor.getLegajo(), pedido.getCodigo())) {
                        System.out.println("Entrega registrada con exito");
                        return;
                    } else {
                        System.out.println("[Error]. Sucedio un error inesperado. La entrega no pudo ser registrada");
                    }
                }
            }
        }

    }

    public static Pedido choosePedido(List<Pedido> pedidos) {
        String option;
        while (true) {
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido pedido = pedidos.get(i);
                System.out.println("[" + i + "]. Destino: " + pedido.getDestino() + ", Peso: "
                        + pedido.getPesoKg());
            }
            option = menu.askCode();
            if (option == null) { // volver al menu
                return null;
            } else {
                try {
                    int code = Integer.parseInt(option);
                    Pedido pedido = null;
                    if (code >= 0 && code < pedidos.size()) {
                        pedido = pedidos.get(code);
                    }
                    if (pedido == null) {
                        System.out.println("[UY!]. Debe seleccionar una opcion de la lista.");
                    } else {
                        return pedido;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[UY!]. Debe ingresar un numero.");
                }
            }
        }

    }

    public static void gestionarRepartidores() {
        String option;
        Repartidor repartidor;
        while (true) {
            option = menu.getDeliveryPersMenuOption();
            switch (option) {
                case "1": // agregar
                    repartidor = createRepartidor(new Repartidor());
                    if (gestor.addNewRepartidor(repartidor)) {
                        System.out.println("[Repartidor agregado con exito]");
                    } else {
                        System.out.println(
                                "[Error]. Sucedio un error inesperado. El repartidor no fue agregado a la lista");
                    }
                    break;
                case "2": // eliminar
                    repartidor = getRepartidor();
                    if (repartidor != null) {
                        deleteRepartidor(repartidor);
                    }
                    break;
                case "3": // buscar por legajo
                    repartidor = getRepartidor();
                    if (repartidor != null) {
                        System.out.println(repartidor);
                        menu.waitEnter();
                    }
                    break;
                case "4": // ver todos
                    showRepartidores();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    public static void showRepartidores() {
        List<Repartidor> repartidores = gestor.getRepartidores();
        if (repartidores.size() <= 0) {
            System.out.println("No hay Repartidores que mostrar!");
        } else {
            System.out.println("==[REPARTIDORES]==");
            for (Repartidor repartidor : repartidores) {
                System.out.println("[" + (repartidor.getLegajo()) + "]. Nombre: " + repartidor.getNombre());
            }
        }
        System.out.println("Presione Enter para volver..");
        menu.waitEnter();
    }

    public static void deleteRepartidor(Repartidor repartidor) {
        String option;
        while (true) {
            System.out.println(repartidor);
            if (!gestor.canDeleteRepartidor(repartidor.getLegajo())) {
                System.out.println(
                        "[UY!]. Este repartidor tiene Pedidos pendientes o en Camino, y no puede ser eliminado hasta que sean cancelados o entregados respectivamente.");
                menu.waitEnter();
            } else {
                System.out.println("Eliminar Repartidor");
                option = menu.askEstaSeguro().toLowerCase();
                switch (option) {
                    case "s":
                        if (gestor.deleteRepartidor(repartidor.getLegajo())) {
                            System.out.println("[Repartidor eliminado con exito]");
                        } else {
                            System.out.println("[No se pudo eliminar el repartidor]");
                        }
                        return;
                    case "n":
                        System.out.println("Volviendo al menu principal.");
                        return;
                    default:
                        System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                        break;
                }
            }

        }
    }

    public static Repartidor getRepartidor() {
        while (true) {
            String legajo = menu.askLegajoRepartidor();
            if (legajo.isBlank()) {
                System.out.println("[<-]. Volviendo..");
                return null;
            } else {
                Repartidor repartidor = gestor.getRepartidor(legajo);
                if (repartidor == null) {
                    System.out.println("[UY!]. Repartidor no encontrado.");
                } else {
                    return repartidor;
                }
            }
        }
    }

    public static Repartidor createRepartidor(Repartidor repartidor) {
        String option;
        while (true) {
            System.out.println(repartidor);
            option = menu.getDeliveryPersFields();
            switch (option) {
                case "1":
                    repartidor.setLegajo(menu.askLegajo());
                    break;
                case "2":
                    repartidor.setNombre(menu.askValidNombre());
                    break;
                case "3":
                    repartidor.setVehiculo(registrarVehiculo());
                    break;
                case "4":
                    System.out.println("Creacion de Repartidor Cancelada.");
                    return null;
                case "5":
                    if (Validation.getNombreStatus(repartidor.getNombre()) != ResultadoValidation.VALID
                            || Validation.getLegajoStatus(repartidor.getLegajo()) != ResultadoValidation.VALID
                            || repartidor.getVehiculo() == null) {
                        System.out.println(
                                "[UY!]. Debe rellenar todo los campos antes de poder agregar el nuevo Repartidor al sistema.");
                        break;
                    } else {
                        if (gestor.isRepartidorInList(repartidor)) {
                            System.out.println("[UY!]. El legajo ingresado ya existe en la lista de repartidores.");
                            break;
                        }
                        return repartidor;
                    }
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    public static Vehiculo registrarVehiculo() {
        String patente = "", option;
        TipoVehiculo tipo = TipoVehiculo.CAMIONETA;
        double capacidad = 0, velocidad = 0;
        BigDecimal costoPorKm = BigDecimal.ZERO;
        while (true) {
            System.out.println("Tipo de Vehiculo:" + (tipo) + "\nPatente: " + (patente != "" ? patente : "sin Patente")
                    + "\nCapacidad: " + capacidad + " KG.\nVelocidad: " + velocidad + "KM/H.\nCosto: " + costoPorKm
                    + "$/KM.");
            option = menu.getVehicleFieldOption();
            switch (option) {
                case "1":
                    tipo = menu.askTipoVehiculo();
                    break;
                case "2":
                    patente = menu.askPatente();
                    break;
                case "3":
                    capacidad = menu.askCapacidad();
                    break;
                case "4":
                    velocidad = menu.askVelocidad();
                    break;
                case "5":
                    costoPorKm = menu.askCostoPorKm();
                    break;
                case "6":
                    System.out.println("CREACION DE VEHICULO CANCELADA");
                    return null;
                case "7":
                    if (Validation.getPatenteStatus(patente) != ResultadoValidation.VALID || capacidad <= 0
                            || velocidad <= 0
                            || costoPorKm.compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("[UY!]. Debe rellenar todo los campos antes de guardar!");
                    } else {
                        switch (tipo) {
                            case CAMIONETA:
                                return new Camioneta(patente, capacidad, velocidad, costoPorKm);
                            case MOTO:
                                return new Moto(patente, capacidad, velocidad, costoPorKm);
                            case BICICLETA:
                                return new Bicicleta(patente, capacidad, velocidad, costoPorKm);
                        }
                    }
                    break;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }

        }
    }

    public static void clientManagement() {
        String option;
        while (true) {
            option = menu.getClientMenuOption();
            switch (option) {
                case "1": // agregar
                    Cliente nuevoCliente = modifyClient(new Cliente());
                    if (nuevoCliente != null) {
                        gestor.addNewClient(nuevoCliente);
                        System.out.println("[Cliente agregado con exito]");
                    }
                    break;
                case "2": // modificar
                    Cliente clienteEditar = getClient();
                    if (clienteEditar != null) {
                        Cliente clienteModificado = modifyClient(clienteEditar);
                        if (clienteModificado != null) {
                            gestor.modifyClient(clienteModificado);
                            System.out.println("[Cliente modificado con exito]");
                        }
                    }
                    break;
                case "3": // eliminar
                    Cliente clienteEliminar = getClient();
                    deleteClient(clienteEliminar);
                    break;
                case "4": // ver todos
                    showClients();
                    break;
                case "5":
                    System.out.println("Volviendo al menu principal.");
                    return;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    public static void showClients() {
        TreeMap<Integer, Cliente> clientes = gestor.getClientes();
        if (clientes.size() <= 0) {
            System.out.println("No hay clientes que mostrar!");
        } else {
            System.out.println("==[CLIENTES]==");
            for (Map.Entry<Integer, Cliente> entry : clientes.entrySet()) {
                System.out.println("[" + (entry.getKey()) + "]. Nombre: " + entry.getValue().getNombre());
            }
        }
        System.out.println("Presione Enter para volver..");
        menu.waitEnter();
    }

    /**
     * Shows the Client passed by parameters and asks user if sure before deleting
     * it.
     */
    public static void deleteClient(Cliente cliente) {
        String option;
        while (true) {
            System.out.println(cliente);
            System.out.println("Eliminar Cliente");
            option = menu.askEstaSeguro().toLowerCase();
            switch (option) {
                case "s":
                    if (gestor.deleteClient(cliente.getCodigo()) != null) {
                        System.out.println("[Cliente eliminado con exito]");
                        return;
                    } else {
                        System.out.println("[No se encontro el cliente para eliminar]");
                        return;
                    }
                case "n":
                    System.out.println("Volviendo al menu principal.");
                    return;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    /**
     * Asks for the code of a client, returns the found client or null if the user
     * choosed
     * to go back.
     * 
     * @return the searched {@link Cliente} or {@code null} if the user choosed to
     *         go back to the main menu.
     */
    public static Cliente getClient() {
        while (true) {
            String code = menu.askCode();
            if (code.isBlank()) {
                System.out.println("[Volviendo al menu principal]");
                return null;
            } else {
                try {
                    int codeNumber = Integer.parseInt(code);
                    Cliente cliente = gestor.getCliente(codeNumber);
                    if (cliente == null) {
                        System.out.println("[UY!]. Cliente no encontrado.");
                    } else {
                        return cliente;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[UY!]. Debe ingresar un numero!");
                }
            }
        }
    }

    /**
     * Allows user to modify a Client passed by parameter and returns it. If its a
     * client with no code, it shows the menu as if creating a new Client, otherwise
     * it will show as if modifying an existing Client.
     * 
     * @param cliente - the {@link Cliente} to modify.
     * @return the {@link Cliente} modified or {@code null} if the modification was
     *         cancelled
     */
    public static Cliente modifyClient(Cliente cliente) {
        String option;
        while (true) {
            if (cliente.getCodigo() == 0) {
                System.out.println("==[CREAR NUEVO CLIENTE]==");
            } else {
                System.out.println("==[MODIFICAR CLIENTE]==");
            }
            System.out.println(cliente.toDisplayString());
            option = menu.getClientFieldsOption();
            switch (option) {
                case "1":
                    cliente.setNombre(menu.askValidNombre());
                    break;
                case "2":
                    cliente.setTelefono(menu.askTelefono());
                    break;
                case "3":
                    cliente.setZonaCiudad(menu.askZonaCiudad());
                    break;
                case "4":
                    System.out.println("[Modificacion de Cliente Cancelada]");
                    return null;
                case "5":
                    if (cliente.getNombre() == null || cliente.getTelefono() == null
                            || cliente.getZonaCiudad() == null) {
                        System.out.println(
                                "[UY!]. Debe ingresar toda la informacion solicitada del cliente antes de poder guardarlo en el sistema.");
                        break;
                    } else {
                        return cliente;
                    }
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    public static void registrarPedido() {
        String option, destino = "";
        double peso = 0, distancia = 0;
        Cliente cliente = null;
        TipoPedido tipo = TipoPedido.PEDIDO_NORMAL;
        while (true) {
            System.out.println("---[MENU PEDIDO NUEVO]---");
            System.out.println("[1]. Cliente: " + (cliente == null ? "sin Cliente" : cliente.getNombre()));
            System.out.println("[2]. Peso del Pedido(KG): " + (peso + "kg"));
            System.out.println("[3]. Distancia(KM): " + (distancia + "km"));
            System.out.println("[4]. Destino: " + (destino == "" ? "Sin destino" : destino));
            System.out.println("[5]. Tipo de Pedido: " + tipo.toString());
            System.out.println("[6]. CANCELAR;");
            System.out.println("[7]. GUARDAR.");
            System.out.println("Ingrese el campo a modificar: ");
            option = scanner.nextLine();
            switch (option) {
                case "1":
                    cliente = getClient();
                    break;
                case "2":
                    peso = menu.askPeso();
                    break;
                case "3":
                    distancia = menu.askDistancia();
                    break;
                case "4":
                    destino = menu.askDestino();
                    break;
                case "5":
                    tipo = menu.askTipoPedido();
                    break;
                case "6":
                    System.out.println("CREACION DE PEDIDO CANCELADA");
                    return;
                case "7":
                    if (cliente == null || Validation.getDestinoStatus(destino) != ResultadoValidation.VALID
                            || peso <= 0
                            || distancia <= 0) {
                        System.out.println("[UY!]. Debe rellenar todo los campos antes de guardar!");
                    } else {
                        try {
                            switch (tipo) {
                                case PEDIDO_NORMAL:
                                    gestor.addNewPedido(new PedidoNormal(cliente, destino, peso, distancia));
                                    break;
                                case PEDIDO_FRAGIL:
                                    gestor.addNewPedido(new PedidoFragil(cliente, destino, peso, distancia));
                                    break;
                                case PEDIDO_URGENTE:
                                    gestor.addNewPedido(new PedidoUrgente(cliente, destino, peso, distancia));
                                    break;
                            }
                            System.out.println("PEDIDO AGREGADO CON EXITO.");
                            return;
                        } catch (PedidoInvalidoException e) {
                            System.out.println("[UY!]." + e.getMessage());
                        }
                    }
                    break;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

    public static void atenderPedido() {
        String option;
        Pedido pedidoSiguiente = gestor.getNextPedido();
        if (!gestor.areTherePedidos()) {
            System.out.println("No hay pedidos que atender! Presione Enter para volver.");
            menu.waitEnter();
        } else {
            System.out.println("===[Siguiente Pedido]===");
            System.out.println(pedidoSiguiente);
            option = menu.askEstaSeguro().toLowerCase();
            switch (option) {
                case "s":
                    ResultadoAtencion resultadoAtencion = gestor.atenderPedido();
                    switch (resultadoAtencion.getEstado()) {
                        case ATENDIDO:
                            System.out.println("El pedido nro " + (resultadoAtencion.getPedido().getCodigo())
                                    + " fue atendido y asignado a el repartidor legajo nro: "
                                    + (resultadoAtencion.getRepartidor().getLegajo()));
                            break;
                        case SIN_REPARTIDOR_DESPONIBLE:
                            System.out.println(
                                    "[UY!]. No hay repartidores disponibles en este momento para el siguiente pedido a atender. Por favor, intentelo mas tarde.");
                            break;
                        default:

                            break;
                    }
                    break;
                case "n":
                    System.out.println("Volviendo...");
                    return;
                default:
                    System.out.println("[UY!] Debe ingresar una opcion valida del menu.");
                    break;
            }
        }
    }

}
