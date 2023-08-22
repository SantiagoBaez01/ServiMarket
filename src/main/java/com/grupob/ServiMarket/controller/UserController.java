package com.grupob.ServiMarket.controller;
import com.grupob.ServiMarket.entity.Image;
import com.grupob.ServiMarket.entity.Score;
import com.grupob.ServiMarket.entity.Solicitud;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.exceptions.MyException;
import com.grupob.ServiMarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private PublicationService pService;

    @Autowired
    private SolicitudService solService;



    //---------------------------READ-----------------------LIST
    @GetMapping("/listaUsuarios")
    public String listar(ModelMap modelo){
        List<UserEntity> user = userService.list();
        modelo.addAttribute("user", user);
return "user_list.html";
    }
    //GET MAPPING
    @GetMapping("/userslist")
    public List<UserEntity> listuser(){
        return userService.list();
    }

    //--------------------------READ USER DETAIL-----------------
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PROVIDER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String profile(ModelMap modelo, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("usuariosession");
        modelo.put("user", user);

        Double promedioPuntaje= scoreService.calcularPromedioPuntaje(user);
        modelo.put("promedioPuntaje",promedioPuntaje);
        List<Solicitud> solicitud = solService.list();
        modelo.addAttribute("solicitud", solicitud);
        List<Score> score = scoreService.list();
        modelo.addAttribute("score", score);
        return "perfil_user.html";
    }


    //--------------------------UPDATE-----------------
    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PROVIDER', 'ROLE_ADMIN')")
    @GetMapping("/edituser/{userId}")
    public String editUser(@PathVariable("userId") long userId, ModelMap model, HttpSession session){

       UserEntity user = (UserEntity) session.getAttribute("usuariosession");

        model.addAttribute("user", user);
        return"perfil_edit.html";
    }

    //@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PROVIDER', 'ROLE_ADMIN')")

    @PostMapping("/edituser/{userId}")
    public String editUser(@PathVariable("userId") Long userId,@RequestParam String name, @RequestParam String lastName,
                           @RequestParam String address,@RequestParam String contact, ModelMap model,
                           MultipartFile archivo, HttpSession session) throws Exception {

        UserEntity updatedUser= userService.updateUser(userId,name,lastName,contact,address,archivo);
        session.setAttribute("usuariosession", updatedUser);
        return "redirect:/user/perfil";

    }






    //-------------------DELETE------------------------ va para ADMIN
   /* @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.delete(id);
        return "redirect:/user/listaUsuarios";
    }*/


    //-------------------SEARCH QUERY------------------------
    @GetMapping("/userslist/search")
    public String searchUsername(@RequestParam(value="query") String query, Model model){
        List<UserEntity> users = userService.searchUsers(query);
        model.addAttribute("users", users);
        return "userslist" ;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/solicitud")
    public String solicitud(){
        return "solicitudUser";
    }
}