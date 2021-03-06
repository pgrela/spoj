package pgrela.spoj.MUL;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;

public class Main {

    public static final int BASE_EXPONENT = 3;
    public static final long TEN_TO_BASE = pow(10, BASE_EXPONENT);
    public static final int MAX_RESULT_NUMBER_LENGTH = 20000;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    protected int resultLength = 0;
    private final long[] numberAAsLong;
    private final long[] numberBAsLong;
    private final char[] reversedResultAsChar;
    private static final long MODULO = 2013265921;
    private static final long PRIMITIVE_ROOT_OF_UNITY = 440564289;

    public static void main(String[] args) throws IOException {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);

        int maxNumberLength = resultLength(MAX_RESULT_NUMBER_LENGTH / BASE_EXPONENT + 1);
        numberAAsLong = new long[maxNumberLength];
        numberBAsLong = new long[maxNumberLength];
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
        resultLength = resultLength(numberA, numberB);
        convertToBase(numberA, BASE_EXPONENT, numberAAsLong);
        convertToBase(numberB, BASE_EXPONENT, numberBAsLong);
        long[] resultInBase = multiply(numberAAsLong, numberBAsLong);
        char[] charArray = convertToCharArray(resultInBase, resultIsPositive);

        if (charArray.length == 2 && charArray[0] == '-' && charArray[1] == '0') {
            return "0".toCharArray();
        } else
            return charArray;
    }

    private int resultLength(String numberA, String numberB) {
        return resultLength(
                ((numberA.length() + BASE_EXPONENT - 1) / BASE_EXPONENT)
                        + ((numberB.length() + BASE_EXPONENT - 1) / BASE_EXPONENT)
        );
    }

    protected char[] convertToCharArray(long[] numberInBase, boolean resultIsPositive) {
        int resultLength = this.resultLength * BASE_EXPONENT;
        for (int i = 0; i < this.resultLength; i++) {
            int numberToWrite = (int) numberInBase[i];
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

    protected void convertToBase(String number, int chunkSize, long[] resultPointer) {
        Arrays.fill(resultPointer, 0L);
        char[] numberAsCharArray = number.toCharArray();
        reverse(numberAsCharArray);
        int digitsInBase = (numberAsCharArray.length + chunkSize - 1) / chunkSize;
        for (int i = 0; i < digitsInBase; i++) {
            long digitInBase = 0;
            long exponent = 1;
            int firstDigitToReadIndex = i * chunkSize;
            int nextNumberDigitToReadIndex = Math.min(firstDigitToReadIndex + chunkSize, numberAsCharArray.length);
            for (int j = firstDigitToReadIndex; j < nextNumberDigitToReadIndex; j++) {
                digitInBase += exponent * Character.digit(numberAsCharArray[j], 10);
                exponent *= 10;
            }
            resultPointer[i] = digitInBase;
        }
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

    public long[] transform(long[] params, long x, long modulo, int paramsLength) {
        if (paramsLength == 1)
            return params;
        int halfLength = paramsLength / 2;
        long[] odd = new long[halfLength];
        long[] even = new long[halfLength];
        for (int i = 0; i < halfLength; i++) {
            even[i] = params[2 * i];
            odd[i] = params[2 * i + 1];
        }
        long xSquared = x * x % modulo;
        long[] evenResults = transform(even, xSquared, modulo, halfLength);
        long[] oddResults = transform(odd, xSquared, modulo, halfLength);
        long[] yValues = new long[paramsLength];
        long inHalf = exp(x, halfLength, modulo);
        long xPowered = 1;
        for (int i = 0; i < halfLength; i++) {
            yValues[i] = (evenResults[i] + xPowered * oddResults[i]) % modulo;
            yValues[i + halfLength] = (evenResults[i] + inHalf * xPowered % modulo * oddResults[i] % modulo + modulo) % modulo;
            xPowered = xPowered * x % modulo;
        }
        return yValues;
    }

    public long[] inverseTransform(long[] values, long x, long modulo) {
        long xToMinusOne = exp(x, resultLength - 1, modulo);
        long inverseModulo = multiplicativeInverseModulo(resultLength, modulo);
        for (int i = 0; i < resultLength; i++) {
            values[i] = values[i] * inverseModulo % modulo;
        }

        return transform(values, xToMinusOne, modulo, resultLength);
    }

    public void multiplyTransformedAndStoreInFirstArgumentArray(long[] numberAAsLong, long[] numberBAsLong, int resultLength, long modulo) {
        for (int i = 0; i < resultLength; i++) {
            numberAAsLong[i] = numberAAsLong[i] * numberBAsLong[i] % modulo;
        }
    }

    protected static long pow(long n, long exp) {
        if (exp == 0) {
            return 1;
        }
        long nSquared = pow(n, exp / 2);
        return nSquared * nSquared * (exp % 2 == 0 ? 1 : n);
    }

    public long[] multiply(long[] numberAAsLong, long[] numberBAsLong) {
        if (resultLength == 0) {
            resultLength = resultLength(
                    ((numberAAsLong.length + BASE_EXPONENT - 1) / BASE_EXPONENT)
                            + ((numberBAsLong.length + BASE_EXPONENT - 1) / BASE_EXPONENT)
            );
        }

        long nthRootOfUnity = exp(PRIMITIVE_ROOT_OF_UNITY, 1024 * 1024 * 32 * 4 * 15 / resultLength, MODULO);
        assert exp(nthRootOfUnity, resultLength, MODULO) == 1;
        long[] resultAAsLong = transform(numberAAsLong, nthRootOfUnity, MODULO, resultLength);
        long[] resultBAsLong = transform(numberBAsLong, nthRootOfUnity, MODULO, resultLength);
        multiplyTransformedAndStoreInFirstArgumentArray(resultAAsLong, resultBAsLong, resultLength, MODULO);
        long[] resultTransformed = inverseTransform(resultAAsLong, nthRootOfUnity, MODULO);
        return normalize(resultTransformed, resultLength);
    }

    private int resultLength(int i) {
        --i;
        int length = 0;
        while (i > 0) {
            i >>= 1;
            ++length;
        }
        return 1 << length;
    }

    private long[] normalize(long[] vector, int resultLength) {
        long rest = 0;
        for (int i = 0; i < resultLength; i++) {
            long nextValue = rest + vector[i];
            vector[i] = nextValue % TEN_TO_BASE;
            rest = nextValue / TEN_TO_BASE;
        }
        return vector;
    }
}
