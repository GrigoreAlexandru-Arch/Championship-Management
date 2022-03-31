package database.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParticipationEntityPK implements Serializable {
    private long competitorId;
    private long stageId;

    @Column(name = "competitor_id", nullable = false)
    @Id
    public long getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(long competitorId) {
        this.competitorId = competitorId;
    }

    @Column(name = "stage_id", nullable = false)
    @Id
    public long getStageId() {
        return stageId;
    }

    public void setStageId(long stageId) {
        this.stageId = stageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationEntityPK pk = (ParticipationEntityPK) o;
        return competitorId == pk.competitorId && stageId == pk.stageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(competitorId, stageId);
    }
}
