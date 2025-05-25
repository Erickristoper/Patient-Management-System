package com.may.patientservice.service;

import com.may.patientservice.dto.PatientResponseDTO;
import com.may.patientservice.mapper.PatientMapper;
import com.may.patientservice.model.Patient;
import com.may.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toDTO).toList();
    }
}
