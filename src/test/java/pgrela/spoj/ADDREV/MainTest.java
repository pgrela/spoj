package pgrela.spoj.ADDREV;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class MainTest {

    @Test
    public void test() throws IOException {
        String input = "3\n" +
                "24 1\n" +
                "4358 754\n" +
                "305 794";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }
}