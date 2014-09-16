package pgrela.spoj.TRIOMINO;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

public class Main {
    public static final int MAX_BOARD_LENGTH = 801;
    private Boolean[] straightBoards;
    private Boolean[] boardsWithoutOneCorner;
    private Boolean[] boardsWithoutTwoCorners;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
        straightBoards = new Boolean[MAX_BOARD_LENGTH];
        boardsWithoutOneCorner = new Boolean[MAX_BOARD_LENGTH];
        boardsWithoutTwoCorners = new Boolean[MAX_BOARD_LENGTH];
    }

    protected void solve() throws IOException {
        int testCases = readInt();
        for (int i = 0; i < testCases; i++) {
            int boardLength = readInt();
            boolean result = isPossibleToWinABoard(boardLength);
            outputStream.println(result ? "X" : "Y");
        }

        outputStream.flush();
        outputStream.close();
    }

    private boolean isPossibleToWinABoard(int boardLength) {
        if (straightBoards[boardLength] != null) {
            return straightBoards[boardLength];
        }
        if (boardLength < 2) {
            return cacheResult(false, straightBoards, boardLength);
        }
        if (boardLength < 4) {
            return cacheResult(true, straightBoards, boardLength);
        }
        for (int i = 0; i < boardLength; i++) {
            boolean isLeftBoardPossibleToWin = isPossibleToWinABoard(i);
            boolean isRightBoardPossibleToWin = isPossibleToWinABorderWithoutOneCorner(boardLength - i - 1);
            if (isLeftBoardPossibleToWin == isRightBoardPossibleToWin) {
                return cacheResult(true, straightBoards, boardLength);
            }
        }
        return cacheResult(false, straightBoards, boardLength);
    }

    private boolean cacheResult(boolean result, Boolean[] cache, int position) {
        cache[position] = result;
        return result;
    }

    private boolean isPossibleToWinABorderWithoutOneCorner(int boardLengthIncludingCorner) {
        if (boardsWithoutOneCorner[boardLengthIncludingCorner] != null) {
            return boardsWithoutOneCorner[boardLengthIncludingCorner];
        }
        if (boardLengthIncludingCorner < 2) {
            return cacheResult(false, boardsWithoutOneCorner, boardLengthIncludingCorner);
        }
        if (boardLengthIncludingCorner < 4) {
            return cacheResult(true, boardsWithoutOneCorner, boardLengthIncludingCorner);
        }
        for (int i = 0; i < boardLengthIncludingCorner; i++) {
            boolean isLeftBoardPossibleToWin = isPossibleToWinABoard(i);
            boolean isRightBoardPossibleToWin = isPossibleToWinABorderWithoutTwoCorners(
                    boardLengthIncludingCorner - i - 1);
            if (isLeftBoardPossibleToWin == isRightBoardPossibleToWin) {
                return cacheResult(true, boardsWithoutOneCorner, boardLengthIncludingCorner);
            }
            isLeftBoardPossibleToWin = isPossibleToWinABorderWithoutOneCorner(i);
            isRightBoardPossibleToWin = isPossibleToWinABorderWithoutOneCorner(boardLengthIncludingCorner - i - 1);
            if (isLeftBoardPossibleToWin == isRightBoardPossibleToWin) {
                return cacheResult(true, boardsWithoutOneCorner, boardLengthIncludingCorner);
            }
        }
        return cacheResult(false, boardsWithoutOneCorner, boardLengthIncludingCorner);
    }

    private boolean isPossibleToWinABorderWithoutTwoCorners(int boardLengthIncludingCorners) {
        if (boardsWithoutTwoCorners[boardLengthIncludingCorners] != null) {
            return boardsWithoutTwoCorners[boardLengthIncludingCorners];
        }
        if (boardLengthIncludingCorners < 2) {
            return cacheResult(false, boardsWithoutTwoCorners, boardLengthIncludingCorners);
        }
        if (boardLengthIncludingCorners < 4) {
            return cacheResult(true, boardsWithoutTwoCorners, boardLengthIncludingCorners);
        }
        for (int i = 0; i < boardLengthIncludingCorners; i++) {
            boolean isLeftBoardPossibleToWin = isPossibleToWinABorderWithoutOneCorner(i);
            boolean isRightBoardPossibleToWin = isPossibleToWinABorderWithoutTwoCorners(boardLengthIncludingCorners -
                    i - 1);
            if (isLeftBoardPossibleToWin == isRightBoardPossibleToWin) {
                return cacheResult(true, boardsWithoutTwoCorners, boardLengthIncludingCorners);
            }
        }
        return cacheResult(false, boardsWithoutTwoCorners, boardLengthIncludingCorners);
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
