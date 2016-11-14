package pgrela.spoj.SBANK;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class MainTest {

    @Test
    public void test() throws IOException {
        String input = "2\n" +
                "6\n" +
                "03 10103538 2222 1233 6160 0142 \n" +
                "03 10103538 2222 1233 6160 0141 \n" +
                "30 10103538 2222 1233 6160 0141 \n" +
                "30 10103538 2222 1233 6160 0142 \n" +
                "30 10103538 2222 1233 6160 0141 \n" +
                "30 10103538 2222 1233 6160 0142 \n" +
                "\n" +
                "5\n" +
                "30 10103538 2222 1233 6160 0144 \n" +
                "30 10103538 2222 1233 6160 0142 \n" +
                "30 10103538 2222 1233 6160 0145 \n" +
                "30 10103538 2222 1233 6160 0146 \n" +
                "30 10103538 2222 1233 6160 0143";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input), output);
        main.solve();

        System.out.println(output);
    }
}