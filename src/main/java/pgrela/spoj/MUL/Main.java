package pgrela.spoj.MUL;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public final long tenToBase;
    public static final int BASE_EXPONENT = 4;
    public static final int MAX_NUMBER_LENGTH = 10000;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    private final long[] numberAAsLong;
    private int resultLength;
    private final long[] numberBAsLong;
    private final long[] resultAsLong;
    private final char[] reversedResultAsChar;
    private final int maxNumberLength;

    public static void main(String[] args) throws IOException {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);

        tenToBase = pow(10, BASE_EXPONENT);
        maxNumberLength = resultLength(MAX_NUMBER_LENGTH / BASE_EXPONENT + 1);
        numberAAsLong = new long[maxNumberLength];
        numberBAsLong = new long[maxNumberLength];
        resultAsLong = new long[maxNumberLength];
        reversedResultAsChar = new char[33333];
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
        long[] aInBase = convertToBase(numberA, BASE_EXPONENT);
        long[] bInBase = convertToBase(numberB, BASE_EXPONENT);
        long[] resultInBase = multiply(aInBase, bInBase);
        return convertToCharArray(resultInBase, resultIsPositive);
    }

    protected char[] convertToCharArray(long[] numberInBase10To9, boolean resultIsPositive) {
        int resultLength = numberInBase10To9.length * BASE_EXPONENT;
        for (int i = 0; i < numberInBase10To9.length; i++) {
            int numberToWrite = (int) numberInBase10To9[i];
            int currentBaseIndex = i * BASE_EXPONENT;
            for (int j = 0; j < BASE_EXPONENT; j++) {
                reversedResultAsChar[currentBaseIndex + j] = Character.forDigit(numberToWrite % 10, 10);
                numberToWrite /= 10;
            }
        }
        int leadingZeroes = 0;
        for (int i = resultLength - 1; i > 0; i--) {
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

    protected long[] convertToBase(String number, int chunkSize) {
        char[] numberAsCharArray = number.toCharArray();
        reverse(numberAsCharArray);
        int digitsInBase = (numberAsCharArray.length + chunkSize - 1) / chunkSize;
        long[] result = new long[digitsInBase];
        for (int i = 0; i < digitsInBase; i++) {
            long digitInBase = 0;
            long exponent = 1;
            int firstDigitToReadIndex = i * chunkSize;
            int nextNumberDigitToReadIndex = Math.min(firstDigitToReadIndex + chunkSize, numberAsCharArray.length);
            for (int j = firstDigitToReadIndex; j < nextNumberDigitToReadIndex; j++) {
                digitInBase += exponent * Character.digit(numberAsCharArray[j], 10);
                exponent *= 10;
            }
            result[i] = digitInBase;
        }
        return result;
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

    private long exp(long number, long exponent, long modulo) {
        if (exponent == 0) {
            return 1;
        }
        long n = exp(number, exponent / 2, modulo);
        long nSquared = n * n % modulo;
        return (exponent % 2 == 0 ? nSquared : nSquared * number) % modulo;
    }

    protected long multiplicativeInverseModulo(long number, long modulo) {
        Matrix2x2 matrix2x2 = new Matrix2x2();
        long originalModulo = modulo;
        while (number != 0) {
            long factor = modulo / number;
            long oldModulo = modulo;
            modulo = number;
            number = oldModulo % number;
            matrix2x2 = matrix2x2.multiply(new Matrix2x2(0, 1, 1, -factor));
        }
        return (matrix2x2.a10 % originalModulo + originalModulo) % originalModulo;
    }

    static class Matrix2x2 {
        public long a00, a01, a10, a11;

        Matrix2x2(long a00, long a01, long a10, long a11) {
            this.a00 = a00;
            this.a01 = a01;
            this.a10 = a10;
            this.a11 = a11;
        }

        Matrix2x2() {
            a00 = a11 = 1;
            a01 = a10 = 0;
        }

        Matrix2x2 multiply(Matrix2x2 otherMatrix) {
            return new Matrix2x2(
                    a00 * otherMatrix.a00 + a01 * otherMatrix.a10,
                    a00 * otherMatrix.a01 + a01 * otherMatrix.a11,
                    a10 * otherMatrix.a00 + a11 * otherMatrix.a10,
                    a10 * otherMatrix.a01 + a11 * otherMatrix.a11
            );
        }
    }

    public long[] transform(long[] params, long x, long modulo) {
        if (params.length == 1)
            return params;
        int halfLength = params.length / 2;
        long[] odd = new long[halfLength];
        long[] even = new long[halfLength];
        for (int i = 0; i < halfLength; i++) {
            even[i] = params[2 * i];
            odd[i] = params[2 * i + 1];
        }
        long xSquared = x * x % modulo;
        long[] evenResults = transform(even, xSquared, modulo);
        long[] oddResults = transform(odd, xSquared, modulo);
        long[] yValues = new long[params.length];
        long inHalf = exp(x, halfLength, modulo);
        long xPowered = 1;
        for (int i = 0; i < halfLength; i++) {
            yValues[i] = (evenResults[i] + xPowered * oddResults[i]) % modulo;
            yValues[i + halfLength] = ((evenResults[i] + inHalf * xPowered % modulo * oddResults[i]) % modulo + modulo) % modulo;
            xPowered = xPowered * x % modulo;
        }
        return yValues;
    }

    public long[] inverseTransform(long[] values, long x, long modulo) {
        long xToMinusOne = exp(x, values.length - 1, modulo);
        long[] params = transform(values, xToMinusOne, modulo);

        long inverseModulo = multiplicativeInverseModulo(values.length, modulo);
        assert inverseModulo * values.length % modulo == 1;

        for (int i = 0; i < values.length; i++) {
            params[i] = params[i] * inverseModulo % modulo;
        }
        return params;
    }

    public long[] multiplyTransformed(long[] numberAAsLong, long[] numberBAsLong, long modulo) {
        long[] result = new long[numberAAsLong.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = numberAAsLong[i] * numberBAsLong[i] % modulo;
        }
        return result;
    }

    protected long pow(long n, long exp) {
        if (exp == 0) {
            return 1;
        }
        long nSquared = pow(n, exp / 2);
        return nSquared * nSquared * (exp % 2 == 0 ? 1 : n);
    }

    public long[] multiply(long[] numberAAsLong, long[] numberBAsLong) {
        long primitiveRootOfUnity = 440564289;
        long modulo = 2013265921;
        resultLength = resultLength(numberAAsLong.length + numberBAsLong.length);
        long nthRootOfUnity = exp(primitiveRootOfUnity, 1024 * 1024 * 32 * 4 * 15 / resultLength, modulo);
        assert exp(nthRootOfUnity, resultLength, modulo) == 1;
        long[] aPadded = pad(numberAAsLong, resultLength);
        long[] bPadded = pad(numberBAsLong, resultLength);
        long[] aTransformed = transform(aPadded, nthRootOfUnity, modulo);
        long[] bTransformed = transform(bPadded, nthRootOfUnity, modulo);
        long[] resultTransformed = multiplyTransformed(aTransformed, bTransformed, modulo);
        long[] result = inverseTransform(resultTransformed, nthRootOfUnity, modulo);
        return normalize(result);
    }

    private int resultLength(int i) {
        i -= 1;
        int length = 0;
        while (i > 0) {
            i >>= 1;
            ++length;
        }
        return 1 << length;
    }

    private long[] normalize(long[] vector) {
        long rest = 0;
        for (int i = 0; i < vector.length; i++) {
            long nextValue = rest + vector[i];
            vector[i] = nextValue % tenToBase;
            rest = nextValue / tenToBase;
        }
        return vector;
    }

    private long[] pad(long[] number, int resultLength) {
        return Arrays.copyOf(number, resultLength);
    }
}
