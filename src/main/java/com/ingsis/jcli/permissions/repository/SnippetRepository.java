package com.ingsis.jcli.permissions.repository;

import com.ingsis.jcli.permissions.models.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {}
