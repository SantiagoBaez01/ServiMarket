package com.grupob.ServiMarket.service;

import com.grupob.ServiMarket.entity.Image;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.exceptions.MyException;
import com.grupob.ServiMarket.repository.ImageRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public Image guardar(MultipartFile archivo) throws MyException, FileUploadException {
        if (archivo != null) {
            try {

                Image image = new Image();

                image.setMime(archivo.getContentType());

                image.setNombre(archivo.getName());

                image.setContenido(archivo.getBytes());

                return imageRepository.save(image);
            } catch (MaxUploadSizeExceededException e) {
                throw new FileUploadException("El tamaño del archivo excede el límite permitido.", e);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    public Image actualizar(MultipartFile archivo, Long idImage) throws Exception{
        if (archivo != null) {
            try {

                Image image = new Image();

                if (idImage != null) {
                    Optional<Image> respuesta = imageRepository.findById(idImage);

                    if (respuesta.isPresent()) {
                        image = respuesta.get();
                    }
                }

                image.setMime(archivo.getContentType());

                image.setNombre(archivo.getName());

                image.setContenido(archivo.getBytes());

                return imageRepository.save(image);
            } catch (MaxUploadSizeExceededException e) {
                throw new FileUploadException("El tamaño del archivo excede el límite permitido.", e);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;

    }

    @Transactional(readOnly = true)
    public List<Image> listarTodos() {
        return imageRepository.findAll();
    }

    public Image getImageById(Long id) {

        return imageRepository.findById(id).orElse(null);
    }


    public Image getDefaultImage() {
        try {
            ClassPathResource defaultImageResource = new ClassPathResource("static/img/avatar.png");
            byte[] imageBytes = Files.readAllBytes(defaultImageResource.getFile().toPath());

            Image defaultImage = new Image();
            defaultImage.setMime("image/png");
            defaultImage.setNombre("avatar.png");
            defaultImage.setContenido(imageBytes);

            return imageRepository.save(defaultImage);
        } catch (IOException e) {

            System.err.println("Error al cargar la imagen por defecto: " + e.getMessage());
            return null;
        }
    }
}
