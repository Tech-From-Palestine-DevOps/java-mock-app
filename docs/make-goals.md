# Make Goals Documentation

This document provides detailed information about the available Make targets in the project.

## Overview

The Makefile provides several targets to help with development, testing, and code quality checks. Each target serves a specific purpose and can be combined to create a comprehensive development workflow.

## Available Targets

### Build and Clean

- `make all`: Runs a complete build cycle including cleaning, testing, checkstyle, and security scanning
- `make clean`: Removes all build artifacts and generated files

### Testing

- `make test`: Runs all tests in the project
- `make unit`: Runs only unit tests (files ending with *Test)
- `make integration`: Runs only integration tests (files ending with *IT)
- `make surefire-report`: Generates a Surefire test report (works even if tests fail)
- `make test-report`: Combines test execution and report generation

### Code Quality

- `make checkstyle`: Runs Checkstyle analysis on the codebase
- `make checkstyle-report`: Generates and opens the Checkstyle report in your default browser

### Security

- `make security`: Runs a custom security scan to detect hardcoded secrets
- `make security-report`: Displays detailed security scan results in a formatted way
- `make security-clean`: Removes all security scan reports

### Development Tools

- `make serve`: Starts a local HTTP server on port 8080 to view various reports
  - Test reports: http://localhost:8080/surefire-reports/index.html
  - Checkstyle reports: http://localhost:8080/site/checkstyle.html

### Help

- `make help`: Displays a list of all available targets with brief descriptions

## Usage Examples

### Complete Development Cycle
```bash
make all
```

### Running Tests and Viewing Reports
```bash
make test-report
make serve  # View reports in browser
```

### Code Quality Check
```bash
make checkstyle
make checkstyle-report
```

### Security Analysis
```bash
make security
make security-report
```

## Notes

- The `serve` target requires Python 3 to be installed
- The `security-report` target provides better output if `jq` is installed
- Some targets may require Maven to be installed and configured
- The security scanner is a custom implementation for detecting hardcoded secrets 