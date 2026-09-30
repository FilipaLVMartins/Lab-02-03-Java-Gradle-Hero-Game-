import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {
    private final Screen screen;
    private int x = 10;
    private int y = 10;

    public Game() throws IOException {
        TerminalSize terminalSize = new TerminalSize(40, 20);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize);
        Terminal terminal = terminalFactory.createTerminal();

        this.screen = new TerminalScreen(terminal);
        this.screen.setCursorPosition(null);   // Ocultar o cursor
        this.screen.startScreen();             // Iniciar o ecrã
        this.screen.doResizeIfNecessary();     // Redimensionar se necessário
    }

    private void draw() throws IOException {
        screen.clear();
        screen.setCharacter(x, y, TextCharacter.fromCharacter('X')[0]);
        screen.refresh();
    }

    private void processKey(KeyStroke key) throws IOException {
        System.out.println(key);

        if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
            screen.close();
        }

        switch (key.getKeyType()) {
            case ArrowUp:
                y--;
                break;
            case ArrowDown:
                y++;
                break;
            case ArrowLeft:
                x--;
                break;
            case ArrowRight:
                x++;
                break;
            default:
                break;
        }
    }

    public void run() {
        try {
            while (true) {
                draw();
                KeyStroke key = screen.readInput();

                if (key.getKeyType() == KeyType.EOF) {
                    break;
                }

                processKey(key);

                if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}