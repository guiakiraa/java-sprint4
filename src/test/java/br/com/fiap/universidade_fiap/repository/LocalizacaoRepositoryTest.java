package br.com.fiap.universidade_fiap.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
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
import br.com.fiap.universidade_fiap.model.Localizacao;
import br.com.fiap.universidade_fiap.model.Moto;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class LocalizacaoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    private Endereco endereco;
    private Filial filial;
    private Moto moto;
    private Localizacao localizacao;

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

        moto = new Moto();
        moto.setPlaca("ABC1234");
        moto.setAno(2020);
        moto.setModelo("Honda CB 600F");
        moto.setTipoCombustivel("Gasolina");
        moto.setFilial(filial);
        moto = entityManager.persistAndFlush(moto);

        localizacao = new Localizacao();
        localizacao.setPontoX(-23.5505);
        localizacao.setPontoY(-46.6333);
        localizacao.setDataHora(LocalDateTime.now());
        localizacao.setFonte("GPS");
        localizacao.setMoto(moto);
    }

    @Test
    void testCreate() {
        // Act
        Localizacao saved = localizacaoRepository.save(localizacao);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(-23.5505, saved.getPontoX());
        assertEquals(-46.6333, saved.getPontoY());
        assertNotNull(saved.getDataHora());
        assertEquals("GPS", saved.getFonte());
        assertNotNull(saved.getMoto());
        assertEquals(moto.getId(), saved.getMoto().getId());
    }

    @Test
    void testRead() {
        // Arrange
        Localizacao saved = entityManager.persistAndFlush(localizacao);

        // Act
        Optional<Localizacao> found = localizacaoRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(-23.5505, found.get().getPontoX());
        assertEquals(-46.6333, found.get().getPontoY());
        assertEquals("GPS", found.get().getFonte());
        assertNotNull(found.get().getMoto());
    }

    @Test
    void testReadAll() {
        // Arrange
        entityManager.persistAndFlush(localizacao);

        Localizacao localizacao2 = new Localizacao();
        localizacao2.setPontoX(-22.9068);
        localizacao2.setPontoY(-43.1729);
        localizacao2.setDataHora(LocalDateTime.now());
        localizacao2.setFonte("App");
        localizacao2.setMoto(moto);
        entityManager.persistAndFlush(localizacao2);

        // Act
        List<Localizacao> all = localizacaoRepository.findAll();

        // Assert
        assertTrue(all.size() >= 2);
    }

    @Test
    void testUpdate() {
        // Arrange
        Localizacao saved = entityManager.persistAndFlush(localizacao);
        Long id = saved.getId();

        // Act
        saved.setPontoX(-22.9068);
        saved.setPontoY(-43.1729);
        saved.setFonte("App Atualizado");
        saved.setDataHora(LocalDateTime.now().plusHours(1));
        Localizacao updated = localizacaoRepository.save(saved);

        // Assert
        assertEquals(id, updated.getId());
        assertEquals(-22.9068, updated.getPontoX());
        assertEquals(-43.1729, updated.getPontoY());
        assertEquals("App Atualizado", updated.getFonte());
    }

    @Test
    void testUpdateMoto() {
        // Arrange
        Localizacao saved = entityManager.persistAndFlush(localizacao);

        Moto moto2 = new Moto();
        moto2.setPlaca("XYZ9876");
        moto2.setAno(2021);
        moto2.setModelo("Yamaha MT-07");
        moto2.setTipoCombustivel("Gasolina");
        moto2.setFilial(filial);
        moto2 = entityManager.persistAndFlush(moto2);

        // Act
        saved.setMoto(moto2);
        Localizacao updated = localizacaoRepository.save(saved);

        // Assert
        assertNotNull(updated.getMoto());
        assertEquals(moto2.getId(), updated.getMoto().getId());
    }

    @Test
    void testDelete() {
        // Arrange
        Localizacao saved = entityManager.persistAndFlush(localizacao);
        Long id = saved.getId();

        // Act
        localizacaoRepository.deleteById(id);

        // Assert
        Optional<Localizacao> deleted = localizacaoRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Localizacao> found = localizacaoRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testCreateWithSpecificDateTime() {
        // Arrange
        LocalDateTime specificDateTime = LocalDateTime.of(2024, 1, 15, 14, 30);
        localizacao.setDataHora(specificDateTime);

        // Act
        Localizacao saved = localizacaoRepository.save(localizacao);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(specificDateTime, saved.getDataHora());
    }
}

