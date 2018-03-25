function theGame() {
        imprimeTabuleiro(config);
        if (estadoFinal[config] == 0) {
            if (config < 8000) {
                //System.out.println("Vez do Jogador O");
                if (bot == 1)
                    botJoga();
                else if (bot == 2)
                    playerJoga();
                else 
                    botJoga();
                cont++;
            } else {
                //System.out.println("Vez do Jogador X");
                if (bot == 1)
                    playerJoga();
                else if (bot == 2)
                    botJoga();
                else 
                    botJoga();
                cont--;
            }
        } else {
            console.log("ACABOU");
            playerClica = 0;

            $("#fim").show();

            if (config < 8000)
                $("#fim").find('p').text("Vitoria dos Pretos");
            else
                $("#fim").find('p').text("Vitoria dos Brancos");

            $("#btnRematch").click(function () {
                inicio();
                limparTabuleiro();
                $("#fim").hide();
                $("#menu").show();
            });
        }
}

function botJoga() {
    
    console.log(listaEstados[config]);
    for (let i in listaEstados[config]) {
        console.log(listaEstados[config][i]);
        if (estadoVencedor[listaEstados[config][i]] == 0){
            config = listaEstados[config][i];
            theGame();
            return;
        }
    }
        
    //jogada aleatoria
    config = listaEstados[config][Math.floor((Math.random() * listaEstados[config].length))];
    theGame();
    return;
}

function playerJoga() {
    playerClica = 1;
}

function movimentaPeao(coluna, linha){
    
    //vez do O else vez do X

    let id = config;

    if (id < 8000) {
        
        let id0 = id % 20;
        let id1 = parseInt((id % 400) / 20);
        let id2 = parseInt(id / 400);
        
        if (coluna == 0) {
            
            if (oid[linha][pos[id0][1]] != -1) {
                id =    Math.pow(20,0)*oid[linha][pos[id0][1]]
                      + Math.pow(20,1)*id1 
                      + Math.pow(20,2)*id2
                      + 8000;
            } else {
                id = -1;
            }
        } else if (coluna == 1) {

            if (oid[linha][pos[id1][1]] != -1) {
                id =    Math.pow(20,0)*id0 
                      + Math.pow(20,1)*oid[linha][pos[id1][1]]
                      + Math.pow(20,2)*id2
                      + 8000;
            } else {
                id = -1;
            }
        } else if (coluna == 2) {

            if (oid[linha][pos[id2][1]] != -1) {
                id =    Math.pow(20,0)*id0 
                      + Math.pow(20,1)*id1 
                      + Math.pow(20,2)*oid[linha][pos[id2][1]]
                      + 8000;
            } else {
                id = -1;
            }
        }
        
    } else {
        
        id = id - 8000;
        
        let id0 = id % 20;
        let id1 = parseInt((id % 400) / 20);
        let id2 = parseInt(id / 400);
        
        if (coluna == 0) {

            if (oid[pos[id0][0]][linha] != -1) {
                id =    Math.pow(20,0)*oid[pos[id0][0]][linha]
                      + Math.pow(20,1)*id1 
                      + Math.pow(20,2)*id2;
            } else {
                id = -1;
            }
        } else if (coluna == 1) {

            if (oid[pos[id1][0]][linha] != -1) {
                id =    Math.pow(20,0)*id0 
                      + Math.pow(20,1)*oid[pos[id1][0]][linha]
                      + Math.pow(20,2)*id2;
            } else {
                id = -1;
            }
        } else if (coluna == 2) {
            
            if (oid[pos[id2][0]][linha] != -1) {
                id =    Math.pow(20,0)*id0 
                      + Math.pow(20,1)*id1 
                      + Math.pow(20,2)*oid[pos[id2][0]][linha];
            } else {
                id = -1;
            }
        }
        
    }
    
    return id;

}

function imprimeTabuleiro(){
        
    let id = config;
    
    if (id >= 8000) {
        id = id - 8000;
    }
    
    let id0 = id % 20;
    let id1 = parseInt((id % 400) / 20);
    let id2 = parseInt(id / 400);

    $("#tabuleiro").find('div').removeClass("peao_preto");
    $("#tabuleiro").find('div').removeClass("peao_branco");

    $("#" + "casa_" + pos[id0][0].toString() + "_0").addClass("peao_branco");
    $("#" + "casa_" + pos[id1][0].toString() + "_1").addClass("peao_branco");
    $("#" + "casa_" + pos[id2][0].toString() + "_2").addClass("peao_branco");
    $("#" + "casa_" + pos[id0][1].toString() + "_0").addClass("peao_preto");
    $("#" + "casa_" + pos[id1][1].toString() + "_1").addClass("peao_preto");
    $("#" + "casa_" + pos[id2][1].toString() + "_2").addClass("peao_preto");

}