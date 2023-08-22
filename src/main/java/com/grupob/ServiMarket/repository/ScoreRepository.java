package com.grupob.ServiMarket.repository;


import com.grupob.ServiMarket.entity.Publication;
import com.grupob.ServiMarket.entity.Score;
import com.grupob.ServiMarket.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByProviderId(Long providerId);

    @Query("SELECT AVG(s.puntaje) FROM Score s WHERE s.provider.id = :providerId")
    Double findAverageScoreByProviderId(@Param("providerId") Long providerId);


    @Query("SELECT s FROM Score s WHERE s.provider = :provider AND s.solicitud.publication = :publication")
    List<Score> getScoresByPublicationAndProvider(UserEntity provider, Publication publication);

    @Query("SELECT s.provider.id, s.puntaje FROM Score s WHERE s.solicitud.publication = :publication")
    List<Object[]> getProviderIdAndScoreByPublication(Publication publication);


}

