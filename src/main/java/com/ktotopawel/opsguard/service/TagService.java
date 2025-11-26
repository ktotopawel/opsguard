package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.entity.Tag;
import com.ktotopawel.opsguard.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;

    public Tag findOrCreate(String name) {
        return repository.findByName(name).orElseGet(() -> this.create(name));
    }

    public Tag create(String name) {
        Tag newTag = new Tag();
        newTag.setName(name);
        return repository.save(newTag);
    }
}
