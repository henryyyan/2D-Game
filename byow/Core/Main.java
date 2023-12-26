package byow.Core;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            engine.interactWithInputString(args[1]);
            System.out.println(engine);
        } else {
            Engine engine = new Engine();
            engine.isFirstLoad = true;
            engine.interactWithKeyboard();
        }
    }
}
