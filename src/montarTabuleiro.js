    //funções
    function montarMenu() {
        $("#menu").append("<p>Brancas começam.</p> <p>Escolha uma cor:</p> <button id='btnPreto' type='button'>Preta</button> <button id='btnBranco' type='button'>Branca</button>");
    }

    function montarTabuleiro() {
        let i;
        for (i = 0; i < 5; i++) {
            $("#tabuleiro").append("<div id='linha_" + i.toString() + "' class='linha' >");

            for (j = 0; j < 3; j++) {
                let nome_casa = "casa_" + i.toString() + "_" + j.toString();
                let classe = (i % 2 == 0 ? (j % 2 == 0 ? "casa_branca" : "casa_preta") : (j % 2 != 0 ? "casa_branca" : "casa_preta"));
                $("#linha_" + i.toString()).append("<div id='" + nome_casa + "' class='casa " + classe + "' />");

            }
        }
    }

    function limparTabuleiro() {
        $("#tabuleiro").find('div').removeClass("peao_preto");
        $("#tabuleiro").find('div').removeClass("peao_branco");
    }

    function inicio() {
        casa_selecionada = null;
        peca_selecionada = null;
        playerClica = null;
        config = configInicial;
        bot = null;
        cont = 0;
    }

$(function () {
    
    $("#fim").hide();
    montarMenu();
    montarTabuleiro();

    $("#btnBranco").click(function () {
        bot = 2;
        $("#menu").hide();
        $("#titulo").show();
        theGame();
    });
    
    $("#btnPreto").click(function () {
        bot = 1;
        $("#menu").hide();
        $("#titulo").show();
        theGame();
    });

    $(".casa").click(function () {

        if (playerClica == 1) {
            
            let tmpCasa = $(this).attr("id");
            let ehValido = false;

            if (peca_selecionada != null) {
                let pecaLinha  = parseInt(peca_selecionada.charAt(5));
                let pecaColuna = parseInt(peca_selecionada.charAt(7));
                let moveLinha  = parseInt(tmpCasa.charAt(5));
                let moveColuna = parseInt(tmpCasa.charAt(7));

                if (moveColuna == pecaColuna) {

                    let movimentosValidos = listaEstados[config];
                    
                    let tmp = movimentaPeao(moveColuna, moveLinha);

                    console.log(movimentosValidos);

                    console.log(tmp);

                    if (movimentosValidos.includes(tmp)) {
                        config = tmp;
                        ehValido = true;
                    } else {
                        console.log("Movimento inválido!");
                    }

                }

            }

            $("#" + casa_selecionada).removeClass("casa_selecionada");
    
            $("#info_casa_selecionada").text("Linha: " + (parseInt(tmpCasa.charAt(5)) + 1) + 
                                            " Coluna: " + (parseInt(tmpCasa.charAt(7)) + 1));
    
            peca_selecionada = $("#" + tmpCasa).attr("class");
            
            if (bot == 1 && peca_selecionada.includes("peao_preto")) {
                casa_selecionada = tmpCasa;
                $("#" + casa_selecionada).addClass("casa_selecionada");
                $("#info_peca_selecionada").text("Peão Preto");
                peca_selecionada = casa_selecionada;
            } else if (bot == 2 && peca_selecionada.includes("peao_branco")) {
                casa_selecionada = tmpCasa;
                $("#" + casa_selecionada).addClass("casa_selecionada");
                $("#info_peca_selecionada").text("Peão Branco");
                peca_selecionada = casa_selecionada;
            }

            if (ehValido) {
                theGame();
            }
    
        }
    });
});