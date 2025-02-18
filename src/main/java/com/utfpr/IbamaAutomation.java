package com.utfpr;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class IbamaAutomation {
    public static void main(String[] args) {
        // 🛠️ Configurar Selenium para abrir o Chrome
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Usuario\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // 🚀 Configurar opções do Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized"); // Abre maximizado

        // 🏁 Inicializar o ChromeDriver
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1️⃣ Acessar o site do IBAMA
            driver.get("https://servicos.ibama.gov.br/ctfcd/sistema.php");
            System.out.println("."); // 🔹 Confirma que o site abriu e o ChromeDriver carregou corretamente

            System.out.println("🕒 Aguarde 15 segundos para login manual...");
            Thread.sleep(15000); // Tempo para login manual

            // 2️⃣ Abrir o formulário
            driver.get("https://servicos.ibama.gov.br/ctfcd/sistema.php?moduloId=236");

            // 3️⃣ Ler a planilha Excel
            FileInputStream file = new FileInputStream(new File("C:\\Users\\Usuario\\Desktop\\Produtos.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 📌 Ler valores do Excel
                String codRaMateriaPrima = getCellValue(row.getCell(0));
                String qtdTransportada = getCellValue(row.getCell(1));
                String municipioOrigem = getCellValue(row.getCell(2));
                String municipioDestino = getCellValue(row.getCell(3));

                // 🔍 Debug no console
                System.out.println("✔️ Linha " + (i + 1) + " - Matéria Prima: " + codRaMateriaPrima);
                System.out.println("✔️ Linha " + (i + 1) + " - Quantidade Transportada: " + qtdTransportada);
                System.out.println("✔️ Linha " + (i + 1) + " - Município Origem: " + municipioOrigem);
                System.out.println("✔️ Linha " + (i + 1) + " - Município Destino: " + municipioDestino);

                JavascriptExecutor js = (JavascriptExecutor) driver;

                try {
                    // Preencher Ano
                    new Select(wait.until(ExpectedConditions.elementToBeClickable(By.id("num_ano")))).selectByValue("2025");

                    // 1️⃣ Clicar na lupa para abrir o modal
                    WebElement lupaButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAux_des_ra_produto_tipo_0")));
                    lupaButton.click();

                    // 2️⃣ Espera o modal abrir completamente
                    Thread.sleep(2000);

                    // 3️⃣ Mudar o foco para o iframe correto
                    driver.switchTo().frame("iframe_opcoes_des_ra_produto_tipo");

                    // 4️⃣ Aguardar o campo de código da matéria-prima estar visível
                    WebElement campoCodMateriaPrima = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("COD_RA_MATERIA_PRIMA_PROD")));

                    // 5️⃣ Preencher o código da matéria-prima
                    campoCodMateriaPrima.clear();
                    campoCodMateriaPrima.sendKeys(codRaMateriaPrima);

                    // 6️⃣ Clicar no botão "Pesquisar"
                    WebElement pesquisarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPesquisar")));
                    pesquisarButton.click();

                    // 7️⃣ Esperar a janela/modal fechar
                    Thread.sleep(2000);

                    // 8️⃣ Voltar para o contexto principal da página
                    driver.switchTo().defaultContent();

                    // Preencher Quantidade Transportada
                    WebElement qtdTransportadaField = driver.findElement(By.id("qtd_transportada"));
                    js.executeScript("arguments[0].value='" + qtdTransportada + "';", qtdTransportadaField);
                    js.executeScript("arguments[0].dispatchEvent(new Event('blur'));", qtdTransportadaField); // Simula o blur

                    // Preencher campos fixos
                    new Select(driver.findElement(By.id("cod_unidmed"))).selectByValue("002");
                    new Select(driver.findElement(By.id("cod_ra_transporte"))).selectByValue("E");
                    new Select(driver.findElement(By.id("cod_ra_armazenamento"))).selectByValue("A ");
                    new Select(driver.findElement(By.id("plano_emergencia"))).selectByValue("S");

                    // 🟢 Selecionar o Estado de Origem
                    Select estadoOrigemField = new Select(driver.findElement(By.id("cod_uf_origem")));
                    estadoOrigemField.selectByValue("35"); // Valor do Estado de Origem (São Paulo, por exemplo)

                    // 🕒 Aguardar o carregamento da lista de municípios de origem
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("cod_municipio_origem")));
                    Select municipioOrigemField = new Select(driver.findElement(By.id("cod_municipio_origem")));

                    // Certifique-se de que o nome do município está correto
                    String municipioOrigemName = municipioOrigem; // Nome do município de origem
                    boolean found = false;

                    for (WebElement option : municipioOrigemField.getOptions()) {
                        if (option.getText().equalsIgnoreCase(municipioOrigemName)) { // Verifica pelo nome
                            found = true;
                            municipioOrigemField.selectByVisibleText(municipioOrigemName);
                            System.out.println("✅ Município de Origem selecionado: " + municipioOrigem);
                            break;
                        }
                    }

                    if (!found) {
                        System.err.println("❌ Não foi possível encontrar o Município de Origem: " + municipioOrigem);
                    }

                    // 🟢 Selecionar o Estado de Destino
                    Select estadoDestinoField = new Select(driver.findElement(By.id("cod_uf_destino")));
                    estadoDestinoField.selectByValue("35"); // Valor do Estado de Destino (São Paulo, por exemplo)

                    // 🕒 Aguardar o carregamento da lista de municípios de destino
                    wait.until(ExpectedConditions.elementToBeClickable(By.id("cod_municipio_destino")));
                    Select municipioDestinoField = new Select(driver.findElement(By.id("cod_municipio_destino")));

                    // Certifique-se de que o nome do município está correto
                    String municipioDestinoName = municipioDestino; // Nome do município de destino
                    boolean foundDestino = false;

                    for (WebElement option : municipioDestinoField.getOptions()) {
                        if (option.getText().equalsIgnoreCase(municipioDestinoName)) { // Verifica pelo nome
                            foundDestino = true;
                            municipioDestinoField.selectByVisibleText(municipioDestinoName);
                            System.out.println("✅ Município de Destino selecionado: " + municipioDestino);
                            break;
                        }
                    }

                    if (!foundDestino) {
                        System.err.println("❌ Não foi possível encontrar o Município de Destino: " + municipioDestino);
                    }

                    // 🟢 Clicar no botão "Gravar Dados"
                    WebElement gravarButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("Gravar Dados"))); // 🔄 Corrigido ID
                    js.executeScript("arguments[0].click();", gravarButton);

                    // Pequena pausa para evitar bloqueios
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.err.println("❌ Erro ao preencher a linha " + (i + 1) + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 5️⃣ Fechar a planilha
            workbook.close();
            file.close();
            System.out.println("✅ Processo finalizado com sucesso!");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Garantir que o navegador feche mesmo em caso de erro
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // 📌 Método para converter valores da planilha para String corretamente
    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue().trim();
            default:
                return "";
        }
    }
}
