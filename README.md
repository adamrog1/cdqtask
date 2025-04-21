## How to Use the API

After running the app (see Getting Started), you can interact with the API using tools like Postman or Curl. Here's how it works:

1. **Create a Person**
   - Endpoint: `POST /api/person`
   - Request Body:
     ```json
     {
       "name": "Mike",
       "surname": "Rogalski",
       "birthDate": "2024-01-12"
     }
     ```
   - Response includes a `taskId` which starts a background classification task.

2. **Update a Person**
   - Endpoint: `PATCH /api/person/{id}`
   - Request Body:
     ```json
     {
       "name": "Tomasz",
       "surname": "Smith",
       "birthDate": "2024-01-12",
       "company": "thr"
     }
     ```
   - Also returns a new `taskId` and starts a task.

3. **Track Task Progress**
   - Use the `taskId` from the create/update response.
   - Endpoint: `GET /api/task/{taskId}`
   - Response shows current status, progress percentage, and field-level classification results like:
     ```json
     {
       "status": "COMPLETED",
       "taskProgress": "100%",
       "result": {
         "name": "HIGH",
         "surname": "HIGH",
         "birthDate": "HIGH",
         "company": "HIGH"
       }
     }
     ```

4. **List All Persons**
   - `GET /api/person/getAll` returns all stored persons with self and update links.

5. **List All Tasks**
   - `GET /api/task/getAll` returns all classification tasks.

- Tasks compare current vs. previous person data and classify field similarity (`HIGH`, `LOW`, `ADDED`).
- All endpoints support HAL-style `_links` for easy navigation.
- Caching is enabled via Redis — tweak settings in `application.properties` to suit your environment.
- For more details check openapi definition
