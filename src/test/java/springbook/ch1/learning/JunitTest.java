package springbook.ch1.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/junit.xml")
public class JunitTest {

    @Autowired
    ApplicationContext context;

    static ApplicationContext contextObject = null;
    static Set<JunitTest> testObjects = new HashSet<>();

    @Test
     void test1() {
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
     void test2() {
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
     void test3() {
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
