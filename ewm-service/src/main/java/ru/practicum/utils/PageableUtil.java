package ru.practicum.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import ru.practicum.utils.exception.ObjectNotFoundException;

public final class PageableUtil {

    private PageableUtil() {
    }

    public static Pageable pageManager(int from, int size, @Nullable String sort) {
        if (size < 0 || from < 0) {
            throw new ObjectNotFoundException("Невалидный индекс");
        }

        if (sort == null) {
            return PageRequest.of(
                    from == 0 ? 0 : (from / size),
                    size
            );
        } else {
            return PageRequest.of(
                    from == 0 ? 0 : (from / size),
                    size,
                    Sort.by(Sort.Direction.DESC, sort)
            );
        }


    }

}
