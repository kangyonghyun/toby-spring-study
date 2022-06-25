package springbook.ch1.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JunitTest {

    @Autowired
    ApplicationContext context;

    static ApplicationContext contextObject = null;
    static Set<JunitTest> testObjects = new HashSet<>();

    @Test
    public void test1() {
        assertThat(testObjects).doesNotContain(this);
        log.info("this1 = {}", this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context).isTrue();
        assertThat(contextObject).satisfiesAnyOf(
                c -> assertThat(contextObject).isNull(),
                c -> assertThat(contextObject).isSameAs(this.context));
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(testObjects).doesNotContain(this);
        log.info("this2 = {}", this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context).isTrue();
        assertThat(contextObject).satisfiesAnyOf(
                c -> assertThat(contextObject).isNull(),
                c -> assertThat(contextObject).isSameAs(this.context));
        contextObject = this.context;
    }

    @Test
    public void test3() {
        assertThat(testObjects).doesNotContain(this);
        log.info("this3 = {}", this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context).isTrue();
        assertThat(contextObject).satisfiesAnyOf(
                c -> assertThat(contextObject).isNull(),
                c -> assertThat(contextObject).isSameAs(this.context));
        contextObject = this.context;
    }

}
