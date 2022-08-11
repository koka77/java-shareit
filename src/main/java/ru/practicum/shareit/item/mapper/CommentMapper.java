package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemRequestMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    @Mapping(source="author.name",target="authorName")
    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto dto);

    List<CommentDto> toDtoList(List<Comment> list);

    List<Comment> toCommentList(List<CommentDto> list);

    void updateCommentFromDto(CommentDto dto, @MappingTarget Comment comment);

}
