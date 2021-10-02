package ru.inie.social.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inie.social.server.repositories.RelationshipsRepository;

@Service
public class RelationshipsService {

    private final RelationshipsRepository relationshipsRepository;

    @Autowired
    public RelationshipsService(RelationshipsRepository relationshipsRepository) {
        this.relationshipsRepository = relationshipsRepository;
    }
}
