package me.dio.rest.resource;

import java.util.Collections;
import java.util.Optional;
import me.dio.rest.entity.Cerveja;
import me.dio.rest.entity.TipoCerveja;
import me.dio.rest.repository.CervejaRepository;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AnyOf.anyOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class CervejaResourceTest {

    @MockBean
    private CervejaRepository cervejaRepo;

    @Autowired
    CervejaResource cervejaResource;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostToCervejasAndValidCerveja_thenCorrectResponse() throws Exception {
        // given
        String cerveja = "{\"nome\": \"Brahma\", \"marca\" : \"Ambev\", \"maximo\": \"50\", \"quantidade\": \"10\", \"tipo\" : \"LAGER\"}";
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cervejas")
                .content(cerveja)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void whenPostToCervejasAndInvalidCerveja_thenBadResponse() throws Exception {
        // given
        String cerveja = "{\"nome\": \"\", \"marca\" : \"Ambev\", \"maximo\": \"\", \"tipo\" : \"LAGER\"}";
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cervejas")
                .content(cerveja)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", anyOf(equalTo("tamanho deve ser entre 1 e 200"), equalTo("size must be between 1 and 200"))));
    }

    @Test
    public void whenGetToCervejasAndValidId_thenCorrectResponse() throws Exception {
        // given
        Cerveja c = new Cerveja(1L, "Brahma", "Ambev", 50, 10, TipoCerveja.LAGER);

        //when
        Mockito.when(cervejaRepo.findById(1L)).thenReturn(Optional.of(c));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cervejas/{id}", c.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", is(c.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.marca", is(c.getMarca())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipo", is(c.getTipo().toString())));
    }

    @Test
    public void whenGetToCervejasAndInvalidId_thenNotFoundResponse() throws Exception {
        // given
        Cerveja c = new Cerveja(1L, "Brahma", "Ambev", 50, 10, TipoCerveja.LAGER);

        //when
        Mockito.when(cervejaRepo.findById(3L)).thenReturn(Optional.of(c));
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cervejas/{id}", c.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenGetToCervejas_thenCorrectResponse() throws Exception {
        // given
        Cerveja c = new Cerveja(1L, "Brahma", "Ambev", 50, 10, TipoCerveja.LAGER);

        //when
        Mockito.when(cervejaRepo.findAll()).thenReturn(Collections.singletonList(c));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cervejas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenPatchToIncrementaAndValidQuantidade_thenCorrectResponse() throws Exception {
        // given
        Integer payload = 10;        
        Cerveja c = new Cerveja(1L, "Brahma", "Ambev", 50, 10, TipoCerveja.LAGER);

        //when
        Mockito.when(cervejaRepo.findById(1L)).thenReturn(Optional.of(c));


        c.setQuantidade(c.getQuantidade() + payload);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cervejas/{id}/incrementa", c.getId())
                .content(payload.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void whenPatchToIncrementaAndInvalidQuantidade_thenBadResponse() throws Exception {
        // given
        Integer payload = 510;        
        Cerveja c = new Cerveja(1L, "Brahma", "Ambev", 50, 10, TipoCerveja.LAGER);

        //when
        Mockito.when(cervejaRepo.findById(1L)).thenReturn(Optional.of(c));


        c.setQuantidade(c.getQuantidade() + payload);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/cervejas/{id}/incrementa", c.getId())
                .content(payload.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
