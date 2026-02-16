package br.com.fiap.hackathon_auth.domain.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

	User save(User user);

	User findById(UUID id);

	Optional<User> findByEmail(String email);

	List<User> findAll();

	void deleteById(UUID id);

}
