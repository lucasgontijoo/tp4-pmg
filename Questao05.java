import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Questao05 {

	public static void main(String[] args) {
		// Cria um array de veuiculos com base no csv que foi lido.
		Veiculo[] veiculos = LeitorCsv.ler("/tmp/veiculos.csv");
		Scanner sc = new Scanner(System.in);
		
		// Lê o id da entrada enquanto for diferente de -1 e procura no array de veículos. Caso encontre, imprime o veículo formatado.
		int id = sc.nextInt();
		while(id != -1) {
			for(int i = 0; i<veiculos.length; i++) {
				if(veiculos[i].getId() == id) {
					System.out.println(veiculos[i].format());
					break;
				}
			}
			id = sc.nextInt();
		}
		
		sc.close();
	}

}

class Veiculo {
	private int id;
	private String marca;
	private String modelo;
	private int ano;
	private String categoria;
	private String[] combustivel;
	private int cilindros;
	private double cilindrada;
	private String transmissao;
	private String tracao;
	private double consumoCidade;
	private double consumoEstrada;
	private double co2;
	private boolean turbo;
	private Data dataRegistro;
	
	public Veiculo(int id, String marca, String modelo, int ano, String categoria, String[] combustivel, int cilindros, double cilindrada, String transmissao, String tracao, double consumoCidade, double consumoEstrada, double co2, boolean turbo, Data dataRegistro) {
		this.id = id;
		this.marca = marca;
		this.modelo = modelo;
		this.ano = ano;
		this.categoria = categoria;
		this.combustivel = combustivel;
		this.cilindros = cilindros;
		this.cilindrada = cilindrada;
		this.transmissao = transmissao;
		this.tracao = tracao;
		this.consumoCidade = consumoCidade;
		this.consumoEstrada = consumoEstrada;
		this.co2 = co2;
		this.turbo = turbo;
		this.dataRegistro = dataRegistro;
	}
	
	public Veiculo() {
		
	}
	
	/*
	 Esse método recebe uma String com as informações do veículo, usa o split para separar cada uma
	 delas e colocá-las nas propriedades do objeto v.
	*/
	public static Veiculo parseVeiculo(String veiculo) {
		String[] aux = veiculo.split(",");
		Veiculo v = new Veiculo();
		
		v.id = Integer.parseInt(aux[0]);
		v.marca = aux[1];
		v.modelo = aux[2];
		v.ano = Integer.parseInt(aux[3]);
		v.categoria = aux[4];
		v.combustivel = aux[5].split(";");
		v.cilindros = Integer.parseInt(aux[6]);
		v.cilindrada = Double.parseDouble(aux[7]);
		v.transmissao = aux[8];
		v.tracao = aux[9];
		v.consumoCidade = Double.parseDouble(aux[10]);
		v.consumoEstrada = Double.parseDouble(aux[11]);
		v.co2 = Double.parseDouble(aux[12]);
		v.turbo = Boolean.parseBoolean(aux[13]);
		v.dataRegistro = Data.parseData(aux[14]);
		
		return v;
	}
	
	// Esse método formata a String que representa o veículo para o formato solicitado.
	public String format() {
		/*
			Como combustivel é um array, essa trataiva é feita para juntar os combustíveis numa String
			e posteriormente usar no return.
		*/
		String comb = "";
		
	    for (int i = 0; i < combustivel.length; i++) {
	        if (i > 0) {
	        	comb += ",";
	        }
	        
	        comb += combustivel[i];
	    }
	    
		return "[" + id + " ## " + marca + " ## " + modelo + " ## " + ano + " ## " + categoria + " ## " + "[" + comb + "]" + " ## " + cilindros + " ## " + String.format("%.1f", cilindrada)  + " ## " + transmissao + " ## " + tracao + " ## " +  String.format("%.2f", consumoCidade)  + " ## " + String.format("%.2f", consumoEstrada) + " ## " + String.format("%.1f", co2) + " ## " + turbo + " ## " + dataRegistro.format() + "]";  
	}
	
	// Pega o id do veículo
	public int getId() {
		return id;
	}

	public String getModelo() {
		return modelo;
	}
}

class LeitorCsv {
	
	/* 
	 Esse método cria um array de veículos e percorre o csv, transforma a linha em um objeto do
	 tipo Veiculo e coloca dentro do array. Posteriormente retorna o array que foi populado.
	*/
	public static Veiculo[] ler(String caminhoArquivo) {
		Veiculo[] veiculos = new Veiculo[500];
		try {
			Scanner sc = new Scanner(new File(caminhoArquivo), "UTF-8");
			
			if(sc.hasNext()) {
				sc.nextLine();
			}
			
			int i = 0;
            while (sc.hasNextLine() && i < 500) {
                String linha = sc.nextLine();
                veiculos[i++] = Veiculo.parseVeiculo(linha);	
            }
            sc.close();
            
		} catch(FileNotFoundException err) {
            System.out.println("Arquivo nao encontrado: " + caminhoArquivo);
		}
		
		return veiculos;
		
	}
}

class Data {
	private int ano;
	private int mes;
	private int dia;
	
	public Data(int ano, int mes, int dia) {
		this.ano = ano;
		this.mes = mes;
		this.dia = dia;
	}
	
	// Esse método pega a data, e separa em ano, mes e dia. Posteriormente cria um objeto data.
	public static Data parseData(String data) {
		String[] separacao = data.split("-");
		
		int ano = Integer.parseInt(separacao[0]);
		int mes = Integer.parseInt(separacao[1]);
		int dia = Integer.parseInt(separacao[2]);

		return new Data(ano, mes, dia);
	}
	
	// Esse método pega dia, mes e ano da data e separa por /. Posteiormente devolve a data formatada.
	public String format() {
		String dataFormatada = "";
		
		dataFormatada = String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
		
		return dataFormatada;
	}
}

class ArvoreBinaria {
	private No raiz;

	public ArvoreBinaria() {
		raiz = null;
	}

	/**
	 * Metodo publico iterativo para inserir elemento.
	 * @param x Elemento a ser inserido.
	 * @throws Exception Se o elemento existir.
	 */
	public void inserir(Veiculo x) throws Exception {
		raiz = inserir(x, raiz);
	}

	/**
	 * Metodo privado recursivo para inserir elemento.
	 * @param x Elemento a ser inserido.
	 * @param i No em analise.
	 * @return No em analise, alterado ou nao.
	 * @throws Exception Se o elemento existir.
	 */
	private No inserir(Veiculo x, No i) throws Exception {
		if (i == null) {
         i = new No(x);

      } else if (x < i.elemento) {
         i.esq = inserir(x, i.esq);

      } else if (x > i.elemento) {
         i.dir = inserir(x, i.dir);

      } else {
         throw new Exception("Erro ao inserir!");
      }

		return i;
	}

	/**
	 * Metodo publico iterativo para pesquisar elemento.
	 * @param x Elemento que sera procurado.
	 * @return <code>true</code> se o elemento existir,
	 * <code>false</code> em caso contrario.
	 */
	public boolean pesquisar(int x) {
		return pesquisar(x, raiz);
	}

	/**
	 * Metodo privado recursivo para pesquisar elemento.
	 * @param x Elemento que sera procurado.
	 * @param i No em analise.
	 * @return <code>true</code> se o elemento existir,
	 * <code>false</code> em caso contrario.
	 */
	private boolean pesquisar(int x, No i) {
      boolean resp;
		if (i == null) {
         resp = false;

      } else if (x == i.elemento) {
         resp = true;

      } else if (x < i.elemento) {
         resp = pesquisar(x, i.esq);

      } else {
         resp = pesquisar(x, i.dir);
      }
      return resp;
	}
}

class No {
	public Veiculo elemento;
	public No esq;
	public No dir;

	public No(Veiculo elemento) {
		this.elemento = elemento;
		this.esq = null;
		this.dir = null;
	}
}
