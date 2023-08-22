package com.grupob.ServiMarket.service;

import com.grupob.ServiMarket.entity.Image;
import com.grupob.ServiMarket.entity.UserEntity;

import com.grupob.ServiMarket.exceptions.MyException;

import com.grupob.ServiMarket.enums.Role;

import com.grupob.ServiMarket.repository.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;
    @Transactional
    public void create(UserEntity us, String password,String password2, MultipartFile archivo) throws MyException, FileUploadException {
        validate(us,password,password2,archivo);
        Image image;
        if (archivo != null && !archivo.isEmpty()) {
            image = imageService.guardar(archivo);
        } else {
            image = imageService.getDefaultImage();
        }
        us.setImage(image);
        us.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(us);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserEntity> list() {
        List<UserEntity> user = new ArrayList<>();
        user = userRepository.findAll();
        return user;
    }

   /* @Transactional
    public void update(UserEntity user, String password, MultipartFile archivo, Long id) throws Exception {
        UserEntity User
        Optional<UserEntity> response = userRepository.findById(id);
        if (response.isPresent()) {

            user.setPassword(new BCryptPasswordEncoder().encode(password));

            if (user.getImage() != null) {
                idImage = user.getImage().getId();
            }
            Image image = imageService.actualizar(archivo,idImage);

            user.setImage(image);

            userRepository.save(user);
        }
    }*/


    //---------------------GET USER BY ID-----------------
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    //---------------------UPDATE USER-----------------

    public UserEntity updateUser(Long id, String name, String lastName, String contact, String address, MultipartFile archivo) throws Exception {
        UserEntity user = new UserEntity();
        Optional<UserEntity> answer = userRepository.findById(id);
        if (answer.isPresent()) {
            user = answer.get();
            user.setName(name);
            user.setLastName(lastName);
            user.setContact(contact);
            user.setAddress(address);
            user.setStatus(true);

            Long idImage = null;

            if (user.getImage() != null) {
                idImage = user.getImage().getId();
            }
            Image image;
            if (archivo != null && !archivo.isEmpty()) {
                image = imageService.actualizar(archivo,idImage);
            } else {
                image = imageService.getDefaultImage();
            }


            user.setImage(image);

            userRepository.save(user);
        }
        return user;
    }

    //---------------------SEARCH USER NAME-----------------
    public List<UserEntity> searchUsers(String query) {
        List<UserEntity> users = userRepository.searchUsername(query);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity us = userRepository.findByEmail(email);

        if(us!=null){

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+us.getRole().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", us);

            return new User(us.getEmail(), us.getPassword(), permisos);
        }else{
            return null;
        }
    }
    private void validate(UserEntity us, String password,String password2, MultipartFile archivo) throws MyException {

        // Verificar que el atributo "name" no sea nulo o vacío
        if (us.getName() == null || us.getName().isEmpty()) {
            throw new MyException("El campo 'NOMBRE' no puede quedar vacío");
        }

        // El atributo "lastName" no se valida ya que no tiene restricciones
        // Verificar que el atributo "contact" no sea nulo o vacío
        if (us.getContact() == null || us.getContact().isEmpty()) {
            throw new MyException("El campo 'CONTACTO' no puede quedar vacío");
        }

        // Verificar que el atributo "address" no sea nulo o vacío
        if (us.getAddress() == null || us.getAddress().isEmpty()) {
            throw new MyException("El campo 'DIRECCION' no puede quedar vacío");
        }

        // Verificar que el atributo "email" no sea nulo o vacío y sea un correo electrónico válido
        if (us.getEmail() == null || us.getEmail().isEmpty()) {
            throw new MyException("El campo 'CORREO ELECTRÓNICO' no puede ser nulo, vacío o inválido");
        }

        // Verificar que el atributo "password" no sea nulo o vacío
        if (password == null || password.isEmpty()) {
            throw new MyException("El campo 'CONTRASEÑA' no puede quedar vacío");
        }
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas ingresadas deben ser iguales");
        }
        if (password.length() <= 8) {
            throw new MyException("La contraseña debe tener mínimo 8 caracteres");
        }

        // Verificar que el correo electrónico no esté registrado previamente
        if (userRepository.existsByEmail(us.getEmail())) {
            throw new MyException("El correo electrónico ya está registrado");
        }
    }
    //----------------CHANGE ROLE----------------------

@Transactional
    public void changeRole(Long id) {
        Optional<UserEntity> answer = userRepository.findById(id);
        if (answer.isPresent()) {
            UserEntity user = answer.get();

            if (user.getRole().equals(Role.USER)) {
                user.setRole(Role.ADMIN);
            } else if (user.getRole().equals(Role.ADMIN)) {
                user.setRole(Role.PROVIDER);
            } else if (user.getRole().equals(Role.PROVIDER)) {
                user.setRole(Role.USER);
            }

        }
    }
    @Transactional
    public void changeUserStatus(Long id) {
        Optional<UserEntity> answer = userRepository.findById(id);
        if (answer.isPresent()) {
            UserEntity user = answer.get();

            if (user.isStatus()) {
                user.setStatus(false);
            } else if (!user.isStatus()) {
                user.setStatus(true);
            }
        }
    }
}
