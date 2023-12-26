package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

import java.nio.file.Paths;

import java.util.*;

import static byow.Core.Interface.instructions;


//  -  ocean mini game when player collides with a fish in menu
//  -  replay
//  -  player can earn money depending on the number of fish they earn

public class Engine {
    public static final int W = 80;
    public static final int H = 40;
    static boolean isFirstLoad;
    private static TERenderer ter;
    private static Interface START;

    public static void interactWithKeyboard() throws IOException, ClassNotFoundException {
        START = new Interface();
        START.mainMenu();
        isFirstLoad = false;
        String input = "";
        while (!StdDraw.isKeyPressed(KeyEvent.VK_DELETE)) {
            while (!StdDraw.hasNextKeyTyped()) {
            }
            char character = StdDraw.nextKeyTyped();
            input += character;
            if (input.equalsIgnoreCase("n")) { // start a new game
                StringBuilder string = new StringBuilder();
                START.seedRequest(string.toString());
                while (!StdDraw.isKeyPressed(KeyEvent.VK_S)) { // while key pressed is not s (user is not done)
                    while (!StdDraw.hasNextKeyTyped()) {
                    }
                    char nextChar = StdDraw.nextKeyTyped();
                    if (nextChar == KeyEvent.VK_BACK_SPACE) {
                        if (string.length() < 2) {
                            string = new StringBuilder();
                        } else {
                            string = new StringBuilder(string.substring(0, string.length() - 1));
                        }
                        START.seedRequest(string.toString());
                        continue;
                    }
                    string.append(nextChar);
                    START.seedRequest(string.toString());
                }
                interactWithInputString(string.toString());
            } else if (character == ':') {
                while (!StdDraw.hasNextKeyTyped()) {
                }
                char nextChar = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (nextChar == 'q') {
                    // return START.board;
                    GameStateUtils.saveGame(START.board, START.playerX, START.playerY);
                    GameStateUtils.saveGameNew(Interface.replayPosition, Interface.SEED, START.playerX, START.playerY);
                    System.exit(0);
                }
            } else if (input.equalsIgnoreCase("l")) {
                interactWithInputString("L");
            } else if (input.equalsIgnoreCase(("q"))) {
                GameStateUtils.saveGame(START.board, START.playerX, START.playerY);
                GameStateUtils.saveGameNew(Interface.replayPosition, Interface.SEED, START.playerX, START.playerY);
                System.exit(0);
            } else {
                input = "";
            }
        }
    }

    /*public static void interactWithKeyboardLoadGame() throws IOException, ClassNotFoundException {
        //START = new Interface();
        START.mainMenu();
        String input = "";
        while (!StdDraw.isKeyPressed(KeyEvent.VK_DELETE)) {
            while (!StdDraw.hasNextKeyTyped()) {
            }
            input += StdDraw.nextKeyTyped();
            if (input.equalsIgnoreCase("n")) { // start a new game
                StringBuilder string = new StringBuilder();
                START.seedRequest(string.toString());
                while (!StdDraw.isKeyPressed(KeyEvent.VK_S)) { // while key pressed is not s (user is not done)
                    while (!StdDraw.hasNextKeyTyped()) {
                    }
                    char nextChar = StdDraw.nextKeyTyped();
                    if (nextChar == KeyEvent.VK_BACK_SPACE) {
                        if (string.length() < 2) {
                            string = new StringBuilder();
                        } else {
                            string = new StringBuilder(string.substring(0, string.length() - 1));
                        }
                        START.seedRequest(string.toString());
                        continue;
                    }
                    string.append(nextChar);
                    START.seedRequest(string.toString());
                }
                interactWithInputString(string.toString());
            } else if (input.equalsIgnoreCase(("l"))) {
                interactWithInputString("L");
            } else if (input.equalsIgnoreCase(("q"))) {
                //GameStateUtils.saveGame(START.board, START.playerX, START.playerY);
                GameStateUtils.saveGameNew(START.replayPosition, START.SEED, START.playerX, START.playerY);
                System.exit(0);
            } else {
                input = "";
            }
        }
    }*/

//    public static void loadGame(String moves) throws IOException, ClassNotFoundException {
//        int integer = START.collected;
//        START = new Interface();
//        generateWorld world = new generateWorld(START.SEED);
//        START.board = new TETile[W][H];
//        world.worldMaker(START.board);
//        START.createLoaded();
//        START.collected = integer;
//        interactWithKeyboard();
//        isFirstLoad = false;
//    }

    public static void interactWithInputString(String input) throws IOException, ClassNotFoundException {
        long seed;
        ter = new TERenderer();
        boolean worldCreated = false;
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < input.length(); i += 1) {
            char character = input.charAt(i);
            if (character == ':') {
                i++;
                character = input.charAt(i);
                if (Character.toLowerCase(character) == 'q') {
                    // return START.board;
                    GameStateUtils.saveGame(START.board, START.playerX, START.playerY);
                    GameStateUtils.saveGameNew(Interface.replayPosition, Interface.SEED, START.playerX, START.playerY);
                    System.exit(0);
                }
            } else if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
            } else if (Character.toLowerCase(character) == 's' && !worldCreated) {
                instructions();
                seed = Integer.parseInt(string.toString());
                generateWorld world = new generateWorld(seed);
                START = new Interface(seed);
                TETile[][] finalFrame = new TETile[W][H];
                world.worldMaker(finalFrame);
                START.board = finalFrame;
                worldCreated = true;
            } else if (Character.toLowerCase(character) == 'l') {
//                if (START.replayPosition == null) {
//                    Engine.generateWorld world = new Engine.generateWorld(START.SEED);
//                    TETile[][] newBoard = new TETile[W][H];
//                    world.worldMaker(newBoard);
//                    START.board = newBoard;
//                    ter.renderFrame(START.board);
//                    START.playerX = 11;
//                    START.playerY = 9;
//                } else {
//                    if (isFirstLoad) {
//                        START = new Interface();
//                        START.board = GameStateUtils.loadGame();
//                        ter.renderFrame(START.board);
//                    } else {
//                        START.createLoaded();
//                    }
//                }
                if (START.replayPosition == null || isFirstLoad) {
                    String moves = GameStateUtils.fileToString(GameStateUtils.movesFile).substring(4);
                    long seeed = Long.parseLong(GameStateUtils.fileToString(GameStateUtils.seedFile));
                    START = new Interface(seeed);
                    START.replayPosition = moves;
                    START.board = GameStateUtils.loadGame(START);
                    GameStateUtils.Avatar(START);
                    ter.renderFrame(START.board);
                    isFirstLoad = false;
                } else {
                    START.createLoaded();
                }
                worldCreated = true;
            } else if (Character.toLowerCase(character) == 'a') {
                START.left(START.board);
            } else if (Character.toLowerCase(character) == 'w') {
                START.up(START.board);
            } else if (Character.toLowerCase(character) == 's') {
                START.down(START.board);
            } else if (Character.toLowerCase(character) == 'd') {
                START.right(START.board);
            } else {
                string.append(character);
            }
        }

        ter.renderFrame(START.board);
        START.HUDMaker(true, START.board);
        START.play(START.board);
        // return START.board;
    }

    static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(W, H, 0, 0);
        ter.renderFrame(START.board);
    }

    public static class generateWorld implements Serializable {
        private final long seed;
        Random r;
        int roomNumber = 0;
        HashMap<Integer, Integer> rnToX = new HashMap<>();
        HashMap<Integer, Integer> rnToY = new HashMap<>();

        public generateWorld(long s) {
            seed = s;
            r = new Random(seed);
        }

        public void worldMaker(TETile[][] board) {
            if (board[0][0] == null) {
                boardMaker(board);
            }
        }

        public void boardMaker(TETile[][] board) {
            for (int y = 0; y < H; y += 1) {
                for (int x = 0; x < W; x += 1) {
                    board[x][y] = Tileset.NOTHING;
                }
            }
            roomMaker(board);
            roomConnector(board, rnToX, rnToY);
            wallMaker(board);
            beachFishPlacer(board);
            board[11][9] = Tileset.AVATAR;
        }

        // create random rooms of random size in random places
        public void roomMaker(TETile[][] board) {
            for (int x = 5; x < W; x += 17 + r.nextInt(8)) {
                for (int y = 5; y < H; y += 11 + r.nextInt(8)) { // random room size

                    // rooms are given a start point and length/width for each of it's x and y dimensions
                    int startX = x + r.nextInt(6);
                    int startY = y + r.nextInt(5);

                    int sizeX = 8 + r.nextInt(6);
                    int sizeY = 5 + r.nextInt(4);
                    if (notOB(startX, sizeX, startY, sizeY) &&
                            noOL(board, startX, startX + sizeX, startY, startY + sizeY)) {
                        for (int x1 = startX; x1 < startX + sizeX; x1 += 1) {
                            for (int y1 = startY; y1 < startY + sizeY; y1 += 1) {
                                board[x1][y1] = Tileset.FLOOR;
                            }
                        }
                        roomNumber++;
                        rnToX.put(roomNumber, startX + sizeX / 2);
                        rnToY.put(roomNumber, startY + sizeY / 2);
                    }
                }
            }
        }

        public void roomConnector(TETile[][] board, HashMap rnToX, HashMap rnToY) {
            ArrayList<Double> dists = new ArrayList<>();
            HashMap<Double, Integer> tracer = new HashMap<>();

            for (int i = 1; i <= rnToX.size(); i += 1) {
                for (int j = 1; j <= rnToY.size(); j += 1) {
                    if (i == j) {
                        continue;
                    }
                    Double dist = Math.sqrt(((((int) rnToX.get(i) - (int) rnToX.get(j)) ^ 2) / 2) + ((int) rnToY.get(i) - (int) rnToY.get(j)) ^ 2);
                    tracer.put(dist, j);
                    dists.add(dist);
                }
                Collections.sort(dists);
                roomConnectorHelper(board, i, tracer.get(dists.get(0)));
                int rand = r.nextInt(3);
                if (rand == 2) {
                    roomConnectorHelper(board, i, tracer.get(dists.get(1)));
                }
            }
        }

        public void makeHallway(TETile[][] board, int startX, int startY, int length, String type) {
            if (type == "upright") {
                for (int y = startY; y < startY + length; y += 1) {
                    board[startX][y] = Tileset.FLOOR;
                }
            }
            if (type == "sideways") {
                for (int x = startX; x < startX + length; x += 1) {
                    board[x][startY] = Tileset.FLOOR;
                }
            }
        }

        public void wallMaker(TETile[][] board) {
            for (int i = 0; i < W; i++) {
                for (int j = 0; j < H; j++) {
                    if ((board[i][j] == Tileset.FLOOR) && (board[i][j - 1] == Tileset.NOTHING) && (board[i][j + 1] == Tileset.NOTHING)) {
                        if ((board[i + 1][j] == Tileset.NOTHING)) {
                            board[i + 1][j] = Tileset.WALL;
                        } else if ((board[i - 1][j] == Tileset.NOTHING)) {
                            board[i - 1][j] = Tileset.WALL;
                        } else {
                            board[i][j - 1] = Tileset.WALL;
                            board[i][j + 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j + 1] == Tileset.NOTHING) {
                            board[i - 1][j + 1] = Tileset.WALL;
                        }
                        if (board[i + 1][j - 1] == Tileset.NOTHING) {
                            board[i + 1][j - 1] = Tileset.WALL;
                        }
                        if (board[i + 1][j + 1] == Tileset.NOTHING) {
                            board[i + 1][j + 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j - 1] == Tileset.NOTHING) {
                            board[i - 1][j - 1] = Tileset.WALL;
                        }

                    } else if ((board[i][j] == Tileset.FLOOR) && (board[i + 1][j] == Tileset.NOTHING) && (board[i - 1][j] == Tileset.NOTHING)) {
                        if ((board[i][j + 1] == Tileset.NOTHING)) {
                            board[i][j + 1] = Tileset.WALL;
                        } else if ((board[i][j - 1] == Tileset.NOTHING)) {
                            board[i][j - 1] = Tileset.WALL;
                        } else {
                            board[i - 1][j] = Tileset.WALL;
                            board[i + 1][j] = Tileset.WALL;
                        }
                        if (board[i + 1][j + 1] == Tileset.NOTHING) {
                            board[i + 1][j + 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j - 1] == Tileset.NOTHING) {
                            board[i - 1][j - 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j + 1] == Tileset.NOTHING) {
                            board[i - 1][j + 1] = Tileset.WALL;
                        }
                        if (board[i + 1][j - 1] == Tileset.NOTHING) {
                            board[i + 1][j - 1] = Tileset.WALL;
                        }
                    } else if ((board[i][j] == Tileset.FLOOR) && (board[i][j + 1] == Tileset.NOTHING)) {
                        if (board[i + 1][j] == Tileset.NOTHING) {
                            board[i + 1][j] = Tileset.WALL;
                        }
                        if (board[i][j + 1] == Tileset.NOTHING) {
                            board[i][j + 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j + 1] == Tileset.NOTHING) {
                            board[i - 1][j + 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j] == Tileset.NOTHING) {
                            board[i - 1][j] = Tileset.WALL;
                        }
                        if (board[i + 1][j + 1] == Tileset.NOTHING) {
                            board[i + 1][j + 1] = Tileset.WALL;
                        }
                        board[i][j + 1] = Tileset.WALL;
                    } else if ((board[i][j] == Tileset.FLOOR) && (board[i][j - 1] == Tileset.NOTHING)) {
                        board[i][j - 1] = Tileset.WALL;
                        if (board[i - 1][j - 1] == Tileset.NOTHING) {
                            board[i - 1][j - 1] = Tileset.WALL;
                        }
                        if (board[i - 1][j] == Tileset.NOTHING) {
                            board[i - 1][j] = Tileset.WALL;
                        }
                    } else if ((board[i][j] == Tileset.FLOOR) && (board[i - 1][j] == Tileset.NOTHING)) {
                        board[i - 1][j] = Tileset.WALL;
                    } else if ((board[i][j] == Tileset.FLOOR) && (board[i + 1][j] == Tileset.NOTHING)) {
                        if (board[i][j - 1] == Tileset.NOTHING) {
                            board[i][j - 1] = Tileset.WALL;
                        }
                        if (board[i + 1][j - 1] == Tileset.NOTHING) {
                            board[i + 1][j - 1] = Tileset.WALL;
                        }
                        board[i + 1][j] = Tileset.WALL;
                    }
                    if ((board[i][j] == Tileset.FLOOR) && (board[i + 1][j + 1] == Tileset.NOTHING)) {
                        board[i + 1][j + 1] = Tileset.WALL;
                    }
                    if ((board[i][j] == Tileset.FLOOR) && (board[i - 1][j - 1] == Tileset.NOTHING)) {
                        board[i - 1][j - 1] = Tileset.WALL;
                    }
                    if ((board[i][j] == Tileset.FLOOR) && (board[i - 1][j + 1] == Tileset.NOTHING)) {
                        board[i - 1][j + 1] = Tileset.WALL;
                    }
                    if ((board[i][j] == Tileset.FLOOR) && (board[i + 1][j - 1] == Tileset.NOTHING)) {
                        board[i + 1][j - 1] = Tileset.WALL;
                    }
                }
            }
        }

        public void roomConnectorHelper(TETile[][] board, int room1, int room2) {
            int room1X = rnToX.get(room1);
            int room2X = rnToX.get(room2);
            int room1Y = rnToY.get(room1);
            int room2Y = rnToY.get(room2);
            if (room2X > room1X && room2Y > room1Y) {
                makeHallway(board, room1X, room1Y, room2Y - room1Y, "upright");
                makeHallway(board, room1X, room2Y, room2X - room1X, "sideways");
                board[room1X][room2Y] = Tileset.FLOOR;
            } else if (room2X > room1X && room2Y < room1Y) {
                makeHallway(board, room2X, room2Y, room1Y - room2Y, "upright");
                makeHallway(board, room1X, room1Y, room2X - room1X, "sideways");
                board[room2X][room1Y] = Tileset.FLOOR;
            } else if (room2X < room1X && room2Y > room1Y) {
                makeHallway(board, room1X, room1Y, room2Y - room1Y, "upright");
                makeHallway(board, room2X, room2Y, room1X - room2X, "sideways");
                board[room1X][room2Y] = Tileset.FLOOR;
            } else if (room2X > room1X && room2Y < room1Y) {
                makeHallway(board, room2X, room2Y, room1Y - room2Y, "upright");
                makeHallway(board, room1X, room1Y, room2X - room1X, "sideways");
                board[room2X][room1Y] = Tileset.FLOOR;
            }
        }

        public void beachFishPlacer(TETile[][] board) {
//            boolean hasEndTile = false;
            for (int i = 0; i < W; i++) {
                for (int j = 0; j < H; j++) {
                    if (board[i][j] == Tileset.FLOOR) {
                        int random = r.nextInt(5);
                        if (random == 0) {
                            int random2 = r.nextInt(21);
                            if (random2 == 0) {
                                board[i][j] = Tileset.FISHA;
                            } else if (random2 <= 10) {
                                board[i][j] = Tileset.FISHB;
                            } else {
                                board[i][j] = Tileset.FISHC;
                            }
                        } else if (random < 3) {
                            board[i][j] = Tileset.SAND;
                        } else {
                            board[i][j] = Tileset.WATER;
                        }
                    }
                }
            }
            board[rnToX.get(roomNumber)][rnToY.get(roomNumber)] = Tileset.END;
        }

        public boolean notOB(int startX, int sizeX, int startY, int sizeY) {
            return startX + sizeX < W - 2 && startY + sizeY < H - 8;
        }

        public boolean noOL(TETile[][] board, int x1, int x2, int y1, int y2) {
            for (int y = y1 - 1; y <= y2; y += 1) {
                for (int x = x1 - 1; x <= x2; x += 1) {
                    if (board[x][y] == Tileset.WALL) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
