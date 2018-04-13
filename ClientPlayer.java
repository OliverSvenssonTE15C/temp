package client_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ClientPlayer extends Circle {

	public static String HOST = "192.168.28.242";
	public static int PORT = 12345;

	private PrintStream to_server;
	private BufferedReader from_server;
	private Scanner sc;
	private Socket socket;
	private double xPos, yPos;
	private ArrayList<ClientPlayer> otherPlayers;

	public ClientPlayer() {

		this.setRadius(30);
		this.setFill(Color.RED);

		sc = new Scanner(System.in);

		//System.out.println("CONNECTING TO SERVER " + HOST + " ON PORT " + PORT);

		try {

			socket = new Socket(HOST, PORT);

			to_server = new PrintStream(socket.getOutputStream());
			from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			xPos = Math.random() * spel.WIDTH;
			yPos = Math.random() * spel.HEIGHT;

			to_server.println("[NEW PLAYER][" + xPos + "," + yPos + "]");
		//	System.out.println("[NEW PLAYER][" + xPos + "," + yPos + "]");

		} catch (IOException e) {
			e.printStackTrace();
		}

		Runnable input_r = () -> {

			while (true) {
				String input = sc.nextLine();
				to_server.println(input);
			}

		};

		new Thread(input_r).start();

		Runnable output_r = () -> {

			while (true) {
				try {
					String line = from_server.readLine();
			//		System.out.println("FROM SERVER: " + line);
					if (line.contains("(") && line.contains(")")) {
						serverInput(line);
					}

				} catch (IOException e) {
			//		System.out.println("LOST CONNECTION TO SERVER");
					System.exit(0);
				}
			}

		};

		new Thread(output_r).start();

	}

	private void serverInput(String line) {
		spel.stack.add(line);

	}

	public double getxPos() {
		return xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public ArrayList<ClientPlayer> getOtherPlayers() {
		return otherPlayers;
	}

	public void moveRight() {
		int speed = 3;
		this.setTranslateX(this.getTranslateX() + speed);
		to_server.println("[MOVE RIGHT][" + speed + "]");
	}

	public void moveLeft() {
		int speed = 3;
		this.setTranslateX(this.getTranslateX() - speed);
		to_server.println("[MOVE LEFT][" + speed + "]");

	}

	public void moveUp() {
		int speed = 3;
		this.setTranslateY(this.getTranslateY() - speed);
		to_server.println("[MOVE UP][" + speed + "]");
		
	}
	public void moveDown() {
		int speed = 3;
		this.setTranslateY(this.getTranslateY() + speed);
		to_server.println("[MOVE DOWN][" + speed + "]");
		
	}

}
