package pgrela.spoj.SCRAPER;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pgrela.spoj.common.AbstractMainTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.BDDAssertions.then;

@RunWith(JUnitParamsRunner.class)
public class SCRAPERTest extends AbstractMainTest {

    @Test
    @Parameters(
            {
                    "121,77,11"
            }
    )
    public void shouldComputeGCD(int a, int b, int expectedGcd) throws IOException {
        int gcd = mainClassFactory.getMain(Main.class).gcd(a, b);

        then(gcd).isEqualTo(expectedGcd);
    }
}