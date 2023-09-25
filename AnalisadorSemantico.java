package interpretador;

import java.util.HashMap;

public class AnalisadorSemantico {
	
	// Tabela de s�mbolos
	private HashMap <String, Variavel> tabela;
	
	// Verifica��o de tipo
	private int tipoAvaliado;
	
	public AnalisadorSemantico()
	{
		this.tabela = new HashMap <>();
		
	}
	
	public boolean checarVariavelDeclarada(String lexema)
	{
		// Verifica se a vari�vel foi adicionada na tabela
		Variavel variavel = tabela.get(lexema);
		// retorna true se a vari�vel est� na tabela
		return variavel != null;
	}
	
	public void adicionaVariavel(String lexema, String tipo)
	{
		// Toda vari�vel rec�m declarada tem valor zero!
		Variavel variavel = new Variavel(tipo, "0");
		// adicionado a nova vari�vel na tabela
		tabela.put(lexema,variavel);
	}
	
	public void imprimeTabela()
	{
		System.out.println("LEXEMA\tTIPO\tVALOR");
		// Iterando sobre cada linha da tabela
		for(String chave : tabela.keySet())
		{
			Variavel variavel = tabela.get(chave);
			System.out.println(chave+"\t"+variavel.getTipo()+"\t"+variavel.getValor());
		}
	}
	
	public void setTipoAvaliado(int tipoAvaliado)
	{
		this.tipoAvaliado = tipoAvaliado;
	}
	
	private int retornaNivelPrecisao(String tipo)
	{
		if(tipo.equals("char"))
			return 0;
		if(tipo.equals("int"))
			return 1;
		if(tipo.equals("float"))
			return 2;
		return 3;
	}
	
	public void checarTipo(String tipo)
	{
		// Encontra o n�vel de precis�o do tipo avaliado
		int nivelTipo = retornaNivelPrecisao(tipo);
		this.tipoAvaliado = Math.max(tipoAvaliado, nivelTipo);
	}
	
	public void verificaTipoFinal(String tipo)
	{
		int nivelTipo = retornaNivelPrecisao(tipo);
		if(nivelTipo < this.tipoAvaliado)
			System.out.println("ALERTA: Perda de precisao!!!");
		
	}
	
	public String retornaTipoVariavel(String lexema)
	{
		Variavel variavel = tabela.get(lexema);
		return variavel.getTipo();
	}
}
