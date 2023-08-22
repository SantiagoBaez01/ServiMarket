package com.grupob.ServiMarket.service;

import com.grupob.ServiMarket.entity.Publication;
import com.grupob.ServiMarket.entity.Score;
import com.grupob.ServiMarket.entity.Solicitud;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.enums.EstadoServicio;
import com.grupob.ServiMarket.enums.Role;
import com.grupob.ServiMarket.repository.PublicationRepository;
import com.grupob.ServiMarket.repository.SolicitudRepository;
import com.grupob.ServiMarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository pRepository;

    //------------------------CREATE--------------------------
    @Transactional
    public void createSol (Solicitud solicitud, Long userId, Long pId)
    {
        Publication publication =pRepository.findById(pId).get();
        Solicitud sol = solicitud;

        UserEntity user = new UserEntity();
        Optional <UserEntity> uanswer = userRepository.findById(userId);


        if (uanswer.isPresent() ){
            user = uanswer.get();
            sol.setProvider(publication.getProvider());
            sol.setPublication(publication);
            sol.setUserClient(user);
            sol.setEstado(EstadoServicio.PENDIENTE);
            sol.setCompleto(false);
            sol.setDardebaja(true);

            solicitudRepository.save(sol);
        }

    }

    //------------------------READ--------------------------

    public List<Solicitud> list(){
        List<Solicitud> solicitud = new ArrayList<>();
        solicitud = solicitudRepository.findAll();
        return solicitud;
    }



    //---------------------GET PUBLICATION BY ID-----------------
    public Solicitud getSolicitudById(Long id){
        return solicitudRepository.findById(id).orElse(null);
    }
    //------------------------UPDATE--------------------------
    public Solicitud updateSolicitud (Solicitud solicitud){
        Solicitud updateSolicitud = solicitudRepository.findById(solicitud.getId()).orElse(null);
        if(updateSolicitud !=null){
            updateSolicitud.setComentario(solicitud.getComentario());
            updateSolicitud.setUserClient(solicitud.getUserClient());

            solicitudRepository.save(updateSolicitud);
            return updateSolicitud;
        }
        return null;
    }

    //------------------------DELETE--------------------------

    @Transactional
    public void delete (Long id){
        Optional<Solicitud> response = solicitudRepository.findById(id);
        if(response!= null){
            Solicitud solicitudToDelete = response.get();
            solicitudRepository.delete(solicitudToDelete);
        }
    }
    //-----------------------CHANGE STATUS-----------------------
    @javax.transaction.Transactional
    public void changeStatus(Long id) {
        Optional<Solicitud> answer = solicitudRepository.findById(id);
        if (answer.isPresent()) {
            Solicitud sol = answer.get();

            if (sol.getEstado().equals(EstadoServicio.PENDIENTE)) {
                sol.setEstado(EstadoServicio.PRESUPUESTADO);
            } else if (sol.getEstado().equals(EstadoServicio.PRESUPUESTADO)) {
                sol.setEstado(EstadoServicio.ACEPTADO);
            } else if (sol.getEstado().equals(EstadoServicio.ACEPTADO)) {
                sol.setEstado(EstadoServicio.CANCELADO);
            } else if (sol.getEstado().equals(EstadoServicio.CANCELADO)) {
                sol.setEstado(EstadoServicio.FINALIZADO);
            } else if (sol.getEstado().equals(EstadoServicio.FINALIZADO)) {
                sol.setEstado(EstadoServicio.PENDIENTE);
            }
        }
    }
    @Transactional
    public void changeCompleto(Long id) {
        Optional<Solicitud> answer = solicitudRepository.findById(id);
        if (answer.isPresent()) {
            Solicitud sol = answer.get();

            if (sol.isCompleto()) {
                sol.setCompleto(false);
            } else if (!sol.isCompleto()) {
                sol.setCompleto(true);
            }
        }
    }
    @Transactional
    public void solicitudAltaBaja(Long id) {
        Optional<Solicitud> answer = solicitudRepository.findById(id);
        if (answer.isPresent()) {
            Solicitud sol = answer.get();

            if (sol.isDardebaja()) {
                sol.setDardebaja(false);
            } else if (!sol.isDardebaja()) {
                sol.setDardebaja(true);
            }
        }
    }



}
