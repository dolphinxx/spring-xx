<#-- @ftlvariable name="imports" type="java.lang.String" -->
<#-- @ftlvariable name="entity" type="com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta" -->
package ${entity.servicePackageName};

${imports}

public interface ${entity.className}Service<#if entity.baseService??> extends ${entity.baseService.className}<#if entity.baseService.typeParameters??>&lt;${entity.baseService.renderTypeParameters(entity)}&gt;</#if></#if> {}