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

    private boolean isFurnitureTransportationPossible(Skyscraper skyscraper) {
        if (liftsAreGoingToFloor(skyscraper.getElevators(), skyscraper.getStartFloor())) {

        }
        for(Elevator firstElevator : skyscraper.getElevators()){
            for(Elevator otherElevator : skyscraper.getElevators()){
                if(firstElevator==otherElevator){
                    continue;
                }
            }
        }
        return false;
    }

    private boolean liftsAreGoingToFloor(Elevator[] elevators, int floor) {
        for (Elevator elevator : elevators) {
            if (floor >= elevator.getStartingFloor()) {
                int floorsToTravel = floor - elevator.getStartingFloor();
                if (floorsToTravel % elevator.getFrequency() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Skyscraper readSkyscraper() {
        int height = readInt();
        int lifts = readInt();
        int startFloor = readInt();
        int targetFloor = readInt();
        Elevator[] elevators = new Elevator[lifts];
        for (int i = 0; i < lifts; i++) {
            int startingFloor = readInt();
            int frequency = readInt();
            elevators[i] = new Elevator(startingFloor, frequency);
        }
        Skyscraper skyscraper = new Skyscraper(height, startFloor, targetFloor, elevators);
        return skyscraper;
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
    int k=0;
    public int gcd(int a, int b) {
        if(a==0){
            return b;
        }
        k+=b/a;
        return gcd(b%a,a);
    }

    private class Skyscraper {
        private int height;
        private int startFloor;
        private int targetFloor;
        private Elevator[] elevators;

        private Skyscraper(int height, int startFloor, int targetFloor, Elevator[] elevators) {
            this.height = height;
            this.startFloor = startFloor;
            this.targetFloor = targetFloor;
            this.elevators = elevators;
        }

        public int getHeight() {
            return height;
        }

        public int getStartFloor() {
            return startFloor;
        }

        public int getTargetFloor() {
            return targetFloor;
        }

        public Elevator[] getElevators() {
            return elevators;
        }
    }

    private class Elevator {
        private int startingFloor;
        private int frequency;

        private Elevator(int startingFloor, int frequency) {
            this.startingFloor = startingFloor;
            this.frequency = frequency;
        }

        public int getStartingFloor() {
            return startingFloor;
        }

        public int getFrequency() {
            return frequency;
        }
    }
}
