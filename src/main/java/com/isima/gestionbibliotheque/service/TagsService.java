package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.CreateTagDto;
import com.isima.gestionbibliotheque.dto.TagDto;
import com.isima.gestionbibliotheque.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagsService {
     List<TagDto> getAllTags();

     List<TagDto> getTagsByBookId(Long bookId);

     Optional<TagDto> getTagById(Long id);
     TagDto createTag(CreateTagDto createTagDto);
     TagDto updateTag(TagDto tagDto);
     void deleteTag(Long id);

}
