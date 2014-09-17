package pgrela.spoj.TRIOMINO;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;

public class Main {
    public static final int MAX_BOARD_LENGTH = 1600;
    public static final int MAX_GRUNDY_NUMBER = 90;
    protected BufferedReader inputStream;
    protected PrintStream outputStream;
    int[] grundyNumbers;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        setupInOutStreams(inputStreamReader, out);

        precomputeGrundyNumbers();
    }

    private void setupInOutStreams(Reader inputStreamReader, OutputStream out) {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
    }

    private void precomputeGrundyNumbers() {
        grundyNumbers = new int[MAX_BOARD_LENGTH + 1];
        boolean[] visitedGrundyNumbers = new boolean[MAX_GRUNDY_NUMBER];
        for (int boardSize = 3; boardSize <= MAX_BOARD_LENGTH; boardSize++) {

            visitSmallerBoardPairs(visitedGrundyNumbers, boardSize);

            grundyNumbers[boardSize] = findFirstNotVisitedNumber(visitedGrundyNumbers);

            Arrays.fill(visitedGrundyNumbers, false);
        }
    }

    private void visitSmallerBoardPairs(boolean[] visitedGrundyNumbers, int boardSize) {
        int leftBoardSize = 0;
        int rightBoardSize = boardSize - 3;
        int maxLeftBoardSize = boardSize / 2;
        do {
            visitedGrundyNumbers[grundyNumbers[leftBoardSize] ^ grundyNumbers[rightBoardSize]] = true;
            ++leftBoardSize;
            --rightBoardSize;
        } while (leftBoardSize < maxLeftBoardSize);
    }

    private int findFirstNotVisitedNumber(boolean[] visitedGrundyNumbers) {
        for (int j = 0; j < MAX_GRUNDY_NUMBER; j++) {
            if (!visitedGrundyNumbers[j]) {
                return j;
            }
        }
        throw new RuntimeException("Didn't find unvisited Grundy number less than " + MAX_GRUNDY_NUMBER);
    }


    protected void solve() throws IOException {
        int testCases = readInt();

        for (int i = 0; i < testCases; i++) {
            int boardLength = readInt();

            boolean isWinningPosition = isWinningPosition(boardLength);

            outputStream.println(isWinningPosition ? "X" : "Y");
        }

        outputStream.flush();
        outputStream.close();
    }

    protected boolean isWinningPosition(int boardLength) {
        return grundyNumbers[boardLength * 2] == 0;
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
