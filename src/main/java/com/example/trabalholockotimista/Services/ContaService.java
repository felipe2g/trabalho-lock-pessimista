package com.example.trabalholockotimista.Services;

import com.example.trabalholockotimista.Entities.Conta;
import com.example.trabalholockotimista.Repositories.ContaRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContaService {
    @Autowired
    private ContaRepository contaRepository;

    @Transactional
    public Conta sacar(String numeroConta, BigDecimal valor) throws Exception {
        Conta conta = contaRepository.findByNumeroConta(numeroConta);

        if (conta == null) {
            throw new Exception("Conta não encontrada");
        }

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new Exception("Saldo insuficiente.");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaRepository.save(conta);
        return conta;
    }

    @Transactional
    public Conta depositar(String numeroConta, BigDecimal valor) throws Exception {
        Conta conta = contaRepository.findByNumeroConta(numeroConta);
        if (conta == null) {
            Conta novaConta = new Conta();
            novaConta.setNumeroConta(numeroConta);
            novaConta.setSaldo(valor);

            contaRepository.save(novaConta);

            return novaConta;
        }
        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
        return conta;
    }
}
