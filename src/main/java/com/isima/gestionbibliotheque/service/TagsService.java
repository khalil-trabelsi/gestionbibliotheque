package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.CreateTagDto;
import com.isima.gestionbibliotheque.dto.TagDto;
import com.isima.gestionbibliotheque.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagsService {
    public List<TagDto> getAllTags();
    public Optional<TagDto> getTagById(Long id);
    public TagDto createTag(CreateTagDto createTagDto);
    public TagDto updateTag(TagDto tagDto);
    public void deleteTag(Long id);

}
