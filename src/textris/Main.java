package textris;

import textris.core.Game;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching textris...");

        Game game = new Game();

        game.start();

        System.out.println("Game Ended");
    }
}
