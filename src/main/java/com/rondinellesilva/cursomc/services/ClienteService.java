package com.rondinellesilva.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rondinellesilva.cursomc.domain.Cidade;
import com.rondinellesilva.cursomc.domain.Cliente;
import com.rondinellesilva.cursomc.domain.Endereco;
import com.rondinellesilva.cursomc.domain.enums.TipoCliente;
import com.rondinellesilva.cursomc.dto.ClienteDTO;
import com.rondinellesilva.cursomc.dto.ClienteNewDTO;
import com.rondinellesilva.cursomc.repositories.CidadeRepository;
import com.rondinellesilva.cursomc.repositories.ClienteRepository;
import com.rondinellesilva.cursomc.repositories.EnderecoRepository;
import com.rondinellesilva.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Long id) {
		Optional<Cliente> cliente = repo.findById(id);

		if (!cliente.isPresent()) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
		}
		return cliente.orElse(new Cliente());
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Long id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Não é possível excluir um cliente por que existem pedidos associados.");
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPage, String direction, String orderBy) {
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO dto) {
		return new Cliente(dto.getId(), dto.getNome(), dto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO dto) {
		Cliente cli = new Cliente(null, dto.getNome(), dto.getEmail(), dto.getCpfOuCnpj(), TipoCliente.toEnum(dto.getTipoCliente()), pe.encode(dto.getSenha()));
		Cidade cid = cidadeRepository.findById(dto.getCidadeId()).orElse(null);
		
		Endereco end = new Endereco(null, dto.getLogradouro(), dto.getNumero(), dto.getComplemento(), dto.getBairro(), dto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(dto.getTelefone1());
		if(dto.getTelefone2() == null)
			cli.getTelefones().add(dto.getTelefone2());
		
		if(dto.getTelefone3() == null)
			cli.getTelefones().add(dto.getTelefone3());
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
