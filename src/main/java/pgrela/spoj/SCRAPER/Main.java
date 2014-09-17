package pgrela.spoj.SCRAPER;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

public class Main {
    protected BufferedReader inputStream;
    protected PrintStream outputStream;

    public static void main(String[] args) throws Exception {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        setupInOutStreams(inputStreamReader, out);
    }

    private void setupInOutStreams(Reader inputStreamReader, OutputStream out) {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
    }



    protected void solve() throws IOException {
        int testCases = readInt();

        for (int i = 0; i < testCases; i++) {
        }

        outputStream.flush();
        outputStream.close();
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
