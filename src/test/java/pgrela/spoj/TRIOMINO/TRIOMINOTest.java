package pgrela.spoj.TRIOMINO;

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
public class TRIOMINOTest extends AbstractMainTest {

    @Test
    @Parameters(
            {
                    "2\n3\n4\n,X\nY\n"
            }
    )
    public void IOTest(String input, String expectedOutput) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        mainClassFactory.getMain(Main.class, new StringReader(input), output).solve();

        Assertions.assertThat(output.toString()).isEqualTo(expectedOutput);
    }
}