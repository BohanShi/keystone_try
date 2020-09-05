package com.example.keystone_try.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("gametwoscore")
public class GameTwoScore {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("score")
    private long score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long ascore) {
        score = ascore;
    }

    @Override
    public String toString() {
        return "GameTwoScore{" +
                "id=" + id +
                ", score=" + score +
                '}';
    }
}
