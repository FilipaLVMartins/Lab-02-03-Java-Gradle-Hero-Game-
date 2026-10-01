import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    private final int width;
    private final int height;
    private final Hero hero;
    private final List<Wall> walls;
    private final List<Coin> coins;
    private final List<Monster> monsters;

    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.hero = new Hero(10, 10);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();
    }

    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();

        for (int c = 0; c < width; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }

        for (int r = 1; r < height - 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }

        return walls;
    }

    private List<Coin> createCoins() {
        Random random = new Random();
        List<Coin> coins = new ArrayList<>();

        while (coins.size() < 5) {
            Position pos = new Position(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);

            // Garantir que a moeda não fica em cima do herói nem de outra moeda
            if (!pos.equals(hero.getPosition()) && coins.stream().noneMatch(c -> c.getPosition().equals(pos))) {
                coins.add(new Coin(pos.getX(), pos.getY()));
            }
        }
        return coins;
    }

    private List<Monster> createMonsters() {
        Random random = new Random();
        List<Monster> monsters = new ArrayList<>();

        while (monsters.size() < 3) {
            Position pos = new Position(random.nextInt(width - 2) + 1, random.nextInt(height - 2) + 1);

            // Garantir que o monstro não nasce em cima do herói nem de outro monstro
            if (!pos.equals(hero.getPosition()) && monsters.stream().noneMatch(m -> m.getPosition().equals(pos))) {
                monsters.add(new Monster(pos.getX(), pos.getY()));
            }
        }
        return monsters;
    }

    private boolean canMove(Position position) {
        if (position.getX() < 0 || position.getX() >= width ||
                position.getY() < 0 || position.getY() >= height) {
            return false;
        }

        for (Wall wall : walls) {
            if (wall.getPosition().equals(position)) {
                return false;
            }
        }

        return true;
    }

    public void moveHero(Position position) {
        if (canMove(position)) {
            hero.setPosition(position);
            retrieveCoins();
        }
    }

    private void retrieveCoins() {
        for (Coin coin : coins) {
            if (coin.getPosition().equals(hero.getPosition())) {
                coins.remove(coin);
                break;
            }
        }
    }

    private void moveMonsters() {
        for (Monster monster : monsters) {
            Position nextPos = monster.move();
            if (canMove(nextPos)) {
                monster.setPosition(nextPos);
            }
        }
    }

    public boolean verifyMonsterCollisions() {
        for (Monster monster : monsters) {
            if (monster.getPosition().equals(hero.getPosition())) {
                System.out.println("O Herói foi apanhado por um monstro! Game Over.");
                return true;
            }
        }
        return false;
    }

    public boolean processKey(KeyStroke key) {
        switch (key.getKeyType()) {
            case ArrowUp:
                moveHero(hero.moveUp());
                break;
            case ArrowDown:
                moveHero(hero.moveDown());
                break;
            case ArrowLeft:
                moveHero(hero.moveLeft());
                break;
            case ArrowRight:
                moveHero(hero.moveRight());
                break;
            default:
                break;
        }

        // Verificar colisão imediatamente após o Herói se mover
        if (verifyMonsterCollisions()) {
            return true;
        }

        // Mover monstros e verificar colisão novamente (caso o monstro tenha andado até ao herói)
        moveMonsters();
        return verifyMonsterCollisions();
    }

    public void draw(TextGraphics graphics) {
        // Pintar o chão
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');

        // Desenhar Paredes
        for (Wall wall : walls) {
            wall.draw(graphics);
        }

        // Desenhar Moedas
        for (Coin coin : coins) {
            coin.draw(graphics);
        }

        // Desenhar Monstros
        for (Monster monster : monsters) {
            monster.draw(graphics);
        }

        // Desenhar Herói por cima
        hero.draw(graphics);
    }
}