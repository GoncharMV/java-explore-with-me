package ru.practicum.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.exception.ObjectNotFoundException;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class FindEntityUtilService {

    private final UserRepository userRepository;
    private final CategoriesRepository catRepository;

    public User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
    }

    public Category findCategoryOrElseThrow(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория не найдена"));
    }

}
