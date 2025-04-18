# 📧 service-mail

Microserviço responsável pelo envio de **e-mails transacionais** na plataforma. Utiliza **Kafka** para escutar eventos e **Thymeleaf** para renderização de templates HTML.

---

## 📌 Objetivo

Processar mensagens Kafka relacionadas a notificações e enviar e-mails com conteúdo personalizado e dinâmico.

---

## ⚙️ Stack Tecnológica

- **Linguagem:** Java
- **Framework:** Spring Boot
- **Mensageria:** Kafka
- **Template engine:** Thymeleaf
- **Gateway de entrada:** Spring Gateway

---

## 📁 Arquitetura

- Template HTML com Thymeleaf
- Serviços desacoplados para envio de e-mail e leitura de templates

---

## 🔗 Fluxo de Mensagens Kafka

- ✅ KafkaListener escuta tópicos como `mail.welcome`, `mail.change-password`, etc.
- ✅ O payload é convertido em DTOs, renderizado em um template `.html` e enviado via SMTP (JavaMail)

---

## 🧪 Testes

- ✅ **Unitários:**
    - Serviços de envio de e-mail
    - Manipulação de templates

- ✅ **Integração:**
    - Listeners Kafka com mensagens simuladas

Para rodar os testes:
```bash
./mvnw test
