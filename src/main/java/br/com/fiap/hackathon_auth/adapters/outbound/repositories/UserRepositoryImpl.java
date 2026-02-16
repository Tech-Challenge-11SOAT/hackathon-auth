package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.hackathon_auth.domain.user.User;
import br.com.fiap.hackathon_auth.domain.user.UserRepository;
import br.com.fiap.hackathon_auth.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final JpaUserRepository jpaUserRepository;
	private final UserMapper userMapper;

	@Override
	public User save(User user) {
		JpaUserEntity jpaUserEntity = userMapper.domainToJpa(user);
		jpaUserEntity = this.jpaUserRepository.saveAndFlush(jpaUserEntity);
		return userMapper.jpaToDomain(jpaUserEntity);
	}

	@Override
	public Optional<User> findById(UUID id) {
		return this.jpaUserRepository.findById(id)
				.map(userMapper::jpaToDomain);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return this.jpaUserRepository.findByEmail(email)
				.map(userMapper::jpaToDomain);
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
