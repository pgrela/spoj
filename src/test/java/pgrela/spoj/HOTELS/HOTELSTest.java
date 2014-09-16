package pgrela.spoj.HOTELS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pgrela.spoj.common.AbstractMainTest;

@RunWith(JUnitParamsRunner.class)
public class HOTELSTest extends AbstractMainTest {

    @Test
    @Parameters(
            {
                    "5 12\n2 1 3 4 5,12",
                    "4 9\n7 3 5 6,8"
            }
    )
    public void IOTest(String input, String expectedOutput) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        mainClassFactory.getMain(Main.class, new StringReader(input), output).solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo(expectedOutput);
    }
}