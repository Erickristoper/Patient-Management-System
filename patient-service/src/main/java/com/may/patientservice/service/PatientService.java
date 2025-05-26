package com.may.patientservice.service;

import com.may.patientservice.dto.PatientRequestDTO;
import com.may.patientservice.dto.PatientResponseDTO;
import com.may.patientservice.exception.EmailAlreadyExistsException;
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

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email: " + patientRequestDTO.getEmail() +
                    " already exists");
        }
        Patient newPatient = patientRepository.save(PatientMapper.toPatientModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }
}
