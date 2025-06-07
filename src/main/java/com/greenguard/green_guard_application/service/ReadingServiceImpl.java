package com.greenguard.green_guard_application.service;

import com.greenguard.green_guard_application.model.dto.ReadingDTO;
import com.greenguard.green_guard_application.model.dto.ReadingFilterDTO;
import com.greenguard.green_guard_application.model.entity.Reading;
import com.greenguard.green_guard_application.model.entity.User;
import com.greenguard.green_guard_application.repository.ReadingRepository;
import com.greenguard.green_guard_application.repository.UserRepository;
import com.greenguard.green_guard_application.service.exception.ReadingNotFoundException;
import com.greenguard.green_guard_application.service.exception.UserNotFoundException;
import com.greenguard.green_guard_application.service.mapper.ReadingMapper;
import com.greenguard.green_guard_application.service.specification.ReadingSpecification;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ReadingServiceImpl implements ReadingService{

    private final ReadingRepository readingRepository;
    private final UserRepository userRepository;
    private final ReadingMapper readingMapper;

    public ReadingServiceImpl(ReadingRepository readingRepository, UserRepository userRepository, ReadingMapper readingMapper) {
        this.readingRepository = readingRepository;
        this.userRepository = userRepository;
        this.readingMapper = readingMapper;
    }

    @Override
    public List<ReadingDTO> findAll(@NotNull ReadingFilterDTO readingFilterDTO) {
        userRepository.findById(readingFilterDTO.getSensorOwnerUsername())
                        .orElseThrow(() -> new UserNotFoundException("User with provided username is not present in the system"));

        List<Reading> readings = readingRepository.findAll(ReadingSpecification.withFilter(readingFilterDTO));

        return readings.stream().map(readingMapper::toDto).toList();
    }

    @Override
    public ReadingDTO findLastReading(@NotNull  ReadingFilterDTO readingFilterDTO) {
        List<Reading> matchingReadings = readingRepository.findAll(ReadingSpecification.withFilter(readingFilterDTO));

        if (matchingReadings.isEmpty()) {
            throw new ReadingNotFoundException("Reading matching filtering criteria hasn't been found");
        }

        Reading latestReading = matchingReadings.stream()
                                                .max(Comparator.comparing(Reading::getTimestamp))
                                                .get();

        return readingMapper.toDto(latestReading);
    }

    @Override
    public List<ReadingDTO> getFavoriteLocationReadings(ReadingFilterDTO readingFilterDTO) {
        User targerUser = userRepository.findById(readingFilterDTO.getSensorOwnerUsername())
                                .orElseThrow(
                                    () -> new UserNotFoundException("User with provided username is not present in the system")
                                );

        List<Reading> allReadings = readingRepository.findAll(ReadingSpecification.withFilter(readingFilterDTO));

        return allReadings.stream().filter(
                        reading -> targerUser.getFavoriteLocations().contains(reading.getSensor().getLocation())
                        ).map(readingMapper::toDto).toList();
    }
}
