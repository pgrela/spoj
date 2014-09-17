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
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;
    private Map<BoardShape, Boolean[]> cacheForShape;
    private Map<BoardShape, Boolean[]> cacheForShapeLost;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);

        createCacheArrays();
    }

    private void createCacheArrays() {
        cacheForShape = new HashMap<BoardShape, Boolean[]>();
        cacheForShape.put(BoardShape.STRAIGHT, new Boolean[MAX_BOARD_LENGTH]);
        cacheForShape.put(BoardShape.ONE_CORNER, new Boolean[MAX_BOARD_LENGTH]);
        cacheForShape.put(BoardShape.TWO_CORNERS, new Boolean[MAX_BOARD_LENGTH]);
        cacheForShapeLost = new HashMap<BoardShape, Boolean[]>();
        cacheForShapeLost.put(BoardShape.STRAIGHT, new Boolean[MAX_BOARD_LENGTH]);
        cacheForShapeLost.put(BoardShape.ONE_CORNER, new Boolean[MAX_BOARD_LENGTH]);
        cacheForShapeLost.put(BoardShape.TWO_CORNERS, new Boolean[MAX_BOARD_LENGTH]);
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

    protected boolean isBoardPossibleToWinCached(BoardShape shape, int boardLengthWithCorners) {
        Boolean[] cache = cacheForShape.get(shape);
        if (cache[boardLengthWithCorners] != null) {
            return cache[boardLengthWithCorners];
        }
        boolean result = isBoardPossibleToWin(shape, boardLengthWithCorners);
        return cacheResult(result, cache, boardLengthWithCorners);
    }

    protected boolean isBoardPossibleToWin(BoardShape shape, int boardLengthWithCorners) {
        if (!isAnyMovePossible(shape, boardLengthWithCorners)) {
            return false;
        }
        switch (shape) {
            case STRAIGHT:
                return isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.STRAIGHT, BoardShape.ONE_CORNER);
            case ONE_CORNER:
                return isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.STRAIGHT, BoardShape.TWO_CORNERS)
                        || isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.ONE_CORNER, BoardShape.ONE_CORNER);
            case TWO_CORNERS:
                return isPossibleToWinBoardAfterSplit(boardLengthWithCorners, BoardShape.TWO_CORNERS, BoardShape.ONE_CORNER);
            default:
                throw new RuntimeException("unrecognized shape");
        }
    }

    protected boolean isBoardPossibleToLoose(BoardShape shape, int boardLengthWithCorners) {
        if (!isAnyMovePossible(shape, boardLengthWithCorners)) {
            return true;
        }
        switch (shape) {
            case STRAIGHT:
                return isPossibleToLooseBoardAfterSplit(boardLengthWithCorners, BoardShape.STRAIGHT, BoardShape.ONE_CORNER);
            case ONE_CORNER:
                return isPossibleToLooseBoardAfterSplit(boardLengthWithCorners, BoardShape.STRAIGHT, BoardShape.TWO_CORNERS)
                        || isPossibleToLooseBoardAfterSplit(boardLengthWithCorners, BoardShape.ONE_CORNER, BoardShape.ONE_CORNER);
            case TWO_CORNERS:
                return isPossibleToLooseBoardAfterSplit(boardLengthWithCorners, BoardShape.TWO_CORNERS, BoardShape.ONE_CORNER);
            default:
                throw new RuntimeException("unrecognized shape");
        }
    }

    private boolean isPossibleToLooseBoardAfterSplit(int boardLengthWithCorners, BoardShape leftBoardShape, BoardShape rightBoardShape) {

        for (int i = 0; i <= boardLengthWithCorners; i++) {
            int leftBoardSize = i;
            int rightBoardsSize = boardLengthWithCorners - leftBoardSize - 1;
            if (!isBoardPossible(leftBoardShape, leftBoardSize) || !isBoardPossible(rightBoardShape, rightBoardsSize)) {
                continue;
            }
            boolean isLeftBoardPossibleToWin = isBoardPossibleToWinCached(leftBoardShape, leftBoardSize);
            boolean isRightBoardPossibleToWin = isBoardPossibleToWinCached(rightBoardShape, rightBoardsSize);
            boolean isLeftBoardPossibleToLoose = isBoardPossibleToLooseCached(leftBoardShape, leftBoardSize);
            boolean isRightBoardPossibleToLoose = isBoardPossibleToLooseCached(rightBoardShape, rightBoardsSize);
            if ((!isLeftBoardPossibleToWin && !isRightBoardPossibleToWin) || (!isLeftBoardPossibleToLoose && !isRightBoardPossibleToLoose)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnyMovePossible(BoardShape shape, int boardLengthWithCorners) {
        if (boardLengthWithCorners < 2) {
            return false;
        }
        if (boardLengthWithCorners == 2 && shape.equals(BoardShape.TWO_CORNERS)) {
            return false;
        }
        return true;
    }

    private boolean isPossibleToWinBoardAfterSplit(int boardLengthWithCorners, BoardShape leftBoardShape,
                                                   BoardShape rightBoardShape) {

        for (int i = 0; i <= boardLengthWithCorners; i++) {
            int leftBoardSize = i;
            int rightBoardsSize = boardLengthWithCorners - leftBoardSize - 1;
            if (!isBoardPossible(leftBoardShape, leftBoardSize) || !isBoardPossible(rightBoardShape, rightBoardsSize)) {
                continue;
            }
            boolean isLeftBoardPossibleToWin = isBoardPossibleToWinCached(leftBoardShape, leftBoardSize);
            boolean isRightBoardPossibleToWin = isBoardPossibleToWinCached(rightBoardShape, rightBoardsSize);
            boolean isLeftBoardPossibleToLoose = isBoardPossibleToLooseCached(leftBoardShape, leftBoardSize);
            boolean isRightBoardPossibleToLoose = isBoardPossibleToLooseCached(rightBoardShape, rightBoardsSize);
            if ((!isLeftBoardPossibleToWin && !isRightBoardPossibleToWin) || (!isLeftBoardPossibleToLoose && !isRightBoardPossibleToLoose)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardPossibleToLooseCached(BoardShape shape, int boardLengthWithCorners) {
        Boolean[] cache = cacheForShapeLost.get(shape);
        if (cache[boardLengthWithCorners] != null) {
            return cache[boardLengthWithCorners];
        }
        boolean result = isBoardPossibleToLoose(shape, boardLengthWithCorners);
        return cacheResult(result, cache, boardLengthWithCorners);
    }

    private boolean isBoardPossible(BoardShape boardShape, int boardSize) {
        switch (boardShape) {
            case STRAIGHT:
                return boardSize >= 0;
            case ONE_CORNER:
                return boardSize >= 1;
            case TWO_CORNERS:
                return boardSize >= 1;
            default:
                throw new RuntimeException("unrecognized shape");
        }
    }

    private boolean cacheResult(boolean result, Boolean[] cache, int position) {
        cache[position] = result;
        return result;
    }

    public static enum BoardShape {
        STRAIGHT, ONE_CORNER, TWO_CORNERS
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
