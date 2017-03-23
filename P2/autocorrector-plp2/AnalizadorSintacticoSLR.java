
import java.util.Stack;

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
public class AnalizadorSintacticoSLR {
    private final boolean flag;
    private final AnalizadorLexico al;
    private Token t;
    private Stack<Integer> reglas, estado;
    private String tipo;
    
    /**
     * Inicializamos el analizador Sintáctico con el analizador léxico
     * @param al 
     */
    AnalizadorSintacticoSLR(AnalizadorLexico al){
        flag=true;//Ponemos el flag de impresion de estados a true
        this.al=al;//Inicializamos el analizador léxico
        t=al.siguienteToken();//inicializamos el token
        tipo="";
        estado= new Stack<>();
        reglas= new Stack<>();
    }
    ///////////////////////////////////////////////////////
    // Reglas
    ///////////////////////////////////////////////////////
    
    
//Pseudocódigo
//    push(0)
//    a := siguienteToken()
//    REPETIR
//        sea s el estado en la cima de la pila
//        SI Accion[s; a] = dj ENTONCES
//            push(j)
//            a := siguienteToken()
//        SI NO SI Accion[s; a] = rk ENTONCES
//            PARA i := 1 HASTA Longitud_Parte_Derecha(k) 
//                  HACER pop()
//            sea p el estado en la cima de la pila
//            sea A la parte izquierda de la regla k
//            push(Ir_A[p; A])
//        SI NO SI Accion[s; a] = aceptar ENTONCES
//            fin del analisis
//        SI NO
//            error()
//        FIN_SI
//    HASTA fin del analisis
    public final void analizar(){
        int actual;
        String ir;
        boolean fin=false;
        estado.push(0);
        //imprimeEstado();
        while(!fin){
            actual=getAccion(estado.lastElement(),t);
            //System.out.println(tipo+actual);
            switch(tipo){
                case "d":
                    estado.push(actual);
                    t = al.siguienteToken();
                   // imprimeEstado();
                    break;
                case "r":
                    ir=aplicaRegla(actual);
                  //  imprimeEstado();
                    estado.push(irA(estado.lastElement(),ir));
                  //  imprimeEstado();
                    break;
                case "aceptar":
                  //  imprimeEstado();
                    fin=true;
                    break;
                default://Error
                    printErrorEstado(actual);
                    break;
            }
        }
        imprimeReglas();
    }
    
    /**
     * Devuelve en la variable privada tipo si es de tipo r, d vacio o error.
     * @param estado
     * @param t
     * @return Devuelve el int de la regla a aplicar o -1 en caso de error.
     */
    public final int getAccion(int estado, Token t){
        switch(t.tipo){
            case Token.CLASS:
                switch(estado){
                    case 0:
                        tipo="d";
                        return 3;
                    case 9:
                        tipo="r";
                        return 2;
                    case 11:
                        tipo="d";
                        return 3;
                    case 12: 
                        tipo="r";
                        return 10;
                    case 13:
                        tipo="d";
                        return 3;
                    case 16:
                        tipo="d";
                        return 3;
                    case 30:
                        tipo="r";
                        return 11;
                    case 31:
                        tipo="r";
                        return 9;
                    case 32:
                        tipo="r";
                        return 12;
                    case 36:
                        tipo="r";
                        return 17;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.ID:
                switch(estado){
                    case 3:
                        tipo="d";
                        return 4;
                    case 14:
                        tipo="r";
                        return 15;
                    case 15:
                        tipo="r";
                        return 16;
                    case 18:
                        tipo="d";
                        return 20;
                    case 22:
                        tipo="d";
                        return 23;
                    case 25:
                        tipo="d";
                        return 26;
                    case 33:
                        tipo="d";
                        return 40;
                    case 38:
                        tipo="d";
                        return 40;
                    case 41:
                        tipo="d";
                        return 45;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.LLAVEI:
                switch(estado){
                    case 4:
                        tipo="d";
                        return 5;
                    case 29:
                        tipo="d";
                        return 33;
                    case 33:
                        tipo="d";
                        return 33;
                    case 38:
                        tipo="d";
                        return 33;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.LLAVED:
                switch(estado){
                    case 5:
                        tipo="r";
                        return 4;
                    case 7:
                        tipo="r";
                        return 6;
                    case 8:
                        tipo="d";
                        return 9;
                    case 9:
                        tipo="r";
                        return 2;
                    case 11:
                        tipo="r";
                        return 8;
                    case 12:
                        tipo="r";
                        return 10;
                    case 13:
                        tipo="r";
                        return 8;
                    case 16:
                        tipo="r";
                        return 8;
                    case 17:
                        tipo="r";
                        return 7;
                    case 19:
                        tipo="r";
                        return 5;
                    case 30:
                        tipo="r";
                        return 11;
                    case 31:
                        tipo="r";
                        return 9;
                    case 32:
                        tipo="r";
                        return 12;
                    case 33:
                        tipo="r";
                        return 19;
                    case 35:
                        tipo="d";
                        return 36;
                    case 36:
                        tipo="r";
                        return 17;
                    case 38:
                        tipo="r";
                        return 19;
                    case 39:
                        tipo="r";
                        return 18;
                    case 46:
                        tipo="r";
                        return 3;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.PUBLIC:
                switch(estado){
                    case 5:
                        tipo="d";
                        return 6;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.PRIVATE:
                switch(estado){
                    case 5:
                        tipo="r";
                        return 4;
                    case 7:
                        tipo="d";
                        return 10;
                    case 9:
                        tipo="r";
                        return 2;
                    case 11:
                        tipo="r";
                        return 8;
                    case 12:
                        tipo="r";
                        return 10;
                    case 13:
                        tipo="r";
                        return 8;
                    case 16:
                        tipo="r";
                        return 8;
                    case 17:
                        tipo="r";
                        return 7;
                    case 30:
                        tipo="r";
                        return 11;
                    case 31:
                        tipo="r";
                        return 9;
                    case 32:
                        tipo="r";
                        return 12;
                    case 36:
                        tipo="r";
                        return 17;
                    case 46:
                        tipo="r";
                        return 3;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.DOSP:
                switch(estado){
                    case 6:
                        tipo="d";
                        return 13;
                    case 10:
                        tipo="d";
                        return 11;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.PARI:
                switch(estado){
                    case 20:
                        tipo="d";
                        return 21;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.PARD:
                switch(estado){
                    case 23:
                        tipo="r";
                        return 14;
                    case 26:
                        tipo="r";
                        return 14;
                    case 27:
                        tipo="r";
                        return 13;
                    case 28:
                        tipo="d";
                        return 29;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.PYC:
                switch(estado){
                    case 29:
                        tipo="d";
                        return 30;
                    case 34:
                        tipo="r";
                        return 21;
                    case 36:
                        tipo="r";
                        return 17;
                    case 37:
                        tipo="d";
                        return 38;
                    case 42:
                        tipo="r";
                        return 20;
                    case 43:
                        tipo="r";
                        return 22;
                    case 44:
                        tipo="r";
                        return 23;
                    case 45:
                        tipo="r";
                        return 24;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.COMA:
                switch(estado){
                    case 23:
                        tipo="d";
                        return 24;
                    case 26:
                        tipo="d";
                        return 24;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.INT:
                switch(estado){
                    case 9:
                        tipo="r";
                        return 2;
                    case 11:
                        tipo="d";
                        return 14;
                    case 12:
                        tipo="r";
                        return 10;
                    case 13:
                        tipo="d";
                        return 14;
                    case 16:
                        tipo="d";
                        return 14;
                    case 21:
                        tipo="d";
                        return 14;
                    case 24:
                        tipo="d";
                        return 14;
                    case 30:
                        tipo="r";
                        return 11;
                    case 31:
                        tipo="r";
                        return 9;
                    case 32:
                        tipo="r";
                        return 12;
                    case 36:
                        tipo="r";
                        return 17;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.FLOAT:
                switch(estado){
                    case 9:
                        tipo="r";
                        return 2;
                    case 11:
                        tipo="d";
                        return 15;
                    case 12:
                        tipo="r";
                        return 10;
                    case 13:
                        tipo="d";
                        return 15;
                    case 16:
                        tipo="d";
                        return 15;
                    case 21:
                        tipo="d";
                        return 15;
                    case 24:
                        tipo="d";
                        return 15;
                    case 30:
                        tipo="r";
                        return 11;
                    case 31:
                        tipo="r";
                        return 9;
                    case 32:
                        tipo="r";
                        return 12;
                    case 36:
                        tipo="r";
                        return 17;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.REAL:
                switch(estado){
                    case 41:
                        tipo="d";
                        return 43;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.ENTERO:
                switch(estado){
                    case 41:
                        tipo="d";
                        return 44;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.ASIG:
                switch(estado){
                    case 40:
                        tipo="d";
                        return 41;
                    default:
                        printErrorEstado(estado);
                }
                break;
            case Token.EOF:
                switch(estado){
                    case 1:
                        tipo="aceptar";
                        return 0;
                    case 2:
                        tipo="r";
                        return 1;
                    case 9:
                        tipo="r";
                        return 2;
                    default:
                        printErrorEstado(estado);
                }
                break;
        }
        return 0;
    }
    
    /**
     * Va al estado acorde con lo establecido segun la regla que se le aplique.
     * @param estado
     * @param left
     * @return Devuelve el estado en el que se encuentra ahora.
     */
    public final int irA(int estado,String left){
       switch(left){
            case "S":
            	if(estado==0) return 1;
            case "C":
            	switch(estado){
                    case 0:  return 2;
                    case 11: return 12;
                    case 13: return 12;
                    case 16: return 12;
            	}
            case "B":
            	if(estado==5) return 7;
            case "V":
            	if(estado==7) return 8;
            case "P":
            	switch(estado){
                    case 11: return 19;
                    case 13: return 46;
                    case 16: return 17;
            	}
            case "D":
            	switch(estado){
                    case 11: return 16;
                    case 13: return 16;
                    case 16: return 16;
            	}
            case "Cod":
            	if(estado==29) return 31;
            case "L":
            	switch(estado){
                    case 23: return 28;
                    case 26: return 27;
            	}
            case "Tipo":
            	switch(estado){
                    case 11: return 18;
                    case 13: return 18;
                    case 16: return 18;
                    case 21: return 22;
                    case 24: return 25;
            	}
            case "Bloque":
            	switch(estado){
                    case 29: return 32;
                    case 33: return 34;
                    case 38: return 34;
            	}
            case "SecInstr":
            	switch(estado){
                    case 33: return 35;
                    case 38: return 39;
            	}
            case "Instr":
            	switch(estado){
                    case 33: return 37;
                    case 38: return 37;
                }
            case "Expr":
            	if(estado==41) return 42;
        }
       return -1;//Si no funciona correctamente saltará por aquí
    }
    
    /**
     * Realiza los pop necesarios para cada una de las reglas
     * @param regla 
     * @return Devuelve la regla a aplicar.
     */
    public final String aplicaRegla(int regla){
        reglas.push(regla);
        int poping=0;
        String irA="";
        //Seleccion del tamaño de las reglas
        switch(regla){
            case 1:
                poping=1;
                irA="S";
                break;
            case 2:
            	poping=6;
            	irA="C";
                break;
            case 3:
            	poping=3;
            	irA="B";
                break;
            case 4:
                irA="B";
                break;
            case 5:
            	poping=3;
            	irA="V";
                break;
            case 6:
                irA="V";
                break;
            case 7:
            	poping=2;
            	irA="P";
                break;
            case 8:
                irA="P";
                break;
            case 9:
            	poping=8;
            	irA="D";
                break;
            case 10:
            	poping=1;
            	irA="D";
                break;
            case 11:
            	poping=1;
            	irA="Cod";
                break;
            case 12:
            	poping=1;
            	irA="Cod";
                break;
            case 13:
            	poping=4;
            	irA="L";
                break;
            case 14:
                irA="L";
                break;
            case 15:
            	poping=1;
            	irA="Tipo";
                break;
            case 16:
            	poping=1;
            	irA="Tipo";
                break;
            case 17:
            	poping=3;
            	irA="Bloque";
                break;
            case 18:
                poping=3;
                irA="SecInstr";
                break;
            case 19:
                irA="SecInstr";
                break;
            case 20:
            	poping=3;
            	irA="Instr";
                break;
            case 21:
            	poping=1;
            	irA="Instr";
            	break;
            case 22:
            	poping=1;
            	irA="Expr";
                break;
            case 23:
                poping=1;
                irA="Expr";
                break;
            case 24:
            	poping=1;
            	irA="Expr";
            	break;
        }
                                                
        for(int i=0;i<poping;++i){
            estado.pop();
        }
        return irA;
    }
      
/////////////////////////////////////////////////////////////////////////////////////
    /**
     * Comprueba el fin de fichero e imprime la lista de reglas
     */
    public final void imprimeReglas(){
        //Nos creamos un string con las reglas
        String aux=reglas.pop().toString();
        while(!reglas.empty()){
            aux+=" "+reglas.pop().toString();
        }
        System.out.println(aux);//Lo imprimimos
    }
    
    public final void imprimeEstado(){
        //Nos creamos un string con las reglas
        Stack<Integer> meh= new Stack<>();
        int actual=estado.pop();
        meh.push(actual);
        String aux=t.lexema+" "+actual;
        //Generamos el string
        while(!estado.empty()){
            actual=estado.pop();
            meh.push(actual);
            aux+=" "+actual;
        }
        //Devolvemos esatdo a su estado anterior
        while(!meh.empty()){
            estado.push(meh.pop());
        }
        System.out.println(aux);//Lo imprimimos
    }

    /**
     * Imprime el error para el estado pasado por parámetro
     * @param estado 
     */
    public final void printErrorEstado(int estado){
        switch(estado){
            case 0:
                errorSintaxis(Token.CLASS);
                break;
            case 1:
                errorSintaxis(Token.EOF);
                break;
            case 2:
                errorSintaxis(Token.EOF);
                break;
            case 3:
                errorSintaxis(Token.ID);
                break;
            case 4:
                errorSintaxis(Token.LLAVEI);
                break;
            case 5:
                errorSintaxis(Token.LLAVED,Token.PUBLIC,Token.PRIVATE);
                break;
            case 6:
                errorSintaxis(Token.DOSP);
                break;
            case 7:
                errorSintaxis(Token.LLAVED,Token.PRIVATE);
                break;
            case 8:
                errorSintaxis(Token.LLAVED);
                break;
            case 9:
                errorSintaxis(Token.LLAVED,Token.CLASS,Token.PRIVATE,Token.FLOAT, Token.INT, Token.EOF);
                break;
            case 10:
                errorSintaxis(Token.DOSP);
                break;
            case 11:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 12:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 13:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 14:
                errorSintaxis(Token.ID);
                break;
            case 15:
                errorSintaxis(Token.ID);
                break;
            case 16:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 17:
                errorSintaxis(Token.LLAVED, Token.PRIVATE);
                break;
            case 18:
                errorSintaxis(Token.ID);
                break;
            case 19:
                errorSintaxis(Token.LLAVED);
                break;
            case 20:
                errorSintaxis(Token.PARI);
                break;
            case 21:
                errorSintaxis(Token.FLOAT, Token.INT);
                break;
            case 22:
                errorSintaxis(Token.ID);
                break;
            case 23:
                errorSintaxis(Token.PARD, Token.COMA);
                break;
            case 24:
                errorSintaxis(Token.FLOAT, Token.INT);
                break;
            case 25:
                errorSintaxis(Token.ID);
                break;
            case 26:
                errorSintaxis(Token.PARD, Token.COMA);
                break;
            case 27:
                errorSintaxis(Token.PARD);
                break;
            case 28:
                errorSintaxis(Token.PARD);
                break;
            case 29:
                errorSintaxis(Token.PYC, Token.LLAVEI);
                break;
            case 30:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 31:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 32:
                errorSintaxis(Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 33:
                errorSintaxis(Token.LLAVEI, Token.LLAVED, Token.ID);
                break;
            case 34:
                errorSintaxis(Token.PYC);
                break;
            case 35:
                errorSintaxis(Token.LLAVED);
                break;
            case 36:
                errorSintaxis(Token.PYC,Token.LLAVED, Token.CLASS,Token.PRIVATE, Token.FLOAT, Token.INT);
                break;
            case 37:
                errorSintaxis(Token.PYC);
                break;
            case 38:
                errorSintaxis(Token.LLAVEI, Token.LLAVED, Token.ID);
                break;
            case 39:
                errorSintaxis(Token.LLAVED);
                break;
            case 40:
                errorSintaxis(Token.ASIG);
                break;
            case 41:
                errorSintaxis(Token.ENTERO, Token.ID, Token.REAL);
                break;
            case 42:
                errorSintaxis(Token.PYC);
                break;
            case 43:
                errorSintaxis(Token.PYC);
                break;
            case 44:
                errorSintaxis(Token.PYC);
                break;
            case 45:
                errorSintaxis(Token.PYC);
                break;
            case 46:
                errorSintaxis(Token.LLAVED, Token.PRIVATE);
                break;
        }
    }
    
    public final void errorSintaxis(int ... args){
        if(t.tipo==Token.EOF){
            System.err.print("Error sintactico: encontrado fin de fichero, esperaba");
        }
        else{
            System.err.print("Error sintactico ("+t.fila+","+t.columna+"): encontrado '"+t.lexema+"', esperaba");
        }
        for(int i=0;i<args.length;++i){
            t.tipo=args[i];
            System.err.print(" "+ t.toString());
        }
        System.err.print(" \n");
        
        System.exit(-1);
    }
    
}
