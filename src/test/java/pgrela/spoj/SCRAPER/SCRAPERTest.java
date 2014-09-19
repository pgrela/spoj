package pgrela.spoj.SCRAPER;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.BDDAssertions.then;
import static pgrela.spoj.SCRAPER.Main.Elevator;
import static pgrela.spoj.SCRAPER.SCRAPERTest.ElevatorBuilder.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pgrela.spoj.common.AbstractMainTest;

@RunWith(JUnitParamsRunner.class)
public class SCRAPERTest extends AbstractMainTest {

    @Test
    @Parameters(
            {
                    "2\n" +
                            "22 4 0 6\n" +
                            "3 2\n" +           //2,5,8,11,14,17,20
                            "4 7\n" +           //7,11,15,19
                            "13 6\n" +          //6,19
                            "10 0\n" +          //0,10,20
                            "1000 2 500 777\n" +
                            "2 0\n" +
                            "2 1,It is possible to move the furniture.\n" +
                            "The furniture cannot be moved.\n"
            }
    )
    public void shouldWorkWithSampleInput(String input, String expectedOutput) throws IOException {
        //given
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Main scraperSolver = mainClassFactory.getMain(Main.class, new StringReader(input), output);

        //when
        scraperSolver.solve();

        then(output.toString()).isEqualTo(expectedOutput);
    }

    @Test
    @Parameters(
            {
                    "121,77,11"
            }
    )
    public void shouldComputeGCD(int a, int b, int expectedGcd) throws IOException {
        //given
        Main scraperSolver = mainClassFactory.getMain(Main.class);

        //when
        long gcd = scraperSolver.gcd(a, b);

        then(gcd).isEqualTo(expectedGcd);
    }

    @Test
    @Parameters(
            {
                    "3,11",
                    "1,7",
                    "7,17",
                    "3,4",
                    "2,3"
            }
    )
    public void shouldComputeInverse(int number, int modulo) throws IOException {
        long inverseModulo = mainClassFactory.getMain(Main.class).multiplicativeInverseModulo(number, modulo);
        System.out.println(inverseModulo);
        then((inverseModulo * number + modulo * modulo) % modulo).isEqualTo(1);
    }

    public Object[] elevatorCommonFloorTestCases() {
        return $(
                $(new Elevator(1L, 7L), new Elevator(100L, 17L), 134L),
                $(new Elevator(2, 14), new Elevator(200, 34), 268L),
                $(new Elevator(100, 17), new Elevator(1, 7), 134L),
                $(new Elevator(200, 34), new Elevator(2, 14), 268L),
                $(new Elevator(1, 2), new Elevator(2, 2), null),
                $(new Elevator(0, 1), new Elevator(0, 2), 0L),
                $(new Elevator(3, 8), new Elevator(3, 6), 3L),
                $(new Elevator(1, 1), new Elevator(0, 2), 2L),
                $(new Elevator(0, 2), new Elevator(1, 3), 4L),
                $(new Elevator(0, 3), new Elevator(1, 4), 9L),
                $(new Elevator(0, 6), new Elevator(2, 8), 18L)
        );
    }

    @Test
    @Parameters(method = "elevatorCommonFloorTestCases")
    public void shouldWorkForTestCase(Elevator firstElevator, Elevator otherElevator,
                                      Long expectedCommonFloor) throws IOException {
        //given
        Main scraperSolver = mainClassFactory.getMain(Main.class);

        //when
        Long commonFloor = scraperSolver.firstCommonFloor(firstElevator, otherElevator);

        then(commonFloor).isEqualTo(expectedCommonFloor);
    }

    public Object[] fullScraperTestCases() {
        return $(
                $(SkyscraperBuilder.builder()
                                .withHeight(100)
                                .withStart(0)
                                .withTarget(1)
                                .withElevators(
                                        builder().withStart(0).withFrequency(3).build(),
                                        builder().withStart(1).withFrequency(97).build()
                                )
                                .build()
                        , false
                ),
                $(SkyscraperBuilder.builder()
                                .withHeight(100)
                                .withStart(0)
                                .withTarget(2)
                                .withElevators(
                                        builder().withStart(100).withFrequency(2).build(),
                                        builder().withStart(0).withFrequency(8).build(),
                                        builder().withStart(2).withFrequency(29).build()
                                )
                                .build()
                        , false
                ),
                $(SkyscraperBuilder.builder()
                                .withHeight(100)
                                .withStart(0)
                                .withTarget(1)
                                .withElevators(
                                        builder().withStart(0).withFrequency(1).build()
                                )
                                .build()
                        , true
                )
        );
    }

    @Test
    @Parameters(method = "fullScraperTestCases")
    public void shouldWorkForTestCaseSky(Main.Skyscraper skyscraper, boolean isExpectedToBePossible) throws IOException {
        //given
        Main scraperSolver = mainClassFactory.getMain(Main.class);

        //when
        boolean isMovingPossible = scraperSolver.isFurnitureTransportationPossible(skyscraper);

        then(isMovingPossible).isEqualTo(isExpectedToBePossible);
    }

    static class SkyscraperBuilder {
        int height;
        int start;
        int target;
        Elevator[] elevators;

        public static SkyscraperBuilder builder() {
            return new SkyscraperBuilder();
        }

        public SkyscraperBuilder withHeight(int height) {
            this.height = height;
            return this;
        }

        public SkyscraperBuilder withStart(int start) {
            this.start = start;
            return this;
        }

        public SkyscraperBuilder withTarget(int target) {
            this.target = target;
            return this;
        }

        public SkyscraperBuilder withElevators(Elevator... elevators) {
            this.elevators = elevators;
            return this;
        }

        Main.Skyscraper build() {
            return new Main.Skyscraper(height, start, target, elevators);
        }
    }

    public static class ElevatorBuilder {
        int start;

        public static ElevatorBuilder builder() {
            return new ElevatorBuilder();
        }

        public ElevatorBuilder withFrequency(int frequency) {
            this.frequency = frequency;
            return this;
        }

        public ElevatorBuilder withStart(int start) {
            this.start = start;
            return this;
        }

        public Elevator build() {
            return new Elevator(start, frequency);
        }

        int frequency;
    }
}