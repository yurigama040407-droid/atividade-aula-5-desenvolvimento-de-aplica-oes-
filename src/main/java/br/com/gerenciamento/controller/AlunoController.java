package br.com.gerenciamento.controller;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AlunoController {

    private final AlunoService alunoService;

    @Autowired
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping("/inserirAlunos")
    public ModelAndView insertAlunos(Aluno aluno) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Aluno/formAluno");
        modelAndView.addObject("aluno", new Aluno());
        return modelAndView;
    }

    @PostMapping("InsertAlunos")
    public ModelAndView inserirAluno(@Valid Aluno aluno, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("Aluno/formAluno");
            modelAndView.addObject("aluno", aluno);
            return modelAndView;
        }
        try {
            alunoService.salvar(aluno);
            modelAndView.setViewName("redirect:/alunos-adicionados");
        } catch (EmailExistsException ex) {
            bindingResult.rejectValue("email", "error.aluno", ex.getMessage());
            modelAndView.setViewName("Aluno/formAluno");
            modelAndView.addObject("aluno", aluno);
        }
        return modelAndView;
    }

    @GetMapping("alunos-adicionados")
    public ModelAndView listagemAlunos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Aluno/listAlunos");
        modelAndView.addObject("alunosList", alunoService.listarTodos());
        return modelAndView;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Aluno/editar");
        Aluno aluno = alunoService.buscarPorId(id);
        modelAndView.addObject("aluno", aluno);
        return modelAndView;
    }

    @PostMapping("/editar")
    public ModelAndView editar(@Valid Aluno aluno, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("Aluno/editar");
            modelAndView.addObject("aluno", aluno);
            return modelAndView;
        }
        try {
            alunoService.atualizar(aluno.getId(), aluno);
            modelAndView.setViewName("redirect:/alunos-adicionados");
        } catch (EmailExistsException ex) {
            bindingResult.rejectValue("email", "error.aluno", ex.getMessage());
            modelAndView.setViewName("Aluno/editar");
            modelAndView.addObject("aluno", aluno);
        }
        return modelAndView;
    }

    @GetMapping("/remover/{id}")
    public String removerAluno(@PathVariable("id") Long id) {
        alunoService.deletarPorId(id);
        return "redirect:/alunos-adicionados";
    }

    @GetMapping("filtro-alunos")
    public ModelAndView filtroAlunos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Aluno/filtroAlunos");
        return modelAndView;
    }

    @GetMapping("alunos-ativos")
    public ModelAndView listaAlunosAtivos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Aluno/alunos-ativos");
        modelAndView.addObject("alunosAtivos", alunoService.listarAtivos());
        return modelAndView;
    }

    @GetMapping("alunos-inativos")
    public ModelAndView listaAlunosInativos() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("Aluno/alunos-inativos");
        modelAndView.addObject("alunosInativos", alunoService.listarInativos());
        return modelAndView;
    }

    @PostMapping("/pesquisar-aluno")
    public ModelAndView pesquisarAluno(@RequestParam(required = false) String nome) {
        ModelAndView modelAndView = new ModelAndView();
        List<Aluno> listaAlunos = alunoService.buscarPorNome(nome);
        modelAndView.addObject("ListaDeAlunos", listaAlunos);
        modelAndView.setViewName("Aluno/pesquisa-resultado");
        return modelAndView;
    }
}
