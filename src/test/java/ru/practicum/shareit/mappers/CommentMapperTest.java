package ru.practicum.shareit.mappers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest extends AbstractControllerTest {

    @Autowired
    CommentMapper commentMapper;

    private Comment comment = new Comment();

    private CommentDto dto = new CommentDto();

    @Test
    void shouldUpdateDtoCorrectly() {
         comment = new Comment();
        comment.setItem(new Item());
         dto = commentMapper.toCommentDto(comment);

        assertEquals(dto.getItem(), comment.getItem());
    }

    @Test
    void shouldConvertToBookingCorrectly() {
         dto = new CommentDto();
        dto.setItem(new Item());

         comment = commentMapper.toComment(dto);

        assertEquals(comment.getItem(), dto.getItem());
    }


    @Test
    void shouldUpdateFromDtoCorrectly() {

        commentMapper.updateCommentFromDto(dto, comment);
        assertEquals(comment.getItem(), dto.getItem());
    }

    @Test
    void shouldConvertToCommentListCorrectly() {

        List<Comment> comments = commentMapper.toCommentList(List.of(dto));
        assertEquals(comments.get(0).getText(), dto.getText());
    }

    @Test
    void shouldReturnNull() {
        assertEquals(commentMapper.toCommentDto(null), null);
        assertEquals(commentMapper.toCommentDto(null), null);
        assertEquals(commentMapper.toCommentList(null), null);
    }
}
