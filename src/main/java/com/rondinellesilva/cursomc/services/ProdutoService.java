package com.rondinellesilva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rondinellesilva.cursomc.domain.Categoria;
import com.rondinellesilva.cursomc.domain.Produto;
import com.rondinellesilva.cursomc.repositories.CategoriaRepository;
import com.rondinellesilva.cursomc.repositories.ProdutoRepository;
import com.rondinellesilva.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Long id) {
		Optional<Produto> categoria = repo.findById(id);
		
		if(!categoria.isPresent()) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Produto.class.getName());
		}
		return categoria.orElse(new Produto());
	}
	
	public Page<Produto> search(String nome, List<Long> ids, Integer page, Integer linesPage, String direction,String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}
