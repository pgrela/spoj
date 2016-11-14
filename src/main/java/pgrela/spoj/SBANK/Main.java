package pgrela.spoj.SBANK;

import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
            solveSingleTestCase();
        }
        outputStream.flush();
        outputStream.close();
    }

    private void solveSingleTestCase() throws IOException {
        SortedMap<String, Integer> accounts = readBankAccounts();

        printBankAccounts(accounts);
    }

    private void printBankAccounts(SortedMap<String, Integer> accounts) {
        for(Map.Entry<String, Integer> entry : accounts.entrySet()){
            outputStream.append(entry.getKey()).append(" ").append(entry.getValue().toString()).append("\n");
        }

        outputStream.println();
    }

    private SortedMap<String, Integer> readBankAccounts() throws IOException {
        int bankAccounts = Integer.parseInt(inputStream.readLine());
        SortedMap<String, Integer> accounts = new TreeMap<>();
        for (int j = 0; j < bankAccounts; j++) {
            String account = inputStream.readLine().trim();
            Integer occurrences = accounts.get(account);
            accounts.put(account, occurrences == null ? 1 : occurrences + 1);
        }
        inputStream.readLine();
        return accounts;
    }

}
