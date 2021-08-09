import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {

        URL url = Main.class.getClassLoader().getResource("index.html");
        System.out.println(url);
    }
}
