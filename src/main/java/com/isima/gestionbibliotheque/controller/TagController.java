package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.dto.CreateTagDto;
import com.isima.gestionbibliotheque.dto.TagDto;
import com.isima.gestionbibliotheque.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagsService tagService;

    @Autowired
    public TagController(TagsService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        return tagService.getTagById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody CreateTagDto tagDto) {
        return ResponseEntity.ok(tagService.createTag(tagDto));
    }

    @PutMapping()
    public ResponseEntity<TagDto> updateTag(@RequestBody TagDto tagDto) {
        return ResponseEntity.ok(tagService.updateTag(tagDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}