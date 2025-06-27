package com.example.prova;

import com.example.prova.model.Categoria;
import com.example.prova.model.Produto;
import com.example.prova.repository.ProdutoRepository;
import com.example.prova.service.ProdutoService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    public void testListarProdutos() {
        Categoria cat1 = new Categoria(1L, "Eletrônicos", "Produtos eletrônicos variados");
        Categoria cat2 = new Categoria(2L, "Livros", "Livros de vários gêneros");

        Produto p1 = new Produto(1L, "Smartphone", 1500.0, "Celular top de linha", cat1);
        Produto p2 = new Produto(2L, "Livro Java", 80.0, "Livro para aprender Java", cat2);
        List<Produto> produtos = Arrays.asList(p1, p2);

        Mockito.when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> resultado = produtoService.listarProdutos();

        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Smartphone", resultado.get(0).getNome());
    }

    @Test
    public void testBuscarProdutoPorId_QuandoEncontrar() {
        Categoria cat = new Categoria(1L, "Eletrônicos", "Produtos eletrônicos variados");
        Produto produto = new Produto(1L, "Notebook", 3500.0, "Notebook gamer", cat);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoService.buscarProdutoPorId(1L);

        Assertions.assertTrue(resultado.isPresent());
        Assertions.assertEquals("Notebook", resultado.get().getNome());
    }

    @Test
    public void testBuscarProdutoPorId_QuandoNaoEncontrar() {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Produto> resultado = produtoService.buscarProdutoPorId(1L);

        Assertions.assertFalse(resultado.isPresent());
    }

    @Test
    public void testSalvarProduto() {
        Categoria cat = new Categoria(1L, "Eletrônicos", "Produtos eletrônicos variados");
        Produto produto = new Produto(0, "Tablet", 1200.0, "Tablet Android", cat);
        Produto produtoSalvo = new Produto(3L, "Tablet", 1200.0, "Tablet Android", cat);

        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produtoSalvo);

        Produto resultado = produtoService.salvarProduto(produto);

        Assertions.assertEquals(3L, resultado.getId());
        Assertions.assertEquals("Tablet", resultado.getNome());
    }

    @Test
    public void testExcluirProduto() {
        Long idParaExcluir = 1L;

        produtoService.excluirProduto(idParaExcluir);

        Mockito.verify(produtoRepository, Mockito.times(1)).deleteById(idParaExcluir);
    }

    @Test 
    public void somar(){
        int resultado = 5 + 45;

        assertEquals(50, resultado);
    }
}
