# 📱 Pokédex Android App

Este repositório contém uma aplicação Android inspirada na Pokédex, desenvolvida como projeto pessoal com o objetivo de aprofundar conhecimentos em arquitetura de software, boas práticas de desenvolvimento e bibliotecas modernas do ecossistema Android.

---

## 📸 Demonstração

https://github.com/user-attachments/assets/d8b3ffed-147d-4177-878a-3d610a112cd2

<img src="https://github.com/user-attachments/assets/55d76a26-804e-4abc-9c89-2bf5118b7613" width="300" />
<img src="https://github.com/user-attachments/assets/bb12f741-9a05-4b11-9e96-c623bf9189b9" width="300" />
<img src="https://github.com/user-attachments/assets/bc7953cf-7902-4914-9e91-440a42ed48b8" width="300" />
<img src="https://github.com/user-attachments/assets/10129391-0fc3-4dc3-aab2-4d5a4aa1ec2e" width="300" />
<img src="https://github.com/user-attachments/assets/b8e3c707-90f7-45ad-8cb8-80b09974771c" width="300" />




> Exibição de lista de Pokémons, detalhes individuais com stats, tipos, regiões e muito mais!

---

## 🛠️ Tecnologias Utilizadas

Este projeto foi desenvolvido utilizando as seguintes tecnologias e padrões:

* **Kotlin** — Linguagem principal do app.
* **XML** — Utilizado para construção das interfaces.
* **MVVM (Model-View-ViewModel)** — Separação clara entre camada de UI, lógica de negócios e dados.
* **Clean Architecture** — Organização modular e desacoplada em camadas: presentation, domain e data.
* **Retrofit** — Para consumo da [PokéAPI](https://pokeapi.co).
* **Room Database** — Armazenamento local dos Pokémons.
* **RecyclerView** — Para exibição da lista de Pokémons.
* **ViewBinding** — Para acesso seguro às views.
* **Coroutines** — Para chamadas assíncronas.
* **Injeção de Dependências**.
* **Testes Unitários** — Cobertura da lógica com testes automatizados.

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

## 📄 Aprendizados

Durante o desenvolvimento, foram reforçados conceitos como:

✅ Boas práticas de arquitetura Android

✅ Aplicação de MVVM + Clean Architecture

✅ Comunicação entre camadas com interfaces e Repositories

✅ Testes unitários e organização modular

✅ Consumo de APIs REST com Retrofit + Gson

✅ Room para persistência local

✅ Experiência real com gerenciamento de estado e navegação entre Activities


---

## 📋 TODO

* [ ] Adicionar testes instrumentados com **Espresso**
* [ ] Criar sistema de favoritos usando Room
* [ ] Adicionar suporte offline completo

---

## 📜 Licença

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
