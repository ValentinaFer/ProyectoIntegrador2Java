package models;

public class Cliente {
    private int codigo;
    private String nombre;
    private String telefono;
    private String zonaCiudad;

    public Cliente(String nombre, String telefono, String zoneCiudad) {
        this.setNombre(nombre);
        this.setTelefono(telefono);
        this.setZonaCiudad(zoneCiudad);
    }

    public Cliente(int codigo, String nombre, String telefono, String zoneCiudad) {
        this.setCodigo(codigo);
        this.setNombre(nombre);
        this.setTelefono(telefono);
        this.setZonaCiudad(zoneCiudad);
    }

    public Cliente() {

    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El codigo del cliente no puede ser negativo ni cero.");
        }
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        ResultadoValidation status = Validation.getNombreStatus(nombre);
        if (status == ResultadoValidation.EMPTY) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacio.");
        } else if (status == ResultadoValidation.TOO_LONG) {
            throw new IllegalArgumentException("El nombre del cliente no puede exceder los 100 caracteres.");
        }
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        ResultadoValidation status = Validation.getTelefonoStatus(telefono);
        if (status == ResultadoValidation.EMPTY) {
            throw new IllegalArgumentException("El telefono del cliente no puede estar vacio.");
        } else if (status == ResultadoValidation.TOO_LONG) {
            throw new IllegalArgumentException("El telefono no puede exceder los 15 caracteres.");
        } else if (status == ResultadoValidation.NAN) {
            throw new IllegalArgumentException("El telefono debe contener caracteres numericos unicamente.");
        }
        this.telefono = telefono;
    }

    public void setZonaCiudad(String zonaCiudad) {
        ResultadoValidation status = Validation.getZonaCiudadStatus(zonaCiudad);
        if (status == ResultadoValidation.EMPTY) {
            throw new IllegalArgumentException("La zona de ciudad del cliente no puede estar vacia.");
        } else if (status == ResultadoValidation.TOO_LONG) {
            throw new IllegalArgumentException("La zona de ciudad del cliente no puede exceder los 300 caracteres.");
        }
        this.zonaCiudad = zonaCiudad;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getZonaCiudad() {
        return this.zonaCiudad;
    }

    public String toDisplayString() {
        return "==[Cliente nro." + (this.getCodigo()) + "]==\nNombre: " + (this.getNombre() + "\nTelefono: "
                + (this.getTelefono()) + "\nZona Ciudad: " + (this.getZonaCiudad()));
    }

    @Override
    public String toString() {
        return "[ Cliente nro." + (this.getCodigo()) + "]. Nombre: " + (this.getNombre());
    }

}