package pgrela.spoj.FCTRL2;

import java.io.*;
import java.math.BigDecimal;

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
        BigDecimal[] solutions = new BigDecimal[101];
        solutions[0] = BigDecimal.ONE;
        for (int i = 1; i < solutions.length; ++i) {
            solutions[i] = solutions[i-1].multiply(BigDecimal.valueOf(i));
        }


        int testCases = Integer.parseInt(inputStream.readLine());

        for (int i = 0; i < testCases; i++) {
            int n = Integer.parseInt(inputStream.readLine());
            outputStream.println(solutions[n].toPlainString());
        }
        outputStream.flush();
        outputStream.close();
    }
}
