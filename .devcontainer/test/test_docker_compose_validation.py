"""
Comprehensive tests for .devcontainer/docker-compose.yml configuration.

This test suite validates the Docker Compose configuration for the development
environment, ensuring all services are properly configured with correct images,
volumes, ports, and dependencies.
"""

import unittest
import yaml
import re
from pathlib import Path


class TestDockerComposeConfiguration(unittest.TestCase):
    """Test suite for docker-compose.yml validation."""

    @classmethod
    def setUpClass(cls):
        """Load the docker-compose.yml file once for all tests."""
        compose_file = Path(__file__).parent.parent / 'docker-compose.yml'
        with open(compose_file, 'r') as f:
            cls.config = yaml.safe_load(f)

    def test_docker_compose_version(self):
        """Test that docker-compose version is specified and valid."""
        self.assertIn('version', self.config)
        version = self.config['version']
        self.assertIsInstance(version, str)
        # Version should be 3.x format
        self.assertTrue(version.startswith('3.'), 
                       f"Expected version 3.x, got {version}")

    def test_volumes_defined(self):
        """Test that required volumes are defined."""
        self.assertIn('volumes', self.config)
        volumes = self.config['volumes']
        
        # Check for postgres_data volume
        self.assertIn('postgres_data', volumes,
                     "postgres_data volume must be defined")
        
        # Check for redis_data volume (renamed from valkey_data)
        self.assertIn('redis_data', volumes,
                     "redis_data volume must be defined")
        
        # Ensure old valkey_data volume is not present
        self.assertNotIn('valkey_data', volumes,
                        "valkey_data volume should be renamed to redis_data")

    def test_services_defined(self):
        """Test that all required services are defined."""
        self.assertIn('services', self.config)
        services = self.config['services']
        
        required_services = ['db', 'redis', 'service-registry', 'zipkin', 'app']
        for service in required_services:
            self.assertIn(service, services,
                         f"Service '{service}' must be defined")

    def test_postgres_service_configuration(self):
        """Test PostgreSQL service configuration."""
        db_service = self.config['services']['db']
        
        # Test image version
        self.assertIn('image', db_service)
        self.assertEqual(db_service['image'], 'postgres:17.0',
                        "PostgreSQL image should be postgres:17.0")
        
        # Test container name format
        self.assertIn('container_name', db_service)
        container_name = db_service['container_name']
        self.assertTrue('postgresdb' in container_name,
                       "Container name should contain 'postgresdb'")
        
        # Test restart policy
        self.assertEqual(db_service.get('restart'), 'unless-stopped',
                        "PostgreSQL should have 'unless-stopped' restart policy")
        
        # Test environment variables
        self.assertIn('environment', db_service)
        env = db_service['environment']
        required_env_vars = ['POSTGRES_USER', 'POSTGRES_PASSWORD', 'POSTGRES_DB']
        for var in required_env_vars:
            self.assertIn(var, env,
                         f"Environment variable {var} must be defined")
        
        # Test network configuration
        self.assertIn('networks', db_service)
        self.assertIn('backend', db_service['networks'])
        
        # Test port mapping
        self.assertIn('ports', db_service)
        ports = db_service['ports']
        self.assertTrue(any('5432' in str(port) for port in ports),
                       "PostgreSQL port 5432 must be exposed")
        
        # Test volume mounting
        self.assertIn('volumes', db_service)
        volumes = db_service['volumes']
        self.assertTrue(
            any('postgres_data' in str(vol) and '/var/lib/postgresql/data' in str(vol) 
                for vol in volumes),
            "PostgreSQL data volume must be mounted to /var/lib/postgresql/data"
        )

    def test_redis_service_configuration(self):
        """Test Redis service configuration (renamed from Valkey)."""
        # Ensure old valkey service is not present
        self.assertNotIn('valkey', self.config['services'],
                        "Old 'valkey' service should be renamed to 'redis'")
        
        redis_service = self.config['services']['redis']
        
        # Test image
        self.assertIn('image', redis_service)
        self.assertEqual(redis_service['image'], 'redis/redis-stack-server:latest',
                        "Redis image should be redis/redis-stack-server:latest")
        
        # Test container name
        self.assertIn('container_name', redis_service)
        container_name = redis_service['container_name']
        self.assertTrue('redis_server' in container_name,
                       "Container name should contain 'redis_server'")
        
        # Test environment variables
        self.assertIn('environment', redis_service)
        env = redis_service['environment']
        self.assertIn('ALLOW_EMPTY_PASSWORD', env,
                     "ALLOW_EMPTY_PASSWORD should be set for development")
        
        # Test restart policy
        self.assertEqual(redis_service.get('restart'), 'unless-stopped',
                        "Redis should have 'unless-stopped' restart policy")
        
        # Test port mapping
        self.assertIn('ports', redis_service)
        ports = redis_service['ports']
        self.assertTrue(any('6379' in str(port) for port in ports),
                       "Redis port 6379 must be exposed")
        
        # Test volume mounting with correct path
        self.assertIn('volumes', redis_service)
        volumes = redis_service['volumes']
        self.assertTrue(
            any('redis_data' in str(vol) for vol in volumes),
            "Redis data volume must be mounted"
        )
        # Ensure old valkey path is not used
        self.assertFalse(
            any('valkey_data' in str(vol) for vol in volumes),
            "Old valkey_data volume reference should not be present"
        )
        
        # Test dependencies
        self.assertIn('depends_on', redis_service)
        self.assertIn('db', redis_service['depends_on'],
                     "Redis should depend on db service")

    def test_service_registry_configuration(self):
        """Test service-registry (Eureka) configuration."""
        registry_service = self.config['services']['service-registry']
        
        # Test build context
        self.assertIn('build', registry_service)
        build = registry_service['build']
        self.assertIn('context', build)
        self.assertTrue('service-registry' in build['context'],
                       "Build context should reference service-registry")
        
        # Test container name
        self.assertIn('container_name', registry_service)
        self.assertTrue('eureka-server' in registry_service['container_name'],
                       "Container name should contain 'eureka-server'")
        
        # Test port mapping
        self.assertIn('ports', registry_service)
        ports = registry_service['ports']
        self.assertTrue(any('8761' in str(port) for port in ports),
                       "Eureka default port 8761 must be exposed")
        
        # Test network configuration
        self.assertIn('networks', registry_service)
        self.assertIn('backend', registry_service['networks'])

    def test_zipkin_service_configuration(self):
        """Test Zipkin distributed tracing service configuration."""
        zipkin_service = self.config['services']['zipkin']
        
        # Test image
        self.assertIn('image', zipkin_service)
        self.assertEqual(zipkin_service['image'], 'openzipkin/zipkin:latest',
                        "Zipkin image should be openzipkin/zipkin:latest")
        
        # Test container name
        self.assertIn('container_name', zipkin_service)
        self.assertTrue('zipkin-server' in zipkin_service['container_name'],
                       "Container name should contain 'zipkin-server'")
        
        # Test port mapping
        self.assertIn('ports', zipkin_service)
        ports = zipkin_service['ports']
        self.assertTrue(any('9411' in str(port) for port in ports),
                       "Zipkin port 9411 must be exposed")
        
        # Test dependencies
        self.assertIn('depends_on', zipkin_service)
        self.assertIn('service-registry', zipkin_service['depends_on'],
                     "Zipkin should depend on service-registry")

    def test_app_service_configuration(self):
        """Test main application service configuration."""
        app_service = self.config['services']['app']
        
        # Test build context
        self.assertIn('build', app_service)
        build = app_service['build']
        self.assertIn('dockerfile', build)
        self.assertTrue('Dockerfile' in build['dockerfile'],
                       "Dockerfile should be specified")
        
        # Test volumes for development
        self.assertIn('volumes', app_service)
        volumes = app_service['volumes']
        # Should mount workspace for development
        self.assertTrue(any('/workspace' in str(vol) for vol in volumes),
                       "Workspace should be mounted for development")
        
        # Test dependencies - should depend on all infrastructure services
        self.assertIn('depends_on', app_service)
        depends_on = app_service['depends_on']
        required_deps = ['service-registry', 'db', 'redis', 'zipkin']
        for dep in required_deps:
            self.assertIn(dep, depends_on,
                         f"App should depend on {dep} service")
        
        # Specifically test redis dependency (not valkey)
        self.assertIn('redis', depends_on,
                     "App should depend on 'redis' service (not valkey)")
        self.assertNotIn('valkey', depends_on,
                        "App should not reference old 'valkey' service")
        
        # Test command for development
        self.assertIn('command', app_service)
        self.assertIn('sleep infinity', app_service['command'],
                     "Development container should use 'sleep infinity'")

    def test_networks_configuration(self):
        """Test network configuration."""
        # All services should be on the backend network
        services = self.config['services']
        for service_name, service_config in services.items():
            if 'networks' in service_config:
                self.assertIn('backend', service_config['networks'],
                            f"Service {service_name} should be on backend network")

    def test_no_legacy_valkey_references(self):
        """Ensure no legacy Valkey references remain in the configuration."""
        config_str = yaml.dump(self.config).lower()
        
        # Check that valkey is not referenced anywhere
        self.assertNotIn('valkey', config_str,
                        "Configuration should not contain any 'valkey' references")

    def test_redis_volume_path_correctness(self):
        """Test that Redis volume uses correct path (not old Valkey path)."""
        redis_service = self.config['services']['redis']
        volumes = redis_service['volumes']
        
        volume_str = str(volumes)
        # Should use redis path
        self.assertIn('/bitnami/redis/data', volume_str,
                     "Redis volume should mount to /bitnami/redis/data")
        # Should not use old valkey path
        self.assertNotIn('/bitnami/valkey', volume_str,
                        "Should not reference old /bitnami/valkey path")

    def test_redis_data_volume_driver(self):
        """Test that redis_data volume has correct driver."""
        volumes = self.config['volumes']
        redis_vol = volumes['redis_data']
        
        self.assertIn('driver', redis_vol)
        self.assertEqual(redis_vol['driver'], 'local',
                        "redis_data should use local driver")

    def test_environment_variable_defaults(self):
        """Test that services use environment variables with sensible defaults."""
        # Test PostgreSQL
        db_env = self.config['services']['db']['environment']
        for key, value in db_env.items():
            if isinstance(value, str) and ':-' in value:
                # Has default value
                self.assertTrue(value.startswith('${') and ':-' in value,
                              f"{key} should have format ${{VAR:-default}}")

    def test_port_mappings_uniqueness(self):
        """Test that port mappings don't conflict."""
        used_ports = set()
        services = self.config['services']
        
        for service_name, service_config in services.items():
            if 'ports' in service_config:
                for port_mapping in service_config['ports']:
                    # Extract host port
                    if isinstance(port_mapping, str):
                        host_port = port_mapping.split(':')[0]
                        self.assertNotIn(host_port, used_ports,
                                       f"Port {host_port} is used by multiple services")
                        used_ports.add(host_port)

    def test_redis_image_version_format(self):
        """Test Redis image version follows expected format."""
        redis_service = self.config['services']['redis']
        image = redis_service['image']
        
        # Should be redis/redis-stack-server with version
        self.assertTrue(image.startswith('redis/redis-stack-server'),
                       "Redis image should be from redis/redis-stack-server")
        self.assertTrue(':' in image,
                       "Redis image should include version tag")

    def test_postgres_image_version_format(self):
        """Test PostgreSQL uses specific version (not latest)."""
        db_service = self.config['services']['db']
        image = db_service['image']
        
        # Should be postgres:17.0, not postgres:latest
        self.assertNotEqual(image, 'postgres:latest',
                          "PostgreSQL should use specific version, not 'latest'")
        self.assertTrue(re.match(r'postgres:\d+\.\d+', image),
                       "PostgreSQL version should be in format postgres:X.Y")

    def test_restart_policies_consistency(self):
        """Test that critical services have appropriate restart policies."""
        critical_services = ['db', 'redis', 'service-registry', 'zipkin']
        
        for service_name in critical_services:
            service = self.config['services'][service_name]
            self.assertIn('restart', service,
                         f"Service {service_name} should have restart policy")
            self.assertEqual(service['restart'], 'unless-stopped',
                           f"Service {service_name} should have 'unless-stopped' policy")

    def test_volume_persistence_configuration(self):
        """Test that data volumes are properly configured for persistence."""
        volumes = self.config['volumes']
        
        # Both data volumes should exist
        for vol_name in ['postgres_data', 'redis_data']:
            self.assertIn(vol_name, volumes,
                         f"Volume {vol_name} must be defined for data persistence")

    def test_service_dependencies_order(self):
        """Test that service dependencies form a valid DAG (no cycles)."""
        services = self.config['services']
        
        def has_cycle(service_name, visited, rec_stack):
            visited.add(service_name)
            rec_stack.add(service_name)
            
            if service_name in services and 'depends_on' in services[service_name]:
                for dep in services[service_name]['depends_on']:
                    if dep not in visited:
                        if has_cycle(dep, visited, rec_stack):
                            return True
                    elif dep in rec_stack:
                        return True
            
            rec_stack.remove(service_name)
            return False
        
        visited = set()
        for service_name in services:
            if service_name not in visited:
                rec_stack = set()
                self.assertFalse(has_cycle(service_name, visited, rec_stack),
                               "Service dependencies should not contain cycles")


class TestDockerComposeEdgeCases(unittest.TestCase):
    """Test edge cases and potential issues in docker-compose configuration."""

    @classmethod
    def setUpClass(cls):
        """Load the docker-compose.yml file."""
        compose_file = Path(__file__).parent.parent / 'docker-compose.yml'
        with open(compose_file, 'r') as f:
            cls.config = yaml.safe_load(f)

    def test_no_empty_service_definitions(self):
        """Test that no service has an empty configuration."""
        services = self.config['services']
        for service_name, service_config in services.items():
            self.assertTrue(service_config,
                          f"Service {service_name} should not have empty configuration")
            self.assertIsInstance(service_config, dict,
                                f"Service {service_name} configuration should be a dict")

    def test_volume_references_are_valid(self):
        """Test that volume references in services match defined volumes."""
        defined_volumes = set(self.config['volumes'].keys())
        services = self.config['services']
        
        for service_name, service_config in services.items():
            if 'volumes' in service_config:
                for volume in service_config['volumes']:
                    if isinstance(volume, str) and ':' in volume:
                        volume_name = volume.split(':')[0]
                        # If it's a named volume (not a bind mount)
                        if not volume_name.startswith('.') and not volume_name.startswith('/'):
                            self.assertIn(volume_name, defined_volumes,
                                        f"Volume {volume_name} used by {service_name} must be defined")

    def test_container_names_are_unique(self):
        """Test that container names are unique across services."""
        container_names = []
        services = self.config['services']
        
        for service_name, service_config in services.items():
            if 'container_name' in service_config:
                name = service_config['container_name']
                self.assertNotIn(name, container_names,
                               f"Container name {name} is duplicated")
                container_names.append(name)

    def test_environment_variables_syntax(self):
        """Test that environment variables follow correct syntax."""
        services = self.config['services']
        
        for service_name, service_config in services.items():
            if 'environment' in service_config:
                env = service_config['environment']
                if isinstance(env, dict):
                    for key, value in env.items():
                        # Keys should be valid environment variable names
                        self.assertTrue(re.match(r'^[A-Z_][A-Z0-9_]*$', key),
                                      f"Invalid env var name: {key} in {service_name}")
                elif isinstance(env, list):
                    for item in env:
                        self.assertIn('=', item,
                                    f"List-style env var should contain '=': {item}")

    def test_port_format_validity(self):
        """Test that port mappings follow valid format."""
        services = self.config['services']
        
        for service_name, service_config in services.items():
            if 'ports' in service_config:
                for port in service_config['ports']:
                    if isinstance(port, str):
                        # Should be in format "host:container" or just "port"
                        parts = port.split(':')
                        self.assertLessEqual(len(parts), 2,
                                           f"Invalid port format in {service_name}: {port}")
                        for part in parts:
                            self.assertTrue(part.isdigit(),
                                          f"Port should be numeric: {part}")


class TestDockerComposeSecurityConsiderations(unittest.TestCase):
    """Test security-related aspects of docker-compose configuration."""

    @classmethod
    def setUpClass(cls):
        """Load the docker-compose.yml file."""
        compose_file = Path(__file__).parent.parent / 'docker-compose.yml'
        with open(compose_file, 'r') as f:
            cls.config = yaml.safe_load(f)

    def test_development_environment_markers(self):
        """Test that configuration is clearly marked as development-only."""
        # Redis should allow empty password only for development
        redis_env = self.config['services']['redis']['environment']
        if isinstance(redis_env, list):
            redis_env = {item.split('=')[0]: item.split('=')[1] for item in redis_env if '=' in item}
        
        self.assertIn('ALLOW_EMPTY_PASSWORD', redis_env,
                     "Redis empty password setting should be explicit")

    def test_no_hardcoded_production_credentials(self):
        """Test that no hardcoded production credentials are present."""
        config_str = yaml.dump(self.config).lower()
        
        # Should use environment variables with defaults
        sensitive_patterns = ['password: ', 'secret: ', 'token: ']
        for pattern in sensitive_patterns:
            if pattern in config_str:
                # If found, should be using environment variable syntax
                self.assertTrue('${' in config_str,
                              "Credentials should use environment variable syntax")


if __name__ == '__main__':
    unittest.main()