#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <unistd.h>
#include <stdbool.h>
#define MAXCHAR 512

char* str_concat(const char* str1, const char* str2);

char* str_concat(const char* str1, const char* str2) {
    char* result = (char*)malloc(sizeof(char) * MAXCHAR);
    if (result != NULL) {
        result[0] = '\0'; // Inizializza la stringa vuota
        strcat(result, str1);
        strcat(result, str2);
    }
    return result;
}