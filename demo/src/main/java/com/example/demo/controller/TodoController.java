package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDto;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDto dto) {
        try{
            String temporaryUserId = "temporary-user";

            // 1. dto -> entity
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setId(null);
            entity.setUserId(temporaryUserId);

            // 2. TodoEntity 생성
            List<TodoEntity> entities = todoService.create(entity);

            // 3. entity -> dto
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());

            // 4. dto -> responseDto
            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        String temporaryUserId = "temporary-user";

        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);

        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto dto){
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDto.toEntity(dto);
        entity.setUserId(temporaryUserId);

        List<TodoEntity> entities = todoService.update(entity);

        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDto dto) {
        try {
            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.delete(entity);

            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
