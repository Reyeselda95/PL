#!/bin/bash

MAXTIME=15 	    # Tiempo máximo de ejecución (en segundos) de una prueba
PRINCIPAL=plp1      # Nombre del ejecutable de la práctica
ENT=fuentes	    # Directorio con los ficheros de pruebas de entrada
SAL=salida-esperada # Directorio con los ficheros de salida correctos
OBT=salida-obtenida # Directorio con los ficheros obtenidos tras la ejecución de la práctica

mata=/tmp/mata
comparefiles=/tmp/comparefiles

# -------------- generar y compilar los ficheros auxiliares mata.c y comparefiles.cc -----
function genMataComparefiles() {

echo "#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <signal.h>
#include <unistd.h>

const int TAMMAXFICH=300000;
int f;int segundos,st;
int segejec=0;
char *nfsal=NULL;
void SeAcabo(int i){  
  fprintf(stderr,\"ERROR, tiempo de ejecución agotado... (%d segundos)\\n\",segundos);
  fflush(stderr); 
  kill(f,SIGKILL);  
  exit(-1); 
}
void SeAcaboFich(int i){  
  fprintf(stderr,\"ERROR, fichero de salida muy grande (cuelgue?)... \\n\");
  fflush(stderr); 
  kill(f,SIGKILL);  
  exit(-1); 
}
int FicheroGrande() {  
  struct stat s;  int ret=0;
  if (nfsal != NULL) {   
    stat(nfsal,&s);    
    if (s.st_size > TAMMAXFICH) ret = 1;  
  }  
  return ret;
}
void Control(int i){ 
  segejec++; 
  if (segejec >= segundos) SeAcabo(i); 
  else if (FicheroGrande()) SeAcaboFich(i); 
  else alarm(1); 
}
void Salir(int i){exit(1);}
int main(int argc,char *argv[]){ 
  if (argc < 4) exit(1);  
  segundos = atoi(argv[1]);  
  nfsal = argv[2]; 
  signal(SIGALRM,Control);  
  signal(SIGCHLD,Salir);  
  alarm(1);  
  if ((f = fork()) == 0) execvp(argv[3],&argv[3]);   
  else wait(&st);
}
" > $mata.c
gcc -o $mata $mata.c

echo "#include <iostream>
#include <fstream>
#include <stdlib.h>

using namespace std;

int main(int argc, char *argv[])
{
  int salida=0;

  if (argc!=3) cout << \"Sintaxis: \" << argv[0] << \" <obtenido.txt> <correcto.txt>\" << endl;
  else 
  {
    ifstream fo(argv[1]);
    ifstream fc(argv[2]);
    if (fo.is_open() && fc.is_open())
    {
      string so,sc,tmp;

      fo >> tmp;
      while (!fo.eof() || tmp.length()!=0)
      {
        so=so+tmp;
        tmp=\"\";
        fo >> tmp;
      }
      fc >> tmp;
      while (!fc.eof() || tmp.length()!=0)
      {
        sc=sc+tmp;
        tmp=\"\";
        fc >> tmp;
      }

      if (so!=sc) {
        exit(-1);
      }
    }
    else {
      cout << \"Fichero \" << argv[1] << \" o \" << argv[2] << \" no encontrado\" << endl;
      exit(-1);
    }
  }
  exit(0);
  return salida;
}
" > $comparefiles.cc

g++ -o $comparefiles $comparefiles.cc

}

echo "*********************************************************"
echo "Autocorrector plp1 "
echo " Generando ficheros auxiliares... "
genMataComparefiles

# Argumentos de programa para las pruebas
let npruebas=0
pok=0;

# Compilacion
echo "*********************************************************"
echo " Compilando..."
echo "*********************************************************"
rm -rf *.class
rm -rf $PRINCIPAL
javac $PRINCIPAL.java
if [ $? -ne 0 ]; then
	echo "LA PRACTICA NO COMPILA"

else

  # Ejecucion y comprobacion de la salida
  echo
  echo "*********************************************************"
  echo " Ejecutando y comprobando salida a la vez..."
  echo "*********************************************************"
  #chmod +x ./mata ./comparefiles

  for i in `ls $ENT`
  do
        bn=$(basename $i .txt)
	echo "Prueba: $bn" 

	# Ejecucion del programa
	$mata $MAXTIME $OBT/$bn.sal java $PRINCIPAL $ENT/$i > $OBT/$bn.sal 2> $OBT/$bn.err

	$comparefiles $OBT/$bn.sal $SAL/$bn.sal
	if [ $? -ne 0 ]; then
	    echo "--- Fallo en la traducción ----------"
	    diff -EwB $OBT/$bn.sal $SAL/$bn.sal 2>&1
	else 
	    $comparefiles  $OBT/${bn}.err $SAL/${bn}.err
	    if [ $? -ne 0 ]; then
	        echo "--- Fallo en los errores ----------"
	        diff -EwB -I "---" $OBT/${bn}.err $SAL/${bn}.err 2>&1
	    else
	        echo "OK" 
		let pok++
	    fi
	fi
	echo "--------------------------"
	let npruebas++
  done

  if [ $pok -eq $npruebas ]; then
	echo -e "\nTODAS LAS PRUEBAS DEL AUTOCORRECTOR FUNCIONAN\n"
  else 
	echo -e "\nOJO: FALLAN" $[npruebas-pok] "PRUEBAS DEL AUTOCORRECTOR\n"
  fi;
fi;

echo -e "\n\nAVISO IMPORTANTE: este autocorrector NO prueba todos los casos posibles\n"
