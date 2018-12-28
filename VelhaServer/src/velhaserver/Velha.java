/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package velhaserver;

/**
 *
 * @author guilhermemarx14
 */
public class Velha {

    char tabuleiro[][];
    int tamanho;
    char vitorioso;

    public Velha(int tamanho) {
        this.tamanho = tamanho;
        tabuleiro = new char[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                tabuleiro[i][j] = '.';
            }
        }
    }

    public void vitorioso(char player) {
        System.out.println("" + player + " Ganhou o jogo!");
    }

    public char ganhador(char player1, char player2) {
        int contadorplayer1 = 0;
        int contadorplayer2 = 0;

        //verifica as linhas da matriz
        for (int i = 0; i < tamanho; i++) {
            contadorplayer1 = 0;
            contadorplayer2 = 0;
            for (int j = 0; j < tamanho; j++) {
                if (tabuleiro[i][j] == player1) {
                    contadorplayer1++;
                } else if (tabuleiro[i][j] == player2) {
                    contadorplayer2++;
                }
            }

            if (contadorplayer1 == tamanho) {
                return player1;
            }
            if (contadorplayer2 == tamanho) {
                return player2;
            }
        }

        //verifica as colunas da matriz
        for (int i = 0; i < tamanho; i++) {
            contadorplayer1 = 0;
            contadorplayer2 = 0;
            for (int j = 0; j < tamanho; j++) {
                if (tabuleiro[j][i] == player1) {
                    contadorplayer1++;
                } else if (tabuleiro[j][i] == player2) {
                    contadorplayer2++;
                }
            }

            if (contadorplayer1 == tamanho) {
                return player1;
            }
            if (contadorplayer2 == tamanho) {
                return player2;
            }
        }

        //verifica a diagonal principal
        contadorplayer1 = 0;
        contadorplayer2 = 0;
        for (int i = 0; i < tamanho; i++) {

            if (tabuleiro[i][i] == player1) {
                contadorplayer1++;
            } else if (tabuleiro[i][i] == player2) {
                contadorplayer2++;
            }

        }
        if (contadorplayer1 == tamanho) {
            return player1;
        }
        if (contadorplayer2 == tamanho) {
            return player2;
        }

        //verifica a diagonal secundaria
        contadorplayer1 = 0;
        contadorplayer2 = 0;
        for (int i = 0; i < tamanho; i++) {

            if (tabuleiro[i][tamanho - 1 - i] == player1) {
                contadorplayer1++;
            } else if (tabuleiro[i][tamanho - 1 - i] == player2) {
                contadorplayer2++;
            }
        }
        if (contadorplayer1 == tamanho) {
            return player1;
        }
        if (contadorplayer2 == tamanho) {
            return player2;
        }

        //verifica se deu velha
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (tabuleiro[i][j] == '.') {
                    return '.';//significa que o jogo ainda n acabou
                }
            }
        }
        return 'v';//significa que deu velha e ninguem ganhou
    }

    public boolean jogada(int linha, int coluna, char player) {
        if (linha >= tamanho || coluna >= tamanho) {
            return false;
        }
        if (tabuleiro[linha][coluna] == '.') {
            tabuleiro[linha][coluna] = player;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String retorno = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n|  ";
        for (int i = 0; i < tamanho; i++) {
            retorno += String.format("|%2d", i);
        }
        retorno += "|\n";

        for (int i = 0; i < tamanho; i++) {
            retorno += String.format("|%2d", i);
            for (int j = 0; j < tamanho; j++) {
                retorno += String.format("|%2c", tabuleiro[i][j]);
            }
            retorno += "|\n";
        }
        return retorno;
    }
}
