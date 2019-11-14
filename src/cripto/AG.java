//########################################RESULTADOS OBTIDOS########################################
//1) SEND + MORE = MONEY ---Quantidade de Convergência 6 , Média de Gerações 12
//2) COCA + COLA = OASIS ---Quantidade de Convergência 5 , Média de Gerações 13

package cripto;

import java.util.List;

public class AG {
    public static int convergencias;
    public static int totalGeracoes;
    public static double mediaGeracoes;

    public AG() {
    }

    private static String calcularCaracteresUnicos(String palavra1, String palavra2, String resultado) {

        String unico = palavra1 + palavra2 + resultado;//concatena as 3 palavras
        String retorno = "";

        for (int i = 0; i < unico.length(); i++) { //percorre tds as letras
            if (retorno.indexOf(unico.charAt(i)) < 0) {//se nao existe no retorno
                retorno = retorno + unico.charAt(i);//adc o caracter pq ele nao apareceu ainda
            }
        }

        return retorno;
    }

    public static Individuo executar(String palavra1, String palavra2, String resultado, int tamPopulacao, int numGeracoes, int percCrossOver, int percMutacao, int tipoSelecao, int tipoCrossOver, int tipoReinsercao, boolean debug) {

        String unico = calcularCaracteresUnicos(palavra1, palavra2, resultado);//calcula os caracteres unicos

        Populacao p = new Populacao(palavra1, palavra2, resultado, unico, debug);

        p.criarPopulacao(tamPopulacao);//cria poulação do tamanho 100

        Individuo melhor = p.melhorSolucao(0);//guarda o melhor individuo

        int geracoes;

        for (geracoes = 1; geracoes <= numGeracoes && melhor.getAptidao() != 0; geracoes++) {//condicoes de termino: atingir o num de geracoes, ou encontrar o melhor individuo (aptidao = 0 )

            p.crossOver(percCrossOver, tipoSelecao, tipoCrossOver);
            //o percentual de crossover é o percentual de filhos que vai gerar sobre a populacao original, 2 pais são selecionados pelo método informado (1-roleta  2-torneio)
            //cria os filhos de acordo com o metodo informado (1-ciclico / 2-pmx)

            p.mutacao(percMutacao);//troca simples atraves de 2 vlrs aleatorios

            p.selecaoNatural(tipoReinsercao, percCrossOver, geracoes);

            melhor = p.melhorSolucao(geracoes);

        }

        if (!debug) {
            if (melhor.getAptidao() == 0) {
                //System.out.println("SIM\t" + (geracoes - 1));
                convergencias++;
                totalGeracoes+=(geracoes-1);
            } else {
                //System.out.println("NAO\t50");
            }
        }

        return melhor;
    }

    public static void main(String[] args) {

        convergencias = 0;
        totalGeracoes = 0;
        mediaGeracoes = 0;


        for (int j = 0; j < 100; j++) {
            System.out.println("J = "+j);
            //Tipo de Seleção: 1 - Roleta, 2 - Torneio Simples
            //Tipo de CrossOver: 1 - Cíclico, 2 - PMX
            //Tipo de Reinserção: 1 - Ordenada 2 - Elitista

           // Individuo i = executar("SEND", "MORE", "MONEY", 100, 100, 60, 3, 2, 1, 1, false);
            Individuo ii = executar("COCA", "COLA", "OASIS", 100, 100, 60, 3, 2, 1, 1, false);

        }

        if(convergencias > 0){
            mediaGeracoes = totalGeracoes/convergencias;
        }
        System.out.println("Quantidade de convergencias: " + convergencias);
        System.out.println("Media de Gerações: " + mediaGeracoes);



    }

}
