/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cripto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individuo implements Comparable<Individuo> {

    private int aptidao;
    private int[] cromossomo = new int[10];

    public static int usados;
    public static String palavra1;
    public static String palavra2;
    public static String resultado;
    public static String unico;
    public static boolean debug;

    private static final Random random = new Random(System.currentTimeMillis());

    public Individuo() {
        aptidao = Integer.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            cromossomo[i] = -1;
        }
    }

    public int compareTo(Individuo o) {//comparar na função de ordenar, vlrs positivos vao pro final da lista e vlr negativos pro inicio
        return getAptidao() - o.getAptidao();
    }

    public String toString() {
        String crom = String.format("Aptidão: %s\n[", this.aptidao);
        for (int i = 0; i < 10; i++) {
            crom += this.cromossomo[i] + ", ";
        }
        return crom.trim() + "]";
    }

    public boolean equals(Individuo i) {
        for (int j = 0; j < 10; j++) {
            if (cromossomo[j] != i.getCromossomo(j)) {
                return false;
            }
        }
        return true;
    }

    public int[] getCromossomo() {
        return this.cromossomo;
    }

    public int getCromossomo(int posicao) {
        return this.cromossomo[posicao];
    }

    public void setCromossomo(int[] cromossomo) {
        this.cromossomo = cromossomo;
    }

    public void setCromossomo(int posicao, int valor) {
        this.cromossomo[posicao] = valor;
    }

    public int getAptidao() {
        return aptidao;
    }

    public void setAptidao(int aptidao) {
        this.aptidao = aptidao;
    }

    public void gerarAleatorio() {//gera pos numeros aleatorios para as letras
        for (int i = 0; i < 10; i++) {
            int p = random.nextInt(10);//gera umn p
            while (cromossomo[p] != -1) {//enquanto a posicao tiver ocupada
                p = random.nextInt(10);//gera outro p
            }
            cromossomo[p] = i;
        }
        calcularAptidao();//calc avaliacao
    }

    public void calcularAptidao() {

        int somaW1 = 0;
        int somaW2 = 0;
        int somaR = 0;

        for (int j = 0; j < palavra1.length(); j++) {//percorre a palavra1
            int posicao_unico = unico.indexOf(String.valueOf(palavra1.charAt(j)));//acha a posicao do caracter na palavra unica (td junto e sem repeticao)
            somaW1 = (somaW1 * 10) + cromossomo[posicao_unico];//soma o vlr do cromossomo na posicao e multiplica por 10 pra avançar a dezena (empurra)
        }

        for (int j = 0; j < palavra2.length(); j++) {
            int posicao_unico = unico.indexOf(String.valueOf(palavra2.charAt(j)));
            somaW2 = (somaW2 * 10) + cromossomo[posicao_unico];
        }

        for (int j = 0; j < resultado.length(); j++) {
            int posicao_unico = unico.indexOf(String.valueOf(resultado.charAt(j)));
            somaR = (somaR * 10) + cromossomo[posicao_unico];
        }

        aptidao = Math.abs((somaW1 + somaW2) - somaR);//diferença absoluta entre as somas das palavras e resultado

    }

    public void mutacao() {

        int pos1 = random.nextInt(10);
        int pos2 = random.nextInt(10);

        while (pos1 == pos2) {//se for iguals gera dnv
            pos1 = random.nextInt(10);
            pos2 = random.nextInt(10);
        }

        if (debug) {
            System.out.println("\n\nMutacao nas posicoes " + pos1 + " e " + pos2 + " :\n\n");
            System.out.println(this.toString());
        }

        //realiza a troca
        int tmp = cromossomo[pos1];
        cromossomo[pos1] = cromossomo[pos2];
        cromossomo[pos2] = tmp;

        this.calcularAptidao();

        if (debug) {
            System.out.println("Depois da mutacao: \n");
            System.out.println(this.toString());
        }

        calcularAptidao();

    }

    public static List<Individuo> crossOver(Individuo pai1, Individuo pai2, int tipoCrossOver) {
        List<Individuo> filhos = new ArrayList<Individuo>();

        Individuo filho1 = new Individuo();//gera filho1
        filho1.setCromossomo(pai2.getCromossomo().clone());//filho1 começa com cromossomo do pai2
        Individuo filho2 = new Individuo();//gera filho2
        filho2.setCromossomo(pai1.getCromossomo().clone());//filho2 começa com cromossomo do pai1

        if (pai1.equals(pai2)) {
            filhos.add(pai1);
            filhos.add(pai2);

            return filhos;
        }

        if (tipoCrossOver == 1) {//ciclico

            int pos = random.nextInt(usados);

            int parada = filho1.getCromossomo(pos);
            int proximo = filho2.getCromossomo(pos);
            int tentativas = 0;

            while (parada == proximo && tentativas <= 10) {//se sao iguais nao teria crossover (troca)
                //gera nova posicao
                pos = random.nextInt(usados);

                parada = filho1.getCromossomo(pos);
                proximo = filho2.getCromossomo(pos);
                tentativas++;
            }

            boolean[] trocados = new boolean[]{false, false, false, false, false, false, false, false, false, false};

            if (debug) {
                System.out.println("Cíclico " + pos);
            }

            while (parada != proximo) {

                int tmp = filho1.getCromossomo(pos);
                filho1.setCromossomo(pos, filho2.getCromossomo(pos));
                filho2.setCromossomo(pos, tmp);

                proximo = filho1.getCromossomo(pos);//atualiza o meu proximo
                trocados[pos] = true; //pra marcar que ele ja foi trocado

                for (int i = 0; i < 10; i++) {
                    if (filho1.getCromossomo(i) == proximo && trocados[i] == false) {//se posicao é igual ao proximo, e se ainda nao foi trocado
                        pos = i;//a proxima posicao a trocar é o i
                        break;
                    }
                }

            }

            filho1.calcularAptidao();
            filho2.calcularAptidao();

            filhos.add(filho1);
            filhos.add(filho2);

        } else {//pmx

            int pos1 = random.nextInt(usados);//gera aleatorio de 0 até usados
            int pos2 = random.nextInt(usados);
            int x1;
            int x2;

            while (pos1 == pos2) {
                pos1 = random.nextInt(usados);//gera aleatorio de 0 até usados
                pos2 = random.nextInt(usados);
            }

            if (pos1 > pos2) {//pra pos1 ser menor e iniciar dela
                int tmp = pos1;
                pos1 = pos2;
                pos2 = tmp;
            }

            if (debug) {
                System.out.println("PMX " + pos1 + " ate " + pos2);
            }

            x1 = x2 = pos1;

            while ((x1 <= pos2) && (x2 <= pos2)) {//garante q ainda estao no intervalo do pmx
                boolean existe = false;
                do {//vai achar os primeiros vlrs q precisam ser trocados fora do pmx (x1, posicao do vetor filho1)
                    existe = false;

                    for (int i = pos1; i <= pos2; i++) {
                        if (filho1.getCromossomo(x1) == filho2.getCromossomo(i)) {
                            existe = true;
                            x1++;
                            break;
                        }
                    }
                } while (existe && x1 <= pos2);

                do {//vai achar os primeiros vlrs q precisam ser trocados fora do pmx (x2, posicao do vetor filho2)
                    existe = false;

                    for (int i = pos1; i <= pos2; i++) {
                        if (filho2.getCromossomo(x2) == filho1.getCromossomo(i)) {
                            existe = true;
                            x2++;
                            break;
                        }
                    }
                } while (existe && x2 <= pos2);

                int valorx1 = filho1.getCromossomo(x1);
                int valorx2 = filho2.getCromossomo(x2);

                for (int i = 0; i < 10; i++) {//pra realizar a troca no vetor filho1
                    if (filho1.getCromossomo(i) == valorx2) {//percorre as posicoes de filho1 para ver se é igual a posicao x2
                        filho1.setCromossomo(i, valorx1);
                    }

                    if (filho2.getCromossomo(i) == valorx1) {//percorre as posicoes de filho2 para ver se é igual a posicao x1
                        filho2.setCromossomo(i, valorx2);
                    }

                }

                x1++;
                x2++;
            }

            //aqui já foi feita todas as trocas fora do intervalo pmx
            //troca no intervalo pmx
            for (int i = pos1; i <= pos2; i++) {//troca o meio do pmx
                int tmp = filho1.getCromossomo(i);
                filho1.setCromossomo(i, filho2.getCromossomo(i));
                filho2.setCromossomo(i, tmp);
            }

            filho1.calcularAptidao();
            filho2.calcularAptidao();

            filhos.add(filho1);
            filhos.add(filho2);

        }

        if (debug) {
            System.out.println("Pai 1: \n" + pai1.toString());
            System.out.println("Pai 2: \n" + pai2.toString());
            System.out.println("Filho 1: \n" + filho1.toString());
            System.out.println("Filho 2: \n" + filho2.toString());
        }

        return filhos;
    }

}
