# 🤖 IbamaAutomation - Robô de Lançamento no Portal do IBAMA

Este projeto implementa um **robô de lançamento** que simula um usuário humano para preencher automaticamente o formulário do portal do IBAMA, que não oferece nenhuma integração via API ou importação de planilhas.

Ideal para quem precisa cadastrar **centenas ou milhares de produtos** e não pode depender de lançamentos manuais.

---

## 🎯 Funcionalidades

Com este robô, é possível:

✅ Realizar login manual com certificado digital  
✅ Ler dados de uma planilha Excel (.xlsx)  
✅ Preencher automaticamente os campos do formulário do IBAMA  
✅ Simular um usuário humano usando **Selenium WebDriver**  
✅ Lidar com campos dinâmicos (ex: estados e municípios com carregamento assíncrono)  
✅ Gravar os dados de forma automatizada  

---

## 🛠️ Tecnologias Utilizadas

- Java 17  
- Selenium WebDriver  
- Apache POI (para leitura da planilha Excel)  
- Google Chrome + ChromeDriver  

---

## 🗂️ Estrutura da Planilha

A planilha deve conter, nesta ordem:

1. Código da Matéria-Prima  
2. Quantidade Transportada  
3. Município de Origem  
4. Município de Destino  

---

## ⚙️ Personalização

O código foi **adaptado à minha realidade**:

- Campos como **estado de origem e destino**, **modo de transporte** e **armazenamento** estão fixos no código para acelerar o processo.
- Esses campos podem ser facilmente movidos para a planilha, conforme a necessidade do projeto.

---

## 📌 Observações

- O login com certificado digital deve ser feito manualmente antes do início do processo.  
- O código simula um usuário navegando e interagindo com o sistema como se fosse manual, respeitando o carregamento e validações do IBAMA.  

---

## 👤 Autor

Lucas Poltronieri Sampaio  
📧 lucaspoltronierisampaio@gmail.com
