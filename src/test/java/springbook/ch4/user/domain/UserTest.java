package springbook.ch4.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void create() {
        User user1 = new User();
        assertThat(user1).isEqualTo(new User());

        User user2 = new User("kyh1", "kyh", "ps");
        assertThat(user2).isEqualTo(new User("kyh1", "kyh", "ps"));

        User user3 = new User("kyh1", "kyh", "ps", Level.BASIC, 0, 0);
        assertThat(user3).isEqualTo(new User("kyh1", "kyh", "ps", Level.BASIC, 0, 0));
    }

    @Test
    void nextLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            user.setLevel(level);
            if (level.nextLevel() == null) {
                continue;
            }
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    void no_nextLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            user.setLevel(level);
            if (level.nextLevel() != null) {
                continue;
            }
            assertThatThrownBy(() -> user.upgradeLevel())
                    .isInstanceOf(IllegalStateException.class);
        }
    }

}