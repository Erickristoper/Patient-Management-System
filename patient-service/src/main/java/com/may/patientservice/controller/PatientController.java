package com.may.patientservice.controller;

import com.may.patientservice.dto.PatientRequestDTO;
import com.may.patientservice.dto.PatientResponseDTO;
import com.may.patientservice.dto.Validators.CreateValidationGroup;
import com.may.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {

    PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok().body(patientService.getAllPatients());
    }

    @PostMapping
    @Operation(summary = "Create Patients")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreateValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok().body(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patients")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                                                            @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO ) {
        return ResponseEntity.ok().body(patientService.updatePatient(id,patientRequestDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
