package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.dto.CreateTagDto;
import com.isima.gestionbibliotheque.dto.TagDto;
import com.isima.gestionbibliotheque.model.Tag;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import com.isima.gestionbibliotheque.repository.TagRepository;
import com.isima.gestionbibliotheque.repository.UserBookRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.TagsService;
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

    public TagServiceImpl(TagRepository tagRepository, UserRepository userRepository, UserBookRepository userBookRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
    }


    @Override
    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream().map(TagDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<TagDto> getTagById(Long id) {
        return tagRepository.findById(id).map(TagDto::fromEntity);
    }

    @Override
    public TagDto createTag(CreateTagDto createTagDto) {
        // Récupérer l'utilisateur connecté (utiliser Spring Security pour ça)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByUsername(username);

        // Récupérer le UserBook
        UserBook userBook = userBookRepository.findById(createTagDto.getUserBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Créer le Tag
        Tag tag = new Tag();
        tag.setLabel(createTagDto.getLabel());
        tag.setColor(createTagDto.getColor());
        tag.setUser(user);
        tag.setUserBook(userBook);

        // Sauvegarder et retourner le DTO
        tag = tagRepository.save(tag);
        return TagDto.fromEntity(tag);
    }

    @Override
    public TagDto updateTag(TagDto tagDto) {
        Tag tag = tagRepository.findById(tagDto.getId())
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        User user = userRepository.findById(tagDto.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserBook book = userBookRepository.findById(tagDto.getUserBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        tag.setLabel(tagDto.getLabel());
        tag.setColor(tagDto.getColor());
        tag.setUser(user);
        tag.setUserBook(book);

        tag = tagRepository.save(tag);
        return TagDto.fromEntity(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }


}
