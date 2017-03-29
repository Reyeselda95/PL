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
        PYC = 2,
        DOSP = 3,
        COMA = 4,
        ASIG = 5,
        LLAVEI = 6,
        LLAVED = 7,
        CLASS = 8,
        PUBLIC = 9,
        PRIVATE = 10,
        FLOAT = 11,
        INT = 12,
        ENTERO = 13,
        ID = 14,
        REAL = 15,
        EOF = 16;
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
                res= ";";
                break;
            case 3:
                res= ":";
                break;
            case 4:
                res= ",";
                break;
            case 5:
                res= "=";
                break;
            case 6:
                res= "{";
                break;
            case 7:
                res= "}";
                break;
            case 8:
                res= "'class'";
                break;
            case 9:
                res= "'public'";
                break;
            case 10:
                res= "'private'";
                break;
            case 11:
                res= "'float'";
                break;
            case 12:
                res= "'int'";
                break;
            case 13:
                res= "numero entero";
                break;
            case 14:
                res= "identificador";
                break;
            case 15:
                res= "numero real";
                break;
            case 16:
                res= "fin de fichero";
                break;
        }
        return res;
    }
}
