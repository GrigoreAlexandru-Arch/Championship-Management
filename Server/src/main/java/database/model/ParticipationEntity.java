package database.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "participation", schema = "public", catalog = "competition")
@IdClass(ParticipationEntityPK.class)
public class ParticipationEntity {
    private long competitorId;
    private long stageId;
    private Integer points = -1;

    @Id
    @Column(name = "competitor_id", nullable = false)
    public long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(long competitorId) {
        this.competitorId = competitorId;
    }

    @Id
    @Column(name = "stage_id", nullable = false)
    public long getStageId() {
        return stageId;
    }

    public void setStageId(long stageId) {
        this.stageId = stageId;
    }

    @Basic
    @Column(name = "points", nullable = true)
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationEntity that = (ParticipationEntity) o;
        return competitorId == that.competitorId && stageId == that.stageId && Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(competitorId, stageId, points);
    }
}
