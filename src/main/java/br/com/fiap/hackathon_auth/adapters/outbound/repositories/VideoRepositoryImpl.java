package br.com.fiap.hackathon_auth.adapters.outbound.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.fiap.hackathon_auth.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_auth.domain.video.Video;
import br.com.fiap.hackathon_auth.domain.video.VideoRepository;
import br.com.fiap.hackathon_auth.utils.mappers.VideoMapper;

@Repository
public class VideoRepositoryImpl implements VideoRepository {

	private final JpaVideoRepository jpaVideoRepository;

	@Autowired
	private VideoMapper videoMapper;

	public VideoRepositoryImpl(JpaVideoRepository jpaVideoRepository) {
		this.jpaVideoRepository = jpaVideoRepository;
	}

	@Override
	public Video save(Video video) {
		JpaVideoEntity jpaVideoEntity = videoMapper.domainToJpa(video);

		this.jpaVideoRepository.save(jpaVideoEntity);
		return videoMapper.jpaToDomain(jpaVideoEntity);
	}

	@Override
	public Video findById(UUID id) {
		Optional<JpaVideoEntity> optionalEntity = this.jpaVideoRepository.findById(id);
		return optionalEntity.map(videoMapper::jpaToDomain).orElse(null);
	}

	@Override
	public List<Video> findAll() {
		List<JpaVideoEntity> entities = this.jpaVideoRepository.findAll();
		return entities.stream().map(videoMapper::jpaToDomain).toList();
	}

	@Override
	public void deleteById(UUID id) {
		this.jpaVideoRepository.deleteById(id);
	}

}
