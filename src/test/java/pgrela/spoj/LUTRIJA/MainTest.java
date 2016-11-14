package pgrela.spoj.LUTRIJA;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {


    @Test
    public void testLongCycle() throws IOException {
        int cycleLength = 4000;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cycleLength).append(" ").append(cycleLength).append("\n");
        buildCycle(1, cycleLength, stringBuilder);
        String input = stringBuilder.toString();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }

    @Test
    public void testFlower() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(4000).append(" ").append(4 * 1000 + 4).append("\n");
        buildCycle(1, 1000, stringBuilder);
        buildCycle(1001, 1000, stringBuilder);
        buildCycle(2001, 1000, stringBuilder);
        buildCycle(3001, 1000, stringBuilder);
        stringBuilder.append(1).append(" ").append(1001).append("\n");
        stringBuilder.append(1001).append(" ").append(2001).append("\n");
        stringBuilder.append(2001).append(" ").append(3001).append("\n");
        stringBuilder.append(3001).append(" ").append(1).append("\n");


        String input = stringBuilder.toString();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }

    @Test
    public void testForPathOfCycles() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(4000).append(" ").append(4 * 1000 + 3).append("\n");
        buildCycle(1, 1000, stringBuilder);
        buildCycle(1001, 1000, stringBuilder);
        buildCycle(2001, 1000, stringBuilder);
        buildCycle(3001, 1000, stringBuilder);
        stringBuilder.append(1).append(" ").append(1001).append("\n");
        stringBuilder.append(1012).append(" ").append(2020).append("\n");
        stringBuilder.append(2202).append(" ").append(3501).append("\n");


        String input = stringBuilder.toString();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }

    @Test
    public void testLongPathOfShortCycles() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int nodes = 0;
        int edges = 0;
        final int shortCyclesCount = 400;
        for (int i = 0; i < shortCyclesCount; ++i) {
            edges += buildCycle(1 + i * 10, 10, stringBuilder);
            nodes += 10;
        }
        for (int i = 1; i < shortCyclesCount; ++i) {
            stringBuilder.append(i * 10).append(" ").append(i * 10 + 1).append("\n");
            ++edges;
        }
        stringBuilder.insert(0, nodes + " " + edges + "\n");


        String input = stringBuilder.toString();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }

    private int buildCycle(int startNode, int cycleLength, StringBuilder stringBuilder) {
        int lastNode = cycleLength + startNode - 1;
        for (int i = startNode; i < lastNode; ++i) {
            stringBuilder.append(i).append(" ").append(i + 1).append("\n");
        }
        stringBuilder.append(lastNode).append(" ").append(startNode).append("\n");
        return cycleLength;
    }

    private int buildSunCycle(int startNode, int cycleLength, StringBuilder stringBuilder) {
        buildCycle(startNode, cycleLength, stringBuilder);
        int lastNode = cycleLength + startNode - 1;
        for (int i = startNode; i <= lastNode; ++i) {
            stringBuilder.append(i).append(" ").append(i + cycleLength).append("\n");
        }
        return cycleLength * 2;
    }

    @Test
    public void testSunWithRaysShapedCycle() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int edges = buildSunCycle(1, 1999, stringBuilder);
        stringBuilder.insert(0, "3998 " + edges + "\n");


        String input = stringBuilder.toString();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }

    @Test
    public void testShortCycle() throws IOException {
        String output = runForInput("3 3\n" +
                "1 2\n" +
                "2 3\n" +
                "3 1");

        assertThat(output).isEqualTo("3 6 6");
    }

    @Test
    public void test() throws IOException {
        String output = runForInput("4 4\n" +
                "1 3\n" +
                "2 3\n" +
                "1 2\n" +
                "1 4");

        assertThat(output).isEqualTo("4 8 10 4");
    }

    @Test
    public void testPath() throws IOException {
        String output = runForInput("4 3\n" +
                "1 2\n" +
                "2 3\n" +
                "3 4");

        assertThat(output).isEqualTo("4 6 4 2");
    }

    @Test
    public void testSmallEvenSunCycle() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int edges = buildSunCycle(1, 4, stringBuilder);
        stringBuilder.insert(0, "8 " + edges + "\n");


        String input = stringBuilder.toString();
        String output = runForInput(input);

        assertThat(output).isEqualTo("8 16 24 32 24 8 0 0");
    }

    @Test
    public void testSmallOddSunCycle() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int edges = buildSunCycle(1, 3, stringBuilder);
        stringBuilder.insert(0, "6 " + edges + "\n");


        String input = stringBuilder.toString();
        String output = runForInput(input);

        assertThat(output).isEqualTo("6 12 18 18 6 0");
    }

    @Test
    public void testWindmill() throws IOException {
        String output = runForInput("9 12\n" +
                "1 2\n" +
                "2 3\n" +
                "3 1\n" +
                "1 4\n" +
                "4 5\n" +
                "5 1\n" +
                "1 6\n" +
                "6 7\n" +
                "7 1\n" +
                "1 8\n" +
                "8 9\n" +
                "9 1");

        assertThat(output).isEqualTo("9 24 72 96 48 0 0 0 0");
    }

    @Test
    public void testStar() throws IOException {
        String output = runForInput("5 4\n" +
                "1 2\n" +
                "1 3\n" +
                "1 4\n" +
                "1 5");

        assertThat(output).isEqualTo("5 8 12 0 0");
    }

    @Test
    public void testMinimum() throws IOException {
        String output = runForInput("2 1\n" +
                "1 2");

        assertThat(output).isEqualTo("2 2");
    }

    @Test
    public void testIllegalDisjoint() throws IOException {
        String output = runForInput("3 1\n" +
                "1 2");

        System.out.println(output);
    }

    private String runForInput(String input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();
        return output.toString().trim();
    }

}