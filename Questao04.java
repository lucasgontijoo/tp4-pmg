import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Questao04 {

	public static void main(String[] args) {
		// Cria um array de veuiculos com base no csv que foi lido.
		Veiculo[] veiculos = LeitorCsv.ler("/tmp/veiculos.csv");
		Scanner sc = new Scanner(System.in);

		// Array que guarda os veiculos da entrdad
		Veiculo[] ordHeap = new Veiculo[veiculos.length + 1];
		int n = 0;
		
		// Lê o id da entrada enquanto for diferente de -1 e procura no array de veículos. Caso encontre, imprime o veículo formatado.
		int id = sc.nextInt();
		while(id != -1) {
			for(int i = 0; i<veiculos.length; i++) {
				if(veiculos[i].getId() == id) {
					ordHeap[++n] = veiculos[i];					
					break;
				}
			}
			id = sc.nextInt();
		}
		
		sc.close();

		// Ordena os veículos com Heapsort
		Heapsort heap = new Heapsort(ordHeap, n, 10);
		heap.heapsort();
		
		// Imprime os veículos já ordenados
		for(int i = 1; i <= 10; i++) {
			System.out.println(ordHeap[i].format());
		}
	}

}

class Heapsort {
	
	// Array usado pelo heap e quantidade de elementos
	private Veiculo[] vet;
	private int n;
	private int k;
	
	public Heapsort(Veiculo[] vet, int n, int k) {
		this.vet = vet;
		this.n = n;
		this.k = k;
	}
	
	// constroi o heap e faz a ordenação propriamente dita.
	public void heapsort() {
		// constroi heap
		for(int tam = 2; tam <= k; tam++) {
			construir(tam);
		}

		for(int i = k+1; i<=n;i++) {
			if(vet[i].getCo2()<vet[1].getCo2()) {
				swap(i, 1);
				reconstruir(k);
			}
		}
		
		// Ordenação propriamente dita
		int tam = k;
		while(tam > 1) {
			swap(1, tam--);
			reconstruir(tam);
		}
	}
	
	// Sobe o elemento da posição tam enquanto ele for maior que o pai dele
	private void construir(int tam) {
		for(int i = tam; i > 1 && (vet[i].getCo2() > vet[i / 2].getCo2() || (vet[i].getCo2() == vet[i / 2].getCo2() && vet[i].getTransmissao().compareTo(vet[i / 2].getTransmissao()) > 0)); i /= 2) {
			swap(i, i / 2);
		}
	}
	
	// Desce o elemento da raiz enquanto ele for menor que o maior de seus filhos
	private void reconstruir(int tam) {
		int i = 1;
		while(hasFilho(i, tam) == true) {
			int filho = getMaiorFilho(i, tam);
			if(vet[i].getCo2() < vet[filho].getCo2() || (vet[i].getCo2() == vet[filho].getCo2() && vet[i].getTransmissao().compareTo(vet[filho].getTransmissao()) < 0)) {
				swap(i, filho);
				i = filho;
			} else {
				i = tam; // Encerra a reconstrução
			}
		}
	}
	
	// Verifica se tem pelo menos um filho dentro do heap
	private boolean hasFilho(int i, int tam) {
		return (i <= (tam / 2));
	}
	
	// indice do maior filho do no
	// comparação é feita primeiro pelo co2 e, em caso de empate, pela transmissao
	// escolhe o filho da esquerda no caso de empate total
	private int getMaiorFilho(int i, int tam) {
		int filho;
		if(2 * i == tam) {
			filho = 2 * i;
		} else {
			Veiculo esq = vet[2 * i];
			Veiculo dir = vet[2 * i + 1];
			
			if(esq.getCo2() > dir.getCo2() || (esq.getCo2() == dir.getCo2() && esq.getTransmissao().compareTo(dir.getTransmissao()) >= 0)) {
				filho = 2 * i;
			} else {
				filho = 2 * i + 1;
			}
		}
		return filho;
	}
	
	// Troca dois elementos de posição
	private void swap(int i, int j) {
		Veiculo tmp = vet[i];
		vet[i] = vet[j];
		vet[j] = tmp;
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

	// Pega o c02 do veículo
	public double getCo2() {
		return co2;
	}

	// Pega o transmissao do veículo
	public String getTransmissao() {
		return transmissao;
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
