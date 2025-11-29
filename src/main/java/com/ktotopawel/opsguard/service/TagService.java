package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.entity.Tag;
import com.ktotopawel.opsguard.exception.TagNotFoundException;
import com.ktotopawel.opsguard.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;

    public Tag findOrCreate(String name) {
        try {
            Tag newTag = new Tag();
            newTag.setName(name);
            return repository.save(newTag);
        } catch (DataIntegrityViolationException e) {
            // shouldn't throw, only enters this block if tag is already found in db
            return repository.findByName(name).orElseThrow(() -> new TagNotFoundException("Failed to find tag with name " + name + "after trying to create it"));
        }
    }

    public Tag create(String name) {
        Tag newTag = new Tag();
        newTag.setName(name);
        return repository.save(newTag);
    }
}
