package pgrela.spoj.common;

import org.junit.Before;

public abstract class AbstractMainTest {

    @Before
    public void setSystemNewLinaParam(){
        System.setProperty("line.separator","\n");
    }

    protected MainClassFactory mainClassFactory = new MainClassFactory();
}
