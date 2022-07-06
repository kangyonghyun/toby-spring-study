package springbook.ch4.user.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String password;

    private Level level;
    private int login;
    private int recommend;

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

    public boolean canUpgradeLevel() {;
        if (level == Level.BASIC){
            return login >= 50;
        }
        if (level == Level.SILVER){
            return recommend >= 30;
        }
        if (level == Level.GOLD) {
            return false;
        }
        throw new IllegalArgumentException("Unknown level : " + level);
    }

    public void upgradeLevel() {
        Level nextLevel = level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드 불가능");
        }
        level = nextLevel;
    }

}
