import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;

import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            // Definir o tamanho do terminal (40 colunas x 20 linhas)
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);

            // Criar o terminal e o ecrã
            Terminal terminal = terminalFactory.createTerminal();
            Screen screen = new TerminalScreen(terminal);

            screen.setCursorPosition(null);   // Ocultar o cursor
            screen.startScreen();             // Iniciar o ecrã
            screen.doResizeIfNecessary();     // Redimensionar se necessário

            // Desenhar um 'X' no ecrã
            screen.clear();
            screen.setCharacter(10, 10, TextCharacter.fromCharacter('X')[0]);
            screen.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}