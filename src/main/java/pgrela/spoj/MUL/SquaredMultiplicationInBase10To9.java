package pgrela.spoj.MUL;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

/**
 * This approach failed as it turned out to be too slow when implemented in java.
 */
public class SquaredMultiplicationInBase10To9 {

    public static final long TEN_TO_9 = 1000*1000*1000;
    public static final int BASE = 9;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    private long[] numberAAsLong = new long[1500];
    private long[] numberBAsLong = new long[1500];
    private long[] resultAsLong = new long[3000];
    private char[] reversedResultAsChar = new char[30000];

    public static void main(String[] args) throws IOException {
        new SquaredMultiplicationInBase10To9(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public SquaredMultiplicationInBase10To9(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);
    }

    protected void solve() throws IOException {
        int testCases = Integer.parseInt(inputStream.readLine());

        for (int i = 0; i < testCases; i++) {
            String[] numbers = inputStream.readLine().split(" ");
            char[] result = multiply(numbers[0], numbers[1]);
            outputStream.println(result);
        }
        outputStream.flush();
        outputStream.close();
    }

    protected char[] multiply(String numberA, String numberB) {
        boolean resultIsPositive = true;
        if (numberA.startsWith("-")) {
            resultIsPositive = false;
            numberA = numberA.replace("-", "");
        }
        if (numberB.startsWith("-")) {
            resultIsPositive = !resultIsPositive;
            numberB = numberB.replace("-", "");
        }
        int lengthOfNumberAInBase10To9 = convertToBase10To9(numberA, numberAAsLong);
        int lengthOfNumberBInBase10To9 = convertToBase10To9(numberB, numberBAsLong);
        long[] resultInBase10To9 = multiply(numberAAsLong, lengthOfNumberAInBase10To9, numberBAsLong, lengthOfNumberBInBase10To9);
        return convertToCharArray(resultInBase10To9, resultIsPositive);
    }

    protected char[] convertToCharArray(long[] numberInBase10To9, boolean resultIsPositive) {
        int resultLength = numberInBase10To9.length * BASE;
        for (int i = 0; i < numberInBase10To9.length; i++) {
            int numberToWrite = (int) numberInBase10To9[i];
            int currentBaseIndex = i * BASE;
            for (int j = 0; j < BASE; j++) {
                reversedResultAsChar[currentBaseIndex + j] = Character.forDigit(numberToWrite % 10, 10);
                numberToWrite /= 10;
            }
        }
        int leadingZeroes = 0;
        for (int i = resultLength - 1; i >= 0; i--) {
            if (reversedResultAsChar[i] == '0') {
                ++leadingZeroes;
            } else {
                break;
            }
        }
        resultLength = resultLength - leadingZeroes;
        if (!resultIsPositive) {
            reversedResultAsChar[resultLength] = '-';
            resultLength++;
        }
        char[] result = new char[resultLength];
        for (int i = 0; i < resultLength; i++) {
            result[i] = reversedResultAsChar[resultLength - i - 1];
        }
        return result;
    }

    protected long[] multiply(long[] numberA, int lengthOfNumberAInBase10To9, long[] numberB, int lengthOfNumberBInBase10To9) {
        int resultLength = lengthOfNumberAInBase10To9 + lengthOfNumberBInBase10To9;
        for (int i = 0; i < resultLength; i++) {
            resultAsLong[i] = 0;
        }
        for (int i = 0; i < lengthOfNumberAInBase10To9; ++i) {
            for (int j = 0; j < lengthOfNumberBInBase10To9; ++j) {
                long singleMultiplicationResult = numberA[i] * numberB[j];
                int precomputedIndex = i+j;
                resultAsLong[precomputedIndex] += singleMultiplicationResult % TEN_TO_9;
                resultAsLong[precomputedIndex + 1] += singleMultiplicationResult / TEN_TO_9;
            }
        }
        for (int i = 0; i < resultLength-1; i++) {
            resultAsLong[i + 1] += resultAsLong[i] / TEN_TO_9;
            resultAsLong[i] %= TEN_TO_9;
        }
        return resultAsLong;
    }

    protected int convertToBase10To9(String number, long[] result) {
        char[] numberAsCharArray = number.toCharArray();
        reverse(numberAsCharArray);
        int digitsInBase10To9 = (number.length() + BASE-1) / BASE;
        for (int i = 0; i < digitsInBase10To9; i++) {
            long digitInBase10To9 = 0;
            long exponent = 1;
            int firstDigitToReadIndex = i * BASE;
            int nextNumberDigitToReadIndex = Math.min(firstDigitToReadIndex + BASE, number.length());
            for (int j = firstDigitToReadIndex; j < nextNumberDigitToReadIndex; j++) {
                digitInBase10To9 += exponent * Character.digit(numberAsCharArray[j], 10);
                exponent *= 10;
            }
            result[i] = digitInBase10To9;
        }
        return digitsInBase10To9;
    }

    private void reverse(char[] array) {
        if (array.length < 2) return;
        int halfLength = array.length / 2;
        for (int i = 0; i < halfLength; i++) {
            char tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }
    }
}
