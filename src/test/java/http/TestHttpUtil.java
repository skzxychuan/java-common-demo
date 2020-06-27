package http;

import org.junit.Test;

import java.io.IOException;

public class TestHttpUtil {

    @Test
    public void testGet() throws IOException {
        HttpUtil.doGet();
    }

    @Test
    public void testPost() throws IOException {
        HttpUtil.doPost();
    }
}
