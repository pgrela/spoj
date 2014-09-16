package pgrela.spoj.GNYR09F;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pgrela.spoj.common.AbstractMainTest;

@RunWith(JUnitParamsRunner.class)
public class GNYR09FTest extends AbstractMainTest {


    @Test
    @Parameters(
            {
                    "1,0,2",
                    "1,1,0",
                    "2,2,0",
                    "2,1,1",
                    "2,0,3",
                    "3,0,5",
                    "3,1,2",
                    "3,2,1",
                    "3,3,0",
                    "4,4,0",
                    "4,1,5",
                    "4,0,8",
                    "5,2,6",
                    "20,8,63426",
                    "30,17,1861225",
                    "40,24,168212501",
                    "50,37,44874764",
                    "60,52,160916",
                    "70,59,22937308",
                    "80,73,99167",
                    "90,84,15476",
                    "100,90,23076518"
            })
    public void testAdjacentBitPairs(int numberLength, int requestedBitPairs, int expectedCombinationsCount) throws
            Exception {
        //given
        Main main = mainClassFactory.getMain(Main.class);

        //when
        int combinationsCount = main.adjacentBitPairs(numberLength, requestedBitPairs);

        //then
        Assertions.assertThat(combinationsCount).isEqualTo(expectedCombinationsCount);
    }
}