package byow.Core;

import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;


class GameStateUtils {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File BYOW_DIR = GameStateUtils.join(CWD, "byow");
    public static final File CURRENT_GAME = GameStateUtils.join(BYOW_DIR, "game.txt");
    public static final File X = GameStateUtils.join(BYOW_DIR, "save_x.txt");
    public static final File Y = GameStateUtils.join(BYOW_DIR, "save_y.txt");
    public static final File movesFile = GameStateUtils.join(BYOW_DIR, "moves.txt");
    public static final File seedFile = GameStateUtils.join(BYOW_DIR, "seedFile.txt");


    public static void saveGame(TETile[][] board, int x, int y) throws IOException {
        CURRENT_GAME.createNewFile();

        FileOutputStream fs = new FileOutputStream(CURRENT_GAME);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(board);
        os.close();

        writeContents(X, String.valueOf(x));
        writeContents(Y, String.valueOf(y));
    }

    public static void saveGameNew(String moves, long seed, int x, int y) throws IOException {
        movesFile.createNewFile();

        writeContents(X, String.valueOf(x));
        writeContents(Y, String.valueOf(y));
        writeContents(movesFile, String.valueOf(moves));
        writeContents(seedFile, String.valueOf(seed));
    }

    // OLD LOAD GAME
    public static TETile[][] loadGame(Interface start) throws IOException, ClassNotFoundException {
//        if (CURRENT_GAME.exists()) {
//            InputStream fs = new FileInputStream(CURRENT_GAME);
//            ObjectInputStream os = new ObjectInputStream(fs);
//            TETile[][] world = (TETile[][]) os.readObject();
//            os.close();
//            return world;
//        }
//        return null;
        start.createLoaded = true;
        Engine.generateWorld world = new Engine.generateWorld(start.SEED);
        TETile[][] newBoard = new TETile[start.W][start.H];
        world.worldMaker(newBoard);
        start.board = newBoard;
        start.playerX = 11;
        start.playerY = 9;

        for (int i = 0; i < start.replayPosition.length(); i++) {
            start.loadGameHUD();
            if (start.replayPosition.charAt(i) == 'w' || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                start.up2(start.board);
            } else if (start.replayPosition.charAt(i) == 'a' || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                start.left2(start.board);
            } else if (start.replayPosition.charAt(i) == 's' || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                start.down2(start.board);
            } else if (start.replayPosition.charAt(i) == 'd' || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                start.right2(start.board);
            }
            start.createLoaded = false;
            start.ter.renderFrame(start.board);
        }
        return start.board;
    }

    //OUR OLD LOAD METHOD
//    public static TETile[][] loadGame(String input) throws IOException, ClassNotFoundException {
//        if (CURRENT_GAME.exists()) {
//            Engine.generateWorld world = new Engine.generateWorld(Interface.SEED);
//            TETile[][] newBoard = new TETile[80][40];
//            world.worldMaker(newBoard);
//            TETile[][] board = newBoard;
//            // CHANGE MOVES TO FLOOR TILS
//            //Engine.interactWithInputString(input);
//            Engine.interactWithKeyboardLoadGame(input);
//            return board;
//        }
//        return null;
//    }

    public static void Avatar(Interface game) {
        game.playerX = Integer.parseInt(fileToString(X));
        game.playerY = Integer.parseInt(fileToString(Y));
    }

    static String fileToString(File file) {
        System.out.println(file);
        return new String(readFile(file), StandardCharsets.UTF_8);
    }

    static byte[] readFile(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("That's not the right kind of file!");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw new IllegalArgumentException("Directory not be overwritten");
            }
            BufferedOutputStream str = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (!(obj instanceof byte[])) {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                } else {
                    str.write((byte[]) obj);
                }
            }
            str.close();
        } catch (IOException | ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
