## Done so far:

user record management:
  - RESTful using spring-data-rest HATEOAS, basis for generally accepted REST API:
    - [x] create
    - [x] update
    - [x] delete
  - Validation
    - [x] JSR303 validation on rest controller
    - [x] Duplicate-Email-Check
  - security:
    - [x] encrypt password using service
    - [ ] http authentication (not required)

Notes:
  - plain spring-data-rest service, no customization considered necessary

testing:
  - [x] unit tests user repository & services
  - [x] integration tests

## Usage:

Please active the "dev" profile to browse the REST API easily

    $ mvn spring-boot:run -P dev

