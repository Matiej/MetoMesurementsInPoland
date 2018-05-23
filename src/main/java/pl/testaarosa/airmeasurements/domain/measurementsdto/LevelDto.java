package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.fasterxml.jackson.annotation.JsonClassDescription;

import java.util.Objects;
import java.util.Optional;

@JsonClassDescription(value = "level")
public class LevelDto {
    private int id;
    private String indexLevelName;

    public LevelDto() {
    }

    private LevelDto(int id, String indexLevelName) {
        this.id = id;
        this.indexLevelName = indexLevelName;
    }

    public int getId() {
        return id;
    }

    public String getIndexLevelName() {
        return indexLevelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LevelDto)) {
            return false;
        }
        LevelDto levelDto = (LevelDto) o;
        return getId() == levelDto.getId() && Objects.equals(getIndexLevelName(), levelDto.getIndexLevelName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIndexLevelName());
    }

    @Override
    public String toString() {
        return "level: " + id + ", level name: " + indexLevelName;
    }

    public static class Builder {
        private int id;
        private String indexLevelName;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder indexLevelName(String indexLevelName) {
            this.indexLevelName = indexLevelName;
            return this;
        }

        public LevelDto build() {
            id = Optional.ofNullable(id).orElse(0);
            indexLevelName = Optional.ofNullable(indexLevelName).orElse("no data available");
            return new LevelDto(id, indexLevelName);
        }
    }
}
