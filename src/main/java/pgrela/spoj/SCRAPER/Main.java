package pgrela.spoj.SCRAPER;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
            Skyscraper skyscraper = readSkyscraper();

            boolean isFurnitureTransportationPossible = isFurnitureTransportationPossible(skyscraper);

            if (isFurnitureTransportationPossible) {
                outputStream.println("It is possible to move the furniture.");
            } else {
                outputStream.println("The furniture cannot be moved.");
            }
        }

        outputStream.flush();
        outputStream.close();
    }

    public boolean isFurnitureTransportationPossible(Skyscraper skyscraper) {
        if (areStartingAndTargetPointOnTheSameFloor(skyscraper)) {
            return true;
        }
        Elevator startingElevator = getElevatorThatGoesTo(skyscraper.getElevators(), skyscraper.getStartFloor());
        if (startingElevator == null) {
            return false;
        }
        Set<Elevator> elevatorsToBeCheckedOut = new TreeSet<Elevator>();
        Set<Elevator> unreachedElevators = new TreeSet<Elevator>();
        Collections.addAll(unreachedElevators, skyscraper.getElevators());

        elevatorsToBeCheckedOut.add(startingElevator);
        unreachedElevators.remove(startingElevator);

        while (!elevatorsToBeCheckedOut.isEmpty()) {
            Elevator currentlyChecked = elevatorsToBeCheckedOut.iterator().next();
            if (currentlyChecked.canReachFloor(skyscraper.getTargetFloor())) {
                return true;
            }
            elevatorsToBeCheckedOut.remove(currentlyChecked);
            Iterator<Elevator> elevatorIterator = unreachedElevators.iterator();
            while (elevatorIterator.hasNext()) {
                Elevator elevator = elevatorIterator.next();

                Long firstCommonFloor = firstCommonFloor(currentlyChecked, elevator);
                if (firstCommonFloor == null) {
                    continue;
                }

                if (firstCommonFloor < skyscraper.getHeight()) {
                    elevatorsToBeCheckedOut.add(elevator);
                    elevatorIterator.remove();
                }
            }
            elevatorsToBeCheckedOut.remove(currentlyChecked);
        }
        return false;
    }

    private boolean areStartingAndTargetPointOnTheSameFloor(Skyscraper skyscraper) {
        return skyscraper.startFloor == skyscraper.targetFloor;
    }

    public Long firstCommonFloor(Elevator firstElevator, Elevator otherElevator) {
        long gcd = gcd(firstElevator.getFrequency(), otherElevator.getFrequency());
        long floorDiff = firstElevator.getStartingFloor() - otherElevator.getStartingFloor();
        if (floorDiff % gcd != 0) {
            return null;
        }

        long otherElevatorFrequencyByGcd = otherElevator.getFrequency() / gcd;

        long inv = multiplicativeInverseModulo(firstElevator.getFrequency() / gcd,
                otherElevatorFrequencyByGcd) + otherElevatorFrequencyByGcd;
        long x = (-floorDiff / gcd % otherElevatorFrequencyByGcd + otherElevatorFrequencyByGcd) * inv % otherElevatorFrequencyByGcd;

        long firstElevatorCommonFloor = (firstElevator.getStartingFloor() / gcd + x * firstElevator.getFrequency() / gcd)
                * gcd;
        if (firstElevatorCommonFloor < otherElevator.getStartingFloor()) {
            return (((firstElevatorCommonFloor - otherElevator.getStartingFloor()) / gcd / otherElevatorFrequencyByGcd)
                    % (firstElevator.getFrequency() / gcd) + firstElevator.getFrequency() / gcd) % (firstElevator
                    .getFrequency() / gcd) * otherElevator.getFrequency() + otherElevator.getStartingFloor();
        }
        return firstElevatorCommonFloor;
    }

    private Elevator getElevatorThatGoesTo(Elevator[] elevators, long floor) {
        for (Elevator elevator : elevators) {
            if (elevator.canReachFloor(floor)) {
                return elevator;
            }
        }
        return null;
    }

    private Skyscraper readSkyscraper() {
        long height = readLong();
        int lifts = readInt();
        long startFloor = readLong();
        long targetFloor = readLong();
        Elevator[] elevators = new Elevator[lifts];
        for (int i = 0; i < lifts; i++) {
            long frequency = readLong();
            long startingFloor = readLong();
            elevators[i] = new Elevator(startingFloor, frequency);
        }
        return new Skyscraper(height, startFloor, targetFloor, elevators);
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

    protected long readLong() {
        try {
            int c;
            do {
                c = inputStream.read();
            } while (!Character.isDigit(c));
            long result = Character.digit(c, 10);
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

    public long gcd(long a, long b) {
        return a == 0 ? b : gcd(b % a, a);
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

    public static class Skyscraper {
        private long height;
        private long startFloor;
        private long targetFloor;
        private Elevator[] elevators;

        public Skyscraper(long height, long startFloor, long targetFloor, Elevator[] elevators) {
            this.height = height;
            this.startFloor = startFloor;
            this.targetFloor = targetFloor;
            this.elevators = elevators;
        }

        public long getHeight() {
            return height;
        }

        public long getStartFloor() {
            return startFloor;
        }

        public long getTargetFloor() {
            return targetFloor;
        }

        public Elevator[] getElevators() {
            return elevators;
        }
    }

    public static class Elevator implements Comparable {
        private long startingFloor;
        private long frequency;

        public Elevator(long startingFloor, long frequency) {
            this.startingFloor = startingFloor;
            this.frequency = frequency;
        }

        public Elevator(int startingFloor, int frequency) {
            this.startingFloor = startingFloor;
            this.frequency = frequency;
        }

        public long getStartingFloor() {
            return startingFloor;
        }

        public long getFrequency() {
            return frequency;
        }

        public boolean canReachFloor(long targetFloor) {
            return targetFloor >= startingFloor && (targetFloor - startingFloor) % frequency == 0;
        }

        @Override
        public int compareTo(Object o) {
            Elevator elevator = (Elevator) o;
            int i = Long.valueOf(startingFloor).compareTo(elevator.getStartingFloor());
            if (i == 0) {
                return Long.valueOf(frequency).compareTo(elevator.getFrequency());
            }
            return i;
        }
    }
}
