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
import br.com.fiap.universidade_fiap.model.Funcionario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class FuncionarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private Endereco endereco;
    private Filial filial;
    private Funcionario funcionario;

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
        filial = entityManager.persistAndFlush(filial);

        funcionario = new Funcionario();
        funcionario.setNome("João Silva");
        funcionario.setFilial(filial);
    }

    @Test
    void testCreate() {
        // Act
        Funcionario saved = funcionarioRepository.save(funcionario);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("João Silva", saved.getNome());
        assertNotNull(saved.getFilial());
        assertEquals(filial.getId(), saved.getFilial().getId());
    }

    @Test
    void testRead() {
        // Arrange
        Funcionario saved = entityManager.persistAndFlush(funcionario);

        // Act
        Optional<Funcionario> found = funcionarioRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("João Silva", found.get().getNome());
        assertNotNull(found.get().getFilial());
    }

    @Test
    void testReadAll() {
        // Arrange
        entityManager.persistAndFlush(funcionario);

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Santos");
        funcionario2.setFilial(filial);
        entityManager.persistAndFlush(funcionario2);

        // Act
        List<Funcionario> all = funcionarioRepository.findAll();

        // Assert
        assertTrue(all.size() >= 2);
    }

    @Test
    void testUpdate() {
        // Arrange
        Funcionario saved = entityManager.persistAndFlush(funcionario);
        Long id = saved.getId();

        // Act
        saved.setNome("João Silva Atualizado");
        Funcionario updated = funcionarioRepository.save(saved);

        // Assert
        assertEquals(id, updated.getId());
        assertEquals("João Silva Atualizado", updated.getNome());
    }

    @Test
    void testUpdateFilial() {
        // Arrange
        Funcionario saved = entityManager.persistAndFlush(funcionario);

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
        filial2 = entityManager.persistAndFlush(filial2);

        // Act
        saved.setFilial(filial2);
        Funcionario updated = funcionarioRepository.save(saved);

        // Assert
        assertNotNull(updated.getFilial());
        assertEquals(filial2.getId(), updated.getFilial().getId());
    }

    @Test
    void testDelete() {
        // Arrange
        Funcionario saved = entityManager.persistAndFlush(funcionario);
        Long id = saved.getId();

        // Act
        funcionarioRepository.deleteById(id);

        // Assert
        Optional<Funcionario> deleted = funcionarioRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Funcionario> found = funcionarioRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testCountByFilial() {
        // Arrange
        entityManager.persistAndFlush(funcionario);

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Maria Santos");
        funcionario2.setFilial(filial);
        entityManager.persistAndFlush(funcionario2);

        // Act
        long count = funcionarioRepository.countByFilial_Id(filial.getId());

        // Assert
        assertEquals(2, count);
    }
}

