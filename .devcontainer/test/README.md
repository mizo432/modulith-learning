# Docker Compose Configuration Tests

This directory contains comprehensive tests for the `.devcontainer/docker-compose.yml` configuration file.

## Overview

The test suite validates the Docker Compose configuration used for the development environment, ensuring:
- Correct service definitions and configurations  
- Proper volume and network setup
- Valid port mappings and dependencies
- No legacy references (e.g., Valkey → Redis migration)
- PostgreSQL version pinning (17.0)
- Security best practices for development environments

## Test Files

### `test_yaml_syntax.sh`
Shell-based validation script that performs:
- YAML syntax validation using Python's yaml module
- Required key presence checks (version, services, volumes)
- Service definition verification (db, redis, service-registry, zipkin, app)
- Legacy reference detection (ensures no 'valkey' references remain)
- Redis service configuration validation
- PostgreSQL version validation (17.0)
- Volume definition checks (postgres_data, redis_data)
- Service dependency verification (app depends on redis)

**Tests Performed:**
1. YAML syntax validation
2. Required top-level keys present
3. All required services defined
4. No legacy 'valkey' references
5. Redis properly configured with redis-stack-server image
6. PostgreSQL uses version 17.0 (not 'latest')
7. Required volumes defined
8. App service dependencies include redis

### `test_docker_compose_validation.py`
Comprehensive Python-based unit tests using the `unittest` framework. This file was created earlier and validates:
- Docker Compose version and structure
- Volume definitions (postgres_data, redis_data)  
- Service configurations (PostgreSQL, Redis, Eureka, Zipkin, App, API Gateway)
- Image versions and restart policies
- Port mappings and network configurations
- Service dependencies and ordering
- Environment variables and security settings
- Edge cases and error conditions

**Test Classes:**
- `TestDockerComposeConfiguration`: Core configuration validation
- `TestDockerComposeEdgeCases`: Edge cases and potential issues
- `TestDockerComposeSecurityConsiderations`: Security-related validations

### `test_docker_compose_integration.py`
Integration tests focusing on:
- Service dependency resolution order
- Network isolation and communication
- Data persistence mapping
- Cross-service configurations
- Migration validation (Valkey → Redis, PostgreSQL versioning)

**Test Classes:**
- `TestServiceIntegration`: Service interaction and dependency tests
- `TestConfigurationMigration`: Valkey to Redis migration validation
- `TestPostgresMigration`: PostgreSQL version migration tests

### `run_tests.sh`
Main test runner that executes all test suites in sequence and reports results.

**Execution Flow:**
1. Checks for Python3 and PyYAML availability
2. Runs shell-based YAML syntax tests
3. Runs Python configuration validation tests (if available)
4. Runs Python integration tests (if available)
5. Provides comprehensive summary report

## Running the Tests

### Run All Tests
```bash
cd .devcontainer/test
./run_tests.sh
```

### Run Shell Tests Only
```bash
cd .devcontainer/test
./test_yaml_syntax.sh
```

### Run Python Tests Only (if created)
```bash
cd .devcontainer/test
python3 -m unittest test_docker_compose_validation.py -v
python3 -m unittest test_docker_compose_integration.py -v
```

Or with pytest (if available):
```bash
cd .devcontainer/test
python3 -m pytest test_docker_compose_validation.py -v
python3 -m pytest test_docker_compose_integration.py -v
```

## Requirements

- **Bash** (for shell-based tests) - Required
- **Python 3.x** (for Python-based tests) - Optional but recommended
- **PyYAML** library: `pip install -r requirements.txt` - Optional but recommended

The shell tests will run even without Python, providing basic validation.

## Test Coverage

### Changes Validated (from git diff develop..HEAD)

The test suite specifically validates the changes made in the current branch:

1. **Redis Migration (valkey → redis)**
   - Service renamed from 'valkey' to 'redis'
   - Image changed from 'bitnami/valkey:8.0' to 'redis/redis-stack-server:latest'
   - Container name updated from 'valkey_server' to 'redis_server'
   - Volume renamed from 'valkey_data' to 'redis_data'
   - Volume path changed from '/bitnami/valkey/data' to '/bitnami/redis/data'
   - App dependency updated from 'valkey' to 'redis'

2. **PostgreSQL Version Pinning**
   - Image changed from 'postgres:latest' to 'postgres:17.0'
   - Ensures specific version is used instead of floating 'latest' tag

### Service-Specific Tests

- **PostgreSQL (db)**: Image version (17.0), environment variables, volumes, ports, restart policy
- **Redis**: Migration validation, image (redis-stack-server), volume paths, port configuration
- **Service Registry (Eureka)**: Build context, port 8761, network configuration
- **Zipkin**: Distributed tracing configuration, dependencies
- **API Gateway**: Port 8085, build context, dependencies
- **App**: Build setup, volume mounts, dependency ordering, development command

### Structural Tests

- YAML syntax and schema validation
- Volume and network definitions
- Port conflict detection
- Container name uniqueness
- Service dependency graph validation (no cycles)
- Environment variable syntax

### Migration-Specific Tests

- No legacy 'valkey' references anywhere in configuration
- All valkey → redis renamings completed
- PostgreSQL not using 'latest' tag
- Redis using correct image and paths

### Security Tests (Python tests if available)

- Development environment markers (ALLOW_EMPTY_PASSWORD)
- Environment variable usage patterns
- No hardcoded production credentials

## CI/CD Integration

These tests can be integrated into CI/CD pipelines:

### GitHub Actions Example
```yaml
name: Validate Docker Compose

on: [push, pull_request]

jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up Python
        uses: actions/setup-python@v4
        with:
          python-version: '3.x'
      
      - name: Install dependencies
        run: |
          cd .devcontainer/test
          pip install -r requirements.txt
      
      - name: Run validation tests
        run: |
          cd .devcontainer/test
          ./run_tests.sh
```

### GitLab CI Example
```yaml
validate_docker_compose:
  image: python:3.11
  script:
    - cd .devcontainer/test
    - pip install -r requirements.txt
    - ./run_tests.sh
```

## Adding New Tests

### Shell Tests (test_yaml_syntax.sh)

Add new test sections following this pattern:

```bash
echo "Test N: Description..."
if [ condition ]; then
    echo "✓ Test passed"
else
    echo "✗ Test failed"
    exit 1
fi
echo ""
```

### Python Tests (if files exist)

Add new test methods to the appropriate test class:

```python
def test_new_feature(self):
    """Test description."""
    # Your test code here
    self.assertEqual(expected, actual)
```

## Maintenance

When updating `docker-compose.yml`:

1. **Review the changes**: `git diff develop docker-compose.yml`
2. **Update relevant tests** to reflect intentional changes
3. **Add new tests** for new features or services  
4. **Run the full test suite**: `./run_tests.sh`
5. **Fix any failures** before committing
6. **Update this README** if test structure changes

## Troubleshooting

### Python Import Errors
```bash
pip install -r requirements.txt
```

### Permission Denied on Shell Scripts
```bash
chmod +x .devcontainer/test/*.sh
```

### Tests Failing After Docker Compose Changes

1. Review the git diff: `git diff docker-compose.yml`
2. Check which specific tests are failing
3. Determine if failures are due to:
   - Intentional configuration changes (update tests)
   - Unintended issues (fix configuration)
4. Update tests to match new expected behavior
5. Re-run test suite to verify

### Shell Tests Pass But Python Tests Fail

- Python tests are more comprehensive
- Check the specific assertion that's failing
- Python tests validate deeper configuration aspects

## Test Philosophy

These tests follow a "trust but verify" approach for infrastructure as code:

- **Configuration files are code** and should be tested
- **Migrations should be complete** with no legacy references
- **Version pinning matters** for reproducibility
- **Dependencies should be explicit** and properly ordered
- **Security settings should be intentional** and documented

## Project Context

This modulith-learning project uses:
- **Java 25** with **Spring Boot 4.0.0**
- **Spring Modulith** for modular monolith architecture
- **PostgreSQL 17.0** for database
- **Redis Stack** for caching/session storage
- **Eureka** for service registry
- **Zipkin** for distributed tracing
- **API Gateway** for request routing

The docker-compose.yml configures the complete development environment with all these services properly networked and dependent on each other.
