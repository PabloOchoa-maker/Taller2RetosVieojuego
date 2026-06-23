
package problema01.problema01;

public abstract class Objeto {
    protected String nombre;
    protected String tipo;
    protected int danio;
    protected int defensa;

    public Objeto(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }
    public abstract int getModificadorAtaque();
    public abstract int getModificadorDefensa();

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getDanio() { return danio; }
    public int getDefensa() { return defensa; }
    
    
    
    
}
