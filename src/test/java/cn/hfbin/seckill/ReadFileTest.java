package cn.hfbin.seckill;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author victor2022
 * @creat 2022/4/13 14:28
 */
public class ReadFileTest {

    @Test
    public void testReadTokenFile() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("./res/tokens.txt");
        Scanner scanner = new Scanner(fis);
        while(scanner.hasNextLine()){
            System.out.println(scanner.nextLine());
        }
    }
}
