# YS Chemical Agencies — Spring Boot Version

Same app, same features, same frontend — rebuilt with Spring Boot instead of
plain Servlets. The biggest practical win: **no separate Tomcat install, no
WAR file, no artifact configuration.** Everything runs from one `.jar`.

## What's different from the Servlet version

| | Servlet version | Spring Boot version |
|---|---|---|
| Run locally | Install Tomcat + Smart Tomcat plugin + configure artifact | Just click ▶️ Run on `YsInventoryApplication.java` |
| Database tables | You run `schema.sql` yourself | Hibernate creates/updates tables automatically from the `@Entity` classes |
| JSON parsing | Hand-rolled `JsonParser`/`JsonUtil` | Jackson does it automatically (built into Spring Web) |
| Password hashing | Custom salted SHA-256 | Real BCrypt (industry standard), via `spring-security-crypto` |
| Deploy to cloud | Multi-stage Dockerfile (Maven build → copy WAR into Tomcat) | 4-line Dockerfile — `java -jar app.jar` |
| Bill line-items | Stored as one JSON blob column | Real `bill_items` table with a foreign key — properly queryable |

## Running it locally in IntelliJ

1. **Open** the `ys-inventory-springboot` folder in IntelliJ (File → Open, pick the folder with `pom.xml`)
2. Wait for Maven to finish importing (bottom-right progress bar)
3. If your local MySQL password isn't blank, either:
   - Edit the default in `application.properties`, or
   - Add a Run Configuration environment variable `DB_PASSWORD=yourpassword`
4. Open `src/main/java/com/ys/YsInventoryApplication.java`
5. Click the green ▶️ next to `public static void main(...)`
6. Wait for the console to show `Tomcat started on port 8080` (yes, Spring Boot still uses Tomcat *internally* — you just never have to install or configure it yourself)
7. Open `http://localhost:8080/` in your browser

No artifact setup, no deployment directory, no context path — it just runs.

## Deploying to Render (or any cloud)

Since everything is now a self-contained Docker image, the exact same steps
as before apply, but simpler:

1. Push this folder to GitHub
2. On Render: New → Web Service → connect your repo → Render detects the `Dockerfile` automatically
3. Add environment variables in Render's dashboard:
   - `DB_URL` = `jdbc:mysql://<your-db4free-host>:3306/<dbname>?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`
   - `DB_USER` = your db4free username
   - `DB_PASSWORD` = your db4free password
4. Deploy — Render builds the Docker image and runs `java -jar app.jar` automatically
5. You do **not** need to run `schema.sql` anywhere — the first time the app starts against an empty database, Hibernate creates all 5 tables (`firms`, `products`, `customers`, `bills`, `bill_items`) itself.

## Folder structure

```
ys-inventory-springboot/
├── pom.xml
├── Dockerfile
└── src/main/
    ├── java/com/ys/
    │   ├── YsInventoryApplication.java   ← run this to start everything
    │   ├── model/          Firm, Product, Customer, Bill, BillItem (@Entity)
    │   ├── repository/     FirmRepository, ProductRepository, CustomerRepository, BillRepository
    │   ├── service/         AuthService, ProductService, CustomerService, BillService
    │   ├── controller/      AuthController, ProductController, CustomerController, BillController
    │   ├── security/        AuthInterceptor (session check)
    │   └── config/          WebConfig (registers the interceptor)
    └── resources/
        ├── application.properties
        └── static/index.html   ← same UI, unchanged
```

## A note on the `@Data` / Lombok annotations

You'll notice the model classes are much shorter than the Servlet version's —
no manual getters/setters. That's Lombok's `@Data` annotation generating them
at compile time. If your IDE shows red squiggly lines under `.getName()` /
`.setName()` calls before you've built once, that's normal — build the
project once and IntelliJ's Lombok plugin (usually bundled) picks it up.
