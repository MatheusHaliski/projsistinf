package com.example.projetandosist
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping
class MenuController(
    private val doacoesRepository: DoacoesRepository,
    private val pessoaRepository: PessoaRepository
) {

    @PostMapping("/realizar-doacao2")
    fun realizarDoacao(
        @RequestParam descricao: String,
        @RequestParam quantidade: Int,
        session: HttpSession
    ): String {
        val username = session.getAttribute("username") as? String ?: "desconhecido"

        val doacao = Doacao(descricao = descricao, quantidade = quantidade, username = username)
        doacoesRepository.save(doacao)

        return "redirect:/menuinicial.html"
    }



    @GetMapping("/visualizar-minhas-doacoes")
    fun visualizarMinhasDoacoes(session: HttpSession, model: Model): String {
        val username = session.getAttribute("username") as? String
            ?: return "redirect:/login"

        val doacoesUsuario = doacoesRepository.findByUsername(username)
        model.addAttribute("doacoes", doacoesUsuario)
        return "visualizar-doacao"
    }

    @PostMapping("/salvar-dados-perfil")
    fun salvarDadosPerfil(
        request: HttpServletRequest,
        session: HttpSession,
        model: Model
    ): String {
        val nome1 = session.getAttribute("username")
        val nome = request.getParameter("nome")
        val email = request.getParameter("email")
        val senha = request.getParameter("senha")
        val pessoa = pessoaRepository.findByNome(nome1.toString())
        // Atualiza os campos da pessoa
        if (pessoa != null) {
            pessoa.nome = nome
        }
        if (pessoa != null) {
            pessoa.email = email
        }

        if (!senha.isNullOrBlank()) {
            if (pessoa != null) {
                pessoa.senha = senha
            }
        }

        if (pessoa != null) {
            pessoaRepository.save(pessoa)
        }

        if (pessoa != null) {
            session.setAttribute("username", pessoa.nome)
        }

        return "redirect:/menuinicial.html"
    }


    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login.html"
    }
}
