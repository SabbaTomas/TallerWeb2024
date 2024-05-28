package com.tallerwebi.integracion;

import com.tallerwebi.controlador.ControladorReserva;
import com.tallerwebi.dominio.ServicioReserva;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Locker;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.presentacion.ReservaDatos;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ControladorReservaTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Mock
    private ServicioReserva servicioReservaMock;

    @InjectMocks
    private ControladorReserva controladorReserva;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controladorReserva).build();
    }

    @Test
    public void consultarUsuarioDeReservaTest() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setPassword("password");

        when(servicioReservaMock.consultarUsuarioDeReserva(anyString(), anyString())).thenReturn(usuario);

        MvcResult result = this.mockMvc.perform(get("/reservas/usuario")
                .param("email", "test@example.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent, containsString("test@example.com"));
    }
/*
    @Test
    public void consultarLockerDeReservaTest() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");

        when(servicioReservaMock.consultarUsuarioDeReserva(anyString(), anyString())).thenReturn(usuario);
        when(servicioReservaMock.consultarLockerDeReserva(any(Usuario.class), any(Locker.class))).thenReturn(usuario);

        MvcResult result = this.mockMvc.perform(get("/reservas/locker")
                .param("email", "test@example.com")
                .param("idLocker", "1"))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent, containsString("test@example.com"));
    }

    @Test
    public void registrarReservaTest() throws Exception {
        ReservaDatos reservaDatos = new ReservaDatos();
        reservaDatos.setEmail("test@example.com");
        reservaDatos.setPassword("password");
        reservaDatos.setIdLocker(1L);

        this.mockMvc.perform(post("/reservas/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"idLocker\":1}"))
                .andExpect(status().isOk())
                .andReturn();
    }

    
    @Test
    public void registrarReservaUsuarioExistenteTest() throws Exception {
        ReservaDatos reservaDatos = new ReservaDatos();
        reservaDatos.setEmail("test@example.com");
        reservaDatos.setPassword("password");
        reservaDatos.setIdLocker(1L);

        when(servicioReservaMock.registrarReserva(any(Usuario.class), any(Locker.class))).thenThrow(new UsuarioExistente());

        MvcResult result = this.mockMvc.perform(post("/reservas/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"idLocker\":1}"))
                .andExpect(status().isConflict())
                .andReturn();
        
        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent, containsString("Usuario ya existe"));
    }
    */
}
