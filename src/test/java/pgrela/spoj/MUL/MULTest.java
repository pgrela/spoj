package pgrela.spoj.MUL;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pgrela.spoj.common.AbstractMainTest;

@RunWith(JUnitParamsRunner.class)
public class MULTest extends AbstractMainTest{

    private StringBuffer input;
    private StringBuffer output;

    @Test
    @Parameters(
            {"2,3,6",
            "-1,-1,1",
            "-1,2,-2",
            "12345678901234567890123,12345678901234567890123,152415787532388367504942236884722755800955129"}
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
        mainClassFactory.getMain(Main.class).convertToBase10To9("1234123456789123456789", computedResult);

        Assertions.assertThat(computedResult).startsWith(123456789, 123456789, 1234);
    }


    @Test
    public void IOTest() throws IOException {
        String input = "1\n2 3";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input),output);
        main.solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo("6");
    }

    //@Ignore
    //@Test
    public void stressTest() throws IOException {
        Random r=new Random();
        Main main = mainClassFactory.getMain(Main.class);
        int fakeResult=0;
        while(true){
            String numberA = String.valueOf(r.nextInt(100000));
            String numberB = String.valueOf(r.nextInt(100000));
            fakeResult+=main.multiply(numberA,numberB).length;
            if(0==1)break;
        }
        System.out.println(fakeResult);
    }
    @Before
    public void before(){
        int testCases=1000;
        int numberLenths=10000;
        char[] nines=new char[numberLenths];
        for (int i = 0; i < numberLenths; i++) {
            nines[i]='9';
        }
        input = new StringBuffer();
        this.output= new StringBuffer();
        input.append(testCases).append('\n');
        for (int i = 0; i < testCases; i++) {
            input.append(nines).append(' ').append(nines).append('\n');
        }

        StringBuffer output = new StringBuffer();
        for (int i = 0; i < numberLenths-1; i++) {
            output.append('9');
        }
        output.append('8');
        for (int i = 0; i < numberLenths-1; i++) {
            output.append('0');
        }
        output.append('1');
        output.append('\n');
        for (int i = 0; i < testCases; i++) {
            this.output.append(output);
        }
    }
    //@Test
    public void maxInputTest() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Main main = new Main(new StringReader(input.toString()),output);
        main.solve();

        Assertions.assertThat(output.toString()).isEqualTo(output.toString());
    }

    @Test
    @Parameters(
            {
                    //"5,4",
                    //"59,21",
                    "89,11",//,
                    "64,7",//,
                    "134217728,7",
                    "536870912,233",
                    "655360001,10000"//,
                    //"59,43",
                    //"59,44",
                    //"59,45"
            }
    )
    public void shouldComputeNthRootOfUnity(int modulo, int nth){
        Main mulSolver = mainClassFactory.getMain(Main.class);

        long root = mulSolver.nthRootOfUnity(nth, modulo);

        then(verifyRoot(root, modulo, nth)).isEqualTo(true);
    }

    private boolean verifyRoot(long root, long modulo, long nth) {
        long k=root;
        for (long i = 1; i < nth; i++) {
            if(k==1){
                return false;
            }
            k=k*root%modulo;
        }
        return k==1;
    }

    @Test
    public void shouldMultiplySmallNumbers(){
        //given
        Main mulSolver = mainClassFactory.getMain(Main.class);

        //when
        long[] root = mulSolver.multiply(new long[]{2},new long[]{3});

        then(root[0]).isEqualTo(6L);
    }

    @Test
    public void shouldMultiplyNotThatSmallNumbers(){
        //given
        Main mulSolver = mainClassFactory.getMain(Main.class);

        //when
        long[] root = mulSolver.multiply(new long[]{1,2},new long[]{4,3});

        then(root).contains(4,0).contains(1,1).contains(7,2);
    }
    @Test
    public void shouldMultiplyNotThatSmallNumbers2(){
        //given
        Main mulSolver = mainClassFactory.getMain(Main.class);

        //when
        long[] root = mulSolver.multiply(new long[]{1,2},new long[]{3});

        then(root).contains(3,0).contains(6,1);
    }
}