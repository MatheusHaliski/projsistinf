package com.example.projetandosist

import jakarta.servlet.http.HttpSession
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pessoas")
class PessoaController(private val pessoaRepository: PessoaRepository) {

    @GetMapping
    fun listar(): List<Pessoa> = pessoaRepository.findAll()

    @PostMapping("/pessoas1")
    fun salvar(@RequestParam nome: String, @RequestParam email: String, @RequestParam senha: String): Pessoa {
        val pessoa = Pessoa(nome = nome, email = email, senha = senha)
        return pessoaRepository.save(pessoa)
    }
    @PostMapping("/pessoas12")
    fun verificarLogin(
        @RequestParam email: String,
        @RequestParam password: String,
        session: HttpSession  // <- Adiciona a sessão aqui
    ): ResponseEntity<Any> {
        val pessoa = pessoaRepository.findByEmailAndSenha(email, password)

        return if (pessoa != null) {
            // Armazena o nome do usuário na sessão
            session.setAttribute("username", pessoa.nome)

            // Redireciona para a página inicial
            ResponseEntity.status(302)
                .header("Location", "/menuinicial.html")
                .build()
        } else {
            ResponseEntity.status(401).body("Email ou senha inválidos.")
        }
    }

}
