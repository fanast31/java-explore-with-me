package ru.practicum.main.service.servise.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.user.UserDtoRequest;
import ru.practicum.main.service.dto.user.UserDtoResponse;
import ru.practicum.main.service.exceptions.ConflictDataException;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.mapper.UserMapper;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.PaginationUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDtoResponse create(UserDtoRequest userDtoRequest) {
        if (userRepository.existsByEmail(userDtoRequest.getEmail())) {
            throw new ConflictDataException("User with this email exist already");
        }
        return UserMapper.toUserDtoResponse(
                userRepository.save(UserMapper.toUser(userDtoRequest)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoResponse> getAll(List<Long> ids, int from, int size) {
        List<User> users;

        Pageable pageable = PaginationUtils.createPageable(from, size);
        if (ids != null) {
            users = userRepository.findAllById(ids);
        } else {
            users = userRepository.findAll(pageable).getContent();
        }

        return users.stream()
                .map(UserMapper::toUserDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        userRepository.delete(user);
    }

}
