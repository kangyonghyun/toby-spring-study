package springbook.ch4.user.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void create() {
        User user1 = new User();
        assertThat(user1).isEqualTo(new User());

        User user2 = new User("kyh1", "kyh", "ps");
        assertThat(user2).isEqualTo(new User("kyh1", "kyh", "ps"));

        User user3 = new User("kyh1", "kyh", "ps", Level.BASIC, 0, 0);
        assertThat(user3).isEqualTo(new User("kyh1", "kyh", "ps", Level.BASIC, 0, 0));
    }

}