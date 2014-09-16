package pgrela.spoj.CHOCOLA;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;

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
        int testCases = readInt();
        for (int i = 0; i < testCases; i++) {
            int verticalCuts = readInt() - 1;
            int horizontalCuts = readInt() - 1;
            Cut[] cuts = new Cut[verticalCuts + horizontalCuts];
            for (int j = 0; j < verticalCuts; j++) {
                cuts[j] = new Cut(readInt(), Direction.VERTICAL);
            }
            for (int j = 0; j < horizontalCuts; j++) {
                cuts[verticalCuts + j] = new Cut(readInt(), Direction.HORIZONTAL);
            }
            Arrays.sort(cuts, new CutsDescendingOrdering());

            int doneHorizontalCuts = 0;
            int doneVerticalCuts = 0;
            int totalCost = 0;

            for (Cut cut : cuts) {
                switch (cut.getDirection()) {
                    case HORIZONTAL:
                        totalCost += (doneVerticalCuts + 1) * cut.getValue();
                        ++doneHorizontalCuts;
                        break;
                    case VERTICAL:
                        totalCost += (doneHorizontalCuts + 1) * cut.getValue();
                        ++doneVerticalCuts;
                        break;
                }
            }
            outputStream.println(totalCost);
        }

        outputStream.flush();
        outputStream.close();
    }

    static class Cut {
        int value;
        Direction direction;

        Cut(int value, Direction direction) {
            this.value = value;
            this.direction = direction;
        }

        public int getValue() {
            return value;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    static enum Direction {
        HORIZONTAL, VERTICAL
    }

    static class CutsDescendingOrdering implements Comparator<Cut> {
        @Override
        public int compare(Cut firstCut, Cut otherCut) {
            return -Integer.valueOf(firstCut.getValue()).compareTo(otherCut.getValue());
        }
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
