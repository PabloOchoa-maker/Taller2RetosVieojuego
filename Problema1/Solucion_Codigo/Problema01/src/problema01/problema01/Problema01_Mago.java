
package problema01.problema01;

public class Problema01_Mago extends Problema01_Personaje {
    private int poderMagico;
    public Problema01_Mago(String nombre, int puntosVidaMaximos, int fuerza, int defensa, int poderMagico) {
        super(nombre, puntosVidaMaximos, fuerza, defensa);
        this.poderMagico = poderMagico;
    }
    @Override
    public int calcularAtaque() {
        return this.fuerza + this.poderMagico;
    }
    @Override
    public int calcularDefensa() {
        return this.defensa + (this.poderMagico / 2);
    }
    @Override
    public String obtenerHabilidadEspecial() {
        return "Bola de Fuego";
    }
}
