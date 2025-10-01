package br.edu.ifrs.canoas.gamestore.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifrs.canoas.gamestore.model.domain.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    public User findByEmail(String email);

    public Optional<User> findById(Long id);
}