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
import br.com.fiap.universidade_fiap.model.Moto;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class MotoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MotoRepository motoRepository;

    private Endereco endereco;
    private Filial filial;
    private Moto moto;

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
    }

    @Test
    void testCreate() {
        // Act
        Moto saved = motoRepository.save(moto);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("ABC1234", saved.getPlaca());
        assertEquals(2020, saved.getAno());
        assertEquals("Honda CB 600F", saved.getModelo());
        assertEquals("Gasolina", saved.getTipoCombustivel());
        assertNotNull(saved.getFilial());
        assertEquals(filial.getId(), saved.getFilial().getId());
    }

    @Test
    void testRead() {
        // Arrange
        Moto saved = entityManager.persistAndFlush(moto);

        // Act
        Optional<Moto> found = motoRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("ABC1234", found.get().getPlaca());
        assertEquals(2020, found.get().getAno());
        assertNotNull(found.get().getFilial());
    }

    @Test
    void testReadAll() {
        // Arrange
        entityManager.persistAndFlush(moto);

        Moto moto2 = new Moto();
        moto2.setPlaca("XYZ9876");
        moto2.setAno(2021);
        moto2.setModelo("Yamaha MT-07");
        moto2.setTipoCombustivel("Gasolina");
        moto2.setFilial(filial);
        entityManager.persistAndFlush(moto2);

        // Act
        List<Moto> all = motoRepository.findAll();

        // Assert
        assertTrue(all.size() >= 2);
    }

    @Test
    void testUpdate() {
        // Arrange
        Moto saved = entityManager.persistAndFlush(moto);
        Long id = saved.getId();

        // Act
        saved.setPlaca("DEF5678");
        saved.setAno(2022);
        saved.setModelo("Kawasaki Ninja");
        saved.setTipoCombustivel("Etanol");
        Moto updated = motoRepository.save(saved);

        // Assert
        assertEquals(id, updated.getId());
        assertEquals("DEF5678", updated.getPlaca());
        assertEquals(2022, updated.getAno());
        assertEquals("Kawasaki Ninja", updated.getModelo());
        assertEquals("Etanol", updated.getTipoCombustivel());
    }

    @Test
    void testUpdateFilial() {
        // Arrange
        Moto saved = entityManager.persistAndFlush(moto);

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
        Moto updated = motoRepository.save(saved);

        // Assert
        assertNotNull(updated.getFilial());
        assertEquals(filial2.getId(), updated.getFilial().getId());
    }

    @Test
    void testDelete() {
        // Arrange
        Moto saved = entityManager.persistAndFlush(moto);
        Long id = saved.getId();

        // Act
        motoRepository.deleteById(id);

        // Assert
        Optional<Moto> deleted = motoRepository.findById(id);
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        // Act
        Optional<Moto> found = motoRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testCountByFilial() {
        // Arrange
        entityManager.persistAndFlush(moto);

        Moto moto2 = new Moto();
        moto2.setPlaca("XYZ9876");
        moto2.setAno(2021);
        moto2.setModelo("Yamaha MT-07");
        moto2.setTipoCombustivel("Gasolina");
        moto2.setFilial(filial);
        entityManager.persistAndFlush(moto2);

        // Act
        long count = motoRepository.countByFilial_Id(filial.getId());

        // Assert
        assertEquals(2, count);
    }

    @Test
    void testBuscarAvancado() {
        // Arrange
        entityManager.persistAndFlush(moto);

        Moto moto2 = new Moto();
        moto2.setPlaca("XYZ9876");
        moto2.setAno(2021);
        moto2.setModelo("Yamaha MT-07");
        moto2.setTipoCombustivel("Gasolina");
        moto2.setFilial(filial);
        entityManager.persistAndFlush(moto2);

        // Act
        List<Moto> resultado = motoRepository.buscarAvancado("ABC", null, null, null, null);

        // Assert
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.stream().anyMatch(m -> m.getPlaca().contains("ABC")));
    }
}

