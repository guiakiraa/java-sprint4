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
import br.com.fiap.universidade_fiap.model.Filial;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class FilialRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilialRepository filialRepository;

    private Endereco endereco;
    private Filial filial;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero(123);
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234-567");
        endereco = entityManager.persistAndFlush(endereco);

        filial = new Filial();
        filial.setNome("Filial Centro");
        filial.setEndereco(endereco);
    }

    @Test
    void testCreate() {
        // Act
        Filial saved = filialRepository.save(filial);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Filial Centro", saved.getNome());
        assertNotNull(saved.getEndereco());
        assertEquals(endereco.getId(), saved.getEndereco().getId());
    }

    @Test
    void testRead() {
        // Arrange
        Filial saved = entityManager.persistAndFlush(filial);

        // Act
        Optional<Filial> found = filialRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Filial Centro", found.get().getNome());
        assertNotNull(found.get().getEndereco());
    }

    @Test
    void testReadAll() {
        // Arrange
        entityManager.persistAndFlush(filial);

        Endereco endereco2 = new Endereco();
        endereco2.setLogradouro("Avenida Principal");
        endereco2.setNumero(456);
        endereco2.setBairro("Jardim");
        endereco2.setCidade("Rio de Janeiro");
        endereco2.setEstado("RJ");
        endereco2.setCep("23456-789");
        endereco2 = entityManager.persistAndFlush(endereco2);

        Filial filial2 = new Filial();
        filial2.setNome("Filial Jardim");
        filial2.setEndereco(endereco2);
        entityManager.persistAndFlush(filial2);

        // Act
        List<Filial> all = filialRepository.findAll();

        // Assert
        assertTrue(all.size() >= 2);
    }

    @Test
    void testUpdate() {
        // Arrange
        Filial saved = entityManager.persistAndFlush(filial);
        Long id = saved.getId();

        // Act
        saved.setNome("Filial Atualizada");
        Filial updated = filialRepository.save(saved);

        // Assert
        assertEquals(id, updated.getId());
        assertEquals("Filial Atualizada", updated.getNome());
    }

    @Test
    void testUpdateEndereco() {
        // Arrange
        Filial saved = entityManager.persistAndFlush(filial);

        Endereco novoEndereco = new Endereco();
        novoEndereco.setLogradouro("Nova Rua");
        novoEndereco.setNumero(789);
        novoEndereco.setBairro("Novo Bairro");
        novoEndereco.setCidade("Belo Horizonte");
        novoEndereco.setEstado("MG");
        novoEndereco.setCep("34567-890");
        novoEndereco = entityManager.persistAndFlush(novoEndereco);

        // Act
        saved.setEndereco(novoEndereco);
        Filial updated = filialRepository.save(saved);

        // Assert
        assertNotNull(updated.getEndereco());
        assertEquals(novoEndereco.getId(), updated.getEndereco().getId());
    }

    @Test
    void testDelete() {
        // Arrange
        Filial saved = entityManager.persistAndFlush(filial);
        Long id = saved.getId();

        // Act
        filialRepository.deleteById(id);

        // Assert
        Optional<Filial> deleted = filialRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Filial> found = filialRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }
}

