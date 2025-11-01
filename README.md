# 📱 Pokédex Android App — MVVM + Clean Architecture + Hilt

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-brightgreen)
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-✔-orange)
![Hilt](https://img.shields.io/badge/DI-Hilt-informational)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

<p align="center">
  <img src="https://raw.githubusercontent.com/AndersonPS94/NovaPokedex/main/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" alt="Logo da NovaPokedex" width="150"/>
</p>

---

Uma Pokédex moderna para Android, construída com **MVVM**, **Clean Architecture** e **Injeção de Dependências com Hilt**. O app consome dados da [PokéAPI](https://pokeapi.co) e apresenta listagem, detalhes completos dos Pokémons e recursos avançados, tudo seguindo boas práticas e arquitetura desacoplada.

---

## 🌟 Análise e Estrutura do Projeto

O projeto segue uma estrutura de módulos e pacotes bem definida, aderindo aos princípios de **Clean Architecture** (Arquitetura Limpa), o que facilita a manutenção, testes e escalabilidade.

| Camada | Pacotes Principais | Responsabilidade |
| :--- | :--- | :--- |
| **Domain** | `domain.model`, `domain.repository`, `domain.usecase` | Contém as regras de negócio e entidades centrais. |
| **Data** | `data.remote`, `data.local`, `data.repository`, `data.mapper`, `data.di` | Responsável pela fonte de dados (API e Banco de Dados Local) e pela implementação dos contratos de repositório. |
| **Presentation** | `presentation.ui`, `presentation.viewmodel` | Lida com a interface do usuário (Activities, Adapters) e o gerenciamento do estado da UI. |

## 🧪 Testes

O projeto possui uma boa cobertura de **Testes Unitários** focados na lógica de negócio (Use Cases) e na camada de apresentação (ViewModels), utilizando **JUnit 4**, **Mockito** e **Kotlin Coroutines Test**.

| Tipo de Teste | Cobertura | Ferramentas |
| :--- | :--- | :--- |
| **Unitários** | Boa (Lógica de Negócio e ViewModels) | JUnit 4, Mockito, Coroutines Test |
| **Instrumentados** | Baixa (Apenas exemplo) | AndroidX Test, Espresso |

**Oportunidade de Melhoria:** Aumentar a cobertura de **Testes Instrumentados** com Espresso para validar a interface do usuário.

## 🛠️ Stack Tecnológico

O projeto utiliza um *stack* moderno e robusto, alinhado com as melhores práticas do ecossistema Android:

| Categoria | Tecnologia | Uso Principal |
| :--- | :--- | :--- |
| **Linguagem** | Kotlin | Linguagem principal do app. |
| **Arquitetura** | MVVM + Clean Architecture | Separação de responsabilidades e manutenibilidade. |
| **DI** | Hilt (Dagger) | Injeção de Dependência. |
| **Networking** | Retrofit + OkHttp | Consumo da API [PokéAPI](https://pokeapi.co). |
| **Persistência** | Room Database | Cache local para modo offline. |
| **Assincronismo** | Coroutines + Flow | Programação assíncrona e reativa. |
| **Imagens** | Coil, Glide, Picasso | Carregamento e cache de imagens (Nota: Recomenda-se consolidar em apenas uma para otimização do APK). |
| **Animações** | Lottie | Animações ricas na interface. |

## 📦 Gerenciamento de Dependências

O gerenciamento de dependências é feito de forma centralizada e organizada através do **Gradle Version Catalogs (`libs.versions.toml`)**.

Este arquivo, localizado em `gradle/libs.versions.toml`, serve como a **área específica** para todas as dependências, garantindo:
*   **Centralização:** Todas as versões são definidas em um único local.
*   **Segurança de Tipo:** Referências seguras em tempo de compilação.
*   **Fácil Atualização:** Simplifica a manutenção das versões.

---

## ✅ Funcionalidades

* 🔍 Listagem dos Pokémons com nome, imagem e tipo.
* 📋 Tela de detalhes com:
  * Número
  * Tipos
  * Região
  * Descrição
  * Gênero
  * Peso, altura e habilidades
* 🎨 Alternância entre sprites normais e shiny.
* 🧠 Carregamento inteligente via coroutines.
* ⚠️ Tratamento de erros e feedback visual.

---

## 📸 Demonstração

🎥 **Vídeo do App**  

https://github.com/user-attachments/assets/d8b3ffed-147d-4177-878a-3d610a112cd2

📷 **Capturas de Tela**  
<img src="https://github.com/user-attachments/assets/55d76a26-804e-4abc-9c89-2bf5118b7613" width="150" />
<img src="https://github.com/user-attachments/assets/bb12f741-9a05-4b11-9e96-c623bf9189b9" width="150" />
<img src="https://github.com/user-attachments/assets/bc7953cf-7902-4914-9e91-440a42ed48b8" width="150" />
<img src="https://github.com/user-attachments/assets/10129391-0fc3-4dc3-aab2-4d5a4aa1ec2e" width="150" />
<img src="https://github.com/user-attachments/assets/b8e3c707-90f7-45ad-8cb8-80b09974771c" width="150" />

---

## 🚀 Como Rodar o Projeto

1.  **Clone o Repositório:**
    ```bash
    git clone https://github.com/AndersonPS94/NovaPokedex.git
    cd NovaPokedex
    ```
2.  **Abra no Android Studio:**
    Abra o projeto no Android Studio (versão recomendada: Flamingo ou superior).
3.  **Sincronize o Gradle:**
    Aguarde a sincronização automática do Gradle para baixar todas as dependências.
4.  **Execute:**
    Selecione um emulador ou dispositivo físico e execute o projeto.

---

## 📜 Licença

O projeto é distribuído sob a licença MIT.

```
The MIT License (MIT)

Copyright (c) 2025 Anderson Pereira

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```
