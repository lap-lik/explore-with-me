package ru.practicum.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.* " +
            "FROM users AS u " +
            "WHERE u.id IN (:ids)" +
            "LIMIT :size OFFSET :from", nativeQuery = true)
    List<User> findAll(@Param("ids") List<Long> ids, @Param("from") int from, @Param("size") int size);
}
