/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ara65
 */
public class Token {

    public int fila;
    public int columna;
    public String lexema;

    public int tipo;    // tipo es: ID, ENTERO, REAL ...

    public static final int 
        PARI = 1,
        PARD = 2,
        MULOP = 3,
        ADOP = 4,
        PYC = 5,
        DOSP = 6,
        COMA = 7,
        ASIG = 8,
        LLAVEI = 9,
        LLAVED = 10,
        CLASS = 11,
        PUBLIC = 12,
        PRIVATE = 13,
        FLOAT = 14,
        INT = 15,
        RETURN = 16,
        ENTERO = 17,
        ID = 18,
        REAL = 19,
        EOF = 20;
    /**
     * Default Constructor
     */
    public Token(){}
    
    /**
     * Constructor with parameters
     * @param fila
     * @param columna
     * @param lexema
     * @param tipo 
     */
    public Token(int fila, int columna, String lexema, int tipo) {
        this.fila = fila;
        this.columna = columna;
        this.lexema = lexema;
        this.tipo = tipo;
    }
    
    public String toString(){
        String res="";
        switch(this.tipo){
            case 1:
                res= "(";
            case 2:
                res= ")";
            case 3:
                res= "*/";
            case 4:
                res= "+-";
            case 5:
                res= ";";
            case 6:
                res= ":";
            case 7:
                res= ",";
            case 8:
                res= "=";
            case 9:
                res= "{";
            case 10:
                res= "}";
            case 11:
                res= "'class'";
            case 12:
                res= "'public'";
            case 13:
                res= "'private'";
            case 14:
                res= "'float'";
            case 15:
                res= "'int'";
            case 16:
                res= "'res='";
            case 17:
                res= "numero entero";
            case 18:
                res= "identificador";
            case 19:
                res= "numero real";
        }
        return res;
    }
}
