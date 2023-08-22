package com.grupob.ServiMarket.controller;

import com.grupob.ServiMarket.entity.Publication;
import com.grupob.ServiMarket.entity.Score;
import com.grupob.ServiMarket.entity.Solicitud;
import com.grupob.ServiMarket.entity.UserEntity;
import com.grupob.ServiMarket.service.PublicationService;
import com.grupob.ServiMarket.service.ScoreService;
import com.grupob.ServiMarket.service.SolicitudService;
import com.grupob.ServiMarket.service.UserService;
import com.grupob.ServiMarket.util.reports.UserExporterExcel;
import com.grupob.ServiMarket.util.reports.UsersExporterPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminController {

private UserService userService;
private PublicationService pService;
private SolicitudService solService;
private ScoreService scoreService;

    @Autowired
    public AdminController(UserService userService, PublicationService pService,SolicitudService solService, ScoreService scoreService) {
        this.userService = userService;
        this.pService=pService;
        this.solService=solService;
        this.scoreService=scoreService;
    }

    @GetMapping("/dashboard")
    public String panelAdmin(){

        return "dashboard";
    }
    //    U - - S - - E - - R - - S
//----------LIST USERS----------------
    @GetMapping("/users")
    public String listUser (ModelMap model){
        List<UserEntity> user = userService.list();
        model.addAttribute("user", user);
        List<Solicitud> solicitud = solService.list();
        model.addAttribute("solicitud", solicitud);
        List<Score> score = scoreService.list();
        model.addAttribute("score", score);
        return "listUsers";
    }

    //----------CHANGE ROLE USER----------------
    @GetMapping("/editRole/{id}")
    public String changeRol(@PathVariable Long id){
        userService.changeRole(id);
        return "redirect:/admin/users";
    }
    @PostMapping("/editRole/{id}")
    public String changeRole(@PathVariable Long id){
        userService.changeRole(id);
        return "redirect:/admin/users";
    }
    //----------DELETE USERS----------------
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model){
        try{
            userService.delete(id);
            return "redirect:/admin/users";
        } catch (Exception ex){
            model.addAttribute("error", "No se puede eliminar el Usuario porque se encuentra vinculado a una publicación o comentario");
            return "error-deleteUser";
    }

    }
    //----------CHANGE STATUS USERS----------------
    @GetMapping("/editUserStatus/{id}")
    public String changeUserStat(@PathVariable Long id){
        userService.changeUserStatus(id);
        return "redirect:/admin/users";
    }
    //----------CHANGE STATUS USERS----------------
    @PostMapping("/editUserStatus/{id}")
    public String changeUserStatus(@PathVariable Long id){
        userService.changeUserStatus(id);
        return "redirect:/admin/users";
    }
//   P - - U - - B - - L - - I - - C - - A - - T - - I - - O - - N
    //----------LIST PUBLICATIONS----------------
    @GetMapping("/publications")
    public String listPublic (ModelMap model){
        List<Publication> publication = pService.list();
        model.addAttribute("publication", publication);
        List<Solicitud> solicitud = solService.list();
        model.addAttribute("solicitud", solicitud);
        List<Score> score = scoreService.list();
        model.addAttribute("score", score);
        return "public-list";
    }
    //----------CHANGE PUBLICATION STATUS----------------
    @GetMapping("/publicated/{id}")
    public String changeStatuspub(@PathVariable Long id){
       pService.changePublicated(id);
        return "redirect:/admin/publications";
    }

    @PostMapping("/publicated/{id}")
    public String changeStatuspubl(@PathVariable Long id){
        pService.changePublicated(id);
        return "redirect:/admin/publications";
    }
    //----------DELETE PUBLICATION----------------
    @GetMapping("/deletePub/{id}")
    public String deletePublication(@PathVariable("id") Long id, Model model){
        try{
            pService.delete(id);
            return "redirect:/admin/publications";
        }catch (Exception ex){
            model.addAttribute("error", "No se puede eliminar la Publicacion porque se encuentra vinculada a una solicitud o comentario");
            return "error-deletePublication";
        }
    }

    //   S- - O - - L - - I - - C - - I - - T - - U - - D
    //----------LIST SOLICITUDES----------------
    @GetMapping("/solicitudes")
    public String listsolicitudes (ModelMap model){
        List<Solicitud> solicitud = solService.list();
        model.addAttribute("solicitud", solicitud);
        List<Score> score = scoreService.list();
        model.addAttribute("score", score);
        return "solicitudes-list";
    }
    //----------CHANGE STATUS SOLICITUD----------------
    @GetMapping("/editStatus/{id}")
    public String changeStat(@PathVariable Long id){
        solService.changeStatus(id);
        return "redirect:/admin/solicitudes";
    }

    @PostMapping("/editStatus/{id}")
    public String changeStatus(@PathVariable Long id){
        solService.changeStatus(id);
        return "redirect:/admin/solicitudes";
    }
    //----------ALTA /BAJA  SOLICITUD----------------
    @GetMapping("/solicitudAltaBaja/{id}")
    public String solicitudAltaOBaja(@PathVariable Long id){
        solService.solicitudAltaBaja(id);
        return "redirect:/admin/solicitudes";
    }

    @PostMapping("/solicitudAltaBaja/{id}")
    public String solicitudAltaBaja(@PathVariable Long id){
        solService.solicitudAltaBaja(id);
        return "redirect:/admin/solicitudes";
    }
    //----------DELETE SOLICITUD----------------
    @GetMapping("/deleteSol/{solid}")
    public String deleteSolicitud(@PathVariable("solid") Long id, Model model){
        try{
            solService.delete(id);
            return "redirect:/admin/solicitudes";
        } catch (Exception ex){
            model.addAttribute("error", "No se puede eliminar la Solicitud porque se encuentra vinculada a un comentario");
            return "error-deleteSol";
        }

    }
    //----------CHANGE COMPLETO SOLICITUD----------------
    @GetMapping("/editCompleto/{id}")
    public String changeCompleto(@PathVariable Long id){
        solService.changeCompleto(id);
        return "redirect:/admin/solicitudes";
    }

    //----------LIST CALIFICATIONS----------------
    @GetMapping("/califications")
    public String listCalifications(ModelMap model){
        List<Score> score = scoreService.list();
        model.addAttribute("score", score);
        List<Solicitud> solicitud = solService.list();
        model.addAttribute("solicitud", solicitud);
        return "califications-list";
    }
    //----------DELETE CALIFICATIONS----------------
    @GetMapping("/deleteScore/{scid}")
    public String deleteScore(@PathVariable("scid") Long id){
        scoreService.delete(id);
        return "redirect:/admin/califications";
    }
    // -----------CENSURE------------------
    @GetMapping("/censureComment/{scid}")
    public String censure(@PathVariable Long scid){
        scoreService.censure(scid);
        return "redirect:/admin/califications";
    }

    @PostMapping("/censureComment/{scid}")
    public String censured(@PathVariable Long scid){
        scoreService.censure(scid);
        return "redirect:/admin/califications";
    }
    @GetMapping("/exportarPDF")
    public void exportarListadoDeUsuariosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Usuarios_" + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<UserEntity> users = userService.list();

        UsersExporterPDF exporter = new UsersExporterPDF(users);
        exporter.exportar(response);
    }
    @GetMapping("/exportarExcel")
    public void exportarListadoDeUsuariosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Usuarios_" + fechaActual + ".xlsx";

        response.setHeader(cabecera, valor);

        List<UserEntity> users = userService.list();

        UserExporterExcel exporter = new UserExporterExcel(users);
        exporter.exportar(response);
    }
}
