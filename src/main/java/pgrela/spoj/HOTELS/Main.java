package pgrela.spoj.HOTELS;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

public class Main {
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
    }

    protected void solve() throws IOException {
        TestCase testCase = readTestCase();

        long maximalSpent = solveTestCase(testCase);

        outputStream.println(maximalSpent);

        outputStream.flush();
        outputStream.close();
    }

    private long solveTestCase(TestCase testCase) {
        int[] hotels = testCase.getHotels();
        int budget = testCase.getBudget();

        long currentlySpent = 0;
        int startHotel = -1;
        int lastHotel = -1;
        long maximalSpent = 0;
        int lastHotelIndex = hotels.length - 1;
        while (didntReachLastHotel(lastHotel, lastHotelIndex) || overspentBudget(budget, currentlySpent)) {
            if (currentlySpent < budget) {
                currentlySpent += hotels[++lastHotel];
            } else {
                currentlySpent -= hotels[++startHotel];
            }
            if (currentlySpent <= budget && currentlySpent > maximalSpent) {
                maximalSpent = currentlySpent;
            }
        }
        return maximalSpent;
    }

    private boolean overspentBudget(int budget, long currentlySpent) {
        return currentlySpent > budget;
    }

    private boolean didntReachLastHotel(int lastHotel, int lastHotelIndex) {
        return lastHotel < lastHotelIndex;
    }

    private int[] readArrayOfIntegers(int quantity) {
        int[] integers = new int[quantity];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = readInt();
        }
        return integers;
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

    private TestCase readTestCase() {
        int availableHotelsNumber = readInt();
        int budget = readInt();
        int[] hotels = readArrayOfIntegers(availableHotelsNumber);
        return new TestCase(hotels, budget);
    }

    private class TestCase {
        private int budget;
        private int[] hotels;

        private TestCase(int[] hotels, int budget) {
            this.budget = budget;
            this.hotels = hotels;
        }

        public int getBudget() {
            return budget;
        }

        public int[] getHotels() {
            return hotels;
        }
    }
}
