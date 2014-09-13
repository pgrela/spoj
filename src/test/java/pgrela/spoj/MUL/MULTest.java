package pgrela.spoj.MUL;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.Random;

@RunWith(JUnitParamsRunner.class)
public class MULTest {
    @Test
    @Parameters(
            {"2,3,6",
            "-1,-1,1",
            "-1,2,-2",
            "12345678901234567890123,12345678901234567890123,152415787532388367504942236884722755800955129"}
    )
    public void multiplicationTest(String numberA, String numberB, String expectedResult){
        //given

        //when
        String computedResult = getMain().multiply(numberA, numberB);

        Assertions.assertThat(computedResult).isEqualTo(expectedResult);
    }

    private Main getMain() {
        return new Main(new InputStreamReader(System.in), new BufferedOutputStream(System.out));
    }

    @Test
    public void IOTest() throws IOException {
        String input = "1\n2 3";
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Main main = new Main(new StringReader(input),output);
        main.solve();

        Assertions.assertThat(output.toString().trim()).isEqualTo("6");
    }

    @Test
    public void stressTest(){
        Random r=new Random();
        Main main = getMain();
        int fakeResult=0;
        while(true){
            String numberA = String.valueOf(r.nextInt(100000));
            String numberB = String.valueOf(r.nextInt(100000));
            fakeResult+=main.multiply(numberA,numberB).length();
            if(0==1)break;
        }
        System.out.println(fakeResult);
    }
}