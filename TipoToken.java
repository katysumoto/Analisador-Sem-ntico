package interpretador;

public enum TipoToken {
	ID,					// identificador
	INT_CONST,			// constante inteira
	CHAR_CONST,			// constante char
	FLOAT_CONST,		// constante float
	DOUBLE_CONST,		// constante double
	TIPO_DADO,			// tipo de dado
	OPERADOR_ARITMETICO,
	OPERADOR_RELACIONAL,
	OPERADOR_LOGICO,
	INICIO_ESCOPO,		// abre chaves
	FIM_ESCOPO,			// fecha chaves
	ABRE_PARENTESES,
	FECHA_PARENTESES,
	SEPARADOR_ARG,		// separador de argumentos(virgula)
	ATRIBUICAO,			// operador de atribuicao (=)
	PALAVRA_RESERVADA,	// palavras reservadas (if, for, switch, case, else, do, ...)
	FIM_COMANDO,		// ponto e virgula
	OPERADOR_INCDEC
}
