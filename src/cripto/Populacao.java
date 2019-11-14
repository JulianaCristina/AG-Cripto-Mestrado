package cripto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Populacao {

    private List<cripto.Individuo> pais; //pai (gerado aleatorio e mantido)
    private List<cripto.Individuo> filhos;//filho (calcula no crossover)

    private int tamPopulacao;

    private boolean debug;

    private static final Random random = new Random(System.currentTimeMillis());

    public Populacao(String palavra1, String palavra2, String resultado, String unico, boolean debug) {
        cripto.Individuo.usados = unico.length();//so atribui vlrs aos pais
        cripto.Individuo.palavra1 = palavra1;
        cripto.Individuo.palavra2 = palavra2;
        cripto.Individuo.resultado = resultado;
        cripto.Individuo.unico = unico;
        cripto.Individuo.debug = debug;
        pais = new ArrayList<cripto.Individuo>();//cria a população de pais vazia
        filhos = new ArrayList<cripto.Individuo>();
        this.debug = debug;
    }

    public List<cripto.Individuo> getPais() {
        return pais;
    }

    public void setPais(List<cripto.Individuo> pais) {
        this.pais = pais;
    }

    public List<cripto.Individuo> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<cripto.Individuo> filhos) {
        this.filhos = filhos;
    }

    public int getTamPopulacao() {
        return tamPopulacao;
    }

    public void setTamPopulacao(int tamPopulacao) {
        this.tamPopulacao = tamPopulacao;
    }

    public void criarPopulacao(int tamPopulacao) {//recebe o tam da populacao

        if (debug) {
            System.out.println("\n\nCriando populacao de tamanho " + tamPopulacao + ":\n\n");
        }

        this.tamPopulacao = tamPopulacao;
        for (int i = 0; i < tamPopulacao; i++) {//roda um for com o tamanho da populacao
            cripto.Individuo ind = new cripto.Individuo();//gera 100 pais aleatorios
            ind.gerarAleatorio();
            pais.add(ind);//adiciona a lista de pais

            if (debug) {
                System.out.println(ind.toString());
            }

        }
    }

    public void imprimirPopulacao() {
        //System.out.println("Pais");
        for (cripto.Individuo i : pais) {
            System.out.println(i.toString());
        }
        /*System.out.println("Filhos");
        for (Individuo i : filhos) {
            System.out.println(i.toString());
        }*/
    }

    public cripto.Individuo melhorSolucao(int geracao) {//olha tds os pais e guarda aquele que tem melhor aptidao
        cripto.Individuo ind = new cripto.Individuo();
        for (cripto.Individuo i : pais) {
            if (i.getAptidao() < ind.getAptidao()) {
                ind = i;
            }
        }
        if (debug) {
            System.out.println("\n\nMelhor solucao na geracao " + geracao + ": \n" + ind.toString() + "\n\n");
        }
        return ind;
    }

    public void crossOver(int percCrossOver, int tipoSelecao, int tipoCrossOver) {
        filhos = new ArrayList<cripto.Individuo>();//esvazia os filhos pq cria uma nova lista
        int qtdCrossOver = (int)(tamPopulacao * (percCrossOver / 100.0));
        int pior = 0;

        String nome = "Torneio 3";

        if (tipoSelecao == 1) {//ou seja, roleta, tem que achar a aptidao do pior individuo (maior aptidao)
            for (cripto.Individuo i : pais) { //pega a lista de pais(pais) e percorre
                if (i.getAptidao() > pior) {//procura a maior nota (pior nesse caso)
                    pior = i.getAptidao();//substitui se achar outra pior
                }
            }
            pior++;
            nome = "Roleta";
        }

        if (debug) {
            System.out.println("\n\nCrossover " + nome + ":\n\n");
        }

        for (int i = 0; i < qtdCrossOver / 2; i++) {//dividido por 2 pq gera 2 filhos a cada iteração
            cripto.Individuo pai1 = new cripto.Individuo(); // selecionar 1 - Roleta / 2 - Torneio (pai1)
            cripto.Individuo pai2 = new cripto.Individuo(); // selecionar 1 - Roleta / 2 - Torneio (pai2)

            int tentativas = 1;

            while (pai1.equals(pai2) && tentativas <= 5) {
                if (tipoSelecao == 1) {//roleta
                    pai1 = roleta(pior);
                    pai2 = roleta(pior);
                } else { //torneio
                    pai1 = torneio(3);
                    pai2 = torneio(3);
                }
                tentativas++;
            }

            if (!pai1.equals(pai2)) {
                List<cripto.Individuo> gerados = cripto.Individuo.crossOver(pai1, pai2, tipoCrossOver);
                filhos.addAll(gerados);
            }
        }
    }

    public cripto.Individuo roleta(int pior) {
        int totalRoleta = 0;

        for (cripto.Individuo i : pais) {
            totalRoleta += pior - i.getAptidao();//para calcular o espaço da roleta
        }

        int resultado = random.nextInt(totalRoleta)+1;//gera aleatorio entre o tamanho da roleta

        for (cripto.Individuo i : pais) {
            int qtdtickets = pior - i.getAptidao();
            if (resultado <= qtdtickets) {//foi sorteado
                return i;//retorna ele
            } else {
                resultado -= qtdtickets;//subtrai a qtd de tickets q a roleta ja andou
            }
        }

        return new cripto.Individuo();

    }

    public cripto.Individuo torneio (int quantidade) {//recebe a qtd de pais pra sortear
        List<cripto.Individuo> individuosTorneio = new ArrayList<cripto.Individuo>();

        for (int i = 0; i < quantidade; i++) {
            cripto.Individuo ind = pais.get(random.nextInt(tamPopulacao));//pega um individuo aleatorio dos pais
            individuosTorneio.add(ind);//adc ele no torneio
        }

        cripto.Individuo melhor = new cripto.Individuo();//o melhor é o q tem menor aptidao
        for (cripto.Individuo i : individuosTorneio) {//percorre os pais selecionados pro torneio
            if (i.getAptidao() < melhor.getAptidao()) {//seleciona o menor deles (objetivo minimizar)
                melhor = i;//se for menor subtitui
            }
        }

        return melhor;
    }

    public void mutacao(int percMutacao) {
        for (cripto.Individuo i : filhos) {

            int probabilidade = random.nextInt(100) + 1;

            if (probabilidade <= percMutacao) {
                i.mutacao();
            }

        }
    }

    public void selecaoNatural(int tipoSelecao, int percCrossOver, int geracao) {

        String nome = "";

        if (tipoSelecao == 1) { // R1 - Reinserção Ordenada
            pais.addAll(filhos);//adc os filhos juntos com os pais
            Collections.sort(pais);//ordena a lista
            while (pais.size() > tamPopulacao) {//vou da 100 ate o final e remove td mundo
                pais.remove(tamPopulacao);
            }
            nome = "Reinsercao Ordenada";
        } else if (tipoSelecao == 2) { // R2 - Reinserção Elitista
            Collections.sort(pais);//ordena os pais para que os 20 melhores estejam no inicio

            int qtdManter = tamPopulacao - filhos.size(); //calcula a qtd de filhos gerados pois tem q remover essa msm qtd
            while (pais.size() >  qtdManter) {//ex.: populacao inicial 100, remover 80 pais, inicia em 100-80=20
                pais.remove(qtdManter);
            }

            pais.addAll(filhos);//adc os 80 filhos no final
            nome = "Reinsercao Elitista";
        }

        if (debug) {
            System.out.println("\n\nGeracao " + geracao + " apos " + nome + ":\n\n");
            imprimirPopulacao();
        }

    }

}
