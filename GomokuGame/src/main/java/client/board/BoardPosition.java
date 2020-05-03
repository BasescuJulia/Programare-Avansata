package client.board;

class BoardPosition {
    private int row;
    private int column;

    public BoardPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public BoardPosition get(int row, int column) {
        return new BoardPosition(row, column);
    }
}
