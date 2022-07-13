package springbook.ch5.user.domain;

import lombok.*;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class User {

    private String id;
    private String name;
    private String password;

    private Level level;
    private int login;
    private int recommend;

    private String email;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public void upgradeLevel() {
        Level nextLevel = level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드 불가능");
        }
        level = nextLevel;
    }

    public String getEmail() {
        return this.email;
    }
}
