package br.com.fiap.hackathon_auth.utils.mappers;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.hackathon_auth.domain.user.User;

@Primary
@Component
public class UserMapperManual implements UserMapper {

	@Override
	public User jpaToDomain(JpaUserEntity jpaUserEntity) {
		if (jpaUserEntity == null) {
			return null;
		}

		User user = new User();
		user.setId(jpaUserEntity.getId());
		user.setName(jpaUserEntity.getName());
		user.setEmail(jpaUserEntity.getEmail());
		user.setPasswordHash(jpaUserEntity.getPasswordHash());
		user.setCreatedAt(jpaUserEntity.getCreatedAt());
		user.setUpdatedAt(jpaUserEntity.getUpdatedAt());
		return user;
	}

	@Override
	public JpaUserEntity domainToJpa(User user) {
		if (user == null) {
			return null;
		}

		JpaUserEntity jpaUserEntity = new JpaUserEntity();
		jpaUserEntity.setId(user.getId());
		jpaUserEntity.setName(user.getName());
		jpaUserEntity.setEmail(user.getEmail());
		jpaUserEntity.setPasswordHash(user.getPasswordHash());
		jpaUserEntity.setCreatedAt(user.getCreatedAt());
		jpaUserEntity.setUpdatedAt(user.getUpdatedAt());
		return jpaUserEntity;
	}
}
