
package problema01.problema01;

import java.util.ArrayList;

public abstract class Problema01_Personaje {
    protected String nombre;
    protected int nivel;
    protected int puntosVida;
    protected int puntosVidaMaximos;
    protected int fuerza;
    protected int defensa;
    protected int experiencia;
    protected ArrayList<Objeto> inventario;
    protected Objeto objetoEquipado;

    public Problema01_Personaje(String nombre, int puntosVidaMaximos, int fuerza, int defensa) {
        this.nombre = nombre;
        this.nivel = 1;
        this.puntosVidaMaximos = puntosVidaMaximos;
        this.puntosVida = puntosVidaMaximos;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.experiencia = 0;
        this.inventario = new ArrayList<Objeto>();
        this.objetoEquipado = null;
    }

    public void agregarObjeto(Objeto objeto) {
        this.inventario.add(objeto);
    }

    public void equipar(Objeto objeto) {
        if (this.inventario.contains(objeto)) {
            this.objetoEquipado = objeto;
        }
    }

    protected int getBonusAtaque() {
        if (objetoEquipado != null) {
            return objetoEquipado.getModificadorAtaque();
        }
        return 0;
    }

    protected int getBonusDefensa() {
        if (objetoEquipado != null) {
            return objetoEquipado.getModificadorDefensa();
        }
        return 0;
    }

    public Objeto getObjetoEquipado() { return objetoEquipado; }
    public ArrayList<Objeto> getInventario() { return inventario; }

    
    public abstract int calcularAtaque();
    public abstract int calcularDefensa();
    public abstract String obtenerHabilidadEspecial();

    public void recibirDano(int danoRecibido) {
        int danoEfectivo = danoRecibido - calcularDefensa();
        if (danoEfectivo < 1) {
            danoEfectivo = 1;
        }
        this.puntosVida -= danoEfectivo;
        if (this.puntosVida < 0) {
            this.puntosVida = 0;
        }
    }

    public void ganarExperiencia(int exp) {
        this.experiencia += exp;
        if (this.experiencia >= 100) {
            this.nivel++;
            this.experiencia -= 100;
            this.puntosVidaMaximos += 20;
            this.fuerza += 5;
            this.defensa += 3;
            this.puntosVida = this.puntosVidaMaximos;
        }
    }

    public boolean estaVivo() {
        return this.puntosVida > 0;
    }
    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public int getPuntosVida() { return puntosVida; }
    public int getPuntosVidaMaximos() { return puntosVidaMaximos; }
    public int getFuerza() { return fuerza; }
    public int getDefensa() { return defensa; }
    public int getExperiencia() { return experiencia; }

    @Override
    public String toString() {
        String equipo = "Ninguno";
        if (objetoEquipado != null) {
            equipo = objetoEquipado.getNombre();
        }
        return String.format("[%s] %s - Nivel: %d | PV: %d/%d | Fuerza: %d | Defensa: %d | EXP: %d/100 | Equipado: %s",
                this.getClass().getSimpleName().replace("Problema01_", ""),
                nombre, nivel, puntosVida, puntosVidaMaximos, fuerza, defensa, experiencia, equipo);
    }
}
