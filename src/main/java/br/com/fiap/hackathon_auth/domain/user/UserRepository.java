package br.com.fiap.hackathon_auth.domain.user;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

	User save(User user);

	User findById(UUID id);

	List<User> findAll();

	void deleteById(UUID id);

}
