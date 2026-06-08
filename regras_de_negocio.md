# 📋 Regras de Negócio — Sistema NeriTech Auto
> Documento gerado automaticamente. Atualizar sempre que novas regras forem implementadas.
> Última atualização: 07/06/2026

---

## Índice

- [COLABORADORES (Cadastro)](#colaboradores-cadastro)
- [EMPRESA (Configurações)](#empresa-configurações)
- [DEPARTAMENTOS](#departamentos)
- [SETORES](#setores)
- [CONTAS BANCÁRIAS](#contas-bancárias)
- [FORMAS DE PAGAMENTO](#formas-de-pagamento)
- [CHECKLISTS](#checklists)
- [SITUAÇÕES](#situações)
- [LOCALIZAÇÃO](#localização)
- [CATEGORIAS DE PRODUTO](#categorias-de-produto)
- [UNIDADES DE MEDIDA](#unidades-de-medida)
- [QUESTIONÁRIOS DE ENVIO](#questionários-de-envio)
- [OPÇÕES DE ENVIO](#opções-de-envio)
- [SERVIÇOS](#serviços)

---

---

# COLABORADORES (Cadastro)

> **Caminho:** `Configurações > Colaboradores > Cadastro de Colaborador`  
> **Arquivos:** `cadastro-colaborador.ts` / `FuncionarioService.java`

---

## 🔵 Aba: Dados Pessoais — Identificação

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-COL-01 | **Nome Completo obrigatório** | Frontend | Ao salvar | *"Nome completo é obrigatório."* | Erro |
| RN-COL-02 | **Matrícula obrigatória** | Frontend | Ao salvar | *"Matrícula é obrigatória."* | Erro |
| RN-COL-03 | **CPF obrigatório** | Frontend | Ao salvar | *"CPF é obrigatório."* | Erro |
| RN-COL-04 | **CPF deve ter 11 dígitos** | Frontend | Ao salvar | *"O CPF deve conter 11 dígitos."* | Erro |
| RN-COL-05 | **Dígitos verificadores do CPF válidos** | Frontend + Backend | Ao salvar | *"O CPF informado não é válido. Verifique os dígitos."* | Erro |
| RN-COL-06 | **CPF único por empresa** | Backend | Ao salvar | *"CPF já cadastrado para esta empresa."* | Erro |
| RN-COL-07 | **Data de nascimento: idade mínima de 16 anos** | Frontend + Backend | Ao salvar | *"O colaborador deve ter pelo menos 16 anos."* | Erro |
| RN-COL-08 | **E-mail pessoal com formato válido** | Frontend | Ao salvar | *"E-mail pessoal inválido."* | Erro |

---

## 🔵 Aba: Dados Pessoais — Localização (Endereço)

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-COL-09 | **CEP obrigatório** | Frontend | Ao salvar | *"CEP é obrigatório."* | Erro |
| RN-COL-10 | **CEP deve ter 8 dígitos** | Frontend | Ao salvar | *"CEP inválido."* | Erro |
| RN-COL-11 | **Logradouro obrigatório** | Frontend | Ao salvar | *"Logradouro é obrigatório."* | Erro |
| RN-COL-12 | **Bairro obrigatório** | Frontend | Ao salvar | *"Bairro é obrigatório."* | Erro |
| RN-COL-13 | **Cidade obrigatória** | Frontend | Ao salvar | *"Cidade é obrigatória."* | Erro |
| RN-COL-14 | **UF obrigatória** | Frontend | Ao salvar | *"UF é obrigatória."* | Erro |

---

## 🔵 Aba: Vínculo Empregatício

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-COL-15 | **Data de Admissão obrigatória** | Frontend | Ao salvar | *"Data de admissão é obrigatória."* | Erro |
| RN-COL-16 | **Data de Demissão não pode ser anterior à Admissão** | Frontend | Ao salvar | *"A data de demissão não pode ser anterior à data de admissão."* | Erro |
| RN-COL-17 | **Motivo de inativação obrigatório para status INATIVO, DEMITIDO ou AFASTADO** | Frontend | Ao salvar | *"O motivo de inativação é obrigatório para o status selecionado."* | Erro |
| RN-COL-18 | **Salário Base não pode ser negativo** | Frontend | Ao salvar | *"O salário base não pode ser negativo."* | Erro |
| RN-COL-19 | **Vale Transporte não pode ser negativo** | Frontend | Ao salvar | *"O vale transporte não pode ser negativo."* | Erro |
| RN-COL-20 | **Vale Alimentação não pode ser negativo** | Frontend | Ao salvar | *"O vale alimentação não pode ser negativo."* | Erro |

---

## 🔵 Aba: Regras de Comissão

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-COL-21 | **A regra de comissão deve ter pelo menos uma modalidade (Serviço, Produto ou Faturamento Geral) como SIM** | Frontend + Backend | Ao adicionar regra | *"A regra deve comissionar sobre serviços, produtos ou faturamento geral."* | Aviso (warn) |
| RN-COL-22 | **Ao criar nova regra para o mesmo setor, as anteriores são arquivadas automaticamente** | Backend | Ao salvar | Exibido como informação na tela | Informativo |
| RN-COL-23 | **Salvar o colaborador antes de adicionar regras de comissão** | Frontend | Ao clicar em "Adicionar Regra" sem ID | *"Salve o colaborador antes de adicionar regras."* | Aviso |

---

## 🔵 Aba: Habilitação (CNH)

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-COL-24 | **Categoria da CNH obrigatória quando número é informado** | Frontend | Ao salvar | *"A categoria da CNH é obrigatória quando o número é informado."* | Erro |
| RN-COL-25 | **CNH não pode estar vencida** | Frontend | Ao salvar | *"A CNH informada está vencida."* | Erro |

---

## 🔵 Aba: Acesso ao Sistema

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-COL-26 | **E-mail de login obrigatório quando acesso habilitado** | Frontend | Ao salvar | *"E-mail de login é obrigatório para habilitar o acesso."* | Erro |
| RN-COL-27 | **E-mail de login com formato válido** | Frontend | Ao salvar | *"E-mail de login inválido."* | Erro |
| RN-COL-28 | **Senha obrigatória na criação (mínimo 6 caracteres)** | Frontend | Ao salvar | *"A senha de login é obrigatória e deve ter pelo menos 6 caracteres."* | Erro |
| RN-COL-29 | **Nova senha com mínimo 6 caracteres** | Frontend | Ao salvar | *"A nova senha deve ter pelo menos 6 caracteres."* | Erro |
| RN-COL-30 | **Pelo menos uma função de acesso selecionada** | Frontend | Ao salvar | *"Selecione pelo menos uma função de acesso."* | Erro |

---

---

# EMPRESA (Configurações)

> **Caminho:** `Configurações > Empresa`  
> **Arquivos:** `empresa.ts` / `EmpresaService.java`

---

## 🟢 Aba: Dados da Oficina — Identificação

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-01 | **Nome Fantasia obrigatório** | Frontend + Backend | Ao salvar | *"O Nome Fantasia da oficina é obrigatório."* | Erro |
| RN-EMP-02 | **Nome Fantasia mínimo 2 caracteres** | Frontend + Backend | Ao salvar | *"O Nome Fantasia deve ter pelo menos 2 caracteres."* | Erro |
| RN-EMP-03 | **Razão Social obrigatória** | Frontend + Backend | Ao salvar | *"A Razão Social é obrigatória."* | Erro |
| RN-EMP-04 | **Razão Social mínimo 2 caracteres** | Frontend + Backend | Ao salvar | *"A Razão Social deve ter pelo menos 2 caracteres."* | Erro |
| RN-EMP-05 | **CPF ou CNPJ obrigatório** | Frontend + Backend | Ao salvar | *"Informe o CPF (para autônomo) ou CNPJ da empresa."* | Erro |
| RN-EMP-06 | **CPF deve ter 11 dígitos** | Frontend | Ao salvar | *"O CPF deve conter 11 dígitos."* | Erro |
| RN-EMP-07 | **CNPJ deve ter 14 caracteres** | Frontend | Ao salvar | *"O CNPJ deve conter 14 caracteres."* | Erro |
| RN-EMP-08 | **Dígitos verificadores do CPF válidos** | Frontend + Backend | Ao salvar + ao sair do campo | *"O CPF informado não é válido. Verifique os dígitos."* | Erro |
| RN-EMP-09 | **Dígitos verificadores do CNPJ válidos** | Frontend + Backend | Ao salvar + ao sair do campo | *"O CNPJ informado não é válido. Verifique os dígitos verificadores."* | Erro |
| RN-EMP-10 | **CNPJ único no sistema** | Backend | Ao salvar | *"Já existe uma empresa cadastrada com este CPF/CNPJ."* | Erro |

---

## 🟢 Aba: Dados da Oficina — Contato

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-11 | **E-mail com formato válido** | Frontend + Backend | Ao salvar | *"O e-mail principal da empresa não é válido."* | Erro |
| RN-EMP-12 | **Site deve iniciar com http:// ou https://** | Frontend + Backend | Ao salvar | *"O site deve começar com http:// ou https://."* | Erro |

---

## 🟢 Aba: Dados da Oficina — Localização

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-13 | **CEP obrigatório** | Frontend | Ao salvar | *"O CEP do endereço é obrigatório."* | Erro |
| RN-EMP-14 | **CEP deve ter 8 dígitos** | Frontend | Ao salvar + ao buscar CEP | *"O CEP deve conter 8 dígitos."* | Erro |
| RN-EMP-15 | **Logradouro obrigatório** | Frontend | Ao salvar | *"O Logradouro (rua/avenida) é obrigatório."* | Erro |
| RN-EMP-16 | **Bairro obrigatório** | Frontend | Ao salvar | *"O Bairro é obrigatório."* | Erro |
| RN-EMP-17 | **Cidade obrigatória** | Frontend | Ao salvar | *"A Cidade é obrigatória."* | Erro |
| RN-EMP-18 | **Confirmação antes de sobrescrever endereço já preenchido via busca de CEP** | Frontend | Ao buscar CEP com campos preenchidos | *"Os campos já estão preenchidos. Deseja sobrescrevê-los?"* | Confirmação |

---

## 🟢 Aba: Dados da Oficina — Logomarca

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-19 | **Formato aceito: PNG, JPG, WebP** | Frontend | Ao selecionar arquivo | *"Somente imagens PNG, JPG ou WebP são aceitas."* | Erro |
| RN-EMP-20 | **Tamanho máximo: 2MB** | Frontend | Ao selecionar arquivo | *"A imagem deve ter no máximo 2MB."* | Erro |
| RN-EMP-21 | **Confirmação para remover logomarca** | Frontend | Ao clicar em Remover | *"Tem certeza que deseja remover a logomarca da sua empresa?"* | Confirmação |

---

## 🟢 Aba: Horários

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-22 | **Horário Seg–Sex: par abertura/fechamento obrigatório** | Frontend | Ao salvar | *"Informe o horário de abertura/fechamento de Segunda a Sexta."* | Erro |
| RN-EMP-23 | **Fechamento Seg–Sex deve ser após a abertura** | Frontend | Ao salvar | *"O horário de fechamento (Seg–Sex) deve ser após a abertura."* | Erro |
| RN-EMP-24 | **Horário Sábado: par abertura/fechamento obrigatório** | Frontend | Ao salvar | *"Informe o horário de abertura/fechamento do Sábado."* | Erro |
| RN-EMP-25 | **Fechamento Sábado deve ser após a abertura** | Frontend | Ao salvar | *"O horário de fechamento do Sábado deve ser após a abertura."* | Erro |
| RN-EMP-26 | **Horários do Domingo obrigatórios se `funcionaDomingo = true`** | Frontend | Ao salvar | *"Informe os horários de abertura e fechamento do Domingo."* | Erro |
| RN-EMP-27 | **Fechamento Domingo deve ser após a abertura** | Frontend | Ao salvar | *"O horário de fechamento do Domingo deve ser após a abertura."* | Erro |
| RN-EMP-28 | **Intervalo: início e fim obrigatórios se ativo** | Frontend | Ao salvar | *"Informe o início e o fim do intervalo (almoço/pausa)."* | Erro |
| RN-EMP-29 | **Fim do intervalo deve ser após o início** | Frontend | Ao salvar | *"O fim do intervalo deve ser após o início."* | Erro |

---

## 🟢 Aba: Configurações

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-30 | **Margem de lucro padrão: 0–100%** | Frontend | Ao salvar | *"A margem de lucro padrão deve estar entre 0% e 100%."* | Erro |
| RN-EMP-31 | **Garantia padrão não pode ser negativa** | Frontend | Ao salvar | *"Os dias de garantia padrão não podem ser negativos."* | Erro |
| RN-EMP-32 | **Duração de agendamento mínima: 5 minutos** | Frontend | Ao salvar | *"A duração mínima de agendamento deve ser de pelo menos 5 minutos."* | Erro |
| RN-EMP-33 | **Código da moeda deve ter exatamente 3 caracteres** | Frontend | Ao salvar | *"O código da moeda deve ter exatamente 3 caracteres (ex: BRL, USD)."* | Erro |

---

## 🟢 Aba: Dados Fiscais

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-34 | **CFOP Dentro do Estado: exatamente 4 dígitos** | Frontend | Ao salvar | *"O CFOP Dentro do Estado deve conter exatamente 4 dígitos (ex: 5102)."* | Erro |
| RN-EMP-35 | **CFOP Fora do Estado: exatamente 4 dígitos** | Frontend | Ao salvar | *"O CFOP Fora do Estado deve conter exatamente 4 dígitos (ex: 6102)."* | Erro |
| RN-EMP-36 | **CFOP de Serviço: exatamente 4 dígitos** | Frontend | Ao salvar | *"O CFOP de Serviço deve conter exatamente 4 dígitos (ex: 5933)."* | Erro |
| RN-EMP-37 | **Alíquota ICMS: 0–100%** | Frontend | Ao salvar | *"A alíquota de ICMS deve estar entre 0% e 100%."* | Erro |
| RN-EMP-38 | **Alíquota PIS: 0–100%** | Frontend | Ao salvar | *"A alíquota de PIS deve estar entre 0% e 100%."* | Erro |
| RN-EMP-39 | **Alíquota COFINS: 0–100%** | Frontend | Ao salvar | *"A alíquota de COFINS deve estar entre 0% e 100%."* | Erro |
| RN-EMP-40 | **Alíquota CSLL: 0–100%** | Frontend | Ao salvar | *"A alíquota de CSLL deve estar entre 0% e 100%."* | Erro |
| RN-EMP-41 | **Alíquota IRPJ: 0–100%** | Frontend | Ao salvar | *"A alíquota de IRPJ deve estar entre 0% e 100%."* | Erro |
| RN-EMP-42 | **Alíquota ISS: 0–100%** | Frontend | Ao salvar | *"A alíquota de ISS deve estar entre 0% e 100%."* | Erro |
| RN-EMP-43 | **Código do município: exatamente 7 dígitos** | Frontend | Ao salvar | *"O código do município deve conter exatamente 7 dígitos (ex: 3550308)."* | Erro |
| RN-EMP-44 | **Senha do certificado digital obrigatória se certificado preenchido** | Frontend | Ao salvar | *"Informe a senha do certificado digital A1."* | Erro |
| RN-EMP-45 | **Alerta: certificado digital expirado** | Frontend | Ao salvar | *"⚠️ A validade do certificado digital já passou. Renove o certificado."* | Aviso (não bloqueia) |
| RN-EMP-46 | **Alerta: ambiente de produção NF-e** | Frontend | Ao salvar | *"⚠️ Atenção: você está configurando emissão em PRODUÇÃO. As notas serão fiscalmente válidas."* | Aviso (não bloqueia) |

---

## 🟢 Aba: E-mail (SMTP)

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-EMP-47 | **E-mail de envio (remetente) obrigatório quando SMTP ativo** | Frontend | Ao salvar | *"O e-mail de envio (remetente) é obrigatório quando o SMTP está ativo."* | Erro |
| RN-EMP-48 | **Formato do e-mail de envio válido** | Frontend | Ao salvar | *"O e-mail de envio informado não é válido."* | Erro |
| RN-EMP-49 | **Formato do e-mail de recebimento válido** | Frontend | Ao salvar | *"O e-mail de recebimento (resposta) informado não é válido."* | Erro |
| RN-EMP-50 | **Servidor SMTP obrigatório quando SMTP ativo** | Frontend | Ao salvar | *"Informe o servidor SMTP (ex: smtp.gmail.com)."* | Erro |
| RN-EMP-51 | **Porta SMTP: entre 1 e 65535** | Frontend | Ao salvar | *"A porta SMTP deve estar entre 1 e 65535 (portas comuns: 25, 465, 587)."* | Erro |
| RN-EMP-52 | **Alerta: porta SMTP recomendada para SSL é 465** | Frontend | Ao salvar | *"⚠️ A porta recomendada para SSL é 465."* | Aviso (não bloqueia) |
| RN-EMP-53 | **Alerta: porta SMTP recomendada para TLS é 587** | Frontend | Ao salvar | *"⚠️ A porta recomendada para TLS é 587."* | Aviso (não bloqueia) |
| RN-EMP-54 | **E-mail de teste: campo obrigatório** | Frontend | Ao clicar "Testar" | *"Informe o e-mail de destino para o teste."* | Aviso |
| RN-EMP-55 | **E-mail de teste: formato válido** | Frontend | Ao clicar "Testar" | *"Informe um endereço de e-mail válido para o teste."* | Erro |
| RN-EMP-56 | **SMTP deve estar configurado antes de testar envio** | Frontend | Ao clicar "Testar" | *"Configure o servidor SMTP antes de testar o envio."* | Aviso |

---

## 🟢 Backend — Regras Adicionais (EmpresaService)

| ID | Regra | Quando | Tipo de Exceção |
|---|---|---|---|
| RN-EMP-BE-01 | **Nome Fantasia obrigatório (≥ 2 chars)** | Toda operação de create/update | `BusinessException` |
| RN-EMP-BE-02 | **Razão Social obrigatória (≥ 2 chars)** | Toda operação de create/update | `BusinessException` |
| RN-EMP-BE-03 | **CPF/CNPJ obrigatório e matematicamente válido** | Toda operação de create/update | `BusinessException` |
| RN-EMP-BE-04 | **CNPJ/CPF único no banco de dados** | Toda operação de create/update | `BusinessException` |
| RN-EMP-BE-05 | **E-mail no formato correto** | Toda operação de create/update | `BusinessException` |
| RN-EMP-BE-06 | **Site começa com http(s)** | Toda operação de create/update | `BusinessException` |
| RN-EMP-BE-07 | **Data de abertura não pode ser futura** | Toda operação de create/update | `BusinessException` |

---

---

# DEPARTAMENTOS

> **Caminho:** `Configurações > Departamentos`  
> **Arquivos:** `departamentos.ts` / `DepartamentoItemDialog.ts` / `DepartamentoContabioService.java`

---

## 🟣 Gestão de Departamentos

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-DEP-01 | **Descrição obrigatória** | Frontend + Backend | Ao salvar | *"A descrição do departamento é obrigatória."* | Erro |
| RN-DEP-02 | **Descrição mínima de 2 caracteres** | Frontend + Backend | Ao salvar | *"A descrição do departamento deve ter pelo menos 2 caracteres."* | Erro |
| RN-DEP-03 | **Descrição única por empresa** | Backend | Ao salvar | *"Já existe um departamento cadastrado com esta descrição."* | Aviso (warn) |

---

# SETORES

> **Caminho:** `Configurações > Setores`  
> **Arquivos:** `setores.ts` / `SetoresItemDialog.ts` / `SetorService.java`

---

## 🟣 Gestão de Setores

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-SET-01 | **Nome do setor obrigatório** | Frontend + Backend | Ao salvar | *"O nome do setor é obrigatório."* | Erro |
| RN-SET-02 | **Nome do setor mínimo de 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome do setor deve ter pelo menos 2 caracteres."* | Erro |
| RN-SET-03 | **Nome do setor único por empresa** | Backend | Ao salvar | *"Já existe um setor cadastrado com este nome."* | Aviso (warn) |

---

# CONTAS BANCÁRIAS

> **Caminho:** `Configurações > Contas`  
> **Arquivos:** `contas.ts` / `ContasItemDialog.ts` / `ContaBancariaService.java`

---

## 🟣 Gestão de Contas Bancárias

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-CON-01 | **Código do banco obrigatório** | Frontend + Backend | Ao salvar | *"O código do banco é obrigatório."* | Erro |
| RN-CON-02 | **Nome do banco obrigatório** | Frontend + Backend | Ao salvar | *"O nome do banco é obrigatório."* | Erro |
| RN-CON-03 | **Agência obrigatória** | Frontend + Backend | Ao salvar | *"A agência é obrigatória."* | Erro |
| RN-CON-04 | **Número de conta obrigatório** | Frontend + Backend | Ao salvar | *"O número da conta é obrigatório."* | Erro |
| RN-CON-05 | **Titular da conta obrigatório** | Frontend + Backend | Ao salvar | *"O nome do titular é obrigatório."* | Erro |
| RN-CON-06 | **CPF/CNPJ do titular obrigatório** | Frontend + Backend | Ao salvar | *"O CPF ou CNPJ do titular é obrigatório."* | Erro |
| RN-CON-07 | **Documento do titular deve conter 11 (CPF) ou 14 (CNPJ) dígitos** | Frontend + Backend | Ao salvar | *"O CPF ou CNPJ do titular deve conter 11 (CPF) ou 14 (CNPJ) dígitos."* | Erro |
| RN-CON-08 | **Dígitos verificadores do CPF do titular válidos** | Frontend | Ao salvar | *"O CPF informado não é válido."* | Erro |
| RN-CON-09 | **Dígitos verificadores do CNPJ do titular válidos** | Frontend | Ao salvar | *"O CNPJ informado não é válido."* | Erro |
| RN-CON-10 | **Validação dinâmica de documento (CPF/CNPJ) ao sair do campo** | Frontend | Blur no input | Exibe toast com tipo identificado | Aviso (warn) |
| RN-CON-11 | **Agência e Conta única por banco e empresa** | Backend | Ao salvar | *"Já existe uma conta bancária cadastrada com esta mesma agência e conta neste banco."* | Aviso (warn) |

---

# FORMAS DE PAGAMENTO

> **Caminho:** `Configurações > Formas de Pagamento`  
> **Arquivos:** `formas-pagamento.ts` / `formas-pagamento-item-dialog.ts` / `FormaPagamentoService.java`

---

## 🟣 Gestão de Formas de Pagamento

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-FPG-01 | **Nome da forma obrigatório** | Frontend + Backend | Ao salvar | *"O nome da forma de pagamento é obrigatório."* | Erro |
| RN-FPG-02 | **Nome mínimo de 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome da forma de pagamento deve ter pelo menos 2 caracteres."* | Erro |
| RN-FPG-03 | **Tipo da forma de pagamento obrigatório** | Frontend + Backend | Ao salvar | *"O tipo da forma de pagamento é obrigatório."* | Erro |
| RN-FPG-04 | **Nome único por empresa** | Backend | Ao salvar | *"Já existe uma forma de pagamento cadastrada com este nome."* | Aviso (warn) |
| RN-FPG-05 | **Quantidade máxima de parcelas >= 2 se aceita parcelamento** | Frontend + Backend | Ao salvar | *"Para aceitar parcelamento, a quantidade máxima de parcelas deve ser maior ou igual a 2."* | Erro |
| RN-FPG-06 | **Forçar quantidade máxima de parcelas para 1 se não aceita parcelamento** | Frontend + Backend | Ao salvar | (Limpo automaticamente) | Silencioso |
| RN-FPG-07 | **Taxa de administração entre 0% e 100%** | Frontend + Backend | Ao salvar | *"A taxa de administração deve estar entre 0% e 100%."* | Erro |
| RN-FPG-08 | **Prazo de recebimento não negativo** | Frontend + Backend | Ao salvar | *"O prazo de recebimento não pode ser negativo."* | Erro |
| RN-FPG-09 | **Limite diário não negativo** | Frontend + Backend | Ao salvar | *"O limite diário não pode ser negativo."* | Erro |
| RN-FPG-10 | **Apenas uma forma de pagamento padrão ativa por empresa** | Backend | Ao salvar | (Desmarca outras automaticamente) | Silencioso |

---

# CHECKLISTS

> **Caminho:** `Configurações > Checklist`  
> **Arquivos:** `checklist.ts` / `checklist-modelo-dialog.ts` / `checklist-item-dialog.ts` / `ChecklistService.java` / `ItChecklistService.java`

---

## 🟣 Gestão de Modelos e Itens de Checklist

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-CKL-01 | **Título do checklist obrigatório** | Frontend + Backend | Ao salvar | *"O título do checklist é obrigatório."* | Erro |
| RN-CKL-02 | **Título mínimo de 2 caracteres** | Frontend + Backend | Ao salvar | *"O título do checklist deve ter pelo menos 2 caracteres."* | Erro |
| RN-CKL-03 | **Título único por empresa** | Backend | Ao salvar | *"Já existe um checklist cadastrado com esta descrição."* | Aviso (warn) |
| RN-CKL-04 | **Descrição do item obrigatória** | Frontend + Backend | Ao salvar | *"A descrição do item de checklist é obrigatória."* | Erro |
| RN-CKL-05 | **Descrição do item mínima de 2 caracteres** | Frontend + Backend | Ao salvar | *"A descrição do item de checklist deve ter pelo menos 2 caracteres."* | Erro |
| RN-CKL-06 | **Descrição do item única no mesmo checklist** | Backend | Ao salvar | *"Já existe um item cadastrado com esta descrição neste checklist."* | Aviso (warn) |

---

# SITUAÇÕES

> **Caminho:** `Configurações > Situação`  
> **Arquivos:** `situacao.ts` / `situacao-item-dialog.ts` / `SituacaoService.java`

---

## 🟣 Gestão de Situações (Status de OS)

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-SIT-01 | **Nome da situação obrigatório** | Frontend + Backend | Ao salvar | *"O nome da situação é obrigatório."* | Erro |
| RN-SIT-02 | **Nome da situação mínimo 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome da situação deve ter pelo menos 2 caracteres."* | Erro |
| RN-SIT-03 | **Nome da situação único por empresa** | Backend | Ao salvar | *"Já existe uma situação cadastrada com este nome nesta empresa."* | Aviso (warn) |
| RN-SIT-04 | **Explicação limite de 1000 caracteres** | Frontend + Backend | Ao salvar | *"A explicação não pode exceder 1000 caracteres."* | Erro |
| RN-SIT-05 | **Normalização da cor da bandeira** | Backend | Ao salvar | (Normaliza prefixo `#` ou define `#2563EB`) | Silencioso |

---

# LOCALIZAÇÃO

> **Caminho:** `Configurações > Localização`  
> **Arquivos:** `localizacao.ts` / `localizacao-item-dialog.ts` / `LocalizacaoService.java`

---

## 🟣 Gestão de Localizações

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-LOC-01 | **Descrição obrigatória** | Frontend + Backend | Ao salvar | *"A descrição da localização é obrigatória."* | Erro |
| RN-LOC-02 | **Descrição mínima de 2 caracteres** | Frontend + Backend | Ao salvar | *"A descrição da localização deve ter pelo menos 2 caracteres."* | Erro |
| RN-LOC-03 | **Descrição máxima de 255 caracteres** | Frontend + Backend | Ao salvar | *"A descrição da localização não pode exceder 255 caracteres."* | Erro |
| RN-LOC-04 | **Descrição única por empresa** | Backend | Ao salvar | *"Já existe uma localização cadastrada com esta descrição nesta empresa."* | Aviso (warn) |

---

## Padrão de Resposta de Erro da API

Todos os erros seguem o padrão `ApiErrorResponse`:

```json
{
  "type": "BUSINESS_RULE | VALIDATION | NOT_FOUND | UNAUTHORIZED | FORBIDDEN | INTERNAL_ERROR",
  "message": "Mensagem amigável ao usuário",
  "errors": { "campo": "detalhe do erro" },
  "timestamp": "2026-06-07T20:00:00"
}
```

### Mapeamento de Tipos → Toast no Frontend

| Tipo da API | Toast | Cor |
|---|---|---|
| `BUSINESS_RULE` | `warn` | 🟡 Amarelo |
| `VALIDATION` | `error` | 🔴 Vermelho |
| `NOT_FOUND` | `error` | 🔴 Vermelho |
| `UNAUTHORIZED` | `error` | 🔴 Vermelho |
| `FORBIDDEN` | `error` | 🔴 Vermelho |
| `INTERNAL_ERROR` | `error` | 🔴 Vermelho |
| HTTP `409 Conflict` | `warn` | 🟡 Amarelo |
| Sucesso | `success` | 🟢 Verde |

---

## Totais

| Tela | Frontend | Backend | Total |
|---|---|---|---|
| COLABORADORES | 28 regras | 5 regras | **30 regras** |
| EMPRESA | 44 regras | 7 regras | **56 regras** |
| DEPARTAMENTOS | 2 regras | 3 regras | **3 regras** |
| SETORES | 2 regras | 3 regras | **3 regras** |
| CONTAS BANCÁRIAS | 10 regras | 8 regras | **11 regras** |
| FORMAS DE PAGAMENTO | 8 regras | 8 regras | **10 regras** |
| CHECKLISTS | 4 regras | 4 regras | **6 regras** |
| SITUAÇÕES | 4 regras | 5 regras | **5 regras** |
| LOCALIZAÇÃO | 3 regras | 4 regras | **4 regras** |
| **TOTAL GERAL** | **105** | **47** | **🔢 128 regras** |

---

*Este documento deve ser atualizado sempre que novas telas receberem validações.*


---

---

# CATEGORIAS DE PRODUTO

> **Caminho:** `Configurações > Categoria > Aba: Categoria de Produtos`  
> **Arquivos:** `categoria.ts` / `categoria-item-dialog.ts` / `CategoriaProdutoService.java`

---

## 🟣 Gestão de Categorias de Produto

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-CAT-01 | **Nome obrigatório** | Frontend + Backend | Ao salvar | *"O nome da categoria é obrigatório."* | Erro |
| RN-CAT-02 | **Nome mínimo de 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome da categoria deve ter pelo menos 2 caracteres."* | Erro |
| RN-CAT-03 | **Nome máximo de 100 caracteres** | Frontend + Backend | Ao salvar | *"O nome da categoria não pode exceder 100 caracteres."* | Erro |
| RN-CAT-04 | **Nome único por empresa** | Backend | Ao salvar | *"Já existe uma categoria de produto cadastrada com este nome."* | Aviso (warn) |

---

# UNIDADES DE MEDIDA

> **Caminho:** `Configurações > Categoria > Aba: Unidade de Produtos`  
> **Arquivos:** `categoria.ts` / `unidade-item-dialog.ts` / `UnidadeMedidaService.java`

---

## 🟣 Gestão de Unidades de Medida

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-UNM-01 | **Nome obrigatório** | Frontend + Backend | Ao salvar | *"O nome da unidade de medida é obrigatório."* | Erro |
| RN-UNM-02 | **Nome mínimo de 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome deve ter pelo menos 2 caracteres."* | Erro |
| RN-UNM-03 | **Nome máximo de 50 caracteres** | Frontend + Backend | Ao salvar | *"O nome não pode exceder 50 caracteres."* | Erro |
| RN-UNM-04 | **Nome único por empresa** | Backend | Ao salvar | *"Já existe uma unidade de medida cadastrada com este nome."* | Aviso (warn) |
| RN-UNM-05 | **Sigla obrigatória** | Frontend + Backend | Ao salvar | *"A sigla da unidade de medida é obrigatória."* | Erro |
| RN-UNM-06 | **Sigla máxima de 10 caracteres** | Frontend + Backend | Ao salvar | *"A sigla não pode exceder 10 caracteres."* | Erro |
| RN-UNM-07 | **Sigla única por empresa** | Backend | Ao salvar | *"Já existe uma unidade de medida cadastrada com esta sigla."* | Aviso (warn) |
| RN-UNM-08 | **Sigla salva em maiúsculas** | Frontend | Ao confirmar | (Normalização silenciosa para maiúsculas) | Silencioso |
| RN-UNM-09 | **Pré-carga de unidades padrão ao criar empresa** | Backend | Ao cadastrar nova empresa | (Cria automaticamente: UN, L, KG, JG, PAR, M, PC, CJ) | Silencioso |

---

*Este documento deve ser atualizado sempre que novas telas receberem validações.*


---

# QUESTIONÁRIOS DE ENVIO

> **Caminho:** `Configurações > Questionário de Envio`  
> **Arquivos:** `questionamento.ts` / `questionamento-modelo-dialog.ts` / `questionamento-item-dialog.ts` / `QuestionarioService.java` / `ItQuestionarioService.java`

---

## 🟣 Gestão de Questionários e Perguntas

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-QUE-01 | **Nome do questionário obrigatório** | Frontend + Backend | Ao salvar | *"O nome do questionário é obrigatório."* | Erro |
| RN-QUE-02 | **Nome do questionário mínimo 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome do questionário deve ter pelo menos 2 caracteres."* | Erro |
| RN-QUE-03 | **Nome do questionário máximo 255 caracteres** | Frontend + Backend | Ao salvar | *"O nome do questionário não pode exceder 255 caracteres."* | Erro |
| RN-QUE-04 | **Nome do questionário único por empresa** | Backend | Ao salvar | *"Já existe um questionário cadastrado com este nome nesta empresa."* | Aviso (warn) |
| RN-ITQ-01 | **Descrição da pergunta obrigatória** | Frontend + Backend | Ao salvar | *"A descrição da pergunta é obrigatória."* | Erro |
| RN-ITQ-02 | **Descrição da pergunta mínima 2 caracteres** | Frontend + Backend | Ao salvar | *"A descrição da pergunta deve ter pelo menos 2 caracteres."* | Erro |
| RN-ITQ-03 | **Descrição da pergunta máxima 255 caracteres** | Frontend + Backend | Ao salvar | *"A descrição da pergunta não pode exceder 255 caracteres."* | Erro |
| RN-ITQ-04 | **Tipo de resposta da pergunta obrigatório** | Frontend + Backend | Ao salvar | *"O tipo de resposta da pergunta é obrigatório."* | Erro |
| RN-ITQ-05 | **Descrição da pergunta única por questionário** | Backend | Ao salvar | *"Já existe uma pergunta cadastrada com esta descrição neste questionário."* | Aviso (warn) |

---

# OPÇÕES DE ENVIO

> **Caminho:** `Configurações > Opções de Envio`  
> **Arquivos:** `cadastro-mensagem.ts` / `TemplateComunicacaoService.java`

---

## 🟣 Gestão de Modelos de Mensagem (Opções de Envio)

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-TPL-01 | **Nome do modelo obrigatório** | Frontend + Backend | Ao salvar | *"O nome do modelo de envio é obrigatório."* | Erro |
| RN-TPL-02 | **Nome do modelo mínimo 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome deve ter entre 2 e 100 caracteres."* | Erro |
| RN-TPL-03 | **Nome do modelo máximo 100 caracteres** | Frontend + Backend | Ao salvar | *"O nome deve ter entre 2 e 100 caracteres."* | Erro |
| RN-TPL-05 | **Categoria do modelo obrigatória** | Frontend + Backend | Ao salvar | *"A categoria do modelo é obrigatória."* | Erro |
| RN-TPL-06 | **Conteúdo do modelo obrigatório** | Frontend + Backend | Ao salvar | *"O conteúdo do modelo é obrigatório."* | Erro |
| RN-TPL-07 | **Conteúdo do modelo mínimo 10 caracteres** | Frontend + Backend | Ao salvar | *"O conteúdo deve ter pelo menos 10 caracteres."* | Erro |
| RN-TPL-08 | **Assunto obrigatório para tipo E-mail** | Frontend + Backend | Ao salvar | *"O assunto é obrigatório para modelos do tipo E-mail."* | Erro |
| RN-TPL-09 | **Assunto entre 2 e 255 caracteres para tipo E-mail** | Frontend + Backend | Ao salvar | *"O assunto deve ter entre 2 e 255 caracteres."* | Erro |
| RN-TPL-10 | **Nome único por empresa** | Backend | Ao salvar | *"Já existe um modelo de envio cadastrado com este nome nesta empresa."* | Aviso (warn) |

---

# SERVIÇOS

> **Caminho:** `Produtos e Serviços > Serviços`  
> **Arquivos:** `servicos.ts` / `ServicoService.java`

---

## 🟣 Gestão de Serviços

| ID | Regra | Camada | Quando | Mensagem ao Usuário | Severidade |
|---|---|---|---|---|---|
| RN-SRV-01 | **Nome do serviço obrigatório** | Frontend + Backend | Ao salvar | *"O nome do serviço é obrigatório."* | Erro |
| RN-SRV-02 | **Nome do serviço mínimo 2 caracteres** | Frontend + Backend | Ao salvar | *"O nome do serviço deve ter entre 2 e 255 caracteres."* | Erro |
| RN-SRV-03 | **Nome do serviço máximo 255 caracteres** | Frontend + Backend | Ao salvar | *"O nome do serviço deve ter entre 2 e 255 caracteres."* | Erro |
| RN-SRV-04 | **Preço de venda obrigatório** | Frontend + Backend | Ao salvar | *"O preço base é obrigatório."* | Erro |
| RN-SRV-05 | **Preço de venda não negativo** | Frontend + Backend | Ao salvar | *"O preço base não pode ser negativo."* | Erro |
| RN-SRV-06 | **Custo do serviço obrigatório** | Frontend + Backend | Ao salvar | *"O custo é obrigatório."* | Erro |
| RN-SRV-07 | **Custo do serviço não negativo** | Frontend + Backend | Ao salvar | *"O custo não pode ser negativo."* | Erro |
| RN-SRV-08 | **Preço de venda deve ser maior ou igual ao custo** | Frontend + Backend | Ao salvar | *"O preço base de venda do serviço não pode ser inferior ao seu custo de execução."* | Erro |
| RN-SRV-09 | **Nome único por empresa** | Backend | Ao salvar | *"Já existe um serviço cadastrado com este nome nesta empresa."* | Aviso (warn) |





