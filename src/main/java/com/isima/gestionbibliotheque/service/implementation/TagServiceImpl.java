package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.dto.CreateTagDto;
import com.isima.gestionbibliotheque.dto.TagDto;
import com.isima.gestionbibliotheque.model.Tag;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import com.isima.gestionbibliotheque.repository.TagRepository;
import com.isima.gestionbibliotheque.repository.UserBookRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagsService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          UserRepository userRepository,
                          UserBookRepository userBookRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
    }


    @Override
    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream().map(TagDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<TagDto> getTagsByBookId(Long bookId) {
        return tagRepository.findAllByUserBookBookId(bookId).stream().map(TagDto::fromEntity).toList();
    }

    @Override
    public Optional<TagDto> getTagById(Long id) {
        return tagRepository.findById(id).map(TagDto::fromEntity);
    }

    @Override
    public TagDto createTag(CreateTagDto createTagDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);

        UserBook userBook = userBookRepository.findById(createTagDto.getUserBookId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Book not found %d", createTagDto.getUserBookId())));

        Tag tag = new Tag();
        tag.setLabel(createTagDto.getLabel());
        tag.setColor(createTagDto.getColor());
        tag.setUser(user);
        tag.setUserBook(userBook);

        return TagDto.fromEntity(tagRepository.save(tag));
    }

    @Override
    public TagDto updateTag(TagDto tagDto) {
        Tag tag = tagRepository.findById(tagDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));

        User user = userRepository.findById(tagDto.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserBook book = userBookRepository.findById(tagDto.getUserBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        tag.setLabel(tagDto.getLabel());
        tag.setColor(tagDto.getColor());
        tag.setUser(user);
        tag.setUserBook(book);

        return TagDto.fromEntity(tagRepository.save(tag));
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }


}
