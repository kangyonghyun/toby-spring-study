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
    public void sumCallbackV1() throws IOException {
        int result = calculator.calSumV1(path);
        assertThat(result).isEqualTo(15);
    }
    @Test
    public void multiCallbackV1() throws IOException {
        int result = calculator.calMultiV1(path);
        assertThat(result).isEqualTo(120);
    }

    @Test
    public void sumCallbackV2() throws IOException {
        int result = calculator.calSumV2(path);
        assertThat(result).isEqualTo(15);
    }

    @Test
    public void multiCallbackV2() throws IOException {
        int result = calculator.calMultiV2(path);
        assertThat(result).isEqualTo(120);
    }

    @Test
    public void concatCallbackGeneric() throws IOException {
        String result = calculator.concatenate(path);
        assertThat(result).isEqualTo("12345");
    }

    @Test
    public void sumCallbackGeneric() throws IOException {
        int result = calculator.sum(path);
        assertThat(result).isEqualTo(15);
    }

    interface BufferedReaderCallback {
        int doSomeThingWithReader(BufferedReader br) throws IOException;
    }

    interface LineCallback {
        int doSomeThingWithLine(String line, int value);
    }

    interface LineCallBackGenerics<T> {
        T doSomeThingWithLine(String line, T value);
    }

    static class Calculator  {

        public <T> T lineReadTemplateGeneric(String path, LineCallBackGenerics<T> callback, T initVal) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                T res = initVal;
                String line;
                while ((line = br.readLine()) != null) {
                    res = callback.doSomeThingWithLine(line, res);
                }
                return res;
            } catch (IOException e) {
                log.info(e.getMessage());
                throw e;
            }
        }

        public int lineReadTemplate(String path, LineCallback callback, int initVal) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                int res = initVal;
                String line;
                while ((line = br.readLine()) != null) {
                    res = callback.doSomeThingWithLine(line, res);
                }
                return res;
            } catch (IOException e) {
                log.info(e.getMessage());
                throw e;
            }
        }

        public int template(String path, BufferedReaderCallback callback) throws IOException {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                return callback.doSomeThingWithReader(br);
            } catch (IOException e) {
                log.info(e.getMessage());
                throw e;
            }
        }

        public String concatenate(String path) throws IOException {
            LineCallBackGenerics<String> callback = (line, value) -> value += line;
            return lineReadTemplateGeneric(path, callback, "");
        }

        public int sum(String path) throws IOException {
            LineCallBackGenerics<Integer> callback = (line, value) -> value += Integer.valueOf(line);
            return lineReadTemplateGeneric(path, callback, 0);
        }

        public int calSumV2(String path) throws IOException {
            LineCallback callback = (line, value) -> value += Integer.valueOf(line);
            return lineReadTemplate(path, callback, 0);
        }

        public int calSumV1(String path) throws IOException {
            BufferedReaderCallback callback = br -> {
                int sum = 0;
                String line;
                while ((line = br.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            };
            return template(path, callback);
        }

        public int calMultiV2(String path) throws IOException {
            LineCallback callBack = (line, value) -> value *= Integer.valueOf(line);
            return lineReadTemplate(path, callBack, 1);
        }

        public int calMultiV1(String path) throws IOException {
            BufferedReaderCallback callback = br -> {
                int multi = 1;
                String line;
                while ((line = br.readLine()) != null) {
                    multi *= Integer.valueOf(line);
                }
                return multi;
            };
            return template(path, callback);
        }
    }
}
