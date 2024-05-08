package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.DatosLocker;
import com.tallerwebi.dominio.locker.ServicioLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class ControladorLocker {

    private final ServicioLocker servicioLocker;

    public ControladorLocker(ServicioLocker servicioLocker) {
        this.servicioLocker = servicioLocker;
    }

    @RequestMapping(path = "/crear-locker", method = RequestMethod.GET)
    public ModelAndView crearLocker(@RequestParam TipoLocker tipoLocker) {
        servicioLocker.crearLocker(tipoLocker);
        return new ModelAndView("redirect:/mensaje-creacion-locker"); // Redirecciona a otra URL
    }


    @PostMapping("/actualizar-locker")
    public ModelAndView actualizarLocker(@RequestParam Long idLocker, @RequestParam TipoLocker tipoLocker) {
        servicioLocker.actualizarLocker(idLocker, tipoLocker);
        return new ModelAndView("redirect:/mensaje-actualizacion-locker");
    }

    @PostMapping("/eliminar-locker")
    public ModelAndView eliminarLocker(@RequestParam Long idLocker) {
        servicioLocker.eliminarLocker(idLocker);
        return new ModelAndView("redirect:/mensaje-eliminacion-locker");
    }


    @RequestMapping(path = "/lockers-por-tipo", method = RequestMethod.GET)
    public ModelAndView buscarLockersPorTipo(TipoLocker tipoLocker) {
        List<DatosLocker> lockers = servicioLocker.obtenerLockersPorTipo(tipoLocker);
        ModelAndView mav = new ModelAndView("lockers-por-tipo");
        mav.addObject("lockers", lockers);
        return mav;
    }
}
