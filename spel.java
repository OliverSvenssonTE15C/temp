package client_side;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class spel extends Application {

	public static final double WIDTH = 500;
	public static final double HEIGHT = 500;
	private static Group root;
	private ClientPlayer player;
	static ArrayList<String> stack = new ArrayList<String>();
	private ArrayList<KeyCode> keys = new ArrayList<KeyCode>();

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		player = new ClientPlayer();

		root = new Group();
		Scene scene = new Scene(root, WIDTH, HEIGHT);

		player.setTranslateX(player.getxPos());
		player.setTranslateY(player.getyPos());
		player.setFill(Color.RED);
		AnimationTimer at = new AnimationTimer() {

			@Override
			public void handle(long now) {

				for (KeyCode key : keys) {

					switch (key) {

					case RIGHT:
						player.moveRight();
						break;
					case LEFT:
						player.moveLeft();
						break;
					case UP:
						player.moveUp();
						break;
					case DOWN:
						player.moveDown();
						break;

					}

				}

				while (stack.size() > 0) {
					readInstruction(stack.get(0));
					stack.remove(0);
				}

			}

		};

		at.start();

		scene.setOnKeyPressed(event -> {
			if (!keys.contains(event.getCode())) {
				keys.add(event.getCode());
			}
		});
		scene.setOnKeyReleased(event -> {
			if (keys.contains(event.getCode())) {
				keys.remove(event.getCode());
			}
		});

		root.getChildren().add(player);
	//	System.out.println("INDEX OF PLAYER:" + root.getChildren().indexOf(player));
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});

	}

	private void readInstruction(String line) {
		int index = line.indexOf(')');
		String action = line.substring(1, index);
		//System.out.println(action);

		if (action.equals("NEW PLAYER")) {
			spawnNewLocalPlayer(line.substring(index + 2, line.length() - 1));
		} else if (action.equals("DROP PLAYER")) {
			int i = Integer.parseInt(line.substring(index + 2, line.length() - 1));
			System.out.println("REMOVE:" + i);
			// player.getOtherPlayers().remove(i);
			root.getChildren().remove(i);
	//		System.out.println("DROP PLAYER" + i);
		} else if (action.equals("MOVE RIGHT")) {
			int komma = line.indexOf(',');
			int i = Integer.parseInt(line.substring(index + 2, komma));
			int speed = Integer.parseInt(line.substring(komma + 1, line.length() - 1));
			root.getChildren().get(i).setTranslateX(root.getChildren().get(i).getTranslateX() + speed);
		} else if (action.equals("MOVE LEFT")) {
			int komma = line.indexOf(',');
			int i = Integer.parseInt(line.substring(index + 2, komma));
			int speed = Integer.parseInt(line.substring(komma + 1, line.length() - 1));
			root.getChildren().get(i).setTranslateX(root.getChildren().get(i).getTranslateX() - speed);
		} else if (action.equals("MOVE UP")) {
			int komma = line.indexOf(',');
			int i = Integer.parseInt(line.substring(index + 2, komma));
			int speed = Integer.parseInt(line.substring(komma + 1, line.length() - 1));
			root.getChildren().get(i).setTranslateY(root.getChildren().get(i).getTranslateY() - speed);
		} else if (action.equals("MOVE DOWN")) {
			int komma = line.indexOf(',');
			int i = Integer.parseInt(line.substring(index + 2, komma));
			int speed = Integer.parseInt(line.substring(komma + 1, line.length() - 1));
			root.getChildren().get(i).setTranslateY(root.getChildren().get(i).getTranslateY() + speed);
		}
	}

	private void spawnNewLocalPlayer(String coords) {

		int index = coords.indexOf(',');
		double xPos = Double.parseDouble(coords.substring(0, index));
		double yPos = Double.parseDouble(coords.substring(index + 1));

		Circle newPlayer = new Circle(30);
		newPlayer.setTranslateX(xPos);
		newPlayer.setTranslateY(yPos);

		newPlayer.setFill(Color.YELLOW);
		root.getChildren().add(newPlayer);
		int i = root.getChildren().indexOf(newPlayer);
	//	System.out.println("APP NEW PLAYER AT INDEX " + i);

	}

}
