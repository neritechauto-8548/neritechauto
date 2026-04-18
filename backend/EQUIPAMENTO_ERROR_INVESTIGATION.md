# Investigação do Erro PropertyReferenceException em Equipamento

## Problema
A aplicação falha ao iniciar com o erro:
```
PropertyReferenceException: No property 'empresaId' found for type 'Equipamento'
```

## Evidências Coletadas

### ✅ Campo Existe no Código-Fonte
```java
@Column(name = "empresa_id", nullable = false)
private Long empresaId;

public Long getEmpresaId() {
    return empresaId;
}

public void setEmpresaId(Long empresaId) {
    this.empresaId = empresaId;
}
```

### ✅ Campo Existe na Classe Compilada
Verificado com `javap -p`:
```
private java.lang.Long empresaId;
public java.lang.Long getEmpresaId();
public void setEmpresaId(java.lang.Long);
```

### ✅ Outras Entidades Funcionam
Mais de 100 outros repositories usam `findByEmpresaId` sem problemas, incluindo:
- `FuncionarioRepository.findByEmpresaId`
- `CargoRepository.findByEmpresaId`
- `OrdemServicoRepository.findByEmpresaId`
- etc.

### ✅ Estrutura Idêntica
`Equipamento` e `Funcionario` têm estrutura idêntica:
- Ambos estendem `BaseEntity`
- Ambos declaram `empresaId` da mesma forma
- Ambos têm getters/setters

## Tentativas de Correção

1. ❌ Adicionar campo `empresaId` - já estava presente
2. ❌ Usar `@Query` manual - Spring Data ignora
3. ❌ Comentar métodos - confirma que o problema é específico de `empresaId`
4. ❌ Clean rebuild - não resolveu
5. ❌ Remover método `dummyMethod` de teste

## Hipóteses Não Testadas

1. **Cache do Hibernate/Spring Data JPA** - pode estar usando metadados antigos
2. **Ordem de inicialização** - `Equipamento` pode ser processado antes de estar completamente carregado
3. **Problema de classloader** - versão antiga da classe em cache
4. **Bug do Spring Data JPA** - problema específico com esta entidade

## Próximos Passos Sugeridos

### Opção 1: Workaround com @Query (Rápido)
Substituir todos os métodos por queries manuais:
```java
@Query("SELECT e FROM Equipamento e WHERE e.empresaId = :empresaId")
Page<Equipamento> findByEmpresaId(@Param("empresaId") Long empresaId, Pageable pageable);
```

### Opção 2: Investigação Profunda (Demorado)
- Verificar logs de debug do Hibernate
- Tentar recriar a entidade do zero
- Verificar se há conflito de nomes

### Opção 3: Limpar Todos os Caches
```bash
mvn clean
Remove-Item -Recurse -Force target
Remove-Item -Recurse -Force ~/.m2/repository/com/neritech
```

## Recomendação
Opção 1 (workaround) para desbloquear a aplicação imediatamente, seguida de investigação mais profunda se o problema persistir em outras entidades.
