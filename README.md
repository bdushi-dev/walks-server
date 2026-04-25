# Contentful GraphQL Proxy

A Spring Boot middleware that sits between your Android app and Contentful, providing:

- **Caching** (Caffeine in-memory, 10 min TTL) to dramatically reduce response times
- **Simplified schema** — `sys { id }` is flattened to just `id: ID!`, locale args removed
- **GraphiQL UI** at `/graphiql` for testing
- **Cache management** REST endpoints
- **Clean error handling** with proper GraphQL error responses

## Architecture

```
Android App ──► Spring Boot Proxy ──► Contentful GraphQL API
                    │
                    └── Caffeine Cache (in-memory, per query type)
```

## Quick Start

### 1. Configure environment variables

```bash
export CONTENTFUL_SPACE_ID=your_space_id
export CONTENTFUL_ACCESS_TOKEN=your_access_token
export CONTENTFUL_ENVIRONMENT=master          # optional, default: master
export CONTENTFUL_DEFAULT_LOCALE=en-US        # optional, default: en-US
```

Or edit `src/main/resources/application.yml` directly.

### 2. Run

```bash
./mvnw spring-boot:run
```

Server starts on **http://localhost:8080**

### 3. Open GraphiQL

Navigate to **http://localhost:8080/graphiql** to explore and test the simplified schema.

---

## Simplified Schema vs Contentful Schema

| Contentful (complex) | This Proxy (simplified) |
|---|---|
| `sys { id }` everywhere | `id: ID!` directly on the type |
| `title(locale: String)` | `title: String!` (locale from config) |
| `imagesCollection { items { ... } }` | `images: [Asset!]!` |
| `categoriesCollection { items { ... } }` | `categories: [Category!]!` |
| `pointOfInterestCollection(locale, where)` | `pointOfInterests: [PointOfInterest!]!` |

### Example: Old Android query
```graphql
query {
  tour(locale: "en-US", id: "abc123") {
    sys { id }
    title(locale: "en-US")
    pointOfInterestCollection(locale: "en-US") {
      items {
        sys { id }
        title(locale: "en-US")
      }
    }
  }
}
```

### Example: New Android query (via this proxy)
```graphql
query {
  tour(id: "abc123") {
    id
    title
    pointOfInterests {
      id
      title
    }
  }
}
```

---

## Available Queries

```graphql
tour(id: String!): Tour!
tours(ids: [String!]): [Tour!]!

pointOfInterest(id: String!): PointOfInterest!
pointOfInterests(limit: Int, categoryName: String): [PointOfInterest!]!

region(id: String!): Region!
regions: [Region!]!

category(id: String!): Category!
categories: [Category!]!

audioGuide(id: String!): AudioGuide!

onboardings: [Onboarding!]!

home(id: String!): Home!
homes: [Home!]!
```

---

## Cache Management

### List caches
```
GET /admin/cache
```

### Evict all caches
```
DELETE /admin/cache
```

### Evict specific cache
```
DELETE /admin/cache/{name}
# e.g. DELETE /admin/cache/tour
```

### Evict specific key
```
DELETE /admin/cache/{name}/{key}
# e.g. DELETE /admin/cache/tour/abc123
```

Cache names: `tour`, `pointOfInterest`, `region`, `category`, `audioGuide`, `onboarding`, `home`

---

## Cache Configuration

Edit `application.yml` to tune cache behavior:

```yaml
spring:
  cache:
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=10m
```

Or change per-type TTL by giving each `CacheManager` bean its own spec in `CacheConfig.java`.

---

## Production Notes

1. **Secure `/admin/cache`** with Spring Security
2. **Scale**: Replace Caffeine with Redis for multi-instance deployments:
   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```
3. **Health check**: `GET /actuator/health`
4. **Metrics**: `GET /actuator/metrics`
