package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exceptions.DatabaseException;
import com.devsuperior.bds02.services.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    static final String CITY_NOT_FOUND = "City not found";
    static final String DATA_INTEGRITY_VIOLATION_EXCEPTION = "Data Integrity Violation";

    private final CityRepository repository;

    public CityService(CityRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CityDTO> findAll() {
        List<City> result = repository.findAll(Sort.by("name"));
        return result.stream().map(CityDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {
        City entity = new City(null, dto.getName());
        entity = repository.save(entity);

        return new CityDTO(entity);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(CITY_NOT_FOUND);
        } catch (DataIntegrityViolationException c) {
            throw new DatabaseException(DATA_INTEGRITY_VIOLATION_EXCEPTION);
        }
    }
}
