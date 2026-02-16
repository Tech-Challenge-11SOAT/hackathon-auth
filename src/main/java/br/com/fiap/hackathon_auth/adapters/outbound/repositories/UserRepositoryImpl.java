package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserRepository;
import br.com.fiap.hackathon_auth.utils.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final JpaUserRepository jpaUserRepository;
	private UserMapper userMapper;

	@Override
	public User save(User user) {
		JpaUserEntity jpaUserEntity = userMapper.domainToJpa(user);

		this.jpaUserRepository.save(jpaUserEntity);
		return userMapper.jpaToDomain(jpaUserEntity);
	}

	@Override
	public User findById(UUID id) {
		Optional<JpaUserEntity> optionalEntity = this.jpaUserRepository.findById(id);
		return optionalEntity.map(userMapper::jpaToDomain).orElse(null);
	}

	@Override
	public List<User> findAll() {
		List<JpaUserEntity> entities = this.jpaUserRepository.findAll();
		return entities.stream().map(userMapper::jpaToDomain).toList();
	}

	@Override
	public void deleteById(UUID id) {
		this.jpaUserRepository.deleteById(id);
	}

}
