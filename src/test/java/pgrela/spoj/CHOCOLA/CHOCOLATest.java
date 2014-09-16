package pgrela.spoj.CHOCOLA;

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
public class CHOCOLATest extends AbstractMainTest {

    @Test
    @Parameters(
            {
                    "1\n\n6 4\n2\n1\n3\n1\n4\n4\n1\n2,42"
            }
    )
    public void IOTest(String input, String expectedOutput) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        mainClassFactory.getMain(Main.class, new StringReader(input), output).solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo(expectedOutput);
    }
}