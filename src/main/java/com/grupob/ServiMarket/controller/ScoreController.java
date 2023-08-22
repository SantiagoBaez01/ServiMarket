package com.grupob.ServiMarket.controller;

import com.grupob.ServiMarket.entity.Score;
import com.grupob.ServiMarket.entity.Solicitud;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.exceptions.MyException;
import com.grupob.ServiMarket.service.ScoreService;
import com.grupob.ServiMarket.service.SolicitudService;
import com.grupob.ServiMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ScoreController {


    @Autowired
    private ScoreService scoreService;
    @Autowired
    private SolicitudService solService;
    @Autowired
    private UserService userService;

    //-------------------CREATE score---------------------------
    @GetMapping("/calification/{solid}/nueva")
    public String newCalifForm(@PathVariable("solid") Long solid, HttpSession session, ModelMap model) {
        UserEntity user = (UserEntity) session.getAttribute("usuariosession");
        Score score = new Score();
        model.put("solid", solid);
        model.put("score", score);
        model.put("user", user);
        return "calification-form";
    }


    @PostMapping("/calification/{solid}")
    public String newScore(@PathVariable("solid") Long solid, @ModelAttribute("score") Score score,
                           HttpSession session, ModelMap model) throws MyException {
        UserEntity user = (UserEntity) session.getAttribute("usuariosession");
        model.put("user", user);
        Long userId = user.getId();

        try {

            model.put("exito", "Calificación modificada correctamente");
            //int puntajeInEstrellas = score.getPuntaje();
            //int puntajeEnPuntos = puntajeInEstrellas * 25;
          //  score.setPuntaje(puntajeEnPuntos);

            scoreService.create(score, solid, userId);
            return "redirect:/solicitudbyUser";

        } catch (MyException ex) {

            model.put("error", ex.getMessage());

            return "calification-form";

        }
    }
        //---------------------------READ-----------------------LIST
        //GET MAPPING
        @GetMapping("/scorelist")  //vista para casa proveedor en su publicación
        public List<Score> calificationList () {


            return scoreService.list();
        }
        @GetMapping("/califications")
        public String listCalifications (ModelMap model){
            List<Score> score = scoreService.list();
            model.put("score", score);
            List<Solicitud> solicitud = solService.list();
            model.addAttribute("solicitud", solicitud);
            return "califications-list-provider.html";
        }

        //-------read calification by solicitud-----
        @GetMapping("/calificationsbySol")
        public String calificationsbySol (ModelMap model){
            List<Score> score = scoreService.list();
            model.put("score", score);
            return "calificationsbySol.html";
        }


        //--------------------------UPDATE-----------------
        @GetMapping("/editScore/{sId}")
        public String editScore (@PathVariable("sId") Long sId, ModelMap model){

            model.put("score", scoreService.getScoreById(sId));

            return "calification-form-edit.html";
        }

        @PostMapping("/editScore/{sId}")
        public String editScore (@PathVariable("sId") Long sId,
                @RequestParam("comentario") String comentario,@RequestParam("puntaje") int puntaje, ModelMap model) throws
        MyException {

            try {

                model.put("exito", "Calificación modificada correctamente");
                scoreService.editCalification(comentario, puntaje, sId);
                return "redirect:/solicitudbyUser"; //string para ser visualizada en postman
            } catch (MyException ex) {

                model.put("error", ex.getMessage());

                return "redirect:/editScore/"+ sId;
            }
        }

        //-------------------DELETE------------------------
        @GetMapping("/score/{sId}/delete")
        public String deleteScore (@PathVariable("sId") Long sId){
            scoreService.delete(sId);
            return "Deleted"; //string para ser visualizada en postman
        }


    }
