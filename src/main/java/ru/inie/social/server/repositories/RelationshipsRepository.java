package ru.inie.social.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.inie.social.server.entities.Relationship;

@Repository
public interface RelationshipsRepository extends JpaRepository<Relationship, Long> {
}
