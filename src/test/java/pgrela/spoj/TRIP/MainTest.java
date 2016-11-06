package pgrela.spoj.TRIP;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class MainTest {

    @Test
    public void test() throws IOException {
        String input = "2\nbbc\ncb\nabcabcaa\nacbacba";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }
}