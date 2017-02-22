import java.io.*;


class testLexico {

    public static String toString(Token t){
		return "("+t.fila+","+t.columna+"): "+t.lexema+" es de tipo "+t.tipo+" : "+t.toString()+'\n'; 
    }


    public static void main(String[] args) {

        AnalizadorLexico al;
        Token t;

        RandomAccessFile entrada;

        try {
            entrada = new RandomAccessFile("/home/ara65/Escritorio/PL/AnalizadorLexico/src/test.txt"/*args[0]*/,"r");
            al = new AnalizadorLexico(entrada);

            while ((t=al.siguienteToken()).tipo != Token.EOF) {
                System.out.println("Token: "+ t.fila + ","
                                            + t.columna+ " " 
                                            + t.lexema+ "  -> "
                                            + toString(t));
            }
        }
        catch (FileNotFoundException e) {
          System.out.println("Error, fichero no encontrado: " + args[0]);
        }
    }
}
