package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.locker.ServicioLocker;
import com.tallerwebi.dominio.locker.TipoLocker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.List;


@Controller
public class ControladorLocker {

    final ServicioLocker servicioLocker;

    public ControladorLocker(ServicioLocker servicioLocker) {
        this.servicioLocker = servicioLocker;
    }


    @Transactional
    @RequestMapping(path = "/crear-locker", method = RequestMethod.GET)
    public ModelAndView crearLocker(@RequestParam("tipoLocker") TipoLocker tipoLocker) {
        if (tipoLocker == null) {
            throw new IllegalArgumentException("El tipo de locker no puede ser nulo.");
        }
        List<Locker> lockers = servicioLocker.obtenerLockersPorTipo(tipoLocker);
        ModelAndView mav = new ModelAndView("crear-locker");
        mav.addObject("lockers", lockers);
        return mav;
    }


    @PostMapping("/actualizar-locker")
    public ModelAndView actualizarLocker(@RequestParam Long idLocker, @RequestParam TipoLocker tipoLocker) {
        servicioLocker.actualizarLocker(idLocker, tipoLocker);
        return new ModelAndView("redirect:/actualizar-locker");
    }

    @PostMapping("/eliminar-locker")
    public ModelAndView eliminarLocker(@RequestParam Long idLocker) {
        servicioLocker.eliminarLocker(idLocker);
        return new ModelAndView("redirect:/mensaje-eliminacion-locker");
    }

    @Transactional
    @RequestMapping(path = "/lockers-por-tipo", method = RequestMethod.GET)
    public ModelAndView buscarLockersPorTipo(TipoLocker tipoLocker) {
        List<Locker> lockers = servicioLocker.obtenerLockersPorTipo(tipoLocker);
        ModelAndView mav = new ModelAndView("lockers-por-tipo");
        mav.addObject("lockers", lockers);
        return mav;
    }

    @Transactional
    @GetMapping("/lockers")
    public ModelAndView mostrarLockers() {
        List<Locker> lockers = servicioLocker.obtenerLockersSeleccionados();
        return crearModelAndViewConCentro(lockers, "lockers");
    }

    @Transactional
    @GetMapping("/lockers/search")
    public ModelAndView buscarLockersPorCodigoPostal(@RequestParam("codigoPostal") String codigoPostal) {
        List<Locker> lockers = servicioLocker.obtenerLockersPorCodigoPostal(codigoPostal);
        ModelAndView mav = crearModelAndViewConCentro(lockers, "lockers");
        mav.addObject("codigoPostal", codigoPostal);
        return mav;
    }

    private ModelAndView crearModelAndViewConCentro(List<Locker> lockers, String vista) {
        ModelAndView mav = new ModelAndView(vista);
        mav.addObject("lockers", lockers);

        double[] center;
        int zoom;

        if (!lockers.isEmpty()) {
            double sumLatitud = 0;
            double sumLongitud = 0;
            for (Locker locker : lockers) {
                sumLatitud += locker.getLatitud();
                sumLongitud += locker.getLongitud();
            }
            center = new double[]{sumLatitud / lockers.size(), sumLongitud / lockers.size()};
            zoom = 14; // Un nivel de zoom más cercano
        } else {
            center = new double[]{-34.6821, -58.5638}; // Centro predeterminado si no hay lockers
            zoom = 12; // Nivel de zoom predeterminado
        }

        mav.addObject("center", center);
        mav.addObject("zoom", zoom);
        return mav;
    }


}
