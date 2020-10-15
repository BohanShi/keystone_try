package com.example.keystone_try.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("gamethreescore")
public class GameThreeScore {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("score")
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "GameThreeScore{" +
                "id=" + id +
                ", score=" + score +
                '}';
    }
}