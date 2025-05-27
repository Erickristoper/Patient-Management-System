package com.may.patientservice.service;

import com.may.patientservice.dto.PatientRequestDTO;
import com.may.patientservice.dto.PatientResponseDTO;
import com.may.patientservice.exception.EmailAlreadyExistsException;
import com.may.patientservice.exception.PatientNotFoundException;
import com.may.patientservice.grpc.BillingServiceGrpcClient;
import com.may.patientservice.kafka.kafkaProducer;
import com.may.patientservice.mapper.PatientMapper;
import com.may.patientservice.model.Patient;
import com.may.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient grpcClient;
    private final kafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient grpcClient, kafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.grpcClient = grpcClient;
        this.kafkaProducer = kafkaProducer;
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

        grpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient updatePatient =
                patientRepository.findById(id).
                        orElseThrow( () -> new PatientNotFoundException("Patient not found with ID: " + id));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email: " + patientRequestDTO.getEmail() +
                    " already exists");
        }
        updatePatient.setName(patientRequestDTO.getName());
        updatePatient.setAddress(patientRequestDTO.getAddress());
        updatePatient.setEmail(patientRequestDTO.getEmail());
        updatePatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));


        return PatientMapper.toDTO(patientRepository.save(updatePatient));
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
