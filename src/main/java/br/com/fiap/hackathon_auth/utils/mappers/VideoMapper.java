package br.com.fiap.hackathon_auth.utils.mappers;

import org.mapstruct.Mapper;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_auth.domain.video.Video;

@Mapper(componentModel = "spring")
public interface VideoMapper {

	Video jpaToDomain(JpaVideoEntity jpaVideoEntity);

	JpaVideoEntity domainToJpa(Video video);

}
