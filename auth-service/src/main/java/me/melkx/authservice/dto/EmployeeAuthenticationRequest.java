package me.melkx.authservice.dto;

public record EmployeeAuthenticationRequest(String email, String companyCode, String password) {
}
