package edu.com.rocketseat.samuels.curso.task;

import edu.com.rocketseat.samuels.curso.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    //gerencia a instancia do repositorio
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var userId = (UUID) request.getAttribute("idUser");
        taskModel.setIdUser(userId);
        //na linha abaixo o o ID do objeto task model é setado por um UUID puxado pelo servlet

        var currentDate = LocalDateTime.now();
        if (taskModel.getStartAt().isBefore(currentDate)||taskModel.getEndAt().isBefore(taskModel.getStartAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio ou a de finalização deve ser anterior" +
                    " a data de criação");
        }

        //var title = taskModel.getTitle();
        //if (title.length() > 35){
        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("o titulo da task não pode passar de 35 caracteries");
        //}
        var task=  this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> listTasks(HttpServletRequest request){

        var idUser = ((UUID)request.getAttribute("idUser"));
        System.out.println(idUser);
        var tasks = this.taskRepository.findByIdUser(idUser);

        return tasks;
    }
    @PutMapping("/{id}")
    public ResponseEntity updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){

        var task = this.taskRepository.findById(id).orElse(null);

        var idUser = ((UUID)request.getAttribute("idUser"));

        if (task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
        //da classe Utils.java
        if (!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário sem permissão para alterar a tarefa");
        }
        Utils.copyNonNullProperties(taskModel, task);
        taskModel.setCreatedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(task));
    }
}
