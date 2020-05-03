package board;


import org.apache.commons.collections4.map.MultiKeyMap;

public class GameState {
    public static int NONE = 0;
    public static int BLACK = 1;
    public static int WHITE = 2;
    private int winner;
    private int size;
    private int piece;
    private static int currentColor = WHITE;

    private final MultiKeyMap<Integer, Integer> gameState = new MultiKeyMap<>();

    public static GameState instance = null;

    public GameState(int size) {
        if (instance == null) {
            this.size = size;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    gameState.put(i, j, 0);
                }
            }
            instance = this;
        }
    }

    public Integer getWinner() {
        Integer winner = 0;
        int rowColorCount = 0;
        int rowColor = NONE;
        int columnColorCount = 0;
        int columnColor = NONE;
        int diagonalColorCount = 0;
        int diagonalColor = NONE;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // verificam ca sunt 5 piese consecutive pe rand
                if (gameState.get(i, j) != NONE && gameState.get(i, j) == rowColor) {
                    rowColorCount++;
                    if (rowColorCount >= 4) {
                        return gameState.get(i, j);
                    }
                } else {
                    rowColor = gameState.get(i, j);
                    rowColorCount = 0;
                }

                // verificam ca sunt 5 piese consecutive pe coloana
                if (gameState.get(j, i) != NONE && gameState.get(j, i) == columnColor) {
                    columnColorCount++;
                    if (columnColorCount >= 4) {
                        return gameState.get(j, i);
                    }
                } else {
                    columnColor = gameState.get(j, i);
                    columnColorCount = 0;
                }

                // verificam ca sunt 5 piese consecutive pe diagonala
                if (gameState.get(i,j) != NONE) {
                    diagonalColor = checkDiagonal(i, j, 1);
                    if (diagonalColor != NONE) {
                        return diagonalColor;
                    }
                    checkDiagonal(i, j, -1);
                    if (diagonalColor != NONE) {
                        return diagonalColor;
                    }
                }
            }
        }

        return winner;
    }

    private int checkDiagonal(int i, int j, int mod) {
        for (int k = 0; k < 5; k++) {
            if (gameState.get(i + k * mod, j + k) != gameState.get(i, j)) {
                return NONE;
            }
        }
        return gameState.get(i, j);
    }

    public Integer getPiece(int row, int column) {
        return gameState.get(row, column);
    }

    public void playPiece(int row, int column) {
        gameState.put(row, column, currentColor);
        currentColor = currentColor == WHITE ? BLACK : WHITE;
    }

    public int getSize() {
        return size;
    }
}
