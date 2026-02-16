package br.com.fiap.hackathon_auth.utils.mappers;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.hackathon_auth.domain.user.User;

public interface UserMapper {

	User jpaToDomain(JpaUserEntity jpaUserEntity);

	JpaUserEntity domainToJpa(User user);

}
