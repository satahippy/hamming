package experimental;

import org.junit.Test;

import static org.junit.Assert.*;

public class HammingTest {

    @Test
    public void testEncode() throws Exception {
        String encoded = Hamming.encode("10011010");
        assertEquals("011100101010", encoded);
    }

    @Test
    public void decode() throws Exception {
        String decoded = Hamming.decode("011100101010");
        assertEquals("10011010", decoded);
    }

    @Test
    public void distance() throws Exception {
        int distance = Hamming.distance(new String[]{"0001", "0011", "0111"});
        assertEquals(2, distance);
    }

}