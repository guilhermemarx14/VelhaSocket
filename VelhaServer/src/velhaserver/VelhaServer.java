/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package velhaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author guilhermemarx14
 */
public class VelhaServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int tamanho = 3;
        char player1;
        char player2;
        String leitura;
        String status = "";
        //System.out.println("Digite o tamanho do tabuleiro: ");
        //tamanho = in.nextInt();
        ServerSocket servidor = null;
        Velha tabuleiro = null;
        Socket conexaoPlayer1 = null, conexaoPlayer2 = null;
        DataOutputStream saidaPlayer1 = null, saidaPlayer2 = null;
        DataInputStream entradaPlayer1 = null, entradaPlayer2 = null;
        try {
            servidor = new ServerSocket(1996);
            conexaoPlayer1 = servidor.accept();
            conexaoPlayer1.setSoTimeout(30000);
            saidaPlayer1 = new DataOutputStream(conexaoPlayer1.getOutputStream()); // Canal de saida
            entradaPlayer1 = new DataInputStream(conexaoPlayer1.getInputStream()); // Canal de entrada

            conexaoPlayer2 = servidor.accept();
            conexaoPlayer2.setSoTimeout(30000);
            saidaPlayer2 = new DataOutputStream(conexaoPlayer2.getOutputStream()); // Canal de saida
            entradaPlayer2 = new DataInputStream(conexaoPlayer2.getInputStream()); // Canal de entrada
        } catch (Exception e) {
            System.out.println("Falha na conexão com os clientes");
        }
        do {
            try {
                tabuleiro = new Velha(tamanho);
                saidaPlayer1.writeUTF("Player1");
                saidaPlayer2.writeUTF("Player2");
                leitura = entradaPlayer1.readUTF();
                player1 = leitura.charAt(0);
                player2 = (player1 == 'X') ? 'O' : 'X';
                saidaPlayer1.writeUTF("Voce e' o " + player1);
                saidaPlayer2.writeUTF("Voce e' o " + player2);

                saidaPlayer1.writeUTF("continua");
                saidaPlayer2.writeUTF("continua");
                char resultado = tabuleiro.ganhador(player1, player2);
                while (resultado == '.') {
                    saidaPlayer1.writeUTF(tabuleiro + "\nPlayer 1 faça sua jogada: ");

                    leitura = entradaPlayer1.readUTF();
                    tabuleiro.jogada(Integer.parseInt(leitura.split(",")[0]), Integer.parseInt(leitura.split(",")[1]), player1);
                    resultado = tabuleiro.ganhador(player1, player2);
                    if (resultado != '.') {
                        saidaPlayer1.writeUTF("fim");
                        saidaPlayer2.writeUTF("fim");
                        break;
                    }
                    saidaPlayer1.writeUTF("continua");
                    saidaPlayer2.writeUTF("continua");
                    saidaPlayer2.writeUTF(tabuleiro + "\nPlayer 2 faça sua jogada: ");

                    leitura = entradaPlayer2.readUTF();
                    try {
                        int teste = Integer.parseInt(leitura.split(",")[0]);
                        teste = Integer.parseInt(leitura.split(",")[1]);
                    } catch (Exception H) {
                    }//jogador fez uma jogada inválida (digitou letra no lugar de numero ou n colocou linha coluna da forma certa)

                    tabuleiro.jogada(Integer.parseInt(leitura.split(",")[0]), Integer.parseInt(leitura.split(",")[1]), player2);
                    //se o jogador escolheu linha coluna q não pode jogar, a jogada dele é desconsiderada (olhar função jogada na classe Velha)
                    resultado = tabuleiro.ganhador(player1, player2);
                    if (resultado != '.') {
                        saidaPlayer1.writeUTF("fim");
                        saidaPlayer2.writeUTF("fim");
                        break;
                    }
                    saidaPlayer1.writeUTF("continua");
                    saidaPlayer2.writeUTF("continua");
                }

                String resultadoGame = tabuleiro.toString();
                if (resultado == player1) {
                    resultadoGame += "\nPlayer 1 ganhou!!";
                } else if (resultado == player2) {
                    resultadoGame += "\nPlayer 2 ganhou!!";
                } else {
                    resultadoGame += "\nDEU VELHA!!";
                }

                saidaPlayer1.writeUTF(resultadoGame);
                saidaPlayer2.writeUTF(resultadoGame);

                saidaPlayer1.writeUTF("\nDeseja iniciar um novo jogo? s-sim, qualquer outra coisa-não");
                saidaPlayer2.writeUTF("\nDeseja iniciar um novo jogo? s-sim, qualquer outra coisa-não");
                status = "recomeca";
                leitura = entradaPlayer1.readUTF();
                if (leitura.charAt(0) != 's') {
                    status = "n";
                }
                leitura = entradaPlayer2.readUTF();
                if (leitura.charAt(0) != 's') {
                    status = "n";
                }
                if (status.equals("recomeca")) {
                    saidaPlayer1.writeUTF("recomeca");
                    saidaPlayer2.writeUTF("recomeca");
                } else {
                    saidaPlayer1.writeUTF("finaliza");
                    saidaPlayer2.writeUTF("finaliza");
                }
            } catch (Exception e) {
                try {
                    saidaPlayer1.writeUTF("\nDeseja iniciar um novo jogo? s-sim, qualquer outra coisa-não");
                    saidaPlayer2.writeUTF("\nDeseja iniciar um novo jogo? s-sim, qualquer outra coisa-não");
                    status = "recomeca";
                    leitura = entradaPlayer1.readUTF();
                    if (leitura.charAt(0) != 's') {
                        status = "n";
                    }
                    leitura = entradaPlayer2.readUTF();
                    if (leitura.charAt(0) != 's') {
                        status = "n";
                    }
                    if (status.equals("recomeca")) {
                        saidaPlayer1.writeUTF("recomeca");
                        saidaPlayer2.writeUTF("recomeca");
                    } else {
                        saidaPlayer1.writeUTF("finaliza");
                        saidaPlayer2.writeUTF("finaliza");
                    }
                } catch (Exception f) {
                    System.out.println("O jogo foi finalizado porque algum player deixou de responder.");
                    status = "finaliza";
                }
            }
        } while (status.equals("recomeca"));
        try {
            saidaPlayer1.writeUTF("Até a próxima!");
            saidaPlayer2.writeUTF("Até a próxima!");

            entradaPlayer1.close();
            entradaPlayer2.close();
            saidaPlayer1.close();
            saidaPlayer2.close();
            conexaoPlayer1.close();
            conexaoPlayer2.close();
        } catch (Exception f) {
        }
    }

}
