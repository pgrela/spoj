package pgrela.spoj.GNYR09F;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

public class Main {
    public static final int CACHE_SIZE = 101;
    int[][] cache;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);

        initiateCache();
    }

    private void initiateCache() {
        cache = new int[CACHE_SIZE][CACHE_SIZE];

        for (int i = 0; i < CACHE_SIZE; i++) {
            for (int j = 0; j < CACHE_SIZE; j++) {
                cache[i][j] = -1;
            }
        }
    }

    protected void solve() throws IOException {
        int testCases = readLineWithSingleInt();

        for (int i = 0; i < testCases; i++) {
            solveSingleTestCase();
        }
        outputStream.flush();
        outputStream.close();
    }

    private void solveSingleTestCase() throws IOException {
        TestCase testCase = readTestCase();

        int solution = adjacentBitPairs(testCase.getNumberLength(),testCase.getRequiredAdjacentBitPairs());

        outputStream.println(testCase.getTestCaseNo() + " " + solution);
    }

    private int readLineWithSingleInt() throws IOException {
        return Integer.parseInt(inputStream.readLine());
    }

    private TestCase readTestCase() throws IOException {
        String numbers[] = inputStream.readLine().split(" ");
        String testCaseNo = numbers[0];
        int numberLength = Integer.parseInt(numbers[1]);
        int pairs = Integer.parseInt(numbers[2]);
        return new TestCase(testCaseNo, numberLength, pairs);
    }

    protected int adjacentBitPairs(int length, int pairs) {
        if (cache[length][pairs] != -1) {
            return cache[length][pairs];
        }
        if (!isPossible(length, pairs)) {
            return 0;
        }

        if (length == 0 && pairs == 0) {
            return 1;
        }
        if (length == 1 && pairs == 0) {
            return 2;
        }
        int result = adjacentBitPairs(length - 1, pairs)
                + adjacentPairsStartingWithOne(length - 1, pairs - 1)
                + adjacentBitPairs(length - 2, pairs);
        cache[length][pairs] = result;
        return result;
    }

    protected int adjacentPairsStartingWithOne(int length, int pairs) {
        if (length == 1 && pairs == 0) {
            return 1;
        }
        if (pairs < 0) {
            return 0;
        }
        return adjacentPairsStartingWithOne(length - 1, pairs - 1)
                + adjacentBitPairs(length - 2, pairs);
    }

    private boolean isPossible(int length, int pairs) {
        if (length < 0) {
            return false;
        }
        if (pairs == 0) {
            return true;
        }
        if (pairs >= length) {
            return false;
        }
        return true;
    }
    class TestCase{
        String testCaseNo;
        int numberLength;
        int requiredAdjacentBitPairs;

        TestCase(String testCaseNo, int numberLentgh, int requiredAdjacentBitPairs) {
            this.testCaseNo = testCaseNo;
            this.numberLength = numberLentgh;
            this.requiredAdjacentBitPairs = requiredAdjacentBitPairs;
        }

        public String getTestCaseNo() {
            return testCaseNo;
        }

        public int getNumberLength() {
            return numberLength;
        }

        public int getRequiredAdjacentBitPairs() {
            return requiredAdjacentBitPairs;
        }
    }
}
