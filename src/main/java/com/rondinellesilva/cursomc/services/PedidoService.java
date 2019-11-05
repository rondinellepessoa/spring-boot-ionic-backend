package com.rondinellesilva.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rondinellesilva.cursomc.domain.Pedido;
import com.rondinellesilva.cursomc.repositories.PedidoRepository;
import com.rondinellesilva.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	public Pedido find(Long id) {
		Optional<Pedido> categoria = repo.findById(id);
		
		if(!categoria.isPresent()) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Pedido.class.getName());
		}
		return categoria.orElse(new Pedido());
	}
}
