package ru.practicum.main.service.servise.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.compilation.CompilationDto;
import ru.practicum.main.service.dto.compilation.CompilationDtoRequest;
import ru.practicum.main.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.exceptions.BadRequestException;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.mapper.CompilationMapper;
import ru.practicum.main.service.model.Compilation;
import ru.practicum.main.service.model.event.Event;
import ru.practicum.main.service.repository.CompilationRepository;
import ru.practicum.main.service.repository.EventsRepository;
import ru.practicum.main.service.utils.PaginationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventsRepository eventsRepository;

    @Override
    public CompilationDto createCompilation(CompilationDtoRequest compilationDtoRequest) {

        List<Event> events = getEvents(compilationDtoRequest.getEvents());

        Compilation compilation = CompilationMapper.toCompilation(compilationDtoRequest);
        compilation.setEvents(events);

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));

    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {

        Compilation compilation = findCompilationById(compId);

        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = getEvents(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        compilation.setPinned(updateCompilationRequest.isPinned());

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));

    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {

        Pageable pageable = PaginationUtils.createPageable(from, size);
        List<Compilation> compilations;

        if (pinned != null)
            compilations = compilationRepository.findAllByPinnedEquals(pinned, pageable);
        else
            compilations = compilationRepository.findAll(pageable).getContent();

        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(long compId) {
        return CompilationMapper.toCompilationDto(findCompilationById(compId));
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Compilation not found"));

        compilationRepository.delete(compilation);
    }

    private Compilation findCompilationById(long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Compilation not found"));
    }

    private List<Event> getEvents(List<Long> ids) {
        if (ids == null) {
            return new ArrayList<>();
        }
        List<Event> events = eventsRepository.findAllById(ids);
        if (events.size() != ids.size()) {
            throw new BadRequestException("Events in compilationDtoRequest.getEvents() not found in DB");
        }
        return events;
    }

}