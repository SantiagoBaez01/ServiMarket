package com.grupob.ServiMarket.controller;

import com.grupob.ServiMarket.entity.UserEntity;

import com.grupob.ServiMarket.enums.Role;
import com.grupob.ServiMarket.exceptions.MyException;
import com.grupob.ServiMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Controller
@RequestMapping("/")
public class indexController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index ( ){
        return "redirect:/publist";
    }


    @GetMapping("/registrarse")
    public String registrarse(ModelMap model){
        UserEntity us = new UserEntity();
        model.put("userEntity",us);
        return "registro.html";
    }
    @PostMapping("/newuser")

    public String saveUser(@Valid UserEntity userEntity, String password, String password2, MultipartFile archivo , BindingResult result, ModelMap model ) throws Exception {
        try {
            userService.create(userEntity, password,password2, archivo);


            model.addAttribute("exito", "Usuario registrado correctamente!");

            return "login.html";
        } catch (MyException ex) {

            model.put("error", ex.getMessage());

            return "registro.html";
        }
    }

    @GetMapping("/ingresar")
    public String login(@RequestParam (required = false) String error, ModelMap modelo) {
        if (error != null){
            modelo.addAttribute("error","Usuario o contrasena invalidos. <br> Intenta nuevamente!");
        }

        return "login.html";
    }

        @GetMapping("/inicio")
        public String inicio(){
        return "inicio.html";
        }
        
        @GetMapping("/perfil")
        public String perfil(){
            return "perfil_user.html";
        }
        
        @GetMapping("/perfil/editarPerfil")
        public String editarPerfil(){
            return "perfil_edit.html";
        }

}
