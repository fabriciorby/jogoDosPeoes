import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

class Peoes {
    
    public static final int NUM_LINHAS  = 5;
    public static final int NUM_COLUNAS = 3;
    
    public static char[][] tabuleiro = new char[NUM_LINHAS][NUM_COLUNAS];
	
	//Matriz com todas as posições possíveis para uma coluna
	//i -> número do jogador
	//j -> id de configuração
	//Retorna a posição do jogador
	public static int pos[][] = new int[2][20];
	
	//Matriz que retornará o id da configuração da coluna
	//i -> posição do jogador o
	//j -> posição do jogador x
	//Retorna id de configuração
	public static int oid[][] = new int[5][5];
	
	//Array que indica se um estado é final
	//estadoFinal[15321] == 1 -> é final
	//estadoFinal[15321] == 0 -> não é final
	public static int estadoFinal[] = new int[16000];
	
	//Lista de adjacência com todos os 16000 vértices possíveis
	//Se o id <  8000 -> estados em que o O joga
	//Se o id >= 8000 -> estados em que o X joga
	//Cada item possui uma lista de estados que podem ser jogados pelo outro jogador
	public static ArrayList<ArrayList<Integer>> listaEstados = new ArrayList<ArrayList<Integer>>();
	
	//Array que indica se naquele estado há uma estratégia vencedora
	//estadoVencedor[15321] == 1 -> possui estratégia vencedora
	//estadoVencedor[15321] == 0 -> não possui estratégia vencedora
	public static int[] estadoVencedor = new int[16000];
    
    
    public static void main(String args[]){
        
        //Jogador da bolinha sempre começa
        //Mudar configuração para saber se o BOT será primeiro ou segundo
        //Caso o número seja diferente de primeiro ou segundo, o bot jogará contra outro bot
        //Jogador O = primeiro
    	//Jogador X = segundo
    	
        final String BOT = args[0];
        
        //System.out.println(BOT);
		
		//Preenchendo as pos[][] e id[][]
		preenchePosId();

        //Criando as 3 colunas que geram o tabuleiro
        //com 8000 config diferentes
        // int id0;
        // int id1;
        // int id2;

        //Config inicial
        //A config inicial da-se quando id0, id1, id2 = 3
        //Utiliza-se base 20 para mapear as 8000 configurações de tabuleiro existentes
        //pois cada coluna possue 20 configurações diferentes
        int configInicial = (int) Math.pow(20,0)*3 + (int) Math.pow(20,1)*3 + (int) Math.pow(20,2)*3;
        
        //Configs finais
        //Para o jogador O: Peças na posição 4
        //Para o jogador X: Peças na posição 0
        //id das colunas
        preencheEstadosFinais();

        //Mapeando todos os movimentos possíveis em todos os estados possíveis.
        //É uma lista de adjacência com 16000 vértices.
        preencheListaEstados();

        //Inicializa array global
        for (int i = 0; i < 16000; i ++) {
            estadoVencedor[i] = -1;
        }
        
        //Cria grafo de estados vencedores para cada vértice do grafo de adjacência
        for (int i = 0; i < 16000; i++) {
            buscaProfunda(i, inicializaMarca(16000));
        }
        
        //Configurando o início do jogo
        
        // System.out.println("Inicio de jogo!");
        // if (BOT.equals("primeiro")) {
        //     System.out.println("Jogador O: BOT");
        //     System.out.println("Jogador X: Humano");
        // } else if (BOT.equals("segundo")) {
        //     System.out.println("Jogador O: Humano");
        //     System.out.println("Jogador X: BOT");
        // } else {
        //     System.out.println("Jogador O: BOT");
        //     System.out.println("Jogador X: BOT");
        // }
        
        int config = configInicial;
        
        int a = 0;
        
        while (true) {
            //imprimeTabuleiro(config);
            if (estadoFinal[config] == 0) {
                if (config < 8000) {
                    //System.out.println("Vez do Jogador O");
                    if (BOT.equals("primeiro"))
                        config = botJoga(config);
                    else if (BOT.equals("segundo"))
                        config = playerJoga(config);
                    else 
                        config = botJoga(config);
                    a++;
                } else {
                    //System.out.println("Vez do Jogador X");
                    if (BOT.equals("primeiro"))
                        config = playerJoga(config);
                    else if (BOT.equals("segundo"))
                        config = botJoga(config);
                    else 
                        config = botJoga(config);
                    a--;
                }
            } else {
                // System.out.println("FIM DE JOGO");
                // if (a == 0)
                //     System.out.println("Vitoria do Jogador X");
                // else
                //     System.out.println("Vitoria do Jogador O");
                break;
            }
        }
        
        // System.out.println();
        // int cont = 0;
        // for (int i = 0; i < 16000; i++) {
        //     if (estadoVencedor[i] == 1) {
        //         cont++;
        //     }
        //System.out.print(estadoVencedor[i]);
        // }
        //System.out.println();
        // System.out.println(cont + " numeros 1");
        
    }
    
    public static String getSaida(int config, int id) {
        if (config < 8000) {
            
            id = id - 8000;
            
            int id0 = id%20;
            int id1 = (id%400)/20;
            int id2 = id/400;
            
            int config0 = config%20;
            int config1 = (config%400)/20;
            int config2 = config/400;
            
            int a;
            int casas = 0;
            
            if (id0 != config0)
                a = 0;
            else if (id1 != config1)
                a = 1;
            else if (id2 != config2)
                a = 2;
            else
                return "1 0";
                
            if (a == 0)
                casas = pos[0][id0] - pos[0][config0];
            else if (a == 1)
                casas = pos[0][id1] - pos[0][config1];
            else if (a == 2)
                casas = pos[0][id2] - pos[0][config2];
            
            return (a + 1) + " " + casas;
                
        } else {
            
            config = config - 8000;
            
            int id0 = id%20;
            int id1 = (id%400)/20;
            int id2 = id/400;
            
            int config0 = config%20;
            int config1 = (config%400)/20;
            int config2 = config/400;
            
            int a;
            int casas = 0;
            
            if (id0 != config0)
                a = 0;
            else if (id1 != config1)
                a = 1;
            else if (id2 != config2)
                a = 2;
            else
                return "1 0";
                
            if (a == 0)
                casas = pos[1][config0] - pos[1][id0];
            else if (a == 1)
                casas = pos[1][config1] - pos[1][id1];
            else if (a == 2)
                casas = pos[1][config2] - pos[1][id2];
            
            return (a + 1) + " " + casas;
            
        }
    }
    
    public static int botJoga(int config) {
        // System.out.print("BOT: ");
        
        for (int i : listaEstados.get(config)) {
            if (estadoVencedor[i] == 0){
                // System.out.println("Tenho estrategia vencedora: " + i);
                System.out.println(getSaida(config, i));
                return i;
            }
        }
        
        Random rand = new Random();
        int id = listaEstados.get(config).get(rand.nextInt(listaEstados.get(config).size()));
        // System.out.println("Nao tenho estrategia vencedora");
        // System.out.println("Portanto jogarei aleatoriamente em " + id);
        System.out.println(getSaida(config, id));
        
        return id;
    }
    
    public static int playerJoga(int config) {
        int tmp;
        Scanner sc = new Scanner(System.in);
        ArrayList<Integer> movimentosValidos = listaEstados.get(config);
        
        //System.out.println("\nDigite a coluna [1, 2, 3] e a qtd de casas:");
        
        do {
            
            // String linha = sc.nextLine();
            // int coluna = linha.charAt(0)-'1';
            // int casas  = linha.charAt(2)-'0';
            
            int coluna = sc.nextInt() - 1;
            int casas = sc.nextInt();
            
            tmp = movimentaPeao(config, coluna, casas);
            if (!movimentosValidos.contains(tmp)) {
                System.out.println("Movimento inválido!");
            }
        } while (!movimentosValidos.contains(tmp));
        
        return tmp;
    }
    
    public static int[] inicializaMarca(int n){
        
        //Cria array cor[] auxiliar para busca em profundidade
        //-1 = cinza  -> visitando
        // 1 = preto  -> visitado
        // 0 = branco -> não visitado
        
        int[] cor = new int[n];

        for (int i = 0; i < n; i ++) {
            cor[i] = 0;
        }
        
       return cor;
    }
    
    public static void buscaProfunda(int u, int[] cor){
        cor[u] = -1;
        ArrayList<Integer> adj = listaEstados.get(u);

        if (estadoFinal[u] == 1) {
            estadoVencedor[u] = 0;
            cor[u] = 1;
            return;
        }
        
        for (int v : adj) {
            
            if (cor[v] == 0) {
                
                buscaProfunda(v, cor);
                
            }
            
            if (estadoVencedor[v] == 0){
                estadoVencedor[u] = 1;
                break;
            } else {
                estadoVencedor[u] = 0;
            }
            
        }
        
        cor[u] = 1;
        return;
        
    }
    
    
    public static int movimentaPeao(int id, int coluna, int casas){
        
        if (coluna < 0 || coluna > 2)
            return -1;
            
        if (casas < 0 || casas > 4)
            return -1;
        
        //vez do O else vez do X
        if (id < 8000) {
            
            int id0 = id%20;
            int id1 = (id%400)/20;
            int id2 = id/400;
            
            if (coluna == 0) {
                
                if (pos[0][id0] + casas > 4)
                    return -1;
                    
                if (oid[ pos[0][id0] + casas ][pos[1][id0]] != -1) {
                    id =    (int) Math.pow(20,0)*oid[ pos[0][id0] + casas][pos[1][id0]]
                          + (int) Math.pow(20,1)*id1 
                          + (int) Math.pow(20,2)*id2
                          + 8000;
                } else {
                    id = -1;
                }
            } else if (coluna == 1) {
                
                if (pos[0][id1] + casas > 4)
                    return -1;
                    
                if (oid[ pos[0][id1] + casas ][pos[1][id1]] != -1) {
                    id =    (int) Math.pow(20,0)*id0 
                          + (int) Math.pow(20,1)*oid[pos[0][id1] + casas][pos[1][id1]]
                          + (int) Math.pow(20,2)*id2
                          + 8000;
                } else {
                    id = -1;
                }
            } else if (coluna == 2) {
                
                if (pos[0][id2] + casas > 4)
                    return -1;
                
                if (oid[ pos[0][id2] + casas ][pos[1][id2]] != -1) {
                    id =    (int) Math.pow(20,0)*id0 
                          + (int) Math.pow(20,1)*id1 
                          + (int) Math.pow(20,2)*oid[pos[0][id2] + casas][pos[1][id2]]
                          + 8000;
                } else {
                    id = -1;
                }
            }
            
        } else {
            
            id = id - 8000;
            
            int id0 = id%20;
            int id1 = (id%400)/20;
            int id2 = id/400;
            
            if (coluna == 0) {
                
                if (pos[1][id0] - casas < 0)
                    return -1;
                    
                if (oid[ pos[0][id0]][pos[1][id0] - casas] != -1) {
                    id =    (int) Math.pow(20,0)*oid[pos[0][id0]][pos[1][id0] - casas]
                          + (int) Math.pow(20,1)*id1 
                          + (int) Math.pow(20,2)*id2;
                } else {
                    id = -1;
                }
            } else if (coluna == 1) {
                
                if (pos[1][id1] - casas < 0)
                    return -1;
                    
                if (oid[ pos[0][id1]][pos[1][id1] - casas] != -1) {
                    id =    (int) Math.pow(20,0)*id0 
                          + (int) Math.pow(20,1)*oid[pos[0][id1]][pos[1][id1] - casas]
                          + (int) Math.pow(20,2)*id2;
                } else {
                    id = -1;
                }
            } else if (coluna == 2) {
                
                if (pos[1][id2] - casas < 0)
                    return -1;
                
                if (oid[ pos[0][id2]][pos[1][id2] - casas] != -1) {
                    id =    (int) Math.pow(20,0)*id0 
                          + (int) Math.pow(20,1)*id1 
                          + (int) Math.pow(20,2)*oid[pos[0][id2]][pos[1][id2] - casas];
                } else {
                    id = -1;
                }
            }
            
        }
        
        return id;

    }
    
    public static void imprimeTabuleiro(int id){
        
        if (id == -1) {
            System.out.println("Não há jogadas disponíveis!");
            return;
        }
        
        if (id >= 8000) {
            id = id - 8000;
        }
        
        criaTabuleiro();
        int id0 = id%20;
        int id1 = (id%400)/20;
        int id2 = id/400;
        
        tabuleiro[pos[0][id0]][0] = 'o';
        tabuleiro[pos[0][id1]][1] = 'o';
        tabuleiro[pos[0][id2]][2] = 'o';
        tabuleiro[pos[1][id0]][0] = 'x';
        tabuleiro[pos[1][id1]][1] = 'x';
        tabuleiro[pos[1][id2]][2] = 'x';
        System.out.println();
        for (int i = 0; i < NUM_LINHAS; i++){
            for (int j = 0; j < NUM_COLUNAS; j++){
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void criaTabuleiro(){
        for (int i = 0; i < NUM_LINHAS; i++){
            for (int j = 0; j < NUM_COLUNAS; j++){
                    tabuleiro[i][j] = '-';
            }
        }
    }
    
    public static void preenchePosId(){
        for (int j = 0; j < 20; j++) {
			pos[0][j] = j/4;
			
		    if (j%4 < j/4)
		        pos[1][j] = j%4;
		    else
		        pos[1][j] = j%4 + 1;
		        
		    oid[pos[0][j]][pos[1][j]] = j;
		}
		
		//Preenchendo a diagonal principal com -1,
		//pois não é possível ter duas peças no mesmo lugar
		for (int i = 0; i < 5; i++) {
            oid[i][i] = -1;
		}
    }
    
    public static ArrayList<Integer> possiveisJogadasX(int id) {
        
        int id0 = id%20;
        int id1 = (id%400)/20;
        int id2 = id/400;
        
        int o;
        int x;
        
        boolean semMovimentos = true;
        
        ArrayList<Integer> possiveisJogadasX = new ArrayList<>();

        //Jogadas possíveis na coluna 0
        o = pos[0][id0];
        x = pos[1][id0];
        
        for (int i = x - 1; i > o; i--) {
            possiveisJogadasX.add((int) Math.pow(20,0)*oid[o][i] 
                                    + (int) Math.pow(20,1)*id1 
                                    + (int) Math.pow(20,2)*id2
                                    );
            semMovimentos = false;
        }
        
        if (x == o + 1 && o > 0) {
            possiveisJogadasX.add((int) Math.pow(20,0)*oid[o][o - 1] 
                                    + (int) Math.pow(20,1)*id1 
                                    + (int) Math.pow(20,2)*id2
                                    );
            semMovimentos = false;
        }
        
        if (x < o)
        for (int i = x - 1; i >= 0; i--) {
            possiveisJogadasX.add((int) Math.pow(20,0)*oid[o][i] 
                                    + (int) Math.pow(20,1)*id1 
                                    + (int) Math.pow(20,2)*id2
                                    );
            semMovimentos = false;
        }
        
        //Jogadas possíveis na coluna 1
        o = pos[0][id1];
        x = pos[1][id1];
        
        for (int i = x - 1; i > o; i--) {
            possiveisJogadasX.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*oid[o][i] 
                                    + (int) Math.pow(20,2)*id2
                                    );
            semMovimentos = false;
        }
        
        if (x == o + 1 && o > 0) {
            possiveisJogadasX.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*oid[o][o - 1] 
                                    + (int) Math.pow(20,2)*id2
                                    );
            semMovimentos = false;
        }
        
        if (x < o)
        for (int i = x - 1; i >= 0; i--) {
            possiveisJogadasX.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*oid[o][i] 
                                    + (int) Math.pow(20,2)*id2
                                    );
            semMovimentos = false;
        }
        
        //Jogadas possíveis na coluna 2
        o = pos[0][id2];
        x = pos[1][id2];
        
        for (int i = x - 1; i > o; i--) {
            possiveisJogadasX.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*id1
                                    + (int) Math.pow(20,2)*oid[o][i]
                                    );
            semMovimentos = false;
        }
        
        if (x == o + 1 && o > 0) {
            possiveisJogadasX.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*id1
                                    + (int) Math.pow(20,2)*oid[o][o - 1]
                                    );
            semMovimentos = false;
        }
        
        if (x < o)
        for (int i = x - 1; i >= 0; i--) {
            possiveisJogadasX.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*id1
                                    + (int) Math.pow(20,2)*oid[o][i]
                                    );
            semMovimentos = false;
        }
        
        //Jogadas em que o jogador precisa passar a vez
        if (semMovimentos == true) {
            possiveisJogadasX.add(id);
        }
        //Collections.sort(possiveisJogadasX);
        //System.out.println(possiveisJogadasX);
        return possiveisJogadasX;
    }
    
    public static ArrayList<Integer> possiveisJogadasO(int id) {
        
        int id0 = id%20;
        int id1 = (id%400)/20;
        int id2 = id/400;
        
        int o;
        int x;
        
        boolean semMovimentos = true;
        
        ArrayList<Integer> possiveisJogadasO = new ArrayList<>();
        
        //Jogadas possíveis na coluna 0
        o = pos[0][id0];
        x = pos[1][id0];
        
        for (int i = o + 1; i < x; i++) {
            possiveisJogadasO.add((int) Math.pow(20,0)*oid[i][x] 
                                    + (int) Math.pow(20,1)*id1 
                                    + (int) Math.pow(20,2)*id2
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (x == o + 1 && x < 4) {
            possiveisJogadasO.add((int) Math.pow(20,0)*oid[x + 1][x] 
                                    + (int) Math.pow(20,1)*id1 
                                    + (int) Math.pow(20,2)*id2
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (o > x)
        for (int i = o + 1; i <= 4; i++) {
            possiveisJogadasO.add((int) Math.pow(20,0)*oid[i][x] 
                                    + (int) Math.pow(20,1)*id1 
                                    + (int) Math.pow(20,2)*id2
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        //Jogadas possíveis na coluna 1
        o = pos[0][id1];
        x = pos[1][id1];
        
        for (int i = o + 1; i < x; i++) {
            possiveisJogadasO.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*oid[i][x]
                                    + (int) Math.pow(20,2)*id2
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (x == o + 1 && x < 4) {
            possiveisJogadasO.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*oid[x + 1][x]
                                    + (int) Math.pow(20,2)*id2
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (o > x)
        for (int i = o + 1; i <= 4; i++) {
            possiveisJogadasO.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*oid[i][x]
                                    + (int) Math.pow(20,2)*id2
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        //Jogadas possíveis na coluna 2
        o = pos[0][id2];
        x = pos[1][id2];
        
        for (int i = o + 1; i < x; i++) {
            possiveisJogadasO.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*id1
                                    + (int) Math.pow(20,2)*oid[i][x]
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (x == o + 1 && x < 4) {
            possiveisJogadasO.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*id1
                                    + (int) Math.pow(20,2)*oid[x + 1][x]
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (o > x)
        for (int i = o + 1; i <= 4; i++) {
            possiveisJogadasO.add((int) Math.pow(20,0)*id0
                                    + (int) Math.pow(20,1)*id1
                                    + (int) Math.pow(20,2)*oid[i][x]
                                    + 8000
                                    );
            semMovimentos = false;
        }
        
        if (semMovimentos == true) {
            possiveisJogadasO.add(id + 8000);
        }
        
        //System.out.println(possiveisJogadasO);
        return possiveisJogadasO;
    }
    
    public static void preencheEstadosFinais() {
        int[] estadosFinaisColunaX = new int[4];
        int[] estadosFinaisColunaO = new int[4];
        
        //Preencho com os as configurações finais das colunas
        for (int i = 0; i < 4; i++) {
            //Todas as configurações em que X está em 0
            estadosFinaisColunaX[i] = oid[i + 1][0];
            
            //Todas as configurações em que O está em 4
            estadosFinaisColunaO[i] = oid[4][i];
        }
        
        //Faço um arranjo com as configurações das colunas
        //Atribuo 1 no estadoFinal[x] caso seja um estadoFinal
        
        for (int i = 0; i < estadosFinaisColunaX.length; i++) {
            for (int j = 0; j < estadosFinaisColunaX.length; j++) {
                for (int k = 0; k < estadosFinaisColunaX.length; k++) {
                    estadoFinal[(int) Math.pow(20,0)*estadosFinaisColunaX[i]
                                     + (int) Math.pow(20,1)*estadosFinaisColunaX[j]
                                     + (int) Math.pow(20,2)*estadosFinaisColunaX[k]] = 1;
                }
            }
        }
        
        for (int i = 0; i < estadosFinaisColunaO.length; i++) {
            for (int j = 0; j < estadosFinaisColunaO.length; j++) {
                for (int k = 0; k < estadosFinaisColunaO.length; k++) {
                    estadoFinal[(int) Math.pow(20,0)*estadosFinaisColunaO[i]
                                     + (int) Math.pow(20,1)*estadosFinaisColunaO[j]
                                     + (int) Math.pow(20,2)*estadosFinaisColunaO[k]
                                     + 8000] = 1;
                }
            }
        }
    }
    
    public static void preencheListaEstados() {
        //Populo a lista de adjacência com todos os estados possíveis a partir de cada vértice
        for (int i = 0; i < 16000; i++) {
            if (i/8000 == 0) {
                ArrayList<Integer> estadosO = possiveisJogadasO(i%8000);
                if (estadosO.isEmpty())
                    estadosO.add(-1);
                listaEstados.add(estadosO);
            } else {
                ArrayList<Integer> estadosX = possiveisJogadasX(i%8000);
                if (estadosX.isEmpty())
                    estadosX.add(-1);
                listaEstados.add(estadosX);
            }
        }
        
    }
    
}