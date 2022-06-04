package me.dio.rest.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import me.dio.rest.entity.Cerveja;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import me.dio.rest.repository.CervejaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/v1/cervejas")
public class CervejaResource {

    private final CervejaRepository repo;

    @Autowired
    public CervejaResource(CervejaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Cerveja> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cerveja> obterCerveja(@PathVariable Long id) {
        Optional<Cerveja> cerveja = repo.findById(id);

        if (cerveja.isPresent()) {
            return ResponseEntity.ok(cerveja.get());
        }

        return ResponseEntity.notFound().build();
    }

//    @GetMapping("/{nome}") //nome?nome=nike
//    public ResponseEntity<Cerveja> encontrarPorNome(@RequestParam(value="nome") String nome) {
//        Optional<Cerveja> cerveja = repo.findByNome(nome);
//
//        if (cerveja.isPresent()) {
//            return ResponseEntity.ok(cerveja.get());
//        }
//
//        return ResponseEntity.notFound().build();
//
//    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cerveja adicionar(@Valid @RequestBody Cerveja cerveja) {
        return repo.save(cerveja);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cerveja> atualizar(@PathVariable Long id, @Valid @RequestBody Cerveja cerveja) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Cerveja save = repo.save(cerveja);
        return ResponseEntity.ok(cerveja);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/incrementa")
    public ResponseEntity<Cerveja> incremetar(@PathVariable Long id, @RequestBody @Max(500) Integer quantidade) {
        Optional<Cerveja> cerveja = repo.findById(id);

        if (!cerveja.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Cerveja existe = cerveja.get();
        int qtdIncrementada = existe.getQuantidade() + quantidade;
        if (qtdIncrementada <= existe.getMaximo()) { //diferente do @max pois pode ser excedido
            existe.setQuantidade(qtdIncrementada);
            Cerveja c = repo.save(existe);
            return ResponseEntity.ok(c);
        }
        return ResponseEntity.badRequest().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
