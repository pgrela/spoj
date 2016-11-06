package pgrela.spoj.MUL;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pgrela.spoj.common.AbstractMainTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(JUnitParamsRunner.class)
public class MULTest extends AbstractMainTest{

    private StringBuffer input;
    private StringBuffer output;

    @Test
    @Parameters(
            {"2,3,6",
            "0,-20,0",
            "901,62892,56665692",
            "-1,-1,1",
            "-1,2,-2",
            "12345678901234567890123,-12345678901234567890123,-152415787532388367504942236884722755800955129"}
    )
    public void multiplicationTest(String numberA, String numberB, String expectedResult) throws IOException {
        //given

        //when
        String computedResult = new String(mainClassFactory.getMain(Main.class).multiply(numberA, numberB));

        Assertions.assertThat(computedResult).isEqualTo(expectedResult);
    }
    @Test
    public void convertToBase10To9Test() throws IOException {
        //given

        //when
        long[] computedResult = new long[100];
        mainClassFactory.getMain(Main.class).convertToBase("1234123456789123456789", 9, computedResult);

        Assertions.assertThat(computedResult).startsWith(123456789, 123456789, 1234);
    }


    @Test
    public void IOTest() throws IOException {
        String input = "1\n11 11";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input),output);
        main.solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo("121");
    }

    @Test
    public void testPolynomial(){
        int[] r = Polynomial.multiply(new int[]{20},new int[]{30});
        then(r[0]).isEqualTo(600);
    }

    //@Ignore
    @Test
    public void stressTest() throws IOException {
        Random r=new Random();
        Main main = mainClassFactory.getMain(Main.class);
        while(true){
            String numberA = String.valueOf(r.nextInt(100000));
            String numberB = String.valueOf(r.nextInt(100000));
            long fakeResult=Long.valueOf(String.valueOf(main.multiply(numberA,numberB)));
            long trueResult = Long.valueOf(numberA) * Long.valueOf(numberB);
            Assertions.assertThat(fakeResult).overridingErrorMessage(numberA+" * "+numberB+" = %s but should be %s",fakeResult,trueResult).isEqualTo(trueResult);
            if(0==1)break;
        }
    }
    @Before
    public void before(){
        int testCases=100;
        int numberLengths=10000;
        char[] nines=new char[numberLengths];
        for (int i = 0; i < numberLengths; i++) {
            nines[i]='9';
        }
        input = new StringBuffer();
        this.output= new StringBuffer();
        input.append(testCases).append('\n');
        for (int i = 0; i < testCases; i++) {
            input.append(nines).append(' ').append(nines).append('\n');
        }

        StringBuffer output = new StringBuffer();
        for (int i = 0; i < numberLengths-1; i++) {
            output.append('9');
        }
        output.append('8');
        for (int i = 0; i < numberLengths-1; i++) {
            output.append('0');
        }
        output.append('1');
        output.append('\n');
        for (int i = 0; i < testCases; i++) {
            this.output.append(output);
        }
    }
    @Test
    public void maxInputTest() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Main main = mainClassFactory.getMain(Main.class, new StringReader(input.toString()), output);
        //SquaredMultiplicationInBase10To9 main = new SquaredMultiplicationInBase10To9(new StringReader(input.toString()),output);
        main.solve();

        Assertions.assertThat(output.toString()).isEqualTo(this.output.toString());
    }

    @Test
    public void shouldMultiplySmallNumbers(){
        //given
        Main mulSolver = mainClassFactory.getMain(Main.class);
        mulSolver.resultLength=2;

        //when
        long[] root = mulSolver.multiply(new long[]{2,0},new long[]{3,0});

        then(root[0]).isEqualTo(6L);
    }

    @Test
    public void shouldMultiplyNotThatSmallNumbers() throws IOException {
        String input = "1\n21 34";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input),output);
        main.solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo("714");
    }
    @Test
    public void shouldMultiplyNotThatSmallNumbers2() throws IOException {
        String input = "1\n90 9";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input),output);
        main.solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo("810");
    }
}