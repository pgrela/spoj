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

    public static final long TEN_TO_9 = 1000 * 1000 * 1000;
    public static final int BASE = 9;
    protected final BufferedReader inputStream;
    protected final PrintStream outputStream;

    private long[] numberAAsLong = new long[1500];
    private long[] numberBAsLong = new long[1500];
    private long[] resultAsLong = new long[3000];
    private char[] reversedResultAsChar = new char[30000];
    private List<Integer> primes;

    public static void main(String[] args) throws IOException {
        new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out)).solve();
    }

    public Main(Reader inputStreamReader, OutputStream out) throws IOException {
        inputStream = new BufferedReader(inputStreamReader);
        outputStream = new PrintStream(out);

        primes = sitoEratostenesa(50000);
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
        long[] resultInBase10To9 = multiply(numberAAsLong, lengthOfNumberAInBase10To9, numberBAsLong,
                lengthOfNumberBInBase10To9);
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

        /*long[] numberAInPointValuesFormat = convertToInPointValueFormat(numberA);
        long[] numberBInPointValuesFormat = convertToInPointValueFormat(numberB);
        long[] resultInPointValuesFormat = multiplyInPointValuesFormat(numberAInPointValuesFormat,
                numberBInPointValuesFormat);
        long[] result = convertToBase10To9(resultInPointValuesFormat);
        return result;                                                */
        return null;
    }

    protected int convertToBase10To9(String number, long[] result) {
        char[] numberAsCharArray = number.toCharArray();
        reverse(numberAsCharArray);
        int digitsInBase10To9 = (number.length() + BASE - 1) / BASE;
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

    protected List<Integer> sitoEratostenesa(int size) {
        boolean[] integers = new boolean[size];
        List<Integer> primes = new ArrayList<Integer>();
        for (int i = 2; i < integers.length; i++) {
            if (!integers[i]) {
                primes.add(i);
                int forwardIndex = i * 2;
                while (forwardIndex < integers.length) {
                    integers[forwardIndex] = true;
                    forwardIndex += i;
                }
            }
        }
        return primes;
    }

    protected long primitiveRootOfUnity(long modulo) {
        Random random = new Random();
        do {
            long root = random.nextInt((int) modulo - 2) + 2;
            if (exp(root, modulo - 1, modulo) != 1) {
                continue;
            }
            int currentPrimeIndex = 0;
            long factorizedRoot = root;
            do {
                long maxPossibleDivisor = (long) (Math.sqrt(factorizedRoot) + 1);
                while (primes.get(currentPrimeIndex) < maxPossibleDivisor) {
                    if (factorizedRoot % primes.get(currentPrimeIndex) == 0) {
                        break;
                    }
                    ++currentPrimeIndex;
                }
                if (factorizedRoot % primes.get(currentPrimeIndex) != 0) {
                    return root;
                    //throw new RuntimeException();
                }
                factorizedRoot /= primes.get(currentPrimeIndex);
                if (factorizedRoot == root) {
                    return root;
                }
                if (exp(root, primes.get(currentPrimeIndex), modulo) == 1) {
                    break;
                }
                return root;
            } while (factorizedRoot > 1);
        } while (true);
    }

    protected long nthRootOfUnity(int nth, int modulo) {
        long primitiveRoot = primitiveRootOfUnity(modulo);
        return exp(primitiveRoot, (modulo - 1) / nth, modulo);
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
        while (number != 0) {
            long factor = modulo / number;
            long oldModulo = modulo;
            modulo = number;
            number = oldModulo % number;
            matrix2x2 = matrix2x2.multiply(new Matrix2x2(0, 1, 1, -factor));
        }
        return matrix2x2.a10;
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
        long xPowered = 1;
        for (int i = 0; i < halfLength; i++) {
            yValues[i] = (evenResults[i] + xPowered * oddResults[i]) % modulo;
            yValues[i + halfLength] = ((evenResults[i] - xPowered * oddResults[i]) % modulo + modulo) % modulo;
            xPowered = xPowered * x % modulo;
        }
        return yValues;
    }

    public long[] inverseTransform(long[] values, long x, long modulo) {
        long xToMinusOne = exp(x, values.length - 0, modulo);
        long[] params = transform(values, xToMinusOne, modulo);

        for (int i = 0; i < values.length; i++) {
            if (params[i] % values.length != 0) {
                throw new RuntimeException("Result cannot be defined!");
            }
            params[i] /= values.length;
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

    public long[] multiply(long[] numberAAsLong, long[] numberBAsLong) {
        long primitiveRootOfUnity = 440564289;
        long modulo = 2013265921;
        int resultLength = 1024 * 16;
        long nthRootOfUnity = exp(primitiveRootOfUnity, 1024 * 1024 * 32 * 4 * 15 / resultLength, modulo);
        assert exp(nthRootOfUnity,resultLength,modulo) == 1;
        long[] aPadded = pad(numberAAsLong, resultLength);
        long[] bPadded = pad(numberBAsLong, resultLength);
        long[] aTransformed = transform(aPadded, nthRootOfUnity, modulo);
        long[] bTransformed = transform(bPadded, nthRootOfUnity, modulo);
        long[] resultTransformed = multiplyTransformed(aTransformed, bTransformed, modulo);
        long[] result = inverseTransform(resultTransformed, nthRootOfUnity, modulo);
        return result;
    }

    private long[] pad(long[] number, int resultLength) {
        return Arrays.copyOf(number, resultLength);
    }
}
