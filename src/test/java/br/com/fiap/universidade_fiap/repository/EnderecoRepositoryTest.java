package br.com.fiap.universidade_fiap.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import br.com.fiap.universidade_fiap.model.Endereco;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class EnderecoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnderecoRepository enderecoRepository;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero(123);
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234-567");
        endereco.setComplemento("Apto 101");
    }

    @Test
    void testCreate() {
        // Act
        Endereco saved = enderecoRepository.save(endereco);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Rua Teste", saved.getLogradouro());
        assertEquals(123, saved.getNumero());
        assertEquals("Centro", saved.getBairro());
        assertEquals("São Paulo", saved.getCidade());
        assertEquals("SP", saved.getEstado());
        assertEquals("01234-567", saved.getCep());
        assertEquals("Apto 101", saved.getComplemento());
    }

    @Test
    void testRead() {
        // Arrange
        Endereco saved = entityManager.persistAndFlush(endereco);

        // Act
        Optional<Endereco> found = enderecoRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Rua Teste", found.get().getLogradouro());
    }

    @Test
    void testReadAll() {
        // Arrange
        entityManager.persistAndFlush(endereco);

        Endereco endereco2 = new Endereco();
        endereco2.setLogradouro("Avenida Principal");
        endereco2.setNumero(456);
        endereco2.setBairro("Jardim");
        endereco2.setCidade("Rio de Janeiro");
        endereco2.setEstado("RJ");
        endereco2.setCep("23456-789");
        entityManager.persistAndFlush(endereco2);

        // Act
        List<Endereco> all = enderecoRepository.findAll();

        // Assert
        assertTrue(all.size() >= 2);
    }

    @Test
    void testUpdate() {
        // Arrange
        Endereco saved = entityManager.persistAndFlush(endereco);
        Long id = saved.getId();

        // Act
        saved.setLogradouro("Rua Atualizada");
        saved.setNumero(789);
        saved.setBairro("Novo Bairro");
        Endereco updated = enderecoRepository.save(saved);

        // Assert
        assertEquals(id, updated.getId());
        assertEquals("Rua Atualizada", updated.getLogradouro());
        assertEquals(789, updated.getNumero());
        assertEquals("Novo Bairro", updated.getBairro());
    }

    @Test
    void testDelete() {
        // Arrange
        Endereco saved = entityManager.persistAndFlush(endereco);
        Long id = saved.getId();

        // Act
        enderecoRepository.deleteById(id);

        // Assert
        Optional<Endereco> deleted = enderecoRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Endereco> found = enderecoRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }
}

