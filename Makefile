.PHONY: clean test unit integration surefire-report checkstyle checkstyle-report serve security security-report all security-clean help

# Default target
all: clean test surefire-report checkstyle security

# Clean build artifacts
clean:
	mvn clean

# Run all tests
test:
	mvn test

# Run only unit tests
unit:
	mvn test -Dtest=*Test

# Run only integration tests
integration:
	mvn verify -Dtest=*IT

# Generate surefire report (even if tests fail)
surefire-report:
	mvn surefire-report:report -Dmaven.test.failure.ignore=true

# Run tests and generate report
test-report: test surefire-report

# Run checkstyle
checkstyle:
	mvn checkstyle:check

# Generate checkstyle report and open it
checkstyle-report:
	mvn checkstyle:checkstyle
	xdg-open target/site/checkstyle.html

# Run security scan (custom scanner)
security:
	@echo "Running custom security scan..."
	./security-scanner.sh

# Run security scan with pretty output
security-report: security
	@echo "\nSecurity Findings Report:"
	@if [ -f target/security-report/security-findings.json ]; then \
		if command -v jq &> /dev/null; then \
			echo "Detailed findings:"; \
			cat target/security-report/security-findings.json | jq '.[] | {file: .file, line: .line, description: .description, secret: .secret}'; \
		else \
			echo "Raw JSON output:"; \
			cat target/security-report/security-findings.json; \
		fi \
	else \
		echo "No security findings file found."; \
	fi

# Clean security reports
security-clean:
	@echo "Cleaning security reports..."
	rm -rf target/security-report/

# Serve target directory on port 8080
serve:
	@echo "Starting HTTP server on port 8080..."
	@echo "Reports available at:"
	@echo "- Tests: http://localhost:8080/surefire-reports/index.html"
	@echo "- Checkstyle: http://localhost:8080/site/checkstyle.html"
	@echo "\nPress Ctrl+C to stop the server"
	@cd target && python3 -m http.server 8080

# Help target
help:
	@echo "Available targets:"
	@echo "  all            - Clean, run tests, checkstyle and security scan"
	@echo "  clean          - Clean build artifacts"
	@echo "  test           - Run all tests"
	@echo "  unit           - Run only unit tests"
	@echo "  integration    - Run only integration tests"
	@echo "  surefire-report - Generate surefire report"
	@echo "  test-report    - Run tests and generate report"
	@echo "  checkstyle     - Run checkstyle analysis"
	@echo "  checkstyle-report - Generate and open checkstyle report"
	@echo "  security       - Run custom security scan (hardcoded secrets)"
	@echo "  security-report - Display custom security scan results"
	@echo "  security-clean - Clean security reports"
	@echo "  serve          - Start HTTP server for reports on port 8080"
	@echo "  help           - Show this help message" 