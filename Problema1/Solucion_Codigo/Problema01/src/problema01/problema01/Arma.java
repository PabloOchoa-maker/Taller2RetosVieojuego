

package problema01.problema01;

public class  Arma extends Objeto{

    public Arma(String nombre, String tipo,int dan) {
        super(nombre, tipo);
        this.danio=dan;
        this.defensa=0;
    }

    @Override
    public int getModificadorAtaque() { return danio;}
        

    @Override
    public int getModificadorDefensa() {return 0;}
    
}
