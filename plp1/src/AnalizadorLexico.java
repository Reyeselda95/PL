
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
 * @author REYES ALBILLAR, ALEJANDRO <ara65@alu.ua.es>
 * <el_reyes_95@hotmmail.com> <el.reyes.95@gmail.com>
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
                        break;
                    case (char)-1:
                        t.tipo=Token.EOF;
                        t.fila=fila;
                        t.columna=columna;
                        return t;
                    default:
                        System.err.println("Error lexico ("+fila+","+columna+"): caracter '"+read+"' incorrecto");
                        System.exit(-1);
                }
                
            }
            else{ 
                if(esFinal(nuevo)){//Si el nodo es final
                    estado=nuevo;    
//                    System.out.println(t.lexema+ " Estado:"+estado);
                    switch(estado){
                        case 4:
                            t.tipo=Token.MULOP;
                            rollBack();
                            break;
                        case 9:
                            t.tipo= Token.REAL ;
                            rollBack();
                            break;
                        case 10:
                            t.tipo=Token.ENTERO;
                            rollBack();
                            break;
                        case 11:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.MULOP;
                            ++columna;
                            break;
                        case 13:
                            t.tipo=reservada(t.lexema);
                            rollBack();
                            break;
                        case 14:
                            t.tipo=Token.ENTERO;
                            t.lexema=t.lexema.substring(0, t.lexema.length()-1);
                            rollBack();
                            rollBack();
                            --columna;
                            break;
                        case 15:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.PARI;
                            ++columna;
                            break;
                        case 16:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.PARD;
                            ++columna;
                            break;
                        case 17:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.ADDOP;
                            ++columna;
                            break;
                        case 18:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.ASIG;
                            ++columna;
                            break;
                        case 19:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.PYC;
                            ++columna;
                            break;
                        case 20:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.DOSP;
                            ++columna;
                            break;
                        case 21:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.COMA;
                            ++columna;
                            break;
                        case 22:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.LLAVEI;
                            ++columna;
                            break;
                        case 23:
                            t.lexema=t.lexema+read;
                            t.tipo= Token.LLAVED;
                            ++columna;
                            break;
                    }
                    t.fila=fila;
                    t.columna=columna-t.lexema.length();                  
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
    	if(estado==4 || estado==9 || estado==11 || estado==13 || estado==10 || estado >= 14){
            return true;
    	}
    	else{
            return false;
    	}
    }

    public int delta (int estado, int c) {
        switch (estado) {
            case 1:
                if(c>='0' && c<='9'){
                    return 6;
                }
                else if ((c>='a' && c<='z')||(c>='A'&&c<='Z')){
                    return 12;
                }
                else{//Casos únicos
                    switch(c){
                        case '/':
                            return 2;
                        case '*':
                            return 11;
                        case '(':
                            return 15;
                        case ')':
                            return 16;
                        case '+':
                            return 17;
                        case '-':
                            return 17;
                        case '=':
                            return 18;
                        case ';':
                            return 19;
                        case ':':
                            return 20;
                        case ',':
                            return 21;
                        case '{':
                            return 22;
                        case '}':
                            return 23;
                        default:
                            return -1;
                    }
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
                else if(c==EOF){
                    System.err.println("Error lexico: fin de fichero inesperado");
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
                else if(c==EOF){
                    System.err.println("Error lexico: fin de fichero inesperado");
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
            case 15: //Estado final pari
                    return -1;
            case 16: //Estado final pard
                    return -1;
            case 17: //Estado final adop
                    return -1;
            case 18: //Estado final asig
                    return -1;
            case 19: //Estado final pyc
                    return -1;
            case 20: //Estado final dosp
                    return -1;
            case 21: //Estado final coma
                    return -1;
            case 22: //Estado final llavei
                    return -1;
            case 23: //Estado final llaved
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
