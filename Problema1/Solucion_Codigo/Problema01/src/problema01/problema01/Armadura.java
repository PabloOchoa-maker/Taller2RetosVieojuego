
package problema01.problema01;

public class  Armadura extends Objeto{

    public Armadura(String nombre, String tipo,int def) {
        super(nombre, tipo);
        this.danio=0;
        this.defensa=def;
    }

    @Override
    public int getModificadorAtaque() { return 0;}


    @Override
    public int getModificadorDefensa() {return defensa;}

}
