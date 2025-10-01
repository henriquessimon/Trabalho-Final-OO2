package br.edu.ifrs.canoas.gamestore.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.Dev;

@Repository
public interface DevRepository extends JpaRepository<Dev, Long>{
    public Dev findByName(String name);

    public List<Dev> findAll();
}
