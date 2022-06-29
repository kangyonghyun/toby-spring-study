package springbook.ch2.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CalcSumTest {

    private Calculator calculator;
    private String path;

    @Before
    public void setUp() {
        calculator = new Calculator();
        path = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumCallback() throws IOException {
        int result = calculator.calSum(path);
        assertThat(result).isEqualTo(15);
    }
    @Test
    public void multiCallback() throws IOException {
        int result = calculator.calMulti(path);
        assertThat(result).isEqualTo(120);
    }

    @Test
    public void sumLineCallBack() throws IOException {
        int result = calculator.calSum2(path);
        assertThat(result).isEqualTo(15);
    }

    @Test
    public void multiLineCallback() throws IOException {
        int result = calculator.calMulti2(path);
        assertThat(result).isEqualTo(120);
    }

    interface BufferedReaderCallback {
        int doSomeThingWithReader(BufferedReader br) throws IOException;
    }

    interface LineCallBack {
        int doSomeThingWithLine(String line, int value);
    }

    static class Calculator  {

        public int lineReadTemplate(String path, LineCallBack callBack, int initVal) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                int res = initVal;
                String line = null;
                while ((line = br.readLine()) != null) {
                    res = callBack.doSomeThingWithLine(line, res);
                }
                return res;
            } catch (IOException e) {
                log.info(e.getMessage());
                throw e;
            }
        }

        public int context(String path, BufferedReaderCallback callBack) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                return callBack.doSomeThingWithReader(br);
            } catch (IOException e) {
                log.info(e.getMessage());
                throw e;
            }
        }
        public int calSum2(String path) throws IOException {
            LineCallBack callback = new LineCallBack() {
                @Override
                public int doSomeThingWithLine(String line, int value) {
                    return value += Integer.valueOf(line);
                }
            };
            return lineReadTemplate(path, callback, 0);
        }

        public int calSum(String path) throws IOException {
            BufferedReaderCallback callback = new BufferedReaderCallback() {
                @Override
                public int doSomeThingWithReader(BufferedReader br) throws IOException {
                    int sum = 0;
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sum += Integer.valueOf(line);
                    }
                    return sum;
                }
            };
            return context(path, callback);
        }

        public int calMulti2(String path) throws IOException {
            LineCallBack callBack = new LineCallBack() {
                @Override
                public int doSomeThingWithLine(String line, int value) {
                    return value *= Integer.valueOf(line);
                }
            };
            return lineReadTemplate(path, callBack, 1);
        }

        public int calMulti(String path) throws IOException {
            BufferedReaderCallback callback = new BufferedReaderCallback() {
                @Override
                public int doSomeThingWithReader(BufferedReader br) throws IOException {
                    int multi = 1;
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        multi *= Integer.valueOf(line);
                    }
                    return multi;
                }
            };
            return context(path, callback);
        }
    }
}
