package springbook.ch4.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Test
    void getValue() {
        assertThat(Level.valueOf("BASIC")).isEqualTo(Level.BASIC);
        assertThat(Level.valueOf("SILVER")).isEqualTo(Level.SILVER);
        assertThat(Level.valueOf("GOLD")).isEqualTo(Level.GOLD);
    }

    @Test
    void getValueOf() {
        assertThat(Level.valueOf(1)).isEqualTo(Level.BASIC);
        assertThat(Level.valueOf(2)).isEqualTo(Level.SILVER);
        assertThat(Level.valueOf(3)).isEqualTo(Level.GOLD);
    }

    @Test
    void value() {
        Level basic = Level.BASIC;
        assertThat(basic.intValue()).isEqualTo(1);
    }



}