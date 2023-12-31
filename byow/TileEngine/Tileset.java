package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile UNAVATAR = new TETile('@', Color.white, Color.black, "you");
    //public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray, "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FISHA = new TETile('♠', Color.yellow, Color.orange, "fish"); // ❀
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile FISHB = new TETile('♠', Color.green, Color.black, "fish");
    public static final TETile FISHC = new TETile('♠', Color.orange, Color.black, "fish");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile WALL = new TETile('▲', Color.gray, Color.black, "mountain");
    //public static final TETile FISHA = new TETile('♠', Color.green, Color.blue, "fish");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile END = new TETile('❖', Color.yellow, Color.green, "end");
    public static final TETile AVATAR = new TETile('✪', Color.black, Color.red, "you");
}

