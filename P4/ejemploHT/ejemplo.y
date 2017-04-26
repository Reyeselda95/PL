/*------------------------------ ejemplo.y -------------------------------*/
%token print id 
%token opas opmd
%token numentero numreal pari pard
%token pyc coma

%{

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <iostream>


using namespace std;

#include "comun.h"

// variables y funciones del A. Léxico
extern int ncol,nlin,findefichero;


extern int yylex();
extern char *yytext;
extern FILE *yyin;


int yyerror(char *s);


const int ENTERO=1;
const int REAL=2;

string operador, s1, s2;  // string auxiliares

%}

%%


S    : print SExp pyc    { /* comprobar que después del programa no hay ningún token más */
                           int tk = yylex();
                           if (tk != 0) yyerror("");
			 }
     ;


SExp : SExp coma Exp     { cout << $3.cod << endl; }
     | Exp               { cout << $1.cod << endl; }
     ;


Exp : Exp opas Factor    { if (!strcmp($2.lexema,"+"))
                                  operador = "sum";
                           else
                                  operador = "res";
                           if ($1.tipo != $3.tipo)
                           {
                                 if ($1.tipo == ENTERO)
                                    s1 = "itor(" + $1.cod + ")";
                                 else
                                    s1 = $1.cod;
                                 if ($3.tipo == ENTERO)
                                    s2 = "itor(" + $3.cod + ")";
                                 else
                                    s2 = $3.cod;
                                 operador +="r";
                                 $$.tipo = REAL;
                                 $$.cod = operador + "(" + s1 + "," + s2 + ")";
                           }
                           else
                           {
                                 s1 = $1.cod;
                                 s2 = $3.cod;
                                 if ($1.tipo == REAL) 
                                    operador += "r";
                                 else
                                    operador += "i";
                                 $$.tipo = $1.tipo;
                                 $$.cod = operador + "(" + s1 + "," + s2 + ")";
                           }
                         }
        | Factor         /* $$ = $1 */
        ;

Factor : numentero           { $$.tipo = ENTERO;
                               $$.cod = $1.lexema;
                             }
       | numreal             { $$.tipo = REAL;
                               $$.cod = $1.lexema;
                             }
       | pari Exp pard       { $$.tipo = $2.tipo;
                               $$.cod = $2.cod;
                             }
       | id                  { $$.tipo  = ENTERO; // todas las variables son enteras
                               $$.cod = $1.lexema;
                             }
       ;

%%

void msgError(int nerror,int nlin,int ncol,const char *s)
{
     switch (nerror) {
         case ERRLEXICO: fprintf(stderr,"Error lexico (%d,%d): caracter '%s' incorrecto\n",nlin,ncol,s);
            break;
         case ERRSINT: fprintf(stderr,"Error sintactico (%d,%d): en '%s'\n",nlin,ncol,s);
            break;
         case ERREOF: fprintf(stderr,"Error sintactico: fin de fichero inesperado\n");
            break;
         case ERRLEXEOF: fprintf(stderr,"Error lexico: fin de fichero inesperado\n");
            break;
     }
        
     exit(1);
}


int yyerror(char *s)
{
    if (findefichero) 
    {
       msgError(ERREOF,0,0,"");
    }
    else
    {  
       msgError(ERRSINT,nlin,ncol-strlen(yytext),yytext);
    }
}

int main(int argc,char *argv[])
{
    FILE *fent;

    if (argc==2)
    {
        fent = fopen(argv[1],"rt");
        if (fent)
        {
            yyin = fent;
            yyparse();
            fclose(fent);
        }
        else
            fprintf(stderr,"No puedo abrir el fichero\n");
    }
    else
        fprintf(stderr,"Uso: ejemplo <nombre de fichero>\n");
}
