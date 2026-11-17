#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int ano;
    int mes;
    int dia;
} Data;

// Esse método pega a data, e separa em ano, mes e dia. Posteriormente cria um objeto data.
Data parseData(char *s) {
    Data d;
    d.dia = 0;
    d.mes = 0;
    d.ano = 0;

    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);

    return d;
}

// Esse método pega dia, mes e ano da data e separa por /. Posteiormente devolve a data formatada.
void formatData(Data d, char *buffer) {
    sprintf(buffer, "%02d/%02d/%04d", d.dia, d.mes, d.ano);
};

typedef struct {
    int id;
    char marca[50];
    char modelo[50];
    int ano;
    char categoria[50];
    char combustivel[4][50];
    int cilindros;
    double cilindrada;
    char transmissao[50];
    char tracao[50];
    double consumoCidade;
    double consumoEstrada;
    double co2;
    _Bool turbo;
    Data dataRegistro;
} Veiculo;

// Esse método faz cópia da string base e repassa para copia, posteriormente vai ser usado dentro de parseVeiculos
void fazerCopia(char *copia, char *base) {
    int i = 0;

    while (base[i] != '\0') {
        copia[i] = base[i];
        i++;
    }

    copia[i] = '\0';
}

/*
    Esse método recebe uma String com as informações do veículo, usa o strtok para separar cada uma
    delas e colocá-las nas propriedades do objeto v.
*/
Veiculo* parseVeiculo(char *s) {
    Veiculo *v = (Veiculo*) malloc(sizeof(Veiculo));
    char copia[1000];
    char *pedaco;
    char *textoCombustivel = NULL;

    fazerCopia(copia, s);

    pedaco = strtok(copia, ",");
    if(pedaco != NULL) {
        v->id = atoi(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        fazerCopia(v->marca, pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        fazerCopia(v->modelo, pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->ano = atoi(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        fazerCopia(v->categoria, pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        textoCombustivel = pedaco; 
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->cilindros = atoi(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->cilindrada = atof(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        fazerCopia(v->transmissao, pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        fazerCopia(v->tracao, pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->consumoCidade = atof(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->consumoEstrada = atof(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->co2 = atof(pedaco);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->turbo = (strcmp(pedaco, "true") == 0);
    }

    pedaco = strtok(NULL, ",");
    if(pedaco != NULL) {
        v->dataRegistro = parseData(pedaco);
    }

    // marca todas as linhas como vazias 
    for (int c = 0; c < 4; c++) {
        v->combustivel[c][0] = '\0';
    }

    // separa os combustíveis só depois que o strtok principal terminou 
    if(textoCombustivel != NULL) {
        int c = 0;
        pedaco = strtok(textoCombustivel, ";");
        while(pedaco != NULL && c < 4) {
            fazerCopia(v->combustivel[c], pedaco);
            c++;
            pedaco = strtok(NULL, ";");
        }
    }

    return v;
}

// Esse método formata a String que representa o veículo para o formato solicitado.
void formatVeiculo(Veiculo v, char *buffer) {
    char dataFormatada[11];
    char combustiveis[200];
    int pos = 0;
    formatData(v.dataRegistro, dataFormatada);

    // junta os combustíveis separados por vírgula 
    for (int c = 0; c < 4 && v.combustivel[c][0] != '\0'; c++) {
        if (c > 0) {
            combustiveis[pos] = ',';
            pos++;
        }

        int i = 0;
        while (v.combustivel[c][i] != '\0') {
            combustiveis[pos] = v.combustivel[c][i];
            pos++;
            i++;
        }
    }
    combustiveis[pos] = '\0';

    sprintf(buffer,
        "[%d ## %s ## %s ## %d ## %s ## [%s] ## %d ## %.1f ## %s ## %s ## %.2f ## %.2f ## %.1f ## %s ## %s]",
        v.id, v.marca, v.modelo, v.ano, v.categoria,
        combustiveis, v.cilindros, v.cilindrada, v.transmissao, v.tracao,
        v.consumoCidade, v.consumoEstrada, v.co2, v.turbo ? "true" : "false", dataFormatada
    );
}

/* 
	Esse método cria um array de veículos e percorre o csv, transforma a linha em um objeto do
	tipo Veiculo e coloca dentro do array. Posteriormente retorna o array que foi populado e guarda
    n (quantidade lida).
*/
Veiculo* lerCsv(char *caminhoArquivo, int *n) {
    *n = 0;

    FILE *arquivo = fopen(caminhoArquivo, "r");
    if (arquivo == NULL) {
        printf("Arquivo nao encontrado");
        return NULL;
    }

    Veiculo *veiculos = (Veiculo*) malloc(500 * sizeof(Veiculo));
    if (veiculos == NULL) {
        fclose(arquivo);
        return NULL;
    }

    char linha[1000];

    // Pula o cabeçalho 
    fgets(linha, 1000, arquivo);

    while (*n < 500 && fgets(linha, 1000, arquivo) != NULL) {
        if (linha[0] != '\0') {
            Veiculo *v = parseVeiculo(linha);
            if (v != NULL) {
                veiculos[*n] = *v;
                free(v);             
                (*n)++;
            }
        }
    }

    fclose(arquivo);
    return veiculos;
}

// Troca a posição de dois veículos no array
void swap(Veiculo *vet, int i, int j) {
    Veiculo temp = vet[i];
    vet[i] = vet[j];
    vet[j] = temp;
}

/*
    Quicksort baseado nos slides.
    A chave de ordenação é consumoEstrada e no empate, a marca.
*/
void quicksort(Veiculo *vet, int esq, int dir, int k) {
    int i = esq, j = dir;
    Veiculo pivo = vet[(esq + dir) / 2];

    // Acontece enquanto i não ultrapassar j
    while (i <= j) {
        // Verifica se consumo na estrada é menor que do pivo. Se igual olha a marca. 
        while (vet[i].consumoEstrada < pivo.consumoEstrada || (vet[i].consumoEstrada == pivo.consumoEstrada && strcmp(vet[i].marca, pivo.marca) < 0)) {
            i++;
        }

        // Verifica se consumo na estrada é maior que do pivo. Se igual olha a marca. 
        while (vet[j].consumoEstrada > pivo.consumoEstrada || (vet[j].consumoEstrada == pivo.consumoEstrada && strcmp(vet[j].marca, pivo.marca) > 0)) {
            j--;
        }
    
        if (i <= j) {
            swap(vet, i, j);
            i++;
            j--;
        }
    }
    if (esq < j)
        quicksort(vet, esq, j, k);
    if (i < dir && i < k)
        quicksort(vet, i, dir, k);
}

int main() {
    int n;

    // Le os veículos do CSV e guarda em veiculos
    Veiculo *veiculos = lerCsv("C:/Users/SUPORTE/AppData/Local/Temp/veiculos.csv", &n);

    if (veiculos == NULL) {
        return 1;
    }

    // Array que vai receber os veículos pesquisados pelo id
    Veiculo *vet = (Veiculo*) malloc(n * sizeof(Veiculo));
    int qtd = 0;

    int id;
    char buffer[200];

    // Le id até encontrar -1
    scanf("%d", &id);
    while (id != -1) {

        // Pesquisa sequencial
        int i = 0;
        while (i < n && veiculos[i].id != id) {
            i++;
        }

        if (i < n) {
            vet[qtd] = veiculos[i];
            qtd++;
        }

        scanf("%d", &id);
    }

    // Ordena os veículos selecionados
    if (qtd > 1) {
        quicksort(vet, 0, qtd - 1, 10);
    }

    // Imprime os veículos ordenados
    for (int i = 0; i < 10; i++) {
        formatVeiculo(vet[i], buffer);
        printf("%s\n", buffer);
    }

    free(vet);
    free(veiculos);
    return 0;
}