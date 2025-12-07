"""
Integration tests for docker-compose.yml configuration.

These tests validate the integration aspects and cross-service configurations
of the Docker Compose setup.
"""

import unittest
import yaml
from pathlib import Path
from typing import Set, List


class TestServiceIntegration(unittest.TestCase):
    """Test integration between services in docker-compose.yml."""

    @classmethod
    def setUpClass(cls):
        """Load the docker-compose.yml file."""
        compose_file = Path(__file__).parent.parent / 'docker-compose.yml'
        with open(compose_file, 'r') as f:
            cls.config = yaml.safe_load(f)

    def test_dependency_resolution_order(self):
        """Test that dependency resolution order is valid."""
        services = self.config['services']
        
        def get_startup_order(service_name: str, visited: Set[str], order: List[str]):
            """Recursively determine startup order."""
            if service_name in visited:
                return
            visited.add(service_name)
            
            if service_name in services and 'depends_on' in services[service_name]:
                for dep in services[service_name]['depends_on']:
                    get_startup_order(dep, visited, order)
            
            order.append(service_name)
        
        # Get startup order for app service (most dependent)
        visited = set()
        order = []
        get_startup_order('app', visited, order)
        
        # db should come before redis (redis depends on db)
        db_idx = order.index('db')
        redis_idx = order.index('redis')
        self.assertLess(db_idx, redis_idx,
                       "db should start before redis")
        
        # service-registry should come before zipkin
        registry_idx = order.index('service-registry')
        zipkin_idx = order.index('zipkin')
        self.assertLess(registry_idx, zipkin_idx,
                       "service-registry should start before zipkin")
        
        # All infrastructure should come before app
        app_idx = order.index('app')
        self.assertLess(db_idx, app_idx, "db should start before app")
        self.assertLess(redis_idx, app_idx, "redis should start before app")
        self.assertLess(registry_idx, app_idx, "service-registry should start before app")
        self.assertLess(zipkin_idx, app_idx, "zipkin should start before app")

    def test_redis_connectivity_from_app(self):
        """Test that app can connect to redis service."""
        services = self.config['services']
        app_service = services['app']
        
        # App should depend on redis
        self.assertIn('depends_on', app_service)
        self.assertIn('redis', app_service['depends_on'],
                     "App should depend on redis for connectivity")


class TestConfigurationMigration(unittest.TestCase):
    """Test that migration from Valkey to Redis was completed correctly."""

    @classmethod
    def setUpClass(cls):
        """Load the docker-compose.yml file."""
        compose_file = Path(__file__).parent.parent / 'docker-compose.yml'
        with open(compose_file, 'r') as f:
            cls.config = yaml.safe_load(f)
            cls.raw_content = compose_file.read_text()

    def test_valkey_service_renamed_to_redis(self):
        """Test that valkey service was completely renamed to redis."""
        services = self.config['services']
        
        # Redis should exist
        self.assertIn('redis', services,
                     "Redis service should exist")
        
        # Valkey should not exist
        self.assertNotIn('valkey', services,
                        "Valkey service should not exist (should be renamed to redis)")

    def test_valkey_volume_renamed_to_redis_data(self):
        """Test that valkey_data volume was renamed to redis_data."""
        volumes = self.config['volumes']
        
        # redis_data should exist
        self.assertIn('redis_data', volumes,
                     "redis_data volume should exist")
        
        # valkey_data should not exist
        self.assertNotIn('valkey_data', volumes,
                        "valkey_data volume should not exist (should be renamed)")

    def test_app_dependency_updated_to_redis(self):
        """Test that app dependency was updated from valkey to redis."""
        app_service = self.config['services']['app']
        depends_on = app_service.get('depends_on', [])
        
        # Should depend on redis
        self.assertIn('redis', depends_on,
                     "App should depend on redis service")
        
        # Should not depend on valkey
        self.assertNotIn('valkey', depends_on,
                        "App should not depend on valkey service")


class TestPostgresMigration(unittest.TestCase):
    """Test PostgreSQL version migration from latest to 17.0."""

    @classmethod
    def setUpClass(cls):
        """Load the docker-compose.yml file."""
        compose_file = Path(__file__).parent.parent / 'docker-compose.yml'
        with open(compose_file, 'r') as f:
            cls.config = yaml.safe_load(f)

    def test_postgres_uses_specific_version(self):
        """Test that PostgreSQL uses version 17.0 instead of latest."""
        db_service = self.config['services']['db']
        image = db_service['image']
        
        # Should be postgres:17.0
        self.assertEqual(image, 'postgres:17.0',
                        "PostgreSQL should use version 17.0")
        
        # Should not be latest
        self.assertNotEqual(image, 'postgres:latest',
                          "PostgreSQL should not use 'latest' tag")


if __name__ == '__main__':
    unittest.main()