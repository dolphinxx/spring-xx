<#-- @ftlvariable name="imports" type="java.lang.String" -->
<#-- @ftlvariable name="entity" type="com.jagsii.springxx.devtools.codegen.admin.meta.EntityMeta" -->
package ${entity.mapperPackageName};

${imports}

@Mapper
public interface ${entity.className}Mapper <#if entity.baseMapper??>extends ${entity.baseMapper.className}<#if entity.baseMapper.typeParameters??>&lt;${entity.baseMapper.renderTypeParameters(entity)}&gt;</#if></#if> {

}
