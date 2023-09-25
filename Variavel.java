package interpretador;

public class Variavel {
	
	// Tipo da vari�vel
	private String tipo;
	// Valor atual da vari�vel
	private String valor;
	
	public Variavel(String tipo, String valor)
	{
		this.tipo = tipo;
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}
	
	
}
