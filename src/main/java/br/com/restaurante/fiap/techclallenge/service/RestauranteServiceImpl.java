package br.com.restaurante.fiap.techclallenge.service;

import br.com.restaurante.fiap.techclallenge.entities.Restaurante;
import br.com.restaurante.fiap.techclallenge.entities.RestauranteDTO;
import br.com.restaurante.fiap.techclallenge.exceptions.IdFilterNotExistException;
import br.com.restaurante.fiap.techclallenge.exceptions.RestauranteNotFoundException;
import br.com.restaurante.fiap.techclallenge.repository.RestauranteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService{

    private final ModelMapper modelMapper;

    private final RestauranteRepository restauranteRepository;

    @Override
    public Restaurante cadastrarRestaurante(RestauranteDTO restauranteDTO) {
        Restaurante restaurante = modelMapper.map(restauranteDTO, Restaurante.class);

        return restauranteRepository.save(restaurante);
    }

    @Override
    public Restaurante buscaRestaurante(Long id) {
        return restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNotFoundException("Restaurante não foi encontrado"));
    }

    @Override
    public Page<RestauranteDTO> buscarTodosRestaurantes(Pageable pageable) {
        var listaRestaurantes = restauranteRepository.findAll(pageable);

        return mapToRestauranteDTO(listaRestaurantes);
    }

    private PageImpl<RestauranteDTO> mapToRestauranteDTO(Page<Restaurante> listaRestaurantes) {
        List<RestauranteDTO> listaRestaurantesDTO = listaRestaurantes.getContent()
                        .stream()
                        .map(restaurante -> modelMapper.map(restaurante, RestauranteDTO.class))
                        .toList();

        return new PageImpl<>(listaRestaurantesDTO, listaRestaurantes.getPageable(), listaRestaurantes.getTotalElements());
    }

    @Override
    public Page<RestauranteDTO> buscaRestaurantePorIdFiltro(int idFiltro, String tipoDeBusca, Pageable pageable) {
        Page<Restaurante> restaurantes;

        restaurantes = getRestaurantes(idFiltro, tipoDeBusca, pageable);

        if (restaurantes.isEmpty())
            throw new RestauranteNotFoundException("Restaurante filtrado não existe na base de dados.");

        return mapToRestauranteDTO(restaurantes);
    }

    private Page<Restaurante> getRestaurantes(int idFiltro, String tipoDeBusca, Pageable pageable) {
        Page<Restaurante> restaurantes;
        Optional<String> nome = Optional.empty();
        Optional<String> localizacao = Optional.empty();
        Optional<String> tipoCozinha = Optional.empty();

        switch (idFiltro) {
            case 1:
                nome = Optional.of(tipoDeBusca);
                break;
            case 2:
                localizacao = Optional.of(tipoDeBusca);
                break;
            case 3:
                tipoCozinha = Optional.of(tipoDeBusca);
                break;
            default:
                throw new IdFilterNotExistException("Valor numérico inválido: " + idFiltro);
        }

        restaurantes = restauranteRepository.listarRestaurantes(nome.orElse(null), localizacao.orElse(null), tipoCozinha.orElse(null), pageable);

        return restaurantes;
    }

}

