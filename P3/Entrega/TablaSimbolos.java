
import java.util.ArrayList;

public class TablaSimbolos {

   public TablaSimbolos padre;
   public ArrayList<Simbolo> simbolos;
   
   
   public TablaSimbolos(TablaSimbolos padre)
   {
      this.padre = padre;
      simbolos = new ArrayList<Simbolo>();
   }
   
   public boolean anyadir(Simbolo s)
   {
     for (Simbolo ss:simbolos)
       if (ss.nombre.equals(s.nombre)) // repetido en el ámbito
          return false;
   
     simbolos.add(s);
     return true;
   }
   
   Simbolo buscar(String nombre)
   {
     for (Simbolo s:simbolos)
       if (s.nombre.equals(nombre)) return s;
       
     if (padre != null)
       return padre.buscar(nombre);
     else
       return null;
   }
}
