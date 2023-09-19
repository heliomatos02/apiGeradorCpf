package com.gerador.cpf.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gerador.cpf.model.Cpf;
import com.gerador.cpf.repository.CpfRepository;


@RestController
@RequestMapping("/gerarCpf")
public class CpfController {
	@Autowired
	private CpfRepository cpfRepository;
	private Random gerador = new Random();

	@GetMapping
	public List<Cpf> listar() {
		return cpfRepository.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String gerarCpf() {
		return criarCpf();
	}

	private String criarCpf() {
		try {
			Cpf cpfAgravar = new Cpf();
			String cpf = "";

			for (int i = 0; i < 8; i++) {
				cpf = cpf.concat(String.valueOf(gerador.nextInt(10)));
			}
			cpf = cpf.concat(geradorDigitoUF());
			/***
			 * 0 (RS)- 1 (DF GO MS MT TO)- 2 (AC AM AP PA RO RR)- 3 (CE MA PI)- 4 (AL PB PE
			 * RN)- 5 (BA SE)- 6 (MG)- 7 (ES RJ)- 8 (SP)- 9 (PR SC).
			 */
			cpf = cpf.concat(calcularDvUm(cpf));
			cpf = cpf.concat(calcularDvDois(cpf));
			cpfAgravar.setCpf(cpf);
			cpfRepository.save(cpfAgravar);
			return cpfAgravar.getCpf();
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	// calcula o dígito da região fiscal
	private String geradorDigitoUF() {
		return String.valueOf(gerador.nextInt(10));
	}

	private String calcularDvUm(String cpf) {
		int soma = 0;
		int resto;
		int multi = 1;
		for (char each : Integer.toString(Integer.parseInt(cpf)).toCharArray()) {
			soma += Integer.parseInt(Character.toString(each)) * multi;
			multi += 1;

		}
		resto = soma % 11;
		resto=verificaRestoZero(resto);
		return String.valueOf(resto);
	}

	private String calcularDvDois(String cpf) {
		int soma = 0;
		int resto;
		int multi = 0;
		for (char each : Long.toString(Long.parseLong(cpf)).toCharArray()) {
			soma += Integer.parseInt(Character.toString(each)) * multi;
			multi += 1;

		}
		resto = soma % 11;
		resto=verificaRestoZero(resto);
		return String.valueOf(resto);
	}
	
	private int verificaRestoZero(int resto) {
		if(resto==10) {
			return 0;
		}
		return resto;
	}
}
