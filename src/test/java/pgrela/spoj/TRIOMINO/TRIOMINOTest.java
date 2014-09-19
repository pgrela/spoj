package pgrela.spoj.TRIOMINO;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

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
    public void shouldWorkWithSampleInput(String input, String expectedOutput) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        mainClassFactory.getMain(Main.class, new StringReader(input), output).solve();

        then(output.toString()).isEqualTo(expectedOutput);
    }
    public Object[] minimalTestCases() {
        return $(
                $(0 , "Y"),
                $(1 , "Y"),
                $(2 , "X"),
                $(3 , "X"),
                $(4 , "Y"),
                $(5 , "X"),
                $(6 , "X"),
                $(7 , "Y"),
                $(8 , "X"),
                $(9 , "X"),
                $(10 , "X")
        );
    }
    public Object[] maximalTestCase() {
        return $(
                $(800 , "X")
        );
    }
    @Test
    @Parameters(method = "minimalTestCases,maximalTestCase")
    public void shouldWorkForTestCase(int input, String winner) throws IOException {
        //given
        Main triominoSolver = mainClassFactory.getMain(Main.class);

        //when
        boolean isXAWinner = triominoSolver.isWinningPosition(input);

        then(isXAWinner).isEqualTo(winner.equals("X"));
    }
}