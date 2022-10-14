package me.dio.sacola.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resource.dto.ItemDTO;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SacolaServiceImpl implements SacolaService {

    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    @Override
    public Item incluirItemNaSacola(ItemDTO itemDto) {
        Sacola sacola = verSacola(itemDto.getSacolaID());

        if (sacola.isFechada()){
        throw new RuntimeException(("Esta sacola está fechada!"));
        }
        Item itemParaSerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                        ))
                .build();

        List<Item> itensDaSacola = sacola.getItens();

        if(itensDaSacola.isEmpty()){
            itensDaSacola.add(itemParaSerInserido);
        }
        else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteParaAdicionar = itemParaSerInserido.getProduto().getRestaurante();
            if(restauranteAtual.equals(restauranteParaAdicionar)) {
                itensDaSacola.add(itemParaSerInserido);
            }
            else{
                throw new RuntimeException("Não é possivel adicionar produtos de restaurantes diferentes , feche a sacola ou esvazie!");
                }


        }

        return null;
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int formaPagamento) {

        Sacola sacola = verSacola(id);
        if(sacola.getItens().isEmpty()){
            throw new RuntimeException("Inclua itens na sacola!");
        }

//        if(formaPagamento ==0){
//            sacola.setFormaPagamento(FormaPagamento.DINHEIRO);
//        }
//        else{
//            sacola.setFormaPagamento(FormaPagamento.MAQUINETA);
//        }
//Condição que é resumida no if ternário abaixo

        FormaPagamento formaPagamentoMetodo =
                formaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;

        sacola.setFormaPagamento(formaPagamentoMetodo);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
}
