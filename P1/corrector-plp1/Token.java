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
public class Token {

    public int fila;
    public int columna;
    public String lexema;

    public int tipo;    // tipo es: ID, ENTERO, REAL ...

    public static final int 
        PARI = 0,
        PARD = 1,
        MULOP = 2,
        ADDOP = 3,
        PYC = 4,
        DOSP = 5,
        COMA = 6,
        ASIG = 7,
        LLAVEI = 8,
        LLAVED = 9,
        CLASS = 10,
        PUBLIC = 11,
        PRIVATE = 12,
        FLOAT = 13,
        INT = 14,
        RETURN = 15,
        ENTERO = 16,
        ID = 17,
        REAL = 18,
        EOF = 19;
    /**
     * Default Constructor
     */
    public Token(){
    this.lexema="";
    this.tipo=1;}
    
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
            case 0:
                res= "(";
                break;
            case 1:
                res= ")";
                break;
            case 2:
                res= "* /";
                break;
            case 3:
                res= "+ -";
                break;
            case 4:
                res= ";";
                break;
            case 5:
                res= ":";
                break;
            case 6:
                res= ",";
                break;
            case 7:
                res= "=";
                break;
            case 8:
                res= "{";
                break;
            case 9:
                res= "}";
                break;
            case 10:
                res= "'class'";
                break;
            case 11:
                res= "'public'";
                break;
            case 12:
                res= "'private'";
                break;
            case 13:
                res= "'float'";
                break;
            case 14:
                res= "'int'";
                break;
            case 15:
                res= "'return'";
                break;
            case 16:
                res= "numero entero";
                break;
            case 17:
                res= "identificador";
                break;
            case 18:
                res= "numero real";
                break;
        }
        return res;
    }
}
