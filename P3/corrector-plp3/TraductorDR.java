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
public class TraductorDR {

    private final boolean flag;
    private final StringBuilder reglas;
    private final AnalizadorLexico al;
    private Token t;

    /**
     * Inicializamos el analizador Sintáctico con el analizador léxico
     *
     * @param al
     */
    TraductorDR(AnalizadorLexico al) {
        reglas = new StringBuilder();
        flag = false;//Ponemos el flag de impresion de estados a true
        this.al = al;//Inicializamos el analizador léxico
        t = al.siguienteToken();//inicializamos el token
    }

    /**
     * Añade la regla num a la lista de reglas e imprime dicha lista con todas
     * las reglas ejecutadas hasta el momento.
     *
     * @param num
     */
    private void addRegla(int num) {
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
     *
     * @return
     */
    public final String S() {
        if (t.tipo == Token.CLASS || t.tipo == Token.EOF) {
            addRegla(1);
            return C("", "",null);
        } else {
            errorSintaxis(Token.CLASS, Token.EOF);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Regla 2
     *
     * @param vis
     * @param pre
     * @param tsa
     * @return
     */
    public final String C(String vis, String pre,TablaSimbolos tsa) {
        tsa= new TablaSimbolos(tsa);
        if (t.tipo == Token.CLASS) {
            addRegla(2);
            pair(Token.CLASS);
            String id = t.lexema;
            if(tsa.buscar(id)==null)
                tsa.anyadir(new Simbolo(id,Simbolo.CLASE, Simbolo.CLASE));
            else errorSemantico(ERRYADECL, t);
            pair(Token.ID);
            pair(Token.LLAVEI);
            String b = B(pre + id + "::",tsa);
            String v = V(pre + id + "::",tsa);
            pair(Token.LLAVED);
            return vis + "clase " + pre + id + "{\n" + b + "\n" + v + "\n}";
        } else {
            errorSintaxis(Token.CLASS);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 3 y 4
     *
     * @param pre
     * @param tsa
     * @return
     */
    public final String B(String pre,TablaSimbolos tsa) {
        if (t.tipo == Token.PUBLIC) {
            addRegla(3);
            pair(Token.PUBLIC);
            pair(Token.DOSP);
            return P("publico ", pre,tsa);
        } else if (t.tipo == Token.PRIVATE || t.tipo == Token.LLAVED) {
            //EPSILON
            addRegla(4);
        } else {
            errorSintaxis(Token.LLAVED, Token.PUBLIC, Token.PRIVATE);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 5 y 6
     *
     * @param pre
     * @param tsa
     * @return
     */
    public final String V(String pre,TablaSimbolos tsa) {
        if (t.tipo == Token.PRIVATE) {
            addRegla(5);
            pair(Token.PRIVATE);
            pair(Token.DOSP);
            return P("privado ", pre,tsa);
        } else if (t.tipo == Token.LLAVED) {
            //EPSILON
            addRegla(6);
        } else {
            errorSintaxis(Token.LLAVED, Token.PRIVATE);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 7 y 8
     *
     * @param vis
     * @param pre
     * @param tsa
     * @return
     */
    public final String P(String vis, String pre,TablaSimbolos tsa) {
        if (t.tipo == Token.CLASS || t.tipo == Token.INT || t.tipo == Token.FLOAT) {
            addRegla(7);
            String d = D(vis, pre,tsa);
            return d + "\n" + P(vis, pre,tsa);
        } else if (t.tipo == Token.PRIVATE || t.tipo == Token.LLAVED) {
            //EPSILON
            addRegla(8);
        } else {
            errorSintaxis(Token.CLASS, Token.LLAVED, Token.PRIVATE, Token.INT, Token.FLOAT);
        }
        return "";
    }

    /**
     * Reglas 9 y 10
     *
     * @param vis
     * @param pre
     * @param tsa
     * @return
     */
    public final String D(String vis, String pre,TablaSimbolos tsa) {
        if (t.tipo == Token.INT || t.tipo == Token.FLOAT) {
            addRegla(9);
            String t1 = Tipo();
            String id1 = t.lexema;
            switch(t1){
                case "entero":
                    if(tsa.buscar(id1)==null)
                        tsa.anyadir(new Simbolo(id1,Simbolo.FUNCION, Simbolo.ENTERO));
                    else errorSemantico(ERRYADECL, t);
                    break;
                case "real":
                    if(tsa.buscar(id1)==null)
                        tsa.anyadir(new Simbolo(id1,Simbolo.FUNCION, Simbolo.REAL));
                    else errorSemantico(ERRYADECL, t);
                    break;
            }
            pair(Token.ID);
            pair(Token.PARI);
            TablaSimbolos tsa2= new TablaSimbolos(tsa);
            String t2 = Tipo();
            String id2 = t.lexema;
            switch(t2){
                case "entero":
                    if(tsa2.buscar(id2)==null)
                        tsa2.anyadir(new Simbolo(id2,Simbolo.VARIABLE, Simbolo.ENTERO));
                    else errorSemantico(ERRYADECL, t);
                    break;
                case "real":
                    if(tsa2.buscar(id2)==null)
                        tsa2.anyadir(new Simbolo(id2,Simbolo.VARIABLE, Simbolo.REAL));
                    else errorSemantico(ERRYADECL, t);
                    break;
            }
            pair(Token.ID);
            String l = L(t1,tsa2);
            pair(Token.PARD);
            return vis + pre + id1 + " (" + id2 + ":" + t2 + l + " -> " + t1 + ")" + Cod(t1,tsa2);
        } else if (t.tipo == Token.CLASS) {
            addRegla(10);
            return C(vis, pre ,tsa);
        } else {
            errorSintaxis(Token.CLASS, Token.INT, Token.FLOAT);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 11 y 12
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final String Cod(String tipo,TablaSimbolos tsa) {
        if (t.tipo == Token.PYC) {
            addRegla(11);
            pair(Token.PYC);
            return ";";
        } else if (t.tipo == Token.LLAVEI) {
            addRegla(12);
            return Bloque(tipo,tsa,false);
        } else {
            errorSintaxis(Token.LLAVEI, Token.PYC);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 13 y 14
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final String L(String tipo,TablaSimbolos tsa) {
        if (t.tipo == Token.COMA) {
            addRegla(13);
            pair(Token.COMA);
            String t1 = Tipo();
            String id = t.lexema;
            switch(t1){
                case "entero":
                    if(tsa.buscar(id)==null)
                        tsa.anyadir(new Simbolo(id,Simbolo.VARIABLE, Simbolo.ENTERO));
                    else errorSemantico(ERRYADECL, t);
                    break;
                case "real":
                    if(tsa.buscar(id)==null)
                        tsa.anyadir(new Simbolo(id,Simbolo.VARIABLE, Simbolo.REAL));
                    else errorSemantico(ERRYADECL, t);
                    break;
            }
            pair(Token.ID);
            return " x " + id + ":" + t1 + L(tipo,tsa);
        } else if (t.tipo == Token.PARD) {
            //EPSILON
            addRegla(14);
        } else {
            errorSintaxis(Token.PARD, Token.COMA);
        }
        return "";
    }

    /**
     * Reglas 15 y 16
     *
     * @return
     */
    public final String Tipo() {
        if (t.tipo == Token.INT) {
            addRegla(15);
            pair(Token.INT);
            return "entero";
        } else if (t.tipo == Token.FLOAT) {
            addRegla(16);
            pair(Token.FLOAT);
            return "real";
        } else {
            errorSintaxis(Token.INT, Token.FLOAT);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Regla 17
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final String Bloque(String tipo,TablaSimbolos tsa,boolean mirar) {
        tsa = new TablaSimbolos(tsa);
        if (t.tipo == Token.LLAVEI) {
            addRegla(17);
            pair(Token.LLAVEI);
            String si = SecInstr(tipo,tsa,mirar);
            pair(Token.LLAVED);
            return "\n{\n" + si + "\n}";
        } else {
            errorSintaxis(Token.LLAVEI);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 18 y 19
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final String SecInstr(String tipo,TablaSimbolos tsa,boolean mirar) {
        if (t.tipo == Token.INT || t.tipo == Token.FLOAT || t.tipo == Token.ID || t.tipo == Token.LLAVEI || t.tipo == Token.RETURN) {
            addRegla(18);
            String i = Instr(tipo,tsa,mirar);
            pair(Token.PYC);
            return i + "\n" + SecInstr(tipo,tsa,mirar);
        } else if (t.tipo == Token.LLAVED) {
            //EPSILON
            addRegla(19);
        } else {
            errorSintaxis(Token.ID, Token.LLAVEI, Token.LLAVED, Token.INT, Token.FLOAT, Token.RETURN);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * Reglas 20, 21, 22 y 23
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final String Instr(String tipo,TablaSimbolos tsa,boolean mirar) {
        if (t.tipo == Token.INT || t.tipo == Token.FLOAT) {
            addRegla(20);
            String t1 = Tipo();
            String id = t.lexema;
            switch(t1){
                case "entero":
                    if(tsa.buscar(id)==null)
                        tsa.anyadir(new Simbolo(id,Simbolo.VARIABLE, Simbolo.ENTERO));
                    else if(tsa.padre.buscar(id)!=null && mirar) 
                        tsa.anyadir(new Simbolo(id,Simbolo.VARIABLE, Simbolo.ENTERO));
                    else errorSemantico(ERRYADECL, t);
                    break;
                case "real":
                    if(tsa.buscar(id)==null)
                        tsa.anyadir(new Simbolo(id,Simbolo.VARIABLE, Simbolo.REAL));
                    else if(tsa.padre.buscar(id)!=null) 
                        tsa.anyadir(new Simbolo(id,Simbolo.VARIABLE, Simbolo.REAL));
                    else errorSemantico(ERRYADECL, t);
                    break;
            }
            pair(Token.ID);
            return "var " + id + ":" + t1+";";
        } else if (t.tipo == Token.ID) {
            addRegla(21);
            String id = t.lexema;
            Simbolo a= tsa.buscar(id);
            if(a==null)//Si no está no lo puedo usar
                errorSemantico(ERRNODECL, t);
            if(a.tipoSimbolo!=Simbolo.VARIABLE)
                errorSemantico(ERRNOVAR, t);
            pair(Token.ID);
            pair(Token.ASIG);
            Atributos expr= Expr(tsa);
            switch(a.tipo){
                case Simbolo.ENTERO:
                    if(expr.tipo.equals("entero"))
                        return id + " := " + expr.trad+";";
                    else errorSemantico(ERRTIPOS, t);
                case Simbolo.REAL:
                    if(expr.tipo.equals("real"))
                        return id + " := " + expr.trad+";";
                    else 
                        return id + " := itor(" + expr.trad+");";
            }
        } else if (t.tipo == Token.LLAVEI) {
            addRegla(22);
            return Bloque(tipo,tsa,true);
        } else if (t.tipo == Token.RETURN) {
            addRegla(23);
            Token ret=t;
            pair(Token.RETURN);
            Atributos ex = Expr(tsa);//REVISAR
            if (ex.tipo.equals(tipo)) {
                return "retorna " + ex.trad+";";
            } else if (ex.tipo.equals("entero") && tipo.equals("real")) {
                return "retorna itor(" + ex.trad + ");";
            } else {
                errorSemantico(ERRTIPOS, ret);//Salta error y finaliza el programa
            }
        } else {
            errorSintaxis(Token.ID, Token.LLAVEI, Token.INT, Token.FLOAT, Token.RETURN);
        }
        return ""; //Nunca debería llegar aqui
    }

    /**
     * @param tsa
     * Regla 24
     *
     * @return
     */
    public final Atributos Expr(TablaSimbolos tsa) {
        if (t.tipo == Token.REAL || t.tipo == Token.ENTERO || t.tipo == Token.ID || t.tipo == Token.PARI) {
            addRegla(24);
            Atributos term = Term(tsa);
            Atributos expp = ExprP(term.tipo,tsa);
            if (term.tipo.equals(expp.tipo) || expp.tipo.equals("")) {
                return new Atributos(term.tipo, term.trad + " " + expp.trad);
            } else if (term.tipo.equals("entero") && expp.tipo.equals("real")) {
                return new Atributos("real", "itor(" + term.trad + ") " + expp.trad);
            } else {
                errorSemantico(ERRTIPOS, t);
            }
        } else {
            errorSintaxis(Token.ID, Token.PARI, Token.REAL, Token.ENTERO);
        }
        return new Atributos("", ""); //Nunca debería llegar aqui
    }

    /**
     * Reglas 25 y 26
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final Atributos ExprP(String tipo,TablaSimbolos tsa) {
        if (t.tipo == Token.ADDOP) {
            addRegla(25);
            String add = t.lexema;
            pair(Token.ADDOP);
            Atributos term = Term(tsa);
            Atributos expp = ExprP(tipo,tsa);
            if (term.tipo.equals(tipo)) {
                if (term.tipo.equals("entero")) {
                    return new Atributos(term.tipo, add + "i " + term.trad + " " + expp.trad);
                } else {
                    return new Atributos(term.tipo, add + "r " + term.trad + " " + expp.trad);
                }
            } else if (term.tipo.equals("entero") && tipo.equals("real")) {
                return new Atributos(tipo, add + "r itor(" + term.trad + ") " + expp.trad);
            } else if (tipo.equals("entero")&& term.tipo.equals("real")){
                return new Atributos(term.tipo, add + "r " + term.trad + expp.trad);
            } else {
                errorSemantico(ERRTIPOS, t);
            }
        } else if (t.tipo == Token.PYC || t.tipo == Token.PARD) {
            addRegla(26);
            //EPSILON
        } else {
            errorSintaxis(Token.PARD, Token.PYC, Token.ADDOP);
        }
        return new Atributos("", "");
    }

    /**
     * Regla 27
     *
     * @param tsa
     * @return
     */
    public final Atributos Term(TablaSimbolos tsa) {
        if (t.tipo == Token.REAL || t.tipo == Token.ENTERO || t.tipo == Token.ID || t.tipo == Token.PARI) {
            addRegla(27);
            Atributos f = Factor(tsa);
            Atributos termp = TermP(f.tipo,tsa);
            if (f.tipo.equals(termp.tipo) || termp.tipo.equals("")) {
                return new Atributos(f.tipo, f.trad + " " + termp.trad);
            } else if (f.tipo.equals("entero") && termp.tipo.equals("real")) {
                return new Atributos("real", "itor(" + f.trad + ") " + termp.trad);
            } else {
                errorSemantico(ERRTIPOS, t);
            }
        } else {
            errorSintaxis(Token.ID, Token.PARI, Token.REAL, Token.ENTERO);
        }
        return new Atributos("", ""); //Nunca debería llegar aqui
    }

    /**
     * Reglas 28 y 29
     *
     * @param tipo
     * @param tsa
     * @return
     */
    public final Atributos TermP(String tipo,TablaSimbolos tsa) {
        if (t.tipo == Token.MULOP) {
            addRegla(28);
            String mulop = t.lexema;
            pair(Token.MULOP);
            Atributos f = Factor(tsa);
            Atributos termp = TermP(tipo,tsa);
            if (f.tipo.equals(tipo)) {
                if (f.tipo.equals("entero")) {
                    return new Atributos(f.tipo, mulop + "i " + f.trad + " " + termp.trad);
                } else {
                    return new Atributos(f.tipo, mulop + "r " + f.trad + " " + termp.trad);
                }
            } else if (f.tipo.equals("entero") && tipo.equals("real")) {
                return new Atributos(tipo, mulop + "r itor(" + f.trad + ") " + termp.trad);
            } else if (tipo.equals("entero")&& f.tipo.equals("real")){
                return new Atributos(f.tipo, mulop + "r " + f.trad + termp.trad);
            } else {
                errorSemantico(ERRTIPOS, t);
            }
        } else if (t.tipo == Token.ADDOP || t.tipo == Token.PYC || t.tipo == Token.PARD) {
            //EPSILON
            addRegla(29);
        } else {
            errorSintaxis(Token.PARD, Token.PYC, Token.ADDOP, Token.MULOP);
        }
        return new Atributos("", "");
    }

    /**
     * Reglas 30, 31, 32 y 33
     * @param tsa
     * @return 
     */
    public final Atributos Factor(TablaSimbolos tsa) {
        if (t.tipo == Token.REAL) {
            addRegla(30);
            String ret = t.lexema;
            pair(Token.REAL);
            return new Atributos("real", ret);
        } else if (t.tipo == Token.ENTERO) {
            addRegla(31);
            String ret = t.lexema;
            pair(Token.ENTERO);
            return new Atributos("entero", ret);
        } else if (t.tipo == Token.ID) {
            addRegla(32);
            String ret = t.lexema;
            Simbolo s= tsa.buscar(ret);
            if(s==null) //Si no se encuentra se lanzaría un error
                errorSemantico(ERRNOVAR, t);
            if(s.tipoSimbolo!=Simbolo.VARIABLE)
                errorSemantico(ERRNOVAR, t);
            pair(Token.ID);
            //Aqui obtenemos el tipo del id y lo devolvemos o, si no está declarado lanzamos un error
            switch(s.tipo){
                case Simbolo.ENTERO:
                    return new Atributos("entero", s.nombre);
                case Simbolo.REAL:
                    return new Atributos("real", s.nombre);
            }
        } else if (t.tipo == Token.PARI) {
            addRegla(33);
            pair(Token.PARI);
            Atributos expr = Expr(tsa);
            pair(Token.PARD);
            return new Atributos(expr.tipo, "(" + expr.trad + ")");
        } else {
            errorSintaxis(Token.ID, Token.PARI, Token.REAL, Token.ENTERO);
        }
        return new Atributos("", ""); //Nunca debería llegar aqui
    }

/////////////////////////////////////////////////////////////////////////////////////
    public final void pair(int tokEsperado) {
        if (t.tipo == tokEsperado) {
            t = al.siguienteToken();
        } else {
            errorSintaxis(tokEsperado);
        }
    }

    /**
     * Comprueba el fin de fichero e imprime la lista de reglas
     */
    public final void comprobarFinFichero() {
        if (t.tipo != Token.EOF) {
            errorSintaxis(Token.EOF);
        }
        if (flag) {
            System.out.println(reglas);
        }
    }

    public final void errorSintaxis(int... args) {
        if (t.tipo == Token.EOF) {
            System.err.print("Error sintactico: encontrado fin de fichero, esperaba");
        } else {
            System.err.print("Error sintactico (" + t.fila + "," + t.columna + "): encontrado '" + t.lexema + "', esperaba");
        }
        for (int i = 0; i < args.length; ++i) {
            t.tipo = args[i];
            System.err.print(" " + t.toString());
        }
        System.err.print(" \n");

        System.exit(-1);
    }

    private final int ERRYADECL = 1, ERRNODECL = 3, ERRNOVAR = 4, ERRTIPOS = 5;

    private void errorSemantico(int nerror, Token tok) {
        System.err.print("Error semantico (" + tok.fila + "," + tok.columna + "): '" + tok.lexema + "' ");
        switch (nerror) {
            case ERRYADECL:
                System.err.println("ya existe en este ambito");
                break;
            case ERRNODECL:
                System.err.println("no ha sido declarado");
                break;
            case ERRNOVAR:
                System.err.println("no es una variable");
                break;
            case ERRTIPOS:
                System.err.println("tipos incompatibles entero/real");
                break;
        }
        System.exit(-1);
    }

}

