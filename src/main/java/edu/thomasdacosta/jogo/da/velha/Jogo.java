package edu.thomasdacosta.jogo.da.velha;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Jogo {
	
	private static final String RESET = "\u001B[0m";
	private static final String VERMELHO = "\u001B[31m";
	private static final String VERDE = "\u001B[32m";
	private static final String AMARELO = "\u001B[33m";
	private static final String AZUL = "\u001B[34m";
	private static final String ROXO = "\u001B[35m";
	private static final String BRANCO = "\u001B[37m";

	private static final char SEM_JOGADA = '*';
	private static final char JOGADOR_1 = 'X';
	private static final char JOGADOR_2 = 'O';
	private static final char EMPATE = 'E';

	private static Integer linha = 0;
	private static Integer coluna = 0;
	private static Boolean jogada = true;
	private static Boolean computador = false;
	private static char vencedor = SEM_JOGADA;
	private static final Scanner scanner = new Scanner(System.in);
	
	private static final char[][] jogo = {
							{ SEM_JOGADA, SEM_JOGADA, SEM_JOGADA },
							{ SEM_JOGADA, SEM_JOGADA, SEM_JOGADA },
							{ SEM_JOGADA, SEM_JOGADA, SEM_JOGADA } };

	public static void main(String[] args) {
		try {
			limparTela();
			jogoContraComputador(args);

			limparTela();
			while (mostrarMenu(scanner) != 2) {
				do {
					limparTela();
					exibirJogo();
					escolherJogada(scanner);

					if (!fazerJogada(jogada(jogada)))
						continue;

					jogada = !jogada;
				} while ((vencedor = vencedor()) == SEM_JOGADA);

				limparTela();
				mostrarVencedor();
				exibirJogo();
				resetarJogo();

				System.out.println(VERDE + "Pressione uma tecla para continuar...");
				System.in.read();

				limparTela();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println(VERMELHO + "######### SAIU DO JOGO #########");
		System.out.println(RESET);
		scanner.close();
	}

	public static void jogoContraComputador(String[] args) throws IOException {
		if (args.length == 1) {
			if (args[0].equals("computador")) {
				computador = true;
				System.out.println(VERDE + "Jogo contra o Computador");
				System.out.println(VERDE + "Pressione uma tecla para continuar...");
				System.in.read();
			}
		}
	}

	public static int mostrarMenu(Scanner scanner) {
		System.out.println(AZUL + "######### JOGO DA VELHA #########");
		System.out.println(VERDE + "1 - JOGAR");
		System.out.println(VERMELHO + "2 - SAIR");
		System.out.println(AMARELO + "Escolha uma opção: ");
		System.out.print(BRANCO);
		scanner.reset();
		return scanner.nextInt();
	}

	public static void exibirJogo() {
		int linha = 1;

		System.out.println(BRANCO + "    1   2   3");
		for (int i = 0; i <= 2; i++) {
			System.out.println(BRANCO + "  -------------");
			for (int j = 0; j <= 2; j++) {
				if (j == 0) {
					System.out.print(linha);
					linha++;
				}
				System.out.print(BRANCO + " | " + corJogador(jogo[i][j]) + jogo[i][j]);
			}
			System.out.println(BRANCO + " |");
		}
		System.out.println(BRANCO + "  -------------");
	}
	
	public static String corJogador(char jogador) {
		if (jogador == JOGADOR_1)
			return AZUL;
		else if (jogador == JOGADOR_2)
			return ROXO;
		else 
			return BRANCO;
	}

	public static void escolherJogada(Scanner scanner) {
		Random random = new Random();
		System.out.println(AMARELO + "Escolha a linha - " + jogada(jogada));
		System.out.print(BRANCO);
		if (jogada || !computador) {
			scanner.reset();
			linha = scanner.nextInt();
		} else {
			linha = random.nextInt(1, 4);
		}

		System.out.println(AMARELO + "Escolha a coluna - " + jogada(jogada));
		System.out.print(BRANCO);
		if (jogada || !computador) {
			scanner.reset();
			coluna = scanner.nextInt();
		} else {
			coluna = random.nextInt(1, 4);
		}
	}

	public static boolean fazerJogada(char jogada) throws IOException {
		if (jogo[linha - 1][coluna - 1] == SEM_JOGADA) {
			jogo[linha - 1][coluna - 1] = jogada;
			return true;
		} else {
			if (!computador) {
				System.out.print(VERMELHO + "JOGADA INVÁLIDA.");
				System.in.read();
			} else {
				if (Jogo.jogada) {
					System.out.print(VERMELHO + "JOGADA INVÁLIDA.");
					System.in.read();
				}
			}
			return false;
		}
	}

	public static char jogada(Boolean jogada) {
		return jogada ? JOGADOR_1 : JOGADOR_2;
	}

	public static char vencedor() {
		int linha;
		int coluna;
		int diagonal1;
		int diagonal2;
		char resultado;

		for (int i = 0; i <= 2; i++) {
			linha = jogo[i][0] + jogo[i][1] + jogo[i][2];
			if ((resultado = validarVencedor(linha)) != SEM_JOGADA) {
				return resultado;
			}
		}

		for (int j = 0; j <= 2; j++) {
			coluna = jogo[0][j] + jogo[1][j] + jogo[2][j];
			if ((resultado = validarVencedor(coluna)) != SEM_JOGADA) {
				return resultado;
			}
		}

		diagonal1 = jogo[0][0] + jogo[1][1] + jogo[2][2];
		if ((resultado = validarVencedor(diagonal1)) != SEM_JOGADA) {
			return resultado;
		}

		diagonal2 = jogo[2][0] + jogo[1][1] + jogo[0][2];
		if ((resultado = validarVencedor(diagonal2)) != SEM_JOGADA) {
			return resultado;
		}
		
		int empate = 0;
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				if (jogo[i][j] != SEM_JOGADA)
					empate += 1;
			}
		}
		
		if (empate >= 9)
			resultado = EMPATE;

		return resultado;
	}

	public static char validarVencedor(Integer valor) {
		if (valor == 264)
			return JOGADOR_1;

		if (valor == 237)
			return JOGADOR_2;

		return SEM_JOGADA;
	}

	public static void mostrarVencedor() {
		if (vencedor == EMPATE)
			System.out.println(AMARELO + "######### JOGO EMPATADO #########");
		else
			System.out.println(VERDE + "######### JOGO ACABOU. O VENCEDOR É '" + vencedor + "' #########");
	}

	public static void resetarJogo() {
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
				jogo[i][j] = SEM_JOGADA;
			}
		}
		
		linha = 0;
		coluna = 0;
		jogada = true;
		vencedor = SEM_JOGADA;
	}
	
	public static void limparTela() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
	}

}
