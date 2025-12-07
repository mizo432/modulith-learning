# Quick Start Guide - Docker Compose Tests

## Run All Tests (Recommended)

```bash
cd .devcontainer/test
./run_tests.sh
```

## Run Individual Test Suites

### Shell Tests (No Dependencies Required)
```bash
cd .devcontainer/test
./test_yaml_syntax.sh
```

### Python Tests (Requires PyYAML)
```bash
cd .devcontainer/test
pip install -r requirements.txt
python3 -m unittest test_docker_compose_validation.py -v
python3 -m unittest test_docker_compose_integration.py -v
```

## What Gets Tested

✅ YAML syntax  
✅ Valkey → Redis migration complete  
✅ PostgreSQL version pinned to 17.0  
✅ All services configured correctly  
✅ Volumes and networks valid  
✅ Port mappings unique  
✅ Dependencies proper  

## Expected Output