# Migration Issues Found by Test Suite

## ⚠️ Issue Detected

The test suite has **successfully identified a configuration issue** in the Valkey → Redis migration!

### Problem

**Line 35** in `docker-compose.yml`:
```yaml
- VALKEY_DISABLE_COMMANDS=FLUSHDB,FLUSHALL
```

This environment variable is specific to Valkey and **will not work** with Redis Stack Server.

### Impact

- The environment variable will be ignored by Redis Stack Server
- The intended security measure (disabling dangerous commands) will not be enforced
- This could lead to accidental data loss in development

### Solution Options

#### Option 1: Use Redis Configuration File (Recommended)

Create a redis.conf file and mount it:

```yaml
redis:
  image: redis/redis-stack-server:latest
  volumes:
    - 'redis_data:/bitnami/redis/data'
    - './redis.conf:/redis-stack.conf:ro'
  command: redis-stack-server /redis-stack.conf
```

**redis.conf:**
```conf
# Disable dangerous commands
rename-command FLUSHDB ""
rename-command FLUSHALL ""
```

#### Option 2: Use Redis Command-Line Arguments

```yaml
redis:
  image: redis/redis-stack-server:latest
  command: >
    redis-stack-server
    --rename-command FLUSHDB ""
    --rename-command FLUSHALL ""
```

#### Option 3: Remove the Setting (Development Only)

If this is purely for development and data loss is acceptable:

```yaml
redis:
  image: redis/redis-stack-server:latest
  environment:
    - ALLOW_EMPTY_PASSWORD=yes
  # Remove VALKEY_DISABLE_COMMANDS - not applicable to Redis Stack
```

### Recommended Action

For this modulith-learning development environment, **Option 3** is recommended:

1. Remove the `VALKEY_DISABLE_COMMANDS` line
2. Document that this is a development environment where FLUSHDB/FLUSHALL are available
3. Add a comment explaining the removal

### Updated Configuration

```yaml
# Redisサーバー（Redis Stack）
redis:
  container_name: ${PROJECT_NAME:-default}-redis_server
  image: redis/redis-stack-server:latest
  environment:
    # 開発環境でのみ空のパスワードを許可
    - ALLOW_EMPTY_PASSWORD=yes
    # Note: FLUSHDB/FLUSHALL are enabled in development
    # In production, disable these commands using redis.conf
  ports:
    - '6379:6379'
  volumes:
    - 'redis_data:/data'  # Redis Stack uses /data by default
  depends_on:
    - db
```

### Test Suite Value

✅ **This demonstrates the value of the test suite!**

The automated tests caught a configuration error that could have caused:
- Silent failures (environment variable ignored)
- Security gaps (intended restrictions not applied)
- Confusion for developers (expecting one behavior, getting another)

## Action Required

1. Decide on the appropriate solution (recommend Option 3 for development)
2. Update `.devcontainer/docker-compose.yml`
3. Re-run tests to verify the fix
4. Update comments to reflect the migration

## Additional Finding

The volume path for Redis Stack Server should be `/data` (not `/bitnami/redis/data`):

**Current:**
```yaml
- 'redis_data:/bitnami/redis/data'
```

**Should be:**
```yaml
- 'redis_data:/data'
```

Redis Stack Server uses `/data` as its default data directory, not `/bitnami/redis/data`.
