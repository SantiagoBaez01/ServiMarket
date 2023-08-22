package com.grupob.ServiMarket.service;

import com.grupob.ServiMarket.entity.Publication;
import com.grupob.ServiMarket.entity.Score;
import com.grupob.ServiMarket.entity.Solicitud;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.exceptions.MyException;
import com.grupob.ServiMarket.repository.PublicationRepository;
import com.grupob.ServiMarket.repository.ScoreRepository;
import com.grupob.ServiMarket.repository.SolicitudRepository;
import com.grupob.ServiMarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository pRepository;


    //------------------------CREATE--------------------------
    @Transactional
    public void create(Score score, Long solid, Long userId) throws MyException {
        validate(score);
        Solicitud sol = solicitudRepository.findById(solid).get();
        Score calif = score;
        UserEntity user = new UserEntity();
        Optional<UserEntity> usAnswer = userRepository.findById(userId);


        if (usAnswer.isPresent()) {
            user = usAnswer.get();
            calif.setProvider(sol.getProvider());
            sol.setCalificado(true);
            calif.setCliente(user);
            calif.setSolicitud(sol);
            calif.setCalificado(true);
            scoreRepository.save(score);
        }


    }

    //------------------------READ--------------------------

    public List<Score> list() {
        List<Score> califications = new ArrayList<>();
        califications = scoreRepository.findAll();
        return califications;
    }


    //---------------------GET PUBLICATION BY ID-----------------
    public Score getScoreById(Long id) {
        return scoreRepository.findById(id).orElse(null);
    }


    //------------------------UPDATE--------------------------

    public Score editCalification( String comentario, int puntaje, Long solid) throws MyException {
        validateEdit(puntaje, comentario);
       Optional<Score> optionalScore = scoreRepository.findById(solid);

        if (optionalScore.isPresent()) {
            Score updateScore = optionalScore.get();
            updateScore.setComentario(comentario);
            updateScore.setPuntaje(puntaje);;
            scoreRepository.save(updateScore);
            return updateScore;
        }
        return null;
    }


    //------------------------DELETE--------------------------

    @Transactional
    public void delete(Long id) {
        Optional<Score> response = scoreRepository.findById(id);
        if (response != null) {
            Score califToDelete = response.get();
            scoreRepository.delete(califToDelete);
        }
    }
//----------------------CENSURE COMMENT----------------------
    @Transactional
    public void censure(Long id) {
        Optional<Score> answer = scoreRepository.findById(id);
        if (answer.isPresent()) {
        Score score = answer.get();

        if (score.isCensured()) {
            score.setCensured(false);
        } else if (!score.isCensured()) {
            score.setCensured(true);
        }
    }
}
    
    public Double calcularPromedioPuntaje(UserEntity provider) {
        return scoreRepository.findAverageScoreByProviderId(provider.getId());
    }
    public List<Score> puntajePorPublicacion(UserEntity provider, Publication publication){
        return scoreRepository.getScoresByPublicationAndProvider(provider,publication);
    }

    public List<Object[]> getProviderIdAndScoreByPublication(Publication publication) {
        return scoreRepository.getProviderIdAndScoreByPublication(publication);
    }

    private void validateEdit(Integer puntaje, String comentario) throws MyException {

        // Verificar que el atributo "puntaje" no sea nulo o vacío
        if (puntaje <= 0 || puntaje == null) {
            throw new MyException("El puntaje no puede ser 0");

        }
        // verificar que el comentario no esté vacio
        if (comentario == null || comentario.isEmpty()) {
            throw new MyException("El comentario no puede estar vacio");

        }
    }
    private void validate(Score score) throws MyException {

        // Verificar que el atributo "puntaje" no sea nulo o vacío
        if (score.getPuntaje() == null || score.getPuntaje() <= 0) {
            throw new MyException("El puntaje no puede ser 0");

        }
        // verificar que el comentario no esté vacio
        if (score.getComentario() == null || score.getComentario().isEmpty()) {
            throw new MyException("El comentario no puede estar vacio");

        }
    }

}
