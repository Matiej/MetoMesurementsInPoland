package pl.testaarosa.airmeasurements.domain.dtoApi;

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
        return levelNameTranslate(indexLevelName);
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
            indexLevelName = Optional.ofNullable(indexLevelName).orElse("N/A");
            return new LevelDto(id, indexLevelName);
        }
    }

    private String levelNameTranslate(String levelName) {
        switch (levelName) {
            case "Bardzo dobry":
                return "Very good";
            case "Dobry":
                return "Good";
            case "Umiarkowany":
                return "Moderate";
            case "Dostateczny":
                return "Sufficient";
            case "Zły":
                return "Bad";
            case "Bardzo zły":
                return "Very bad";
            default:
                return "N/A";
        }
    }
}
