package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.algs4.StdDraw;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Interface implements Serializable {
    public static final int W = 80;
    public static final int H = 40;
    static long SEED;
    static String replayPosition;
    TETile[][] board;
    int playerX;
    int playerY;
    TERenderer ter;
    int mouseX = (int) StdDraw.mouseX();
    int mouseY = (int) StdDraw.mouseY();
    ArrayList<String> HUDText = new ArrayList<>();
    int collected = 0;
    boolean playing;
    boolean replaying;
    boolean miniGame;
    Random r;
    boolean createLoaded;

    public Interface() {
        replaying = false;
        miniGame = false;
        r = new Random(SEED);
        ter = new TERenderer();
        ter.initialize(W, H);
        GameStateUtils.Avatar(this);
        HUDText.add("Start Game!");
        HUDText.add("Capture fish to earn the most money!");
    }

    public Interface(long seed) {
        SEED = seed;
        ter = new TERenderer();
        ter.initialize(W, H);

        playerX = 11;
        playerY = 9;

        r = new Random(SEED);

        HUDText.add("Start Game!");
        HUDText.add("Capture fish to earn the most money!");

        replaying = false;
        miniGame = false;
    }

    public static void instructions() {
        StdDraw.clear(Color.BLACK);
        drawFrame("Welcome to the Ocean!");
        StdDraw.pause(1500);
/*        drawFrame("Are you real Fisherman that can handle the cold, dark sea??");
        StdDraw.pause(2500);
        drawFrame("... or are you just a Wannabe?!");
        StdDraw.pause(1500);
        drawFrame("Collect as many fish as possible to prove yourself!");
        StdDraw.pause(2500);
        drawFrame("Reach the green square to end your adventure!");
        StdDraw.pause(2000);*/
    }

    public static void drawFrame(String input) {
        StdDraw.clear(Color.BLACK);
        if (!input.isEmpty()) {
            Font font = new Font("Arial", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(W / 2, H / 2, input);
        }
        StdDraw.show();
    }

    public boolean play(TETile[][] board) throws IOException, ClassNotFoundException {
        miniGame = false;
        boolean playing = true;
        replaying = false;

        Font font = new Font("Arial", Font.BOLD, 14);
        StdDraw.setFont(font);

        int newMouseX;
        int newMouseY;
        HUDMaker(false, board);
//        ter.renderFrame(board);

        while (playing) {
            newMouseX = (int) Math.round(StdDraw.mouseX());
            newMouseY = (int) Math.round(StdDraw.mouseY());
            HUDMaker(true, board);
            cursor(newMouseX, newMouseY);
            if (StdDraw.hasNextKeyTyped()) {
                char move = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (Character.toLowerCase(move) == 'q') {
                    //playing = false;
                    quitGame();
                    drawFrame("Press (N) to return to Menu | Press (R) for Replay | Press (Q) to quit");
                    while (!StdDraw.hasNextKeyTyped()) {
                    }
                    exit();
                    return false;
                } else if (move == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                    }
                    char nextChar = Character.toLowerCase(StdDraw.nextKeyTyped());
                    if (nextChar == 'q') {
                        // return START.board;
                        GameStateUtils.saveGame(board, playerX, playerY);
                        GameStateUtils.saveGameNew(replayPosition, SEED, playerX, playerY);
                        System.exit(0);
                    }
                } else if (move == 'w' || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                    replayPosition += "w";
                    up(board);
                } else if (move == 'a' || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                    replayPosition += "a";
                    left(board);
                } else if (move == 's' || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                    replayPosition += "s";
                    down(board);
                } else if (move == 'd' || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                    replayPosition += "d";
                    right(board);
                }
                HUDMaker(false, board);
                ter.renderFrame(board);
            }
        }
        GameStateUtils.saveGame(board, playerX, playerY);
        GameStateUtils.saveGameNew(replayPosition, SEED, playerX, playerY);
        return playing;
    }

    public void quitGame() {
        StdDraw.clear(StdDraw.WHITE);
        StdDraw.setPenColor(Color.BLUE);
        Font font = new Font("Arial", Font.BOLD, 60);
        StdDraw.setFont(font);
        StdDraw.text(W / 2, H / 2, "Quitting Game ...");
        Font options = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(options);
        StdDraw.text(W / 2, H / 2 - 4, "Replay (R) | New Game (N) | Quit (Q)");
        StdDraw.setFont(new Font("Arial", Font.BOLD, 14));
        StdDraw.show();
    }

    public void cursor(int mX, int mY) {
        if (!(mY == mouseY && mX == mouseX)) {
            if (mX < (W - 1) && mX >= 0 && mY < (H - 1) && mY >= 0) {
                mouseX = mX;
                mouseY = mY;
                HUDMaker(true, board);
                ter.renderFrame(board);
            } else {
                ter.renderFrame(board);
            }
        }
    }

    public void exit() throws IOException, ClassNotFoundException {
        GameStateUtils.saveGame(board, playerX, playerY);
        GameStateUtils.saveGameNew(replayPosition, SEED, playerX, playerY);
        mainMenu();
        while (!StdDraw.isKeyPressed(KeyEvent.VK_DELETE)) {
            while (!StdDraw.hasNextKeyTyped()) {
            }
            char nextTypedKey = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (nextTypedKey == 'n') {
                playing = true;
                Engine.interactWithKeyboard();
            } else if (nextTypedKey == 'r') {
                replaying = true;
                doReplay();
            } else if (nextTypedKey == 'q') {
                playing = false;
                GameStateUtils.saveGame(board, playerX, playerY);
                GameStateUtils.saveGameNew(replayPosition, SEED, playerX, playerY);
                System.exit(0);
            }
        }
    }

    public void doReplay() throws IOException, ClassNotFoundException {
        Engine.generateWorld world = new Engine.generateWorld(SEED);
        TETile[][] newBoard = new TETile[W][H];
        world.worldMaker(newBoard);
        board = newBoard;
        ter.renderFrame(newBoard);
        replayHUD();
        playerX = 11;
        playerY = 9;
        StdDraw.pause(600);
        for (int i = 0; i < replayPosition.length(); i += 1) {
            replayHUD();
            if (replayPosition.charAt(i) == 'w' || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                up(board);
            } else if (replayPosition.charAt(i) == 'a' || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                left(board);
            } else if (replayPosition.charAt(i) == 's' || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                down(board);
            } else if (replayPosition.charAt(i) == 'd' || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                right(board);
            }
            ter.renderFrame(board);
            replayHUD();
            StdDraw.pause(200);
        }
        StdDraw.pause(1000);
        drawFrame("Those were your moves!");
        StdDraw.pause(2500);
        drawFrame("Time to head back to the menu...");
        StdDraw.pause(2000);
        drawFrame("Press (n) to return to the Main Menu");
        StdDraw.pause(100);
    }

    public void createLoaded() throws IOException, ClassNotFoundException {
        createLoaded = true;
        Engine.generateWorld world = new Engine.generateWorld(SEED);
        TETile[][] newBoard = new TETile[W][H];
        world.worldMaker(newBoard);
        board = newBoard;
        playerX = 11;
        playerY = 9;

        for (int i = 0; i < replayPosition.length(); i++) {
            loadGameHUD();
            if (replayPosition.charAt(i) == 'w' || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                up2(board);
            } else if (replayPosition.charAt(i) == 'a' || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                left2(board);
            } else if (replayPosition.charAt(i) == 's' || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                down2(board);
            } else if (replayPosition.charAt(i) == 'd' || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                right2(board);
            }
            createLoaded = false;
            ter.renderFrame(board);
        }
    }

    public void loadGameHUD() {
        StdDraw.clear(Color.BLACK);
        drawFrame("Loading your previous world...");
        StdDraw.pause(100);
    }

    public void miniGame() throws IOException, ClassNotFoundException {
        if (!replaying) {

            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            miniGame = true;

            Font miniGameFont = new Font("Arial", Font.BOLD, 18);
            StdDraw.setFont(miniGameFont);
            StdDraw.setPenColor(Color.WHITE);

            StdDraw.line(0, H - (4 * H / 25), W, H - (4 * H / 25));
            StdDraw.text(W / 2, H - 1.5, "Welcome to Captain's Cove!");
            StdDraw.text(70, H - 1.5, "Fish Collected: " + collected);

            GameStateUtils.saveGame(board, playerX, playerY);
            GameStateUtils.saveGameNew(replayPosition, SEED, playerX, playerY);
            int currentY = playerY;
            int currentX = playerX;
            Font font = new Font("Ariel", Font.BOLD, 25);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.white);

            drawFrame("You caught a golden fish!");
            StdDraw.pause(1500);
            drawFrame("You'll now be transported to the Captain's Cove mini game!");
            StdDraw.pause(1500);
            drawFrame("Collect as many fish as possible in 10 seconds!");
            StdDraw.pause(1500);
            miniGameBoard();
            playerX = W / 4 + 1;
            playerY = H / 4 + 4;
            long end = System.currentTimeMillis() + 10000;
            while (System.currentTimeMillis() < end) {
                int newMouseX;
                int newMouseY;
                newMouseX = (int) StdDraw.mouseX();
                newMouseY = (int) StdDraw.mouseY();
                HUDMaker(true, board);
                cursor(newMouseX, newMouseY);
                if (!replaying) {
                    if (StdDraw.hasNextKeyTyped()) {
                        char nextTypedChar = Character.toLowerCase(StdDraw.nextKeyTyped());
                        if (nextTypedChar == 'w' || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                            up(board);
                        } else if (nextTypedChar == 'a' || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                            left(board);
                        } else if (nextTypedChar == 's' || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                            down(board);
                        } else if (nextTypedChar == 'd' || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                            right(board);
                        }
                        HUDMaker(false, board);
                    }
                }
            }
            drawFrame("Time's up! Now, back to the start room..." + "\n" + "You captured " + collected + " fish!");
            StdDraw.pause(3000);

            playerX = currentX;
            playerY = currentY;
            board = GameStateUtils.loadGame(this);
            miniGame = false;
        }
    }

    public void miniGameBoard() {
        // line
        Font gameFont = new Font("Arial", Font.BOLD, 30);     //  Rahil edit
        // menu text
        StdDraw.clear();
        StdDraw.setFont(gameFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(W / 7, H - 10, "Wannabe Fisherman!");


        TETile[][] seaBoard = new TETile[W][H];

        int mainGameW = r.nextInt(W / 2 - 1) + (W / 2);
        int mainGameH = r.nextInt(H / 2 - 5) + (H / 2);
        int seas;
        for (int y = 0; y < H; y += 1) {
            for (int x = 0; x < W; x += 1) {
                //initalizes board of nothing ness
                seaBoard[x][y] = Tileset.NOTHING;
            }
        }
        for (int y = H / 4; y < mainGameH; y++) {
            for (int x = W / 4; x < mainGameW; x++) {
                if (x == W / 4 || y == H / 4 || x == mainGameW - 1 || y == mainGameH - 1) { // check logic here
                    seaBoard[x][y] = Tileset.WALL;
                } else { // seaBoard[x][y] = Tileset.MOUNTAIN;
                    int next = r.nextInt(30);
                    if (x == W / 4 + 1 && y == H / 4 + 4) {
                        seaBoard[x][y] = Tileset.AVATAR;
                    } else if (next == 0) {
                        seaBoard[x][y] = Tileset.FISHA;
                    } else {
                        seaBoard[x][y] = Tileset.WATER;
                    }
                }
            }
        }
        ter.renderFrame(seaBoard);
        board = seaBoard;
    }

    public boolean checkFishHelper(TETile[][] board, int x, int y) {
            return board[playerX + x][playerY + y] == Tileset.FISHA
                    || board[playerX + x][playerY + y] == Tileset.FISHB
                    || board[playerX + x][playerY + y] == Tileset.FISHC;
        }

    public void up(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX][playerY + 1] == Tileset.WALL) {
            ter.renderFrame(board);
        } else {
            if (board[playerX][playerY + 1] == Tileset.END) {
                endGameSound();
                finalscreen();
            } else if (checkFishHelper(board, 0, 1)) {
                if (miniGame) {
                    collected++;
                    fishCatchSound();
                } else {
                    if (board[playerX][playerY + 1] == Tileset.FISHA) {
                        MGentrySound();
                        board[playerX][playerY + 1] = Tileset.AVATAR;
                        board[playerX][playerY] = Tileset.FLOOR;
                        miniGame();
                    } else {
                        collected++;
                        if (!createLoaded) {
                            fishCatchSound();
                        }
                    }
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX][playerY + 1] = Tileset.AVATAR;
            playerY++;
            if (!createLoaded) {
                ter.renderFrame(board);
            }
        }
    }

    public void left(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX - 1][playerY] == Tileset.WALL) {
            ter.renderFrame(board);
        } else {
            if (board[playerX - 1][playerY] == Tileset.END) {
                endGameSound();
                finalscreen();
            } else if (checkFishHelper(board, -1, 0)) {
                if (miniGame) {
                    collected++;
                    fishCatchSound();
                } else {
                    if (board[playerX - 1][playerY] == Tileset.FISHA) {
                        MGentrySound();
                        board[playerX][playerY] = Tileset.FLOOR;
                        board[playerX - 1][playerY] = Tileset.AVATAR;
                        miniGame();
                    } else {
                        collected++;
                        if (!createLoaded) {
                            fishCatchSound();
                        }
                    }
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX - 1][playerY] = Tileset.AVATAR;
            playerX--;
            if (!createLoaded) {
                ter.renderFrame(board);
            }
        }
    }

    public void down(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX][playerY - 1] == Tileset.WALL) {
            ter.renderFrame(board);
        } else {
            if (board[playerX][playerY - 1] == Tileset.END) {
                endGameSound();
                finalscreen();
            } else if (checkFishHelper(board, 0, -1)) {
                if (miniGame) {
                    collected++;
                    fishCatchSound();
                } else {
                    if (board[playerX][playerY - 1] == Tileset.FISHA) {
                        MGentrySound();
                        board[playerX][playerY] = Tileset.FLOOR;
                        board[playerX][playerY - 1] = Tileset.AVATAR;
                        miniGame();
                    } else {
                        collected++;
                        if (!createLoaded) {
                            fishCatchSound();
                        }
                    }
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX][playerY - 1] = Tileset.AVATAR;
            playerY--;
            if (!createLoaded) {
                ter.renderFrame(board);
            }
        }
    }

    public void right(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX + 1][playerY] == Tileset.WALL) {
            ter.renderFrame(board);
        } else {
            if (board[playerX + 1][playerY] == Tileset.END) {
                endGameSound();
                finalscreen();
            } else if (checkFishHelper(board, 1, 0)) {
                if (miniGame) {
                    collected++;
                    fishCatchSound();
                } else {
                    if (board[playerX + 1][playerY] == Tileset.FISHA) {
                        MGentrySound();
                        board[playerX][playerY] = Tileset.FLOOR;
                        board[playerX + 1][playerY] = Tileset.AVATAR;
                        miniGame();
                    } else {
                        collected++;
                        if (!createLoaded) {
                            fishCatchSound();
                        }
                    }
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX + 1][playerY] = Tileset.AVATAR;
            playerX++;
            if (!createLoaded) {
                ter.renderFrame(board);
            }
        }
    }

    public void up2(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX][playerY + 1] != Tileset.WALL) {
            if (checkFishHelper(board, 0, 1)) {
                if (board[playerX][playerY + 1] == Tileset.FISHA) {
                    board[playerX][playerY] = Tileset.FLOOR;
                    board[playerX][playerY + 1] = Tileset.AVATAR;
                    miniGame();
                } else {
                    collected++;
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX][playerY + 1] = Tileset.AVATAR;
            playerY++;
        }
    }

    public void left2(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX - 1][playerY] != Tileset.WALL) {
            if (checkFishHelper(board, -1, 0)) {
                if (board[playerX - 1][playerY] == Tileset.FISHA) {
                    board[playerX][playerY] = Tileset.FLOOR;
                    board[playerX - 1][playerY] = Tileset.AVATAR;
                    miniGame();
                } else {
                    collected++;
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX - 1][playerY] = Tileset.AVATAR;
            playerX--;
        }
    }

    public void down2(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX][playerY - 1] != Tileset.WALL) {
            if (checkFishHelper(board, 0, -1)) {
                if (board[playerX][playerY - 1] == Tileset.FISHA) {
                    board[playerX][playerY] = Tileset.FLOOR;
                    board[playerX][playerY - 1] = Tileset.AVATAR;
                    miniGame();
                } else {
                    collected++;
                }

            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX][playerY - 1] = Tileset.AVATAR;
            playerY--;
        }
    }

    public void right2(TETile[][] board) throws IOException, ClassNotFoundException {
        if (board[playerX + 1][playerY] != Tileset.WALL) {
            if (checkFishHelper(board, 1, 0)) {
                if (board[playerX + 1][playerY] == Tileset.FISHA) {
                    board[playerX][playerY] = Tileset.FLOOR;
                    board[playerX + 1][playerY] = Tileset.AVATAR;
                } else {
                    collected++;
                }
            }
            board[playerX][playerY] = Tileset.FLOOR;
            board[playerX + 1][playerY] = Tileset.AVATAR;
            playerX++;
        }
    }

    public void mainMenu() {
        StdDraw.setXscale(0, W);
        StdDraw.setYscale(0, H);
        StdDraw.clear(Color.WHITE);

        fishFont();
        for (int i = 3; i < W; i += 3) {
            for (int j = 4; j < H; j += 4) {
                StdDraw.text(i, j, "♠");
            }
        }
        // BLUE RECTANGLE
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.filledRectangle(40, 20, 25, 12.5);
        // TITLE "WANNABE FISHERMAN"
        titleFont();
        StdDraw.text(W / 2, H - 15, "Wannabe Fisherman");

        // SUBTITLE STUFF
        subtitle1Font();
        StdDraw.text(W / 2, H - 19, "By: Henry Yan and Rahil Shaik");
        StdDraw.text(W / 2, H - 23, "Will you be next great Fishermen??");
        // CONTROLS TO START GAME
        subtitleFont();
        StdDraw.text(W / 2, H - 27, "Replay (R) | Load Game (L) | New Game (N) | Quit (Q)");
        StdDraw.show();
    }

    public void fontStyle() {
        Font gameFont = new Font("Arial", Font.BOLD, 28);
        StdDraw.setFont(gameFont);
        StdDraw.setPenColor(Color.BLACK);
    }

    public void titleFont() {
        Font titleFont = new Font("Arial", Font.BOLD, 60);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(titleFont);
    }

    public void fishFont() {
        Font titleFont = new Font("Monaco", Font.BOLD, 45);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.setFont(titleFont);
    }

    public void subtitleFont() {
        Font subtitleFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(subtitleFont);
    }

    public void subtitle1Font() {
        Font subtitle1Font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(subtitle1Font);
    }

    public void seedRequest(String seed) {
        StdDraw.clear(Color.WHITE);
        fontStyle();
        StdDraw.text(W / 2, H / 2 + 5, "ENTER A RANDOM INTEGER SEED:");
        StdDraw.text(W / 2, H / 2 + 2, "HIT (S) TO START | HIT (BACKSPACE) TO GO BACK");
        StdDraw.line(10, H / 2 - 3, W - 10, H / 2 - 3);

        if (!seed.equals("")) {
            Font seedFont = new Font("Arial", Font.PLAIN, 20);
            StdDraw.setFont(seedFont);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.text(W / 2, H / 2 - 2, seed);
        }
        StdDraw.setFont(new Font("Arial", Font.BOLD, 14));
        StdDraw.show();
    }

    public void finalscreen() throws IOException, ClassNotFoundException {
        StdDraw.clear(Color.BLACK);
        Font gameFont = new Font("Arial", Font.BOLD, 28);
        StdDraw.setFont(gameFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 60));
        StdDraw.text(W / 2, H / 2 + 6, "Game Over!");
        StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
        StdDraw.text(W / 2, H / 2, "You collected " + collected + " fish!");
        StdDraw.setFont(new Font("Arial", Font.BOLD, 20));
        StdDraw.text(W / 2, H / 2 - 6, "<10 fish: $");
        StdDraw.text(W / 2, H / 2 - 9, "10-25 fish: $$");
        StdDraw.text(W / 2, H / 2 - 12, ">25 fish: $$$");

        if (collected > 0 && collected < 10) {
            StdDraw.text(W / 2, H / 2 - 15, "You earned a $!");
        } else if (collected >= 10 && collected <= 25) {
            StdDraw.text(W / 2, H / 2 - 15, "You earned $$!");
        } else if (collected > 25) {
            StdDraw.text(W / 2, H / 2 - 15, "You earned $$$!");
        } else if (collected == 0) {
            StdDraw.text(W / 2, H / 2 - 15, "You earned nothing!");
        }
        StdDraw.setFont(new Font("Arial", Font.BOLD, 14));
        StdDraw.show();
        StdDraw.pause(40000);
        drawFrame("Back to main menu! (loading) ");
        StdDraw.pause(5000);
        Engine.interactWithKeyboard();
    }

    public void HUDMaker(boolean mouse, TETile[][] board) {
        HUDtitleFont();
        if (mouse) {
            Font HUDfont = new Font("Arial", Font.PLAIN, 18);
            StdDraw.setFont(HUDfont);
            StdDraw.setPenColor(Color.WHITE);
            mouseTracker();
        } else {
//            StdDraw.clear(Color.BLACK);
            ter.renderFrame(board);
        }
        StdDraw.show();
    }

    public void HUDtitleFont() {
        Font titleFont = new Font("Arial", Font.BOLD, 26);
        StdDraw.setFont(titleFont);
        StdDraw.text(W / 2, H - 1.5, "Navigate the deep sea!");
        StdDraw.text(8.0, H - 1.5, "Fish Collected: " + collected);
        StdDraw.line(0, H - (3 * H / 25), W, H - (3 * H / 25));
    }

    public void miniGameHUD() {
        while (miniGame) {
            Font miniGameFont = new Font("Arial", Font.BOLD, 18);
            StdDraw.setFont(miniGameFont);
            StdDraw.setPenColor(Color.WHITE);

            StdDraw.line(0, H - (4 * H / 25), W, H - (4 * H / 25));
            StdDraw.text(W / 2, H - 1.5, "Welcome to Captain's Cove!");
            StdDraw.text(70, H - 1.5, "Fish Collected: " + collected);

        }

    }

    public void replayHUD() {
        Font font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(W / 2, H / 2, "These were your moves...");
        StdDraw.setFont(new Font("Arial", Font.BOLD, 14));
        StdDraw.show();
    }

    public void mouseTracker() {
        Font mouseFont = new Font("Arial", Font.BOLD, 18);
        StdDraw.setFont(mouseFont);
        StdDraw.setPenColor(Color.WHITE);

        if (!(board[mouseX][mouseY].description().equals("nothing"))) {
            StdDraw.text(70, H - 1.5, "Tile:" + board[mouseX][mouseY].description());
            StdDraw.show();

        } else {
            StdDraw.show();
        }
    }

    public void sideMenu() { //FAILURE
        // line
        Font gameFont = new Font("Arial", Font.BOLD, 28);
        StdDraw.clear();
        StdDraw.setFont(gameFont);
        StdDraw.setPenRadius((double) W / 80);
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.line((double) W / 7, 0, (double) W / 7, H);
        StdDraw.show();

        // menu text
        StdDraw.setFont(gameFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(W / 7, H - 10, "Wannabe Fisherman!");
    }

    public void fishCatchSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("byow/bloop_x.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void MGentrySound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("byow/bubbles_sfx.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void endGameSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("byow/buzzer_x.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

}
