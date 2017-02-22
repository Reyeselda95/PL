
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int fila=1, columna=1,pos=0;
    
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

    public void rollBack(){
        try {
            --pos;
            fichero.seek(pos);
            
        } catch (IOException ex) {
            Logger.getLogger(AnalizadorLexico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Token siguienteToken(){
        Token t= new Token();
        boolean finish=false;
        int estado=1;
        int nuevo;
        char read=leerCaracter();
        ++pos;
        do{
            nuevo=delta(estado,read);
            if(nuevo==-1)//Si el estado es error
            {
               // System.out.println("He entrado aqui por el caracter: "+read+" con el estado:"+nuevo);
                switch(read){
                    case ' ':
                        ++columna;
                        read=leerCaracter();
                        ++pos;
                        estado=1;
                        nuevo=delta(estado,read);
                        ++columna;
                        break;
                    case '\n':
                        read=leerCaracter();
                        ++pos;
                        estado=1;
                        nuevo=delta(estado,read);
                        columna=1;
                        ++fila;
                        break;
                    case '\t':
                        ++columna;
                        read=leerCaracter();
                        ++pos;
                        estado=1;
                        nuevo=delta(estado,read);
                        ++columna;
                        break;
                    default:
                        System.err.println("Error lexico ("+t.fila+","+t.columna+"): caracter ’"+read+"’ incorrecto");
                        System.exit(-1);
                }
                
            }
            else{ 
                if(esFinal(nuevo)){//Si el nodo es final
                    estado=nuevo;
                    if(read==EOF){
                        t.tipo=Token.EOF;
                        return t;
                    }
//                    System.out.println(t.lexema+ " Estado:"+estado);
                    switch(estado){
                        case 4:
                            t.tipo=Token.MULOP;
                            rollBack();
                            --columna;
                            break;
                        case 9:
                            t.tipo= Token.REAL ;
                            rollBack();
                            --columna;
                            break;
                        case 10:
                            t.tipo=Token.ENTERO;
                            rollBack();
                            --columna;
                            break;
                        case 11:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.MULOP;
                            break;
                        case 13:
                            t.tipo=reservada(t.lexema);
                            rollBack();
                            --columna;
                            break;
                        case 14:
                            t.tipo=Token.ENTERO;
                            rollBack();
                            rollBack();
                            --columna;
                            --columna;
                            break;
                    }
                    t.fila=fila;
                    t.columna=columna;
                    return t;
                }
                else{
                    //System.out.println("El lexema es: "+t.lexema+" en el estado:"+estado);
                    if(nuevo==1){
                        t.lexema="";
                    }
                    else{
                        t.lexema=t.lexema+read;
                    }
                    ++columna;

                    estado=nuevo;

                    read=leerCaracter();
                    ++pos;
                }
            }
            
        }while(true);
    }
    
    public boolean esFinal(int estado){
    	if(estado==4 || estado==9 || estado==11 || estado==13 || estado==10 || estado == 14){
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
                    return 14;
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
                    return 13;
                }
            case 13: //Estado final identificador
                    return -1;
            case 14: //Estado final numero entero //Volverá 2 posiciones atrás
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
