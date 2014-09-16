package pgrela.spoj.TRIOMINO;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static final int MAX_BOARD_LENGTH = 801;
    private Boolean[] straightBoards;
    private Boolean[] boardsWithoutOneCorner;
    private Boolean[] boardsWithoutTwoCorners;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;
    private Map<BoardShape, Boolean[]> cacheForShape;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
        straightBoards = new Boolean[MAX_BOARD_LENGTH];
        boardsWithoutOneCorner = new Boolean[MAX_BOARD_LENGTH];
        boardsWithoutTwoCorners = new Boolean[MAX_BOARD_LENGTH];
        cacheForShape = new HashMap<BoardShape, Boolean[]>();
        cacheForShape.put(BoardShape.STRAIGHT, straightBoards);
        cacheForShape.put(BoardShape.ONE_CORNER, boardsWithoutOneCorner);
        cacheForShape.put(BoardShape.TWO_CORNES, boardsWithoutTwoCorners);
    }

    protected void solve() throws IOException {
        int testCases = readInt();
        for (int i = 0; i < testCases; i++) {
            int boardLength = readInt();
            boolean result = isBoardPossibleToWin(BoardShape.STRAIGHT, boardLength);
            outputStream.println(result ? "X" : "Y");
        }

        outputStream.flush();
        outputStream.close();
    }

    private boolean isBoardPossibleToWin(BoardShape shape, int boardLengthWithCorners) {
        Boolean[] cache = cacheForShape.get(shape);
        if (cache[boardLengthWithCorners] != null) {
            return cache[boardLengthWithCorners];
        }
        if (boardLengthWithCorners < 2) {
            return cacheResult(false, cache, boardLengthWithCorners);
        }
        if (boardLengthWithCorners < 4) {
            return cacheResult(true, cache, boardLengthWithCorners);
        }
        boolean result = false;
        switch (shape) {
            case STRAIGHT:
                result = isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.STRAIGHT,
                        BoardShape.ONE_CORNER);
                break;
            case ONE_CORNER:
                result = isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.STRAIGHT,
                        BoardShape.TWO_CORNES);
                if (!result) {
                    result = isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.ONE_CORNER,
                            BoardShape.ONE_CORNER);
                }
                break;
            case TWO_CORNES:
                result = isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.TWO_CORNES,
                        BoardShape.ONE_CORNER);
                break;
        }
        return cacheResult(result, cache, boardLengthWithCorners);
    }

    private boolean isPossibleToWinBoardAfterSplit(int boardLengthWithCorners, BoardShape leftBoardShape,
                                                   BoardShape rightBoardShape) {

        for (int i = 0; i < boardLengthWithCorners; i++) {
            boolean isLeftBoardPossibleToWin = isBoardPossibleToWin(leftBoardShape, i);
            boolean isRightBoardPossibleToWin = isBoardPossibleToWin(rightBoardShape, boardLengthWithCorners - 1 - i);
            if (isLeftBoardPossibleToWin == isRightBoardPossibleToWin) {
                return true;
            }
        }
        return false;
    }

    private boolean cacheResult(boolean result, Boolean[] cache, int position) {
        cache[position] = result;
        return result;
    }

    public static enum BoardShape {
        STRAIGHT, ONE_CORNER, TWO_CORNES
    }


    protected int readInt() {
        try {
            int c;
            do {
                c = inputStream.read();
            } while (!Character.isDigit(c));
            int result = Character.digit(c, 10);
            while (true) {
                c = inputStream.read();
                if (Character.isDigit(c)) {
                    result = result * 10 + Character.digit(c, 10);
                } else {
                    break;
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
