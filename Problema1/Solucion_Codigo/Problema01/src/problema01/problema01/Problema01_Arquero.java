
package problema01.problema01;

public class Problema01_Arquero  extends Problema01_Personaje {
    private int precision;
    public Problema01_Arquero(String nombre, int puntosVidaMaximos, int fuerza, int defensa, int precision) {
        super(nombre, puntosVidaMaximos, fuerza, defensa);
        this.precision = precision;
    }
    @Override
    public int calcularAtaque() {
        return this.fuerza + (this.precision / 2);
    }
    @Override
    public int calcularDefensa() {
        return this.defensa + (this.precision / 4);
    }
    @Override
    public String obtenerHabilidadEspecial() {
        return "Lluvia de Flechas";
    }
}
