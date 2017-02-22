
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ara65
 */
public class AnalizadorLexico {

    private RandomAccessFile fichero;
    private static char EOF=(char)-1;
    
    public AnalizadorLexico(RandomAccessFile a) {
        fichero=a;
    }
    
    public int reservada(String pal){
        switch(pal){
            case "class":
                return Token.CLASS;
            case "public":
                return Token.PUBLIC;
            case "private":
                return Token.PRIVATE;
            case "float":
                return Token.FLOAT;
            case "int":
                return Token.INT;
            case "return":
                return Token.RETURN;
            default:
                return Token.ID;       
        }
    }

    public Token siguienteToken(){
        Token t= new Token();
        boolean finish=false;
        int estado=1;
        int nuevo;
        char read=leerCaracter();
        do{
            nuevo=delta(estado,read);
            if(nuevo==-1)
            {
                System.err.println("Error lexico ("+t.fila+","+t.columna+"): caracter ’"+read+"’ incorrecto");
                System.exit(-1);
            }
            if(esFinal(nuevo)){
                
                
                
                if(read=='\n'){
                    ++t.columna;
                    t.fila=0;
                }else{
                    ++t.fila;
                }
                
                if(read==EOF){
                    t.tipo=Token.EOF;
                    return t;
                }
                
                switch(estado){
                    case 4:
                        t.tipo=Token.MULOP;
                        --t.columna;
                        break;
                    case 9:
                        t.tipo= Token.REAL ;
                        --t.columna;
                        break;
                    case 10:
                        t.tipo=Token.ENTERO;
                        --t.columna;
                    case 11:
                        t.tipo= Token.MULOP;
                        break;
                    case 13:
                        t.tipo=reservada(t.lexema);
                        --t.columna;
                        break;
                }
                
                
                return t;
      
            }
            else{
                t.lexema=t.lexema+read;
                ++t.columna;
                estado=nuevo;
                read=leerCaracter();
            }
            
        }while(true);
    }
    
    public boolean esFinal(int estado){
    	if(estado==4 || estado==9 || estado==11 || estado==13 || estado==10){
            return true;
    	}
    	else{
            return false;
    	}
    }

    public int delta (int estado, int c) {
        switch (estado) {
            case 1: 
                if(c=='*'){
                    return 11;
                }
                else if(c=='/'){
                    return 2;
                }
                else if(c>='0' && c<='9'){
                    return 6;
                }
                else if ((c>='a' && c<='z')||(c>='A'&&c<='Z')){
                    return 12;
                }
                else{
                    return -1;
                }
            case 2: 
                if(c=='*'){
                    return 3;
                }
                else{
                    return 4;
                }
            case 3: 
                if(c=='*'){
                    return 5;
                }
                else{
                    return 3;
                }
            case 4: //Estado final mulop
                return -1;
            case 5: 
                if(c=='/'){
                    return 1;
                }
                else if(c=='*'){
                    return 5;
                }
                else{
                    return 3;
                }
            case 6:
                if(c>='0' && c<='9'){
                    return 6;
                }
                else if(c=='.'){
                    return 7;
                }
                else{
                    return 10;
                }
            case 7:
                if(c>='0' && c<='9'){
                    return 8;
                }
                else{
                    return -1;
                }
            case 8:
                if(c>='0' && c<='9'){
                    return 8;
                }
                else{
                    return 9;
                }
            case 9: //Estado final numero real
                return -1;
            case 10: //Estado final numero entero
                return -1;
            case 11: //Estado final mulop
                return -1;
            case 12:
                if((c>='0' && c<='9')||(c>='a' && c<='z')||(c>='A'&&c<='Z')){
                    return 12;
                }
                else{
                    return -1;
                }
            case 13: //Estado final identificador
                    return -1;
            default: // error interno
                return -1;
        }
    }

    public char leerCaracter(){
        char currentChar;
        try {
            currentChar = (char)fichero.readByte();
            return currentChar;
        }
        catch (EOFException e) {
                return EOF; // constante estática de la clase
        } catch (IOException e) { //... // error lectura
        
        }
        return ' ';
    }
}
