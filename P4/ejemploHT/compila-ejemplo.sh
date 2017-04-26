#!/bin/bash

flex ejemplo.l
bison -d ejemplo.y
g++ -o ejemplo ejemplo.tab.c lex.yy.c
