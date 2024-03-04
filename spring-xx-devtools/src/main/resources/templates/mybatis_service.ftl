<#-- @ftlvariable name="imports" type="java.lang.String" -->
<#-- @ftlvariable name="entity" type="com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta" -->
package ${entity.serviceImplPackageName};

${imports}

@Service
public class ${entity.className}MybatisService<#if entity.baseServiceImpl??> extends ${entity.baseServiceImpl.className}<#if entity.baseServiceImpl.typeParameters??>&lt;${entity.baseServiceImpl.renderTypeParameters(entity)}&gt;</#if></#if> implements ${entity.className}Service {}