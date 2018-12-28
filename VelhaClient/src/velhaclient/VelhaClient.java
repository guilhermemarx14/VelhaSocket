/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package velhaclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author guilhermemarx14
 */
public class VelhaClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Digite o ip do Servidor: ");
        String IpServer = "";
        String leitura = "";
        Socket conexao = null;

        boolean alguemDormiu = false;
        DataOutputStream saida = null;
        DataInputStream entrada = null;
        String status = "";
        char player;
        int timer;
        try {
            IpServer = in.readLine();
            conexao = new Socket(IpServer, 1996);
            conexao.setSoTimeout(30000);//se o servidor demorar 30 segundos pra responder é porque o outro player dormiu no jogo
            saida = new DataOutputStream(conexao.getOutputStream()); // Canal de saida
            entrada = new DataInputStream(conexao.getInputStream()); // Canal de entrada
        } catch (Exception e) {
            System.out.println("Falha na conexao");
        }
        do {
            try {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                status = entrada.readUTF();
                if (status.equals("Player1")) {

                    System.out.println("Deseja ser X ou O?");
                    timer = 0;
                    while (!in.ready()) {
                        Thread.sleep(500);
                        timer++;
                        if (timer == 60) {
                            throw new Exception();
                        }
                    }
                    leitura = in.readLine();
                    player = leitura.charAt(0);
                    saida.writeUTF(String.format("%c", player));
                }

                System.out.println(entrada.readUTF());//servidor avisa qual o simbolo do player

                status = entrada.readUTF();
                while (!status.equals("fim")) {
                    while (status.equals("continua")) {
                        status = entrada.readUTF();
                    }
                    if (status.equals("fim")) {
                        break;
                    }
                    System.out.println(status);
                    timer = 0;
                    while (!in.ready()) {
                        Thread.sleep(500);
                        timer++;
                        if (timer == 60) {
                            throw new Exception();
                        }
                    }
                    leitura = in.readLine();
                    saida.writeUTF(leitura);
                    status = entrada.readUTF();
                    while (status.equals("continua")) {
                        status = entrada.readUTF();
                    }
                }
                System.out.println(entrada.readUTF());//imprime resultado do jogo

                System.out.println(entrada.readUTF());//pergunta se quer recomeçar o jogo
                timer = 0;
                while (!in.ready()) {
                    Thread.sleep(500);
                    timer++;
                    if (timer == 60) {
                        throw new Exception();
                    }
                }
                leitura = in.readLine();
                saida.writeUTF(String.format("%c", leitura.charAt(0)));//envia resposta ao servidor
                status = entrada.readUTF();//recebe se o jogo vai recomeçar ou nao
            } catch (Exception e) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("Alguém dormiu no jogo...");
                try {
                    System.out.println(entrada.readUTF());//pergunta se quer recomeçar o jogo
                    timer = 0;
                    while (!in.ready()) {
                        Thread.sleep(500);
                        timer++;
                        if (timer == 60) {
                            throw new Exception();
                        }
                    }
                    leitura = in.readLine();
                    saida.writeUTF(String.format("%c", leitura.charAt(0)));//envia resposta ao servidor
                    status = entrada.readUTF();//recebe se o jogo vai recomeçar ou nao
                } catch (Exception f) {
                    System.out.println("O jogo será finalizado porque um dos jogadores não responde.");
                    status = "finaliza";
                }

            }
        } while (status.equals("recomeca"));
        try {
            System.out.println(entrada.readUTF());
            in.close();//fecha a leitura do teclado
            entrada.close();//fecha o canal de entrada da comunicacao
            saida.close();//fecha o canal de saida da comunicacao
            conexao.close();//fecha a conexao com o servidor
        } catch (Exception e) {
        }

    }

}
