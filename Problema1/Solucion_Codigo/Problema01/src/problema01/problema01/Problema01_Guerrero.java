
package problema01.problema01;

public class Problema01_Guerrero extends Problema01_Personaje {
    private int fuerzaEscudo;
    public Problema01_Guerrero(String nombre, int puntosVidaMaximos, int fuerza, int defensa, int fuerzaEscudo) {
        super(nombre, puntosVidaMaximos, fuerza, defensa);
        this.fuerzaEscudo = fuerzaEscudo;
    }

    @Override
    public int calcularAtaque() {
        return this.fuerza + this.fuerzaEscudo + getBonusAtaque();
    }

    @Override
    public int calcularDefensa() {
        return this.defensa + this.fuerzaEscudo + getBonusDefensa();
    }

    @Override
    public String obtenerHabilidadEspecial() {
        return "Carga con Escudo";
    }
}
