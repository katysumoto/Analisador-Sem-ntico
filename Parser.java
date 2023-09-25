package interpretador;

import java.io.FileReader;
import java.io.IOException;

public class Parser {
	
	private Lexer lexer;
	private Yytoken lookAhead;
	private AnalisadorSemantico analisadorSemantico;
	
	public Parser(String nomeArquivo)
	{
		try{
			// Inicializando o lexer
			lexer = new Lexer(new FileReader(nomeArquivo));
			// Ler o primeiro Token
			lookAhead = lexer.yylex();
			// Inicializando o analisador Semântico
			analisadorSemantico = new AnalisadorSemantico();
			
		}catch(IOException ex)
		{
			System.out.println("Erro ao abrir o arquivo!");
		}
	}
	
	private void match(TipoToken esperado)
	{
		try{
			// Se o que eu li (lookAhead) for o que estou esperando...
			if(lookAhead.getTipo() == esperado)
				// continue, leia o próximo Token.
				lookAhead = lexer.yylex();
			else
			{
				/// Caso contrário, erro sintático
				System.out.println("Erro sintático, esperado: "+esperado+",lido: "+lookAhead.getTipo());
				// encerra o programa
				System.exit(0);
			}
		}catch(IOException ex)
		{
			System.out.println("Erro ao ler o arquivo!");
		}
		
		// Análise da grámatica da linguagem
		
	}
	
	public void programa()
	{
		// int
		match(TipoToken.TIPO_DADO);
		// main
		match(TipoToken.ID);
		// ()
		match(TipoToken.ABRE_PARENTESES);
		match(TipoToken.FECHA_PARENTESES);
		// {
		match(TipoToken.INICIO_ESCOPO);
		
		// Não terminal
		corpo();
		// }
		match(TipoToken.FIM_ESCOPO);
		
	
	}
	
	private void corpo()
	{
		declaracao();
		corpoCMD();
	}
	
	private void declaracao()
	{
		if(lookAhead.getTipo() == TipoToken.TIPO_DADO)
		{
			// salvo o tipo, antes do método "match"
			String tipo = lookAhead.getLexema();
			match(TipoToken.TIPO_DADO);
			
			// A variável não existe?
			if(!analisadorSemantico.checarVariavelDeclarada(lookAhead.getLexema()))
				// Adicionar na tabela!
				analisadorSemantico.adicionaVariavel(lookAhead.getLexema(), tipo);
			// Adicionar na tabela
			else
			{
				System.out.println("Variavel previamente declarada!!!! " + lookAhead.getLexema());
				System.exit(0);
			}
			match(TipoToken.ID);
			listaVar(tipo);
		}
	}
	
	private void corpoCMD()
	{
		if(lookAhead.getTipo() == TipoToken.ID)
		{
			//Iniciando analise de tipo
			String tipo = analisadorSemantico.retornaTipoVariavel(lookAhead.getLexema());
			//Resetar o tipo avaliado
			analisadorSemantico.setTipoAvaliado(0);
			
			match(TipoToken.ID);
			match(TipoToken.ATRIBUICAO);
			expressao();
			
			//Verificar se ocorreu perda de precisão
			analisadorSemantico.verificaTipoFinal(tipo);
			
			match(TipoToken.FIM_COMANDO);
			corpoCMD();
		}
		else if(lookAhead.getTipo() == TipoToken.PALAVRA_RESERVADA)
		{
			comandoBloco();
			corpoCMD();
			
		}
	}
	
	private void comandoBloco()
	{
		if(lookAhead.getTipo() == TipoToken.PALAVRA_RESERVADA && lookAhead.getLexema().equals("if"))
		{
			match(TipoToken.PALAVRA_RESERVADA);
			match(TipoToken.ABRE_PARENTESES);
			condicao();
			match(TipoToken.FECHA_PARENTESES);
			match(TipoToken.INICIO_ESCOPO);
			corpoCMD();
			match(TipoToken.FIM_ESCOPO);
			_else();	
		}
		else if(lookAhead.getTipo() == TipoToken.PALAVRA_RESERVADA && lookAhead.getLexema().equals("while"))
		{
			match(TipoToken.PALAVRA_RESERVADA);
			match(TipoToken.ABRE_PARENTESES);
			condicao();
			match(TipoToken.FECHA_PARENTESES);
			match(TipoToken.INICIO_ESCOPO);
			corpoCMD();
			match(TipoToken.FIM_ESCOPO);	
		}
		else if(lookAhead.getTipo() == TipoToken.PALAVRA_RESERVADA && lookAhead.getLexema().equals("do"))
		{
			match(TipoToken.PALAVRA_RESERVADA);
			match(TipoToken.INICIO_ESCOPO);
			corpoCMD();
			match(TipoToken.FIM_ESCOPO);
			match(TipoToken.PALAVRA_RESERVADA);
			match(TipoToken.ABRE_PARENTESES);
			condicao();
			match(TipoToken.FECHA_PARENTESES);
			match(TipoToken.FIM_COMANDO);
		}
		else 
		{
			if(lookAhead.getTipo() == TipoToken.PALAVRA_RESERVADA && lookAhead.getLexema().equals("for"))
			{
				match(TipoToken.PALAVRA_RESERVADA);
				match(TipoToken.ABRE_PARENTESES);
				match(TipoToken.TIPO_DADO);
				match(TipoToken.ID);
				match(TipoToken.ATRIBUICAO);
				match(TipoToken.INT_CONST);
				match(TipoToken.FIM_COMANDO);
				condicao();
				match(TipoToken.FIM_COMANDO);
				incremento();
				match(TipoToken.FECHA_PARENTESES);
				match(TipoToken.INICIO_ESCOPO);
				corpoCMD();
				match(TipoToken.FIM_ESCOPO);
			}
		}
	}
	
	private void incremento()
	{
		match(TipoToken.ID);
		match(TipoToken.OPERADOR_INCDEC);
	}
	
	private void _else()
	{
		match(TipoToken.PALAVRA_RESERVADA);
		match(TipoToken.INICIO_ESCOPO);
		corpoCMD();
		match(TipoToken.FIM_ESCOPO);	
	}
	
	private void condicao()
	{
		match(TipoToken.ID);
		match(TipoToken.OPERADOR_RELACIONAL);
		if(lookAhead.getTipo() == TipoToken.INT_CONST)
			match(TipoToken.INT_CONST);
		else
			match(TipoToken.ID);
		if(lookAhead.getTipo() == TipoToken.OPERADOR_LOGICO)
			condicao();
	}
	private void expressao()
	{
		termo();
		if(lookAhead.getTipo() == TipoToken.OPERADOR_ARITMETICO && (lookAhead.getLexema().equals("+") || lookAhead.getLexema().equals("-")))
		{
			match(TipoToken.OPERADOR_ARITMETICO);
			expressao();
		}
	}
	
	private void termo()
	{
		fator();
		if(lookAhead.getTipo() == TipoToken.OPERADOR_ARITMETICO && (lookAhead.getLexema().equals("*") || lookAhead.getLexema().equals("/")))
		{
			match(TipoToken.OPERADOR_ARITMETICO);
			termo();
		}
	}
	
	private void fator()
	{
		if(lookAhead.getTipo() == TipoToken.ID)
		{
			// Verificar o tipo da variavel
			String tipo = analisadorSemantico.retornaTipoVariavel(lookAhead.getLexema());
			analisadorSemantico.checarTipo(tipo);
			match(TipoToken.ID);
		}
		else if(lookAhead.getTipo() == TipoToken.INT_CONST)
		{
			match(TipoToken.INT_CONST);
		}
		else
		{
			match(TipoToken.ABRE_PARENTESES);
			expressao();
			match(TipoToken.FECHA_PARENTESES);	
		}
	}
	
	private void listaVar(String tipo)
	{
		if(lookAhead.getTipo() == TipoToken.FIM_COMANDO)
		{
			match(TipoToken.FIM_COMANDO);
			declaracao();
		}
		else
		{
			match(TipoToken.SEPARADOR_ARG);
			
			// a variável não existe?
			if(!analisadorSemantico.checarVariavelDeclarada(lookAhead.getLexema()))
				// Adicionar na tabela
				analisadorSemantico.adicionaVariavel(lookAhead.getLexema(), tipo);
			else
			{
				System.out.println("Variavel previamente declarada!!! " + lookAhead.getLexema());
				System.exit(0);
			}
			match(TipoToken.ID);
			listaVar(tipo);
		}
	}
}
