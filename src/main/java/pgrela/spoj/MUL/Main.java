package pgrela.spoj.MUL;

import java.io.*;

public class Main {

    public static final int TEN_TO_9 = 1000000000;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    public static void main(String[] args) throws IOException {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
    }

    protected void solve() throws IOException {
        int testCases = Integer.parseInt(inputStream.readLine());

        for (int i = 0; i < testCases; i++) {
            String[] numbers = inputStream.readLine().split(" ");
            String result = multiply(numbers[0], numbers[1]);
            outputStream.println(result);
        }
    }

    protected String multiply(String numberA, String numberB) {
        boolean resultIsPositive = true;
        if (numberA.startsWith("-")) {
            resultIsPositive = false;
            numberA = numberA.replace("-", "");
        }
        if (numberB.startsWith("-")) {
            resultIsPositive = !resultIsPositive;
            numberB = numberB.replace("-", "");
        }
        long[] numberAConvertedToBase10To9 = convertToBase10To9(numberA);
        long[] numberBConvertedToBase10To9 = convertToBase10To9(numberB);
        long[] resultInBase10To9 = multiply(numberAConvertedToBase10To9, numberBConvertedToBase10To9);
        String stringResult = convertToString(resultInBase10To9);
        if (!resultIsPositive)
            stringResult = "-" + stringResult;
        return stringResult;
    }

    protected String convertToString(long[] numberInBase10To9) {
        int resultLength = numberInBase10To9.length * 9;
        StringBuffer stringBuffer = new StringBuffer(String.format("%" + resultLength + "s", "").replace(" ", "0"));
        for (int i = 0; i < numberInBase10To9.length; i++) {
            String partialNumber = String.valueOf(numberInBase10To9[numberInBase10To9.length - i - 1]);
            int endPosition = i * 9 + 9;
            int insertPosition = endPosition - partialNumber.length();
            stringBuffer.replace(insertPosition, endPosition, partialNumber);
        }
        return stringBuffer.toString().replaceFirst("^0+(?!$)", "");
    }

    protected long[] multiply(long[] numberA, long[] numberB) {
        int resultLength = numberA.length + numberB.length;
        long[] result = new long[resultLength];
        for (int i = 0; i < numberA.length; i++) {
            for (int j = 0; j < numberB.length; j++) {
                long singleMultiplicationResult = numberA[i] * numberB[j];
                result[i + j] += singleMultiplicationResult % TEN_TO_9;
                result[i + j + 1] += singleMultiplicationResult / TEN_TO_9 + result[i + j] / TEN_TO_9;
                result[i + j] %= TEN_TO_9;
                int possibleOverFlow = i + j + 1;
                while (result[possibleOverFlow] >= TEN_TO_9) {
                    result[possibleOverFlow + 1] += result[possibleOverFlow] / TEN_TO_9;
                    result[possibleOverFlow] %= TEN_TO_9;
                    ++possibleOverFlow;
                }
            }
        }
        return result;
    }

    protected long[] convertToBase10To9(String number) {
        int digitsInBase10To9 = (number.length() + 8) / 9;
        long[] result = new long[digitsInBase10To9];
        {
            int lastDigitIndex = number.length();
            int firstDigitIndex = Math.max(0, lastDigitIndex - 9);
            for (int i = 0; i < digitsInBase10To9; i++) {
                result[i] = Long.parseLong(number.substring(firstDigitIndex, lastDigitIndex));
                firstDigitIndex -= 9;
                lastDigitIndex -= 9;
                firstDigitIndex = Math.max(0, firstDigitIndex);
            }
        }
        return result;
    }
}
