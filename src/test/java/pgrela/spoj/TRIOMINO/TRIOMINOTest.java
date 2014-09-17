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

import static junitparams.JUnitParamsRunner.$;
import static pgrela.spoj.TRIOMINO.Main.BoardShape.ONE_CORNER;
import static pgrela.spoj.TRIOMINO.Main.BoardShape.STRAIGHT;
import static pgrela.spoj.TRIOMINO.Main.BoardShape.TWO_CORNERS;

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

        Assertions.assertThat(output.toString()).isEqualTo(expectedOutput);
    }
    public Object[] minimalTestCases() {
        return $(
                $(0, STRAIGHT, "Y"),
                $(0, ONE_CORNER, "Y"),
                $(0, TWO_CORNERS, "Y"),
                $(1, STRAIGHT, "Y"),
                $(1, ONE_CORNER, "Y"),
                $(1, TWO_CORNERS, "Y"),
                $(2, STRAIGHT, "X"),
                $(2, ONE_CORNER, "X"),
                $(2, TWO_CORNERS, "Y"),
                $(3, STRAIGHT, "X"),
                $(3, ONE_CORNER, "X"),
                $(3, TWO_CORNERS, "X"),
                $(4, STRAIGHT, "Y"),
                $(4, ONE_CORNER, "X"),
                $(4, TWO_CORNERS, "X"),
                $(5, STRAIGHT, "X"),
                $(5, ONE_CORNER, "X"),
                $(5, TWO_CORNERS, "Y"),
                $(6, STRAIGHT, "X"),
                $(6, ONE_CORNER, "X"),
                $(6, TWO_CORNERS, "X"),
                $(7, STRAIGHT, "Y")
//                $(7, ONE_CORNER, "X"),
//                $(7, TWO_CORNERS, "X")
        );
    }
    @Test
    @Parameters(method = "minimalTestCases")
    public void shouldWorkForMinimalCases(int input, Main.BoardShape shape, String winner) throws IOException {
        //given
        Main triominoSolver = mainClassFactory.getMain(Main.class);

        //when
        boolean isXAWinner = triominoSolver.isBoardPossibleToWinCached(shape, input);

        //then
        Assertions.assertThat(isXAWinner).isEqualTo(winner.equals("X"));
    }
}