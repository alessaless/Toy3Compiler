#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <unistd.h>
#include <stdbool.h>
#define MAXCHAR 512

char* str_concat(const char* str1, const char* str2);
char* integer_to_str(int i);
char* real_to_str(double i);
char* bool_to_str(bool i);

char* str_concat(const char* str1, const char* str2) {
    char* result = (char*)malloc(sizeof(char) * MAXCHAR);
    if (result != NULL) {
        result[0] = '\0'; // Inizializza la stringa vuota
        strcat(result, str1);
        strcat(result, str2);
    }
    return result;
}

char* integer_to_str(int i) {
    int length = snprintf(NULL, 0, "%d", i);
    char* result = (char*)malloc(length + 1);
    if (result != NULL) {
        snprintf(result, length + 1, "%d", i);
    }
    return result;
}

char* real_to_str(double i) {
    int length = snprintf(NULL, 0, "%lf", i);
    char* result = (char*)malloc(length + 1);
    if (result != NULL) {
        snprintf(result, length + 1, "%f", i);
    }
    return result;
}

char* bool_to_str(bool i) {
    int length = snprintf(NULL, 0, "%d", i);
    char* result = (char*)malloc(length + 1);
    if (result != NULL) {
        snprintf(result, length + 1, "%d", i);
    }
    return result;
}