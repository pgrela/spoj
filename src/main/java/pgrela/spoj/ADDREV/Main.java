package pgrela.spoj.ADDREV;

import java.io.*;

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
        int testCases = Integer.parseInt(inputStream.readLine());

        for (int i = 0; i < testCases; i++) {
            String[] numbers = inputStream.readLine().trim().split(" ");
            outputStream.println(Long.valueOf(reverse(String.valueOf(Long.valueOf(reverse(numbers[0])) + Long.valueOf(reverse(numbers[1]))))));
        }
        outputStream.flush();
        outputStream.close();
    }

    private String reverse(String number) {
        return new StringBuilder(number).reverse().toString();
    }

}
