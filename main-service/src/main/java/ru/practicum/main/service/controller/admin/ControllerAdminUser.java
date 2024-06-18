package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.user.UserDtoRequest;
import ru.practicum.main.service.dto.user.UserDtoResponse;
import ru.practicum.main.service.servise.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Slf4j
public class ControllerAdminUser {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDtoResponse> create(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        log.info("UserControllerAdmin.post: {}", userDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userDtoRequest));
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAll(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("UserControllerAdmin.get (ids = {}, from = {}, size = {})", ids, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll(ids, from, size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable long userId) {
        log.info("UserControllerAdmin.delete userId: {}", userId);
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
