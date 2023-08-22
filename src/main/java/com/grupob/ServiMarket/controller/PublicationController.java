package com.grupob.ServiMarket.controller;
import com.grupob.ServiMarket.entity.*;
import com.grupob.ServiMarket.enums.Rubro;
import com.grupob.ServiMarket.exceptions.MyException;
import com.grupob.ServiMarket.service.PublicationService;
import com.grupob.ServiMarket.service.ScoreService;
import com.grupob.ServiMarket.service.SolicitudService;
import com.grupob.ServiMarket.service.UserService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class PublicationController  {

    @Autowired
    private PublicationService pService;
    @Autowired
    private UserService userService;
    @Autowired
    private SolicitudService solService;
    @Autowired
    private ScoreService scoreService;


    //-------------------CREATE PUBLICATION---------------------------

    @PreAuthorize("hasAnyRole('ROLE_PROVIDER')")
    @GetMapping("/publicacion")
    public String newpublication(ModelMap model, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("usersesssion");
        Publication publication = new Publication();

        model.put("publicacion", publication);
        model.put("user", user);

        return "Formulario_Servicios.html";
    }
    @PostMapping("/newPublication")
    public String savePublication(@ModelAttribute("publicacion") Publication publication,
                                  HttpSession session, ModelMap modelMap,
                                  BindingResult result,
                                  List<MultipartFile> archivos) throws MyException, FileUploadException {

        UserEntity user = (UserEntity) session.getAttribute("usuariosession");
        modelMap.put("user", user);
        Long userId = user.getId();


        if (result.hasErrors()) {
            modelMap.put("publicacion", publication);
            modelMap.put("user", user);

            return "Formulario_Servicios.html";
        }
        publication.setDescription2(publication.getDescription2().replace("\n", "<br>"));
        pService.create(publication, userId, archivos);
        return "redirect:/publist";

    }

    //---------------------------READ-----------------------LIST
    //GET MAPPING

    @GetMapping("/publist")
    public String publicationsList(Model model, Authentication authentication){

        List<Publication> publication = pService.list();
        model.addAttribute("publication",publication);


        return "inicio.html";
    }
    @GetMapping("/publistAdminView")
    public String publicationsListAdmin(Model model, Authentication authentication){

        List<Publication> publication = pService.list();
        model.addAttribute("publication",publication);


        return "pubListAdminView.html";
    }

    @GetMapping("/publicationsbyUser")
    public String listPublic (ModelMap model, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("usuariosession");
        model.put("user", user);
        Long userId = user.getId();
        List<Publication> publication = pService.list();
        model.addAttribute("publication", publication);
        List<Solicitud> solicitud = solService.list();
        model.addAttribute("solicitud", solicitud);
        List<Score> score = scoreService.list();
        model.addAttribute("score", score);
        return "public-list-provider";
    }

    //----------------------READ-----------------------DETAIL
    @GetMapping("/publication/{pubId}")
    public String newsDetail(@PathVariable("pubId") Long pubId, Model model, ModelMap modelo){
        Publication pub = pService.getPublicationById(pubId);
        UserEntity user = pub.getProvider();
        model.addAttribute("pub", pub);
        model.addAttribute("user", user);
        List<Score> score = scoreService.list();
        model.addAttribute("score", score);
        Double promedioPuntaje= scoreService.calcularPromedioPuntaje(user);
        modelo.put("promedioPuntaje",promedioPuntaje);
        //List<Object[]> scores = scoreService.getProviderIdAndScoreByPublication(pub);
        //modelo.addAttribute("scores", scores);
        return "Vista_formulario_Servicios2.html";
    }


    //--------------------------UPDATE-----------------
    @GetMapping("/editPublication/{pId}")
    public String editPub(@PathVariable("pId") Long id, ModelMap model){

        model.put("publication", pService.getPublicationById(id));

        return"Formulario_Servicios_Edit.html";
    }

    @PostMapping("/editPublication/{pId}")
    public String editPub(@PathVariable("pId") Long pId, @RequestParam List<MultipartFile> archivos,
                          @RequestParam String title, @RequestParam String description,
                          @RequestParam String description2, @RequestParam Rubro rubro
                           ) throws Exception {
        pService.updatePublication(archivos,title,description,description2, pId, rubro);
        return "redirect:/";


    }

    //-------------------DELETE------------------------
    @GetMapping("/publicationdel/{pubId}")
    public String deletePub(@PathVariable("pubId") Long pubId){
        pService.delete(pubId);
        return "redirect:/publicationsbyUser";
    }


    //-------------------SEARCH QUERY------------------------

    @GetMapping("/publist/rubro")
    public String barraBusqueda(@RequestParam (value = "rubro") Rubro rubro, Model model){
            List<Publication> publication = pService.findByRubro(rubro);
            if (publication.isEmpty()) {
                model.addAttribute("error", "No se encontraron Publicaciones en ese Rubro");
            } else {
                model.addAttribute("publication",publication);
            }

            return "inicio.html";


    }
    @GetMapping("/publist/search")
    public String publicationsSearch(@RequestParam(value="query") String query,Model model){

        Set<Publication> uniquePublications = new HashSet<>();


            List<Publication> publications = pService.searchPublication(query);
            if (publications != null) {
                uniquePublications.addAll(publications);
            }

            List<Publication> content1 = pService.searchContentPublication(query);
            if (content1 != null) {
                uniquePublications.addAll(content1);
            }

            List<Publication> content2 = pService.searchContentPublication2(query);
            if (content2 != null) {
                uniquePublications.addAll(content2);
            }
        if (uniquePublications.isEmpty()) {
            model.addAttribute("error", "No se encontraron resultados para la búsqueda.");
        } else {
            model.addAttribute("publication", new ArrayList<>(uniquePublications));
        }


        return "inicio.html";

    }

}
