package com.grupob.ServiMarket.controller;

import com.grupob.ServiMarket.entity.Image;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.service.ImageService;
import com.grupob.ServiMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImageController {

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario (@PathVariable Long id){
        UserEntity userEntity = userService.getUserById(id);

        byte[] image= userEntity.getImage().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG);



        return new ResponseEntity<>(image,headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        Image imagen = imageService.getImageById(id);

        if (imagen != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(imagen.getMime()));
            headers.setContentLength(imagen.getContenido().length);
            return new ResponseEntity<>(imagen.getContenido(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

