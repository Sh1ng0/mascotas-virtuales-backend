package cibernarium.virtualPetBackEnd.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // Query methods personalizados

    Optional<User> findByUsername(String username);




}


