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
public class AnalizadorSintacticoDR {
    private final boolean flag;
    private final StringBuilder reglas;
    private final AnalizadorLexico al;
    private Token t;
    
    /**
     * Inicializamos el analizador Sintáctico con el analizador léxico
     * @param al 
     */
    AnalizadorSintacticoDR(AnalizadorLexico al){
        reglas= new StringBuilder();
        flag=true;//Ponemos el flag de impresion de estados a true
        this.al=al;//Inicializamos el analizador léxico
        t=al.siguienteToken();//inicializamos el token
    }
    
    /**
     * Añade la regla num a la lista de reglas e imprime dicha lista con todas las reglas ejecutadas hasta el momento.
     * @param num 
     */
    private void addRegla(int num){
        reglas.append(" ").append(num);
        /*if(flag){
            System.out.println(reglas);
        }*/
    }
    ///////////////////////////////////////////////////////
    // Reglas
    ///////////////////////////////////////////////////////
    
    
    /**
     * Regla 1
     */
    public final void S(){
        if (t.tipo == Token.CLASS || t.tipo==Token.EOF) {
            addRegla(1);
            C();
        }
        else errorSintaxis(Token.CLASS, Token.EOF);
    }
    
    /**
     * Regla 2
     */
    public final void C(){
        if (t.tipo == Token.CLASS ) {
            addRegla(2);
            pair(Token.CLASS);
            pair(Token.ID);
            pair(Token.LLAVEI);
            B();
            V();
            pair(Token.LLAVED);
        }
        else errorSintaxis(Token.CLASS);
    }
    
    /**
     * Reglas 3 y 4
     */
    public final void B(){
        if (t.tipo == Token.PUBLIC) {
            addRegla(3);
            pair(Token.PUBLIC);
            pair(Token.DOSP);
            P();
        }
        else if (t.tipo==Token.PRIVATE || t.tipo==Token.LLAVED){
            //EPSILON
            addRegla(4);
        }
        else errorSintaxis(Token.LLAVED,Token.PUBLIC,Token.PRIVATE);
    }
    
    /**
     * Reglas 5 y 6
     */
    public final void V(){
        if (t.tipo == Token.PRIVATE) {
            addRegla(5);
            pair(Token.PRIVATE);
            pair(Token.DOSP);
            P();
        }
        else if(t.tipo==Token.LLAVED){
            //EPSILON
            addRegla(6);
        }
        else errorSintaxis(Token.LLAVED, Token.PRIVATE);
    }
    
    /**
     * Reglas 7 y 8
     */
    public final void P(){
        if (t.tipo == Token.CLASS || t.tipo==Token.INT || t.tipo==Token.FLOAT) {
            addRegla(7);
            D();
            P();
        }
        else if(t.tipo==Token.PRIVATE || t.tipo==Token.LLAVED){
            //EPSILON
            addRegla(8);
        }
        else errorSintaxis(Token.CLASS,Token.LLAVED,Token.PRIVATE,Token.INT,Token.FLOAT);
    }
    
    /**
     * Reglas 9 y 10
     */
    public final void D(){
        if (t.tipo == Token.INT || t.tipo==Token.FLOAT) {
            addRegla(9);
            Tipo();
            pair(Token.ID);
            pair(Token.PARI);
            Tipo();
            pair(Token.ID);
            L();
            pair(Token.PARD);
            Cod();
        }
        else if (t.tipo==Token.CLASS){
            addRegla(10);
            C();
        }
        else errorSintaxis(Token.CLASS,Token.INT,Token.FLOAT);
    }
    
    /**
     * Reglas 11 y 12
     */
    public final void Cod(){
        if (t.tipo == Token.PYC) {
            addRegla(11);
            pair(Token.PYC);
        }
        else if(t.tipo==Token.LLAVEI){
            addRegla(12);
            Bloque();
        }
        else errorSintaxis(Token.LLAVEI,Token.PYC);
    }
    
    /**
     * Reglas 13 y 14
     */
    public final void L(){
        if (t.tipo == Token.COMA) {
            addRegla(13);
            pair(Token.COMA);
            Tipo();
            pair(Token.ID);
            L();
        }
        else if(t.tipo==Token.PARD){
            //EPSILON
            addRegla(14);
        }
        else errorSintaxis(Token.PARD,Token.COMA);
    }
    
    /**
     * Reglas 15 y 16
     */
    public final void Tipo(){
        if (t.tipo == Token.INT) {
            addRegla(15);
            pair(Token.INT);
        }
        else if(t.tipo==Token.FLOAT){
            addRegla(16);
            pair(Token.FLOAT);
        }
        else errorSintaxis(Token.INT,Token.FLOAT);
    }
    
    /**
     * Regla 17
     */
    public final void Bloque(){
        if (t.tipo == Token.LLAVEI) {
            addRegla(17);
            pair(Token.LLAVEI);
            SecInstr();
            pair(Token.LLAVED);
        }
        else errorSintaxis(Token.LLAVEI);
    }
    
    /**
     * Reglas 18 y 19
     */
    public final void SecInstr(){
        if (t.tipo == Token.INT || t.tipo==Token.FLOAT || t.tipo==Token.ID || t.tipo==Token.LLAVEI || t.tipo==Token.RETURN){
            addRegla(18);
            Instr();
            pair(Token.PYC);
            SecInstr();
        }
        else if(t.tipo==Token.LLAVED){
            //EPSILON
            addRegla(19);
        }
        else errorSintaxis(Token.ID, Token.LLAVEI, Token.LLAVED, Token.INT, Token.FLOAT, Token.RETURN);
    }
    
    /**
     * Reglas 20, 21, 22 y 23
     */
    public final void Instr(){
        if (t.tipo == Token.INT || t.tipo==Token.FLOAT) {
            addRegla(20);
            Tipo();
            pair(Token.ID);
        }
        else if(t.tipo==Token.ID){
            addRegla(21);
            pair(Token.ID);
            pair(Token.ASIG);
            Expr();
        }
        else if(t.tipo==Token.LLAVEI){
            addRegla(22);
            Bloque();
        }
        else if(t.tipo==Token.RETURN){
            addRegla(23);
            pair(Token.RETURN);
            Expr();
        }
        else errorSintaxis(Token.ID, Token.LLAVEI, Token.INT, Token.FLOAT, Token.RETURN);
    }
    
    /**
     * Regla 24
     */
    public final void Expr(){
        if (t.tipo == Token.REAL || t.tipo==Token.ENTERO || t.tipo==Token.ID || t.tipo==Token.PARI) {
            addRegla(24);
            Term();
            ExprP();
        }
        else errorSintaxis(Token.ID, Token.PARI, Token.REAL, Token.ENTERO);
    }
    
    /**
     * Reglas 25 y 26
     */
    public final void ExprP(){
        if (t.tipo == Token.ADDOP) {
            addRegla(25);
            pair(Token.ADDOP);
            Term();
            ExprP();
        }
        else if(t.tipo==Token.PYC || t.tipo==Token.PARD){
            addRegla(26);
            //EPSILON
        }
        else errorSintaxis(Token.PARD, Token.PYC, Token.ADDOP);
    }
    
    /**
     * Regla 27
     */
    public final void Term(){
        if (t.tipo == Token.REAL || t.tipo==Token.ENTERO || t.tipo==Token.ID || t.tipo==Token.PARI) {
            addRegla(27);
            Factor();
            TermP();
        }
        else errorSintaxis(Token.ID, Token.PARI, Token.REAL, Token.ENTERO);
    }
    
    /**
     * Reglas 28 y 29
     */
    public final void TermP(){
        if (t.tipo == Token.MULOP) {
            addRegla(28);
            pair(Token.MULOP);
            Factor();
            TermP();
        }
        else if(t.tipo==Token.ADDOP ||t.tipo==Token.PYC || t.tipo==Token.PARD){
            //EPSILON
            addRegla(29);
        }
        else errorSintaxis(Token.PARD, Token.PYC, Token.ADDOP, Token.MULOP);
    }
    
    /**
     * Reglas 30, 31, 32 y 33
     */
    public final void Factor(){
        if (t.tipo == Token.REAL) {
            addRegla(30);
            pair(Token.REAL);
        }
        else if (t.tipo == Token.ENTERO) {
            addRegla(31);
            pair(Token.ENTERO);
        }
        else if (t.tipo == Token.ID) {
            addRegla(32);
            pair(Token.ID);
        }
        else if (t.tipo == Token.PARI) {
            addRegla(33);
            pair(Token.PARI);
            Expr();
            pair(Token.PARD);
        }
        else errorSintaxis(Token.ID, Token.PARI, Token.REAL, Token.ENTERO);
    }
    
/////////////////////////////////////////////////////////////////////////////////////
    public final void pair(int tokEsperado){
        if (t.tipo == tokEsperado)
            t = al.siguienteToken();
        else
            errorSintaxis(tokEsperado);
    }

    /**
     * Comprueba el fin de fichero e imprime la lista de reglas
     */
    public final void comprobarFinFichero(){
        if (t.tipo != Token.EOF)
            errorSintaxis(Token.EOF);
        if(flag){
            System.out.println(reglas);
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
