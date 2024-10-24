package com.ingsis.jcli.permissions.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ingsis.jcli.permissions.models.Snippet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SnippetRepositoryTest {

  @Autowired private SnippetRepository snippetRepository;

  @Test
  void testSaveSnippet() {
    Snippet snippet = new Snippet();
    snippet.setContent("Test Snippet Content");

    Snippet savedSnippet = snippetRepository.save(snippet);

    assertThat(savedSnippet.getId()).isNotNull();
    assertThat(savedSnippet.getContent()).isEqualTo("Test Snippet Content");
  }

  @Test
  void testFindById() {
    Snippet snippet = new Snippet();
    snippet.setContent("Another Snippet Content");

    Snippet savedSnippet = snippetRepository.save(snippet);

    Optional<Snippet> foundSnippet = snippetRepository.findById(savedSnippet.getId());

    assertThat(foundSnippet).isPresent();
    assertThat(foundSnippet.get().getId()).isEqualTo(savedSnippet.getId());
    assertThat(foundSnippet.get().getContent()).isEqualTo("Another Snippet Content");
  }

  @Test
  void testUpdateSnippet() {
    Snippet snippet = new Snippet();
    snippet.setContent("Initial Content");

    Snippet savedSnippet = snippetRepository.save(snippet);

    savedSnippet.setContent("Updated Content");
    Snippet updatedSnippet = snippetRepository.save(savedSnippet);

    assertThat(updatedSnippet.getContent()).isEqualTo("Updated Content");
  }

  @Test
  void testDeleteSnippet() {
    Snippet snippet = new Snippet();
    snippet.setContent("Snippet to be deleted");

    Snippet savedSnippet = snippetRepository.save(snippet);

    snippetRepository.delete(savedSnippet);

    Optional<Snippet> deletedSnippet = snippetRepository.findById(savedSnippet.getId());
    assertThat(deletedSnippet).isNotPresent();
  }
}
