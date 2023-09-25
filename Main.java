package interpretador;

import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		Parser parser = new Parser("fonte.c");
		parser.programa();
		
	}

}
