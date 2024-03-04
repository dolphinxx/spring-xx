<#-- @ftlvariable name="imports" type="java.lang.String" -->
<#-- @ftlvariable name="entity" type="com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta" -->
<#assign service=entity.variableName + 'Service'/>
package ${entity.controllerPackageName};

${imports}

@RestController
@RequestMapping("/${entity.module.name}")
public class ${entity.className}Controller<#if entity.baseController??> extends ${entity.baseController.className}<#if entity.baseController.typeParameters??><${entity.baseController.renderTypeParameters(entity)}></#if></#if> {
  private final ${entity.className}Service ${service};

  public R&lt;List&lt;${entity.className}&gt;&gt; list(Query query) {
    return ${service}.findByQuery(query);
  }

  @GetMapping("/page")
  public R&lt;Page&lt;${entity.className}&gt;&gt; list(Query query, PageRequest page) {
    return ${service}.findByQuery(query);
  }

  @GetMapping("/:id")
  public R&lt;${entity.className}&gt; detail(${entity.primaryKey.javaType} id) {
    return ${service}.findById(id);
  }

  @PostMapping("/save")
  public R&lt;Void&gt; save(${entity.className} entity) {
    ${service}.save(entity);
    return R.success();
  }

  @PostMapping("/update")
  public R&lt;Void&gt; update(${entity.className} entity) {
    ${service}.updateById(entity);
    return R.success();
  }

  @PostMapping("/remove")
  public R&lt;Void&gt; delete(${entity.primaryKey.javaType} id) {
    ${service}.deleteById(id);
    return R.success();
  }
}