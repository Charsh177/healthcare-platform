# Contributing to Healthcare Platform

## Code of Conduct
- Be respectful and inclusive
- Focus on constructive feedback
- Help others succeed
- Report security issues privately

## Getting Started

1. **Fork the repository**
2. **Create a feature branch:** `git checkout -b feature/your-feature`
3. **Make your changes**
4. **Write tests** for new functionality
5. **Commit with clear messages:** `git commit -m "Add patient search feature"`
6. **Push to branch:** `git push origin feature/your-feature`
7. **Create Pull Request** with description

## Development Workflow

### Before You Code
- Read `/docs/DEVELOPMENT.md`
- Run tests: `mvn test`
- Check code style: `mvn checkstyle:check`

### While Coding
- Follow [Java Naming Conventions](#naming-conventions)
- Write meaningful variable names
- Add comments for complex logic
- Keep methods small and focused

### After Coding
- Run all tests: `mvn test`
- Check code quality: `mvn clean package`
- Update documentation
- Create comprehensive Pull Request

## Naming Conventions

```java
// Classes
public class PatientService { }

// Methods
public void registerPatient() { }

// Variables
private String patientName;

// Constants
private static final String STATUS_ACTIVE = "ACTIVE";

// Packages
com.healthcare.patient.service
```

## Pull Request Guidelines

1. **Clear title:** `Add patient search by name`
2. **Detailed description:** What, why, how
3. **Reference issues:** Fixes #123
4. **Test results:** Attach test results
5. **Documentation:** Update if needed

## Testing Requirements

- Minimum 80% code coverage
- Unit tests for business logic
- Integration tests for API endpoints
- Test naming: `test<Feature><Scenario>`

```java
@Test
void testCreatePatient_WithValidData_ShouldSucceed() {
    // Arrange
    PatientRequest request = new PatientRequest(...);
    
    // Act
    PatientResponse response = patientService.createPatient(request);
    
    // Assert
    assertNotNull(response.getPatientId());
    assertEquals("John", response.getFirstName());
}
```

## Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Examples:
```
feat(patient): add patient search by name
fix(auth): correct JWT token validation
docs: update API documentation
```

## Code Style

- **Indentation:** 4 spaces
- **Line length:** Max 120 characters
- **Braces:** Opening brace on same line
- **Comments:** Use Javadoc for public methods

```java
/**
 * Registers a new patient in the system.
 *
 * @param request the patient registration request
 * @return the created patient response
 * @throws IllegalArgumentException if data is invalid
 */
public PatientResponse createPatient(PatientRequest request) {
    // implementation
}
```

## Security

- Never commit credentials
- Use environment variables
- Don't log sensitive data
- Validate all inputs
- Use prepared statements

## Performance Considerations

- Use database indexes for frequent queries
- Implement caching for read-heavy operations
- Paginate large result sets
- Profile before optimizing

## Documentation

Update when:
- Adding new endpoints
- Changing API contracts
- Adding new services
- Modifying database schema

Files to update:
- `README.md`
- `docs/API.md`
- `docs/DEVELOPMENT.md`
- Inline code comments

## Review Process

1. **Automated checks** - Tests, linting, coverage
2. **Code review** - Design, maintainability, standards
3. **Approval** - Minimum 2 approvals required
4. **Merge** - Squash and merge to main

## Reporting Issues

1. Check if issue already exists
2. Provide clear reproduction steps
3. Include environment info
4. Attach logs/screenshots if relevant
5. Label appropriately (bug, enhancement, etc.)

## Releases

- Semantic versioning: MAJOR.MINOR.PATCH
- Update CHANGELOG
- Tag release in Git
- Create GitHub release notes

## Questions?

- Check existing documentation
- Search closed issues
- Ask in discussions
- Contact maintainers

---

Thank you for contributing! 🎉
