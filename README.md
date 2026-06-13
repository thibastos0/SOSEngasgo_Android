# 🚑 SOS Engasgo (Android)

> **Status do Projeto:** 🏗️ Em Desenvolvimento  
> Aplicativo dedicado a fornecer orientações rápidas sobre manobras de desengasgo e acionamento de emergência.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Telegram](https://img.shields.io/badge/Telegram-2CA5E0?style=for-the-badge&logo=telegram&logoColor=white)

---

## 📋 Sobre o Projeto
O **SOS Engasgo** é um projeto acadêmico (Projeto Integrador) da **Fatec Indaiatuba**. O objetivo é oferecer um guia visual interativo para primeiros socorros em casos de obstrução de vias aéreas (Manobra de Heimlich).

Esta versão mobile prioriza a agilidade no acesso à informação e o registro de acionamentos para facilitar o socorro.

## 🗺️ Roadmap de Desenvolvimento
- [x] Configuração inicial do projeto Android Studio.
- [x] Implementação da **Tela de Boas-vindas**.
- [x] Integração com **Firebase Authentication** (E-mail/Senha e Google Login).
- [x] Implementação de animações visuais (Heartbeat).
- [x] Implementação de banco de dados local (**Room**) para histórico de acionamentos.
- [x] Integração com **Mapas (OpenStreetMap)** e **Geocodificação** para localização do incidente.
- [x] Interface de **Acionamento de Emergência** interativa.
- [x] Integração com **Telegram Bot** para notificações de socorro em tempo real.
- [ ] Desenvolvimento da interface detalhada de orientações (Passo a passo visual).
- [ ] Implementação de sistema de chamadas de emergência rápida (Discagem direta 192/193).
- [ ] Refatoração para arquitetura MVVM.

## 🛠️ Tecnologias e Ferramentas
* **Linguagem:** Java.
* **Interface:** XML Layouts (Material Design 3).
* **Backend:** Firebase (Auth) e API REST (Render).
* **Notificações:** Integração com Telegram Bot.
* **Banco de Dados Local:** Room Persistence Library.
* **Mapas:** osmdroid (OpenStreetMap).
* **Rede:** OkHttp para consumo de API.
* **Min SDK:** API 24 (Android 7.0+).
* **Ferramenta de Build:** Gradle (KTS).
* **IDE:** Android Studio.
* **Versionamento:** Git & GitHub.

## 🚀 Como Executar o Projeto
Para colaborar ou testar o aplicativo em sua máquina:

1. **Clone o repositório:**
   `git clone https://github.com/thibastos0/SOSEngasgo_Android.git`
2. **Abra no Android Studio:** Selecione `File > Open` e escolha a pasta do projeto clonado.
3. **Sincronize o Gradle:** O Android Studio baixará automaticamente as dependências.
4. **Firebase:** É necessário adicionar o arquivo `google-services.json` na pasta `/app` para as funcionalidades de login.
5. **API de Emergência:** Para testar o acionamento com o bot do Telegram, o serviço hospedado no Render ([https://sosengasgo.onrender.com/](https://sosengasgo.onrender.com/)) deve estar ativo.
6. **Execute:** Use um Emulador ou dispositivo físico (API 24+).

## 🤝 Metodologia de Trabalho (Fluxo de Branch e Pull Request)
Para garantir a estabilidade do código, **não é permitido dar Push direto na `main`**. Siga este fluxo:

1. **PULL antes de tudo:** Certifique-se de estar na branch `main` e dê um `git pull`.
2. **Crie uma BRANCH:** No Android Studio, `Git: main` > `New Branch`. Use nomes descritivos (ex: `feature-mapas`, `fix-login-layout`).
3. **Desenvolva e Commite:** Faça suas alterações e dê o `Commit` na sua branch.
4. **PUSH da Branch:** Dê o `Push` da sua branch para o GitHub.
5. **Abra um PULL REQUEST (PR):** Descreva o que você fez e solicite a revisão.
6. **REVISÃO e MERGE:** Após a revisão, o Merge será feito para a `main`.

---

## 👥 Equipe
* **Thiago Bastos** [thibastos0](https://github.com/thibastos0)
* **Gustavo Martins** [GustaMMartins](https://github.com/GustaMMartins)
* **Lucas Corrêa** [Lucas-C3p](https://github.com/Lucas-C3p)
* **Ruylis Bialta** [ruylis](https://github.com/ruylis)
* **Gustavo Bravo** [wgustw](https://github.com/wgustw)

---
*Este projeto tem fins educacionais e busca disseminar conhecimento sobre primeiros socorros.*
