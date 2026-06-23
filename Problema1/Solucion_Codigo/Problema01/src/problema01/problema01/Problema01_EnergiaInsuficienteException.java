package problema01.problema01;

/**
 * Excepción comprobada que se lanza cuando un personaje intenta usar su
 * habilidad especial sin disponer de la energía (maná) necesaria.
 */
public class Problema01_EnergiaInsuficienteException extends Exception {
    private final String personaje;
    private final String habilidad;
    private final int costoRequerido;
    private final int energiaActual;

    public Problema01_EnergiaInsuficienteException(String personaje, String habilidad,
            int costoRequerido, int energiaActual) {
        super("¡" + personaje + " no tiene energía suficiente para usar '" + habilidad
                + "'! (Requiere " + costoRequerido + ", dispone de " + energiaActual + ")");
        this.personaje = personaje;
        this.habilidad = habilidad;
        this.costoRequerido = costoRequerido;
        this.energiaActual = energiaActual;
    }

    public String getPersonaje() { return personaje; }
    public String getHabilidad() { return habilidad; }
    public int getCostoRequerido() { return costoRequerido; }
    public int getEnergiaActual() { return energiaActual; }
}
