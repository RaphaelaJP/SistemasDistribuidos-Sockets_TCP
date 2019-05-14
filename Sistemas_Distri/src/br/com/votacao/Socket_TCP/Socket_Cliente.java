package br.com.votacao.Socket_TCP;

import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Socket_Cliente {
	private Socket conexao;
	private String ip;
	private int porta;

	private Scanner teclado;
	private Scanner entradaServidor;
	private PrintStream saida;

	public Socket getConexao() {
		return conexao;
	}

	public void setConexao(Socket conexao) {
		this.conexao = conexao;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	public void executar() {
		try {
			teclado = new Scanner(System.in);
			System.out.println("Informe IP do Servidor:");
			//ip = teclado.nextLine();
			ip = "127.0.0.1";
			porta = 8000;
			conexao = new Socket(ip, porta);
			System.out.println("Voce se conectou ao Servidor de Votação");

			saida = new PrintStream(conexao.getOutputStream());
			entradaServidor = new Scanner(conexao.getInputStream());

			boolean continuar = true;

			while (continuar) {

				String temp = entradaServidor.nextLine();

				if (temp.equals("erro")) {

					System.out.println(entradaServidor.nextLine());

				} else if (temp.equals("sucesso")) {

					System.out.println(entradaServidor.nextLine());

					resultadoServer(10);

				} else if (temp.equals("sucesso-limite")) {

					System.out.println(entradaServidor.nextLine());
					System.out.println(entradaServidor.nextLine());
					break;

				} else if (temp.equals("limite")) {

					System.out.println(entradaServidor.nextLine());
					break;

				} else {

					int qtd = Integer.parseInt(temp);
					resultadoServer(qtd);

				}

				System.out.println("Deseja continuar? S ou N");

				while (true) {

					temp = teclado.nextLine();

					if (temp.equalsIgnoreCase("S")) {

						System.out.println("Digite o número do candidato:");
						saida.println(teclado.nextLine());
						break;

					} else if (temp.equalsIgnoreCase("N")) {

						System.out.println("Obrigado por votar!!!");
						continuar = false;
						break;

					} else {
						System.out.println("Digite S ou N");
					}

				}

			}

			conexao.shutdownInput();
			conexao.shutdownOutput();
			conexao.close();

		} catch (SocketException e) {
			System.err.println("Falha na conexão!" + e.getMessage());

		} catch (Exception w) {
			System.err.println("Erro: " + w.getMessage());

		}

	}

	private void resultadoServer(int qtd) {
		for (int i = 0; i < qtd; i++) {
			System.out.println(entradaServidor.nextLine());
		}
	}

	public static void main(String[] args) {

		Socket_Cliente clienteSocket = new Socket_Cliente();
		clienteSocket.executar();

	}

}
