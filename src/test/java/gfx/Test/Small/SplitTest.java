package gfx.Test.Small;

import static org.junit.Assert.*;

import org.junit.Test;

public class SplitTest {

	@Test
	public void test() {
		String test = "f 3780//1047 2451//1048 754//1049 1554//1050";
		String[] arraySpace;
		String[] arraySlash;
		arraySpace = test.split("\\s");
		arraySlash = arraySpace[1].split("\\/\\/");
		String[] resultArray = new String[] {"3780", "1047"};
		assertEquals(arraySlash[0]+arraySlash[1], resultArray[0]+resultArray[1]);
	}

}
