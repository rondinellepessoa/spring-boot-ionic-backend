package com.rondinellesilva.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rondinellesilva.cursomc.domain.Cliente;
import com.rondinellesilva.cursomc.repositories.ClienteRepository;
import com.rondinellesilva.cursomc.services.exceptions.ObjectNotFoundException;


@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Long id) {
		Optional<Cliente> cliente = repo.findById(id);
		
		if(!cliente.isPresent()) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Cliente.class.getName());
		}
		return cliente.orElse(null);
	}
}
