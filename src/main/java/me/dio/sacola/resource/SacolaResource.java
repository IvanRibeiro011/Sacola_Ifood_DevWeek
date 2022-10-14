package me.dio.sacola.resource;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.resource.dto.ItemDTO;
import me.dio.sacola.service.SacolaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ifoodDevWeek/sacolas")
@RequiredArgsConstructor
public class SacolaResource {

    private final SacolaService sacolaService;

    @PostMapping
    public ItemDTO incluirItemNaSacola(@RequestBody ItemDTO itemDTO){
        return sacolaService.incluirItemNaSacola(itemDTO);
    }

    @GetMapping("/{id}")
    public Sacola verSacola(@PathVariable("id") Long id){
        return sacolaService.verSacola(id);
    }

    @PatchMapping("/fecharSacola/{idSacola}")
    public Sacola fecharSacola(@PathVariable("{idSacola}") Long idSacola ,@RequestParam("formaPagamento") int formaPagamento){
    return sacolaService.fecharSacola(idSacola, formaPagamento);
    }
}
