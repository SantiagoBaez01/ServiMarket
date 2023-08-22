package com.grupob.ServiMarket.repository;

import com.grupob.ServiMarket.entity.Publication;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.enums.Rubro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Optional <Publication> findByTitle(String title);
    @Query("SELECT p from Publication p WHERE p.title LIKE CONCAT('%',:query,'%')")
    List<Publication> searchPublication(String query);
    @Query("SELECT p from Publication p WHERE p.description LIKE CONCAT('%',:query,'%')")
    List<Publication> searchContentPublication(String query);
    @Query("SELECT p from Publication p WHERE p.description2 LIKE CONCAT('%',:query,'%')")
    List<Publication> searchContentPublication2(String query);
    
    List<Publication> findByRubro(Rubro rubro);


}
