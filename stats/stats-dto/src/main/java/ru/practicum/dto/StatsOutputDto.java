package ru.practicum.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsOutputDto {

    private String app;
    private String uri;
    private Long hits;

    @Override
    public String toString() {
        return "ViewStats{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsOutputDto viewStats = (StatsOutputDto) o;
        return Objects.equals(app, viewStats.app) && Objects.equals(uri, viewStats.uri) && Objects.equals(hits, viewStats.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }

}
