
public class Simbolo {

  public static final int ENTERO=1, REAL=2;
  public static final int VARIABLE=3, CLASE=4, FUNCION=5;


  public String nombre;
  public int tipoSimbolo;   // variable, clase, funcion
  public int tipo;          // ENTERO o REAL
  
  
  public Simbolo(String nombre,int tipoSimbolo,int tipo)
  {
    this.nombre = nombre;
    this.tipoSimbolo = tipoSimbolo;
    this.tipo = tipo;
  }

}
