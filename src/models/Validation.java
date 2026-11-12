package models;

public final class Validation {

    private Validation(){

    }

    /**
     * Returns state of Destino string. Will not be valid if empty or too long(over 100 characters).
     * 
     * @param destino the string to validate.
     * @return ResultadoValidation enum value:
     * <ul>
     * <li>{@link ResultadoValidation#EMPTY} – if the strings is null or empty</li>
     * 
     * <li>{@link ResultadoValidation#TOO_LONG} – if the strings is too long(>100 characters)</li>
     * 
     * <li>{@link ResultadoValidation#VALID} – if the strings is otherwise not one of the previous cases</li>
     * </ul>
     */
    public static ResultadoValidation getDestinoStatus(String destino){
        if (destino == null || destino.isBlank()){
            return ResultadoValidation.EMPTY;
        } else if (destino.length() > 100){
            return ResultadoValidation.TOO_LONG;
        }
        return ResultadoValidation.VALID;
    }
    
    /**
     * Returns state of Nombre string. Will not be valid if empty or too long(over 100 characters).
     * 
     * @param nombre the string to validate.
     * @return ResultadoValidation enum value:
     * <ul>
     * <li>{@link ResultadoValidation#EMPTY} – if the strings is null or empty</li>
     * 
     * <li>{@link ResultadoValidation#TOO_LONG} – if the strings is too long(>100 characters)</li>
     * 
     * <li>{@link ResultadoValidation#VALID} – if the strings is otherwise not one of the previous cases</li>
     * </ul>
     */
    public static ResultadoValidation getNombreStatus(String nombre){
        if (nombre == null || nombre.isBlank()){
            return ResultadoValidation.EMPTY;
        } else if (nombre.length() > 100){
            return ResultadoValidation.TOO_LONG;
        }
        return ResultadoValidation.VALID;
    }

    /**
     * Returns state of Telefono string. Will not be valid if empty or too long(over 15 characters), or is not made of numbers alone.
     * 
     * @param telefono the string to validate.
     * @return ResultadoValidation enum value:
     * <ul>
     * <li>{@link ResultadoValidation#EMPTY} – if the strings is null or empty</li>
     * 
     * <li>{@link ResultadoValidation#TOO_LONG} – if the strings is too long(>15 characters)</li>
     * 
     * <li>{@link ResultadoValidation#NAN} – if the strings contains characters that aren't numbers only([0-9]+)</li>
     * 
     * <li>{@link ResultadoValidation#VALID} – if the strings is otherwise not one of the previous cases</li>
     * </ul>
     */
    public static ResultadoValidation getTelefonoStatus(String telefono){
        if (telefono == null || telefono.isBlank()){
            return ResultadoValidation.EMPTY;
        } else if (telefono.length() > 15){
            return ResultadoValidation.TOO_LONG;
        } else if (!telefono.matches("[0-9]+")){
            return ResultadoValidation.NAN;
        }
        return ResultadoValidation.VALID;
    }

    /**
     * Returns state of zonaCiudad string. Will not be valid if empty or too long(over 300 characters).
     * 
     * @param zonaCiudad the string to validate.
     * @return ResultadoValidation enum value:
     * <ul>
     * <li>{@link ResultadoValidation#EMPTY} – if the strings is null or empty</li>
     * 
     * <li>{@link ResultadoValidation#TOO_LONG} – if the strings is too long(>300 characters)</li>
     * 
     * <li>{@link ResultadoValidation#VALID} – if the strings is otherwise not one of the previous cases</li>
     * </ul>
     */
    public static ResultadoValidation getZonaCiudadStatus(String zonaCiudad){
        if (zonaCiudad == null || zonaCiudad.isBlank()){
            return ResultadoValidation.EMPTY;
        } else if (zonaCiudad.length() > 300){
            return ResultadoValidation.TOO_LONG;
        }
        return ResultadoValidation.VALID;
    }

    /**
     * Returns state of legajo string. Will not be valid if empty or too long(over 100 characters).
     * 
     * @param legajo the string to validate.
     * @return ResultadoValidation enum value:
     * <ul>
     * <li>{@link ResultadoValidation#EMPTY} – if the strings is null or empty</li>
     * 
     * <li>{@link ResultadoValidation#TOO_LONG} – if the strings is too long(>100 characters)</li>
     * 
     * <li>{@link ResultadoValidation#VALID} – if the strings is otherwise not one of the previous cases</li>
     * </ul>
     */
    public static ResultadoValidation getLegajoStatus(String legajo){
        if (legajo == null || legajo.isBlank()){
            return ResultadoValidation.EMPTY;
        } else if (legajo.length() > 100){
            return ResultadoValidation.TOO_LONG;
        }
        return ResultadoValidation.VALID;
    }

    /**
     * Returns state of legajo string. Will not be valid if empty or too long(over 100 characters).
     * 
     * @param legajo the string to validate.
     * @return ResultadoValidation enum value:
     * <ul>
     * <li>{@link ResultadoValidation#EMPTY} – if the strings is null or empty</li>
     * 
     * <li>{@link ResultadoValidation#TOO_LONG} – if the strings is too long(>100 characters)</li>
     * 
     * <li>{@link ResultadoValidation#VALID} – if the strings is otherwise not one of the previous cases</li>
     * </ul>
     */
    public static ResultadoValidation getPatenteStatus(String patente){
        if (patente == null || patente.isBlank()){
            return ResultadoValidation.EMPTY;
        } else if (patente.length() > 10){
            return ResultadoValidation.TOO_LONG;
        } 
        return ResultadoValidation.VALID;
    }

}
