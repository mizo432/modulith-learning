#!/bin/bash
# Comprehensive Test Runner for docker-compose.yml

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
EXIT_CODE=0
TOTAL_TESTS=0
PASSED_TESTS=0

echo "=========================================="
echo "Docker Compose Configuration Test Suite"
echo "=========================================="
echo ""
echo "Testing: .devcontainer/docker-compose.yml"
echo ""

# Check dependencies
echo "Checking dependencies..."
if ! command -v python3 &> /dev/null; then
    echo "⚠ Warning: python3 not found. Python tests will be skipped."
fi

if command -v python3 &> /dev/null; then
    if ! python3 -c "import yaml" 2>/dev/null; then
        echo "⚠ Warning: PyYAML not installed. Installing..."
        pip3 install -q PyYAML 2>/dev/null || echo "  Failed to install PyYAML."
    fi
fi
echo ""

# Run shell-based YAML syntax tests
echo "=========================================="
echo "Phase 1: YAML Syntax and Structure Tests"
echo "=========================================="
((TOTAL_TESTS++))
if bash "$SCRIPT_DIR/test_yaml_syntax.sh"; then
    echo "✓ YAML syntax tests passed"
    ((PASSED_TESTS++))
else
    echo "✗ YAML syntax tests failed"
    EXIT_CODE=1
fi
echo ""

# Run Python-based configuration validation tests
if command -v python3 &> /dev/null && python3 -c "import yaml" 2>/dev/null; then
    if [ -f "$SCRIPT_DIR/test_docker_compose_validation.py" ]; then
        echo "=========================================="
        echo "Phase 2: Configuration Validation Tests"
        echo "=========================================="
        ((TOTAL_TESTS++))
        if python3 -m unittest "$SCRIPT_DIR/test_docker_compose_validation.py" -v 2>&1; then
            echo "✓ Configuration validation tests passed"
            ((PASSED_TESTS++))
        else
            echo "✗ Configuration validation tests failed"
            EXIT_CODE=1
        fi
        echo ""
    fi
    
    if [ -f "$SCRIPT_DIR/test_docker_compose_integration.py" ]; then
        echo "=========================================="
        echo "Phase 3: Integration Tests"
        echo "=========================================="
        ((TOTAL_TESTS++))
        if python3 -m unittest "$SCRIPT_DIR/test_docker_compose_integration.py" -v 2>&1; then
            echo "✓ Integration tests passed"
            ((PASSED_TESTS++))
        else
            echo "✗ Integration tests failed"
            EXIT_CODE=1
        fi
        echo ""
    fi
fi

# Summary
echo "=========================================="
echo "Test Summary"
echo "=========================================="
echo "Total test suites: $TOTAL_TESTS"
echo "Passed: $PASSED_TESTS"
echo "Failed: $((TOTAL_TESTS - PASSED_TESTS))"
echo ""

if [ $EXIT_CODE -eq 0 ]; then
    echo "=========================================="
    echo "All tests passed successfully! ✓"
    echo "=========================================="
else
    echo "=========================================="
    echo "Some tests failed! ✗"
    echo "=========================================="
fi

exit $EXIT_CODE