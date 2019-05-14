package br.com.votacao.Socket_TCP;


import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Socket_Servidor {

	private ServerSocket servidor;
	private Socket cliente;
	private int porta;
	private List<Candidato> candidatos;
	private List<Voto> votos;
	private int cont = 0;

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	public List<Candidato> getCandidatos() {
		return candidatos;
	}

	public void setCandidatos(List<Candidato> candidatos) {
		this.candidatos = candidatos;
	}

	public List<Voto> getVotos() {
		return votos;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}

	public Socket_Servidor(int porta) {
		this.porta = porta;
	}

	public Socket_Servidor(List<Candidato> candidatos, int porta) {
		this.candidatos = candidatos;
		this.porta = porta;
	}

	public Socket_Servidor(int porta, List<Candidato> candidatos, List<Voto> votos) {
		this.porta = porta;
		this.candidatos = candidatos;
		this.votos = votos;
	}

	public void executar() {

		try {

			servidor = new ServerSocket(porta);
			System.out.println("Servidor inicializado na porta: " + porta);

			while (true) {

				cliente = servidor.accept();
				new Thread(new VerificaServer(cliente)).start();
			}

		} catch (Exception w) {
			System.err.println("Erro ao executar o servidor: " + w.getMessage());
		}

	}

	private class VerificaServer implements Runnable {

		private Socket socket;
		private Scanner ler;
		private PrintStream escrever;

		public VerificaServer(Socket socket) {
			this.socket = socket;

			try {
				confRede();
			} catch (Exception e) {
				System.out.println("Erro ao configurar a rede");
			}

		}

		@Override
		public void run() {

			try {

				String ip = socket.getRemoteSocketAddress().toString();
				System.out.println("Conexao com o cliente: " + ip);

				// TODO Quantidade de votos do ip
				for (int i = 0; i < votos.size(); i++) {

					if (votos.get(i).getEleitor().getId().equals(ip)) {
						cont++;
					}
				}

				String numeroDoCandidato;

				// TODO Quantidade de votos é igual a 5
				if (cont == 5) {

					escrever.println("limite");
					escrever.println("Limite de votos excedido!");

				} else {

					// TODO Enviar lista de candidatos
					exibirCandidatos();

					while (ler.hasNext()) {
						numeroDoCandidato = ler.nextLine();

						if (ehCandidatos(numeroDoCandidato)) {

							// TODO Criar o voto
							Voto voto = new Voto();
							Eleitor e = new Eleitor(ip);
							voto.setEleitor(e);
							Candidato c = VerificarCandidato(numeroDoCandidato);
							voto.setCandidado(c);
							votos.add(voto);

							cont = 0;
							for (int i = 0; i < votos.size(); i++) {

								if (votos.get(i).getEleitor().getId().equals(ip)) {
									cont++;
								}
							}

							if (cont == 5) {
								escrever.println("sucesso-limite");
								escrever.println("Voto computado com sucesso!");
								escrever.println("Limite de votos excedido!");
								fecharconexao();
								break;

							} else {
								escrever.println("sucesso");
								escrever.println("Voto computado com sucesso!");
								contabilizarVoto(votos, candidatos, escrever);
							}

						} else {
							escrever.println("erro");
							escrever.println("Erro: Candidato não cadastrado.");
						}
					}

				}

				fecharconexao();

			} catch (SocketException e) {

				System.err.println("Conexão foi encerrada .... ");

			} catch (Exception e) {

				System.out.println("Exception" + e.getMessage());

			} finally {

				System.err.println("\nConexao finalizada: " + socket.getRemoteSocketAddress().toString() + " "
						+ new Date().toString());

			}

		}

		private void confRede() throws IOException {

			ler = new Scanner(socket.getInputStream());

			escrever = new PrintStream(socket.getOutputStream());
		}

		private void exibirCandidatos() {
			escrever.println("11");
			escrever.println("Candidatos: ");

			for (int i = 0; i < candidatos.size(); i++) {
				escrever.println("  " + candidatos.get(i).toString());
			}
		}

		private Candidato VerificarCandidato(String info) {
			for (int i = 0; i < candidatos.size(); i++) {
				if (candidatos.get(i).getNumero() == Integer.parseInt(info)) {
					return candidatos.get(i);
				}
			}
			return null;
		}

		private boolean ehCandidatos(String numero) {
			try {
				for (int i = 0; i < candidatos.size(); i++) {
					if (candidatos.get(i).getNumero() == Integer.parseInt(numero)) {
						return true;
					}
				}
				return false;
			} catch (Exception e) {
				return false;
			}
		}

		private void fecharconexao() throws IOException {
			ler.close();
			escrever.close();
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();

		}
	}

	private void contabilizarVoto(List<Voto> votos, List<Candidato> candidatos, PrintStream escritor) {

		for (Candidato c : candidatos) {

			int cont = 0;

			for (int i = 0; i < votos.size(); i++) {
				if (votos.get(i).getCandidado().getNome().equals(c.getNome())) {
					cont++;
				}
			}
			escritor.println(c.getNome() + " - " + c.getNumero() + " - votos: " + cont);
		}

	}
	public static void main(String[] args) {
		
		Candidato c1 = new Candidato("Lilian", 8686);
		Candidato c2 = new Candidato("Carlos", 4141);
		Candidato c3 = new Candidato("Samuel", 8888);
		Candidato c4 = new Candidato("Ótavio", 6565);
		Candidato c5 = new Candidato("Odete", 1234);
		Candidato c6 = new Candidato("Rosângela", 2609);
		Candidato c7 = new Candidato("Alex", 7575);
		Candidato c8 = new Candidato("Jean", 9495);
		Candidato c9 = new Candidato("Francisco", 3132);
		Candidato c10 = new Candidato("Rubens", 4949);

		List<Candidato> candidatos = new ArrayList<Candidato>();

		candidatos.add(c1);
		candidatos.add(c2);
		candidatos.add(c3);
		candidatos.add(c4);
		candidatos.add(c5);
		candidatos.add(c6);
		candidatos.add(c7);
		candidatos.add(c8);
		candidatos.add(c9);
		candidatos.add(c10);

		List<Voto> votos = new ArrayList<Voto>();

		Socket_Servidor servidor = new Socket_Servidor(8000, candidatos, votos);
		servidor.executar();
	}

	}


