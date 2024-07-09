package ru.practicum.comment.dao;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentDAO extends CrudRepository<Comment, Long> {

    List<Comment> findByEvent_IdIn(List<Long> eventIds);
}
