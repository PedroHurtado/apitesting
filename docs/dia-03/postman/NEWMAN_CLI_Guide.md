# Automatización con Newman (CLI)

Newman es el ejecutor de colecciones de Postman desde línea de comandos. Ideal para integración continua (CI/CD).

## 📦 Instalación

```bash
# Instalar Newman globalmente
npm install -g newman

# Verificar instalación
newman --version
```

## 🚀 Ejecutar la Colección

### Ejecución Básica

```bash
newman run UpdatePizza_Tests.postman_collection.json
```

### Con Variables de Entorno

#### Opción 1: Archivo de Environment

Crea un archivo `pizza-environment.json`:

```json
{
  "name": "Pizza API Environment",
  "values": [
    {
      "key": "baseUrl",
      "value": "https://api.mipizzeria.com",
      "enabled": true
    },
    {
      "key": "existingPizzaId",
      "value": "123e4567-e89b-12d3-a456-426614174000",
      "enabled": true
    }
  ]
}
```

Ejecuta con el environment:

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --environment pizza-environment.json
```

#### Opción 2: Variables Globales

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --global-var "baseUrl=https://api.mipizzeria.com" \
  --global-var "existingPizzaId=123e4567-e89b-12d3-a456-426614174000"
```

#### Opción 3: Variables de Colección (Override)

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --env-var "baseUrl=https://api.mipizzeria.com" \
  --env-var "existingPizzaId=123e4567-e89b-12d3-a456-426614174000"
```

## 📊 Opciones de Reporte

### Reporte en Consola (por defecto)

```bash
newman run UpdatePizza_Tests.postman_collection.json
```

### Reporte HTML

```bash
# Instalar reporter HTML
npm install -g newman-reporter-html

# Ejecutar con reporte HTML
newman run UpdatePizza_Tests.postman_collection.json \
  --reporters cli,html \
  --reporter-html-export report.html
```

### Reporte JSON

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --reporters cli,json \
  --reporter-json-export report.json
```

### Reporte JUnit (para CI/CD)

```bash
# Instalar reporter JUnit
npm install -g newman-reporter-junitfull

# Ejecutar con reporte JUnit
newman run UpdatePizza_Tests.postman_collection.json \
  --reporters cli,junitfull \
  --reporter-junitfull-export results.xml
```

## 🔧 Opciones Avanzadas

### Ejecutar con Timeout

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --timeout-request 10000  # 10 segundos por request
```

### Ejecutar con Reintentos

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --bail  # Detener en el primer error
```

### Ejecutar Requests Específicos

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --folder "Validaciones de Name"
```

### Verbose Output

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --verbose
```

### Deshabilitar Verificación SSL (desarrollo)

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --insecure
```

## 🔄 Integración CI/CD

### GitHub Actions

Crea `.github/workflows/api-tests.yml`:

```yaml
name: API Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
    
    - name: Install Newman
      run: npm install -g newman newman-reporter-htmlextra
    
    - name: Run API Tests
      env:
        BASE_URL: ${{ secrets.API_BASE_URL }}
        PIZZA_ID: ${{ secrets.EXISTING_PIZZA_ID }}
      run: |
        newman run UpdatePizza_Tests.postman_collection.json \
          --global-var "baseUrl=$BASE_URL" \
          --global-var "existingPizzaId=$PIZZA_ID" \
          --reporters cli,htmlextra \
          --reporter-htmlextra-export testResults/report.html
    
    - name: Upload Test Results
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: test-results
        path: testResults/
```

### GitLab CI

Crea `.gitlab-ci.yml`:

```yaml
stages:
  - test

api_tests:
  stage: test
  image: node:18
  before_script:
    - npm install -g newman newman-reporter-html
  script:
    - |
      newman run UpdatePizza_Tests.postman_collection.json \
        --global-var "baseUrl=$API_BASE_URL" \
        --global-var "existingPizzaId=$EXISTING_PIZZA_ID" \
        --reporters cli,html \
        --reporter-html-export report.html
  artifacts:
    when: always
    paths:
      - report.html
    reports:
      junit: report.xml
```

### Azure DevOps

Crea `azure-pipelines.yml`:

```yaml
trigger:
- main

pool:
  vmImage: 'ubuntu-latest'

steps:
- task: NodeTool@0
  inputs:
    versionSpec: '18.x'
  displayName: 'Install Node.js'

- script: npm install -g newman newman-reporter-junitfull
  displayName: 'Install Newman'

- script: |
    newman run UpdatePizza_Tests.postman_collection.json \
      --global-var "baseUrl=$(API_BASE_URL)" \
      --global-var "existingPizzaId=$(EXISTING_PIZZA_ID)" \
      --reporters cli,junitfull \
      --reporter-junitfull-export results.xml
  displayName: 'Run API Tests'

- task: PublishTestResults@2
  inputs:
    testResultsFiles: '**/results.xml'
    testRunTitle: 'API Tests'
  condition: always()
```

### Jenkins Pipeline

Crea `Jenkinsfile`:

```groovy
pipeline {
    agent any
    
    environment {
        API_BASE_URL = credentials('api-base-url')
        EXISTING_PIZZA_ID = credentials('existing-pizza-id')
    }
    
    stages {
        stage('Install Newman') {
            steps {
                sh 'npm install -g newman newman-reporter-htmlextra'
            }
        }
        
        stage('Run API Tests') {
            steps {
                sh '''
                    newman run UpdatePizza_Tests.postman_collection.json \
                      --global-var "baseUrl=${API_BASE_URL}" \
                      --global-var "existingPizzaId=${EXISTING_PIZZA_ID}" \
                      --reporters cli,htmlextra \
                      --reporter-htmlextra-export testResults/report.html
                '''
            }
        }
    }
    
    post {
        always {
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'testResults',
                reportFiles: 'report.html',
                reportName: 'API Test Report'
            ])
        }
    }
}
```

## 🐳 Docker

### Dockerfile para Tests

```dockerfile
FROM node:18-alpine

# Instalar Newman
RUN npm install -g newman newman-reporter-htmlextra

# Copiar colección
WORKDIR /tests
COPY UpdatePizza_Tests.postman_collection.json .

# Ejecutar tests
CMD ["newman", "run", "UpdatePizza_Tests.postman_collection.json", \
     "--reporters", "cli,htmlextra", \
     "--reporter-htmlextra-export", "/results/report.html"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  api-tests:
    build: .
    environment:
      - BASE_URL=${API_BASE_URL:-https://localhost:7000}
      - PIZZA_ID=${EXISTING_PIZZA_ID}
    volumes:
      - ./results:/results
    command: >
      newman run UpdatePizza_Tests.postman_collection.json
      --global-var "baseUrl=${BASE_URL}"
      --global-var "existingPizzaId=${PIZZA_ID}"
      --reporters cli,htmlextra
      --reporter-htmlextra-export /results/report.html
```

Ejecutar:

```bash
docker-compose run api-tests
```

## 📈 Monitoreo Continuo

### Newman con Cron (Linux/Mac)

```bash
# Editar crontab
crontab -e

# Ejecutar tests cada hora
0 * * * * cd /path/to/tests && newman run UpdatePizza_Tests.postman_collection.json --global-var "baseUrl=https://api.mipizzeria.com" --reporters cli,json --reporter-json-export /path/to/logs/$(date +\%Y\%m\%d_\%H\%M).json
```

### Script de Monitoreo

Crea `monitor.sh`:

```bash
#!/bin/bash

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
LOG_DIR="./logs"
REPORT_FILE="$LOG_DIR/report_$TIMESTAMP.html"

mkdir -p $LOG_DIR

newman run UpdatePizza_Tests.postman_collection.json \
  --global-var "baseUrl=https://api.mipizzeria.com" \
  --global-var "existingPizzaId=123e4567-e89b-12d3-a456-426614174000" \
  --reporters cli,html \
  --reporter-html-export "$REPORT_FILE"

EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    echo "Tests failed! Check report: $REPORT_FILE"
    # Enviar notificación (email, Slack, etc.)
    curl -X POST https://hooks.slack.com/services/YOUR/WEBHOOK/URL \
      -H 'Content-Type: application/json' \
      -d "{\"text\":\"⚠️ API Tests Failed! Report: $REPORT_FILE\"}"
fi

exit $EXIT_CODE
```

## 🎯 Mejores Prácticas

1. **Usa variables de entorno para datos sensibles**
   ```bash
   export BASE_URL="https://api.mipizzeria.com"
   export PIZZA_ID="your-guid-here"
   ```

2. **Versiona tu colección con Git**
   ```bash
   git add UpdatePizza_Tests.postman_collection.json
   git commit -m "Update API tests"
   ```

3. **Mantén reportes históricos**
   ```bash
   newman run ... --reporter-html-export reports/$(date +%Y%m%d).html
   ```

4. **Ejecuta tests en diferentes entornos**
   ```bash
   # Development
   newman run ... --global-var "baseUrl=https://dev.api.com"
   
   # Staging
   newman run ... --global-var "baseUrl=https://staging.api.com"
   
   # Production
   newman run ... --global-var "baseUrl=https://api.com"
   ```

## 🔍 Debugging

### Ver requests completos

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --verbose \
  --reporter-cli-no-assertions \
  --reporter-cli-no-console
```

### Exportar datos de requests

```bash
newman run UpdatePizza_Tests.postman_collection.json \
  --reporters cli,json \
  --reporter-json-export debug.json

# Ver requests enviados
cat debug.json | jq '.run.executions[].request'
```

## 📚 Recursos Adicionales

- [Newman Documentation](https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/)
- [Newman Reporters](https://www.npmjs.com/search?q=newman-reporter)
- [Postman CLI vs Newman](https://learning.postman.com/docs/postman-cli/postman-cli-overview/)
